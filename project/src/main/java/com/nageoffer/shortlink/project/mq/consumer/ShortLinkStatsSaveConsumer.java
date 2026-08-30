package com.nageoffer.shortlink.project.mq.consumer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nageoffer.shortlink.project.common.convention.exception.ServiceException;
import com.nageoffer.shortlink.project.dao.entity.*;
import com.nageoffer.shortlink.project.dao.mapper.*;
import com.nageoffer.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.nageoffer.shortlink.project.mq.idempotent.MessageQueueIdempotentHandler;
import com.nageoffer.shortlink.project.mq.producer.DelayShortLinkStatsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.nageoffer.shortlink.project.common.constant.RedisKeyConstant.LOCK_GID_UPDATE_KEY;
import static com.nageoffer.shortlink.project.common.constant.ShortLinkConstant.AMAP_REMOTE_URL;

/** 短链接访问统计 Redis Stream 消费者。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShortLinkStatsSaveConsumer implements StreamListener<String, MapRecord<String, String, String>> {

    private final ShortLinkMapper shortLinkMapper;
    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;
    private final DelayShortLinkStatsProducer delayShortLinkStatsProducer;

    @Value("${spring.data.redis.channel-topic.short-link-stats-group}")
    private String group;

    @Value("${short-link.stats.locale.amap-key}")
    private String statsLocaleAmapKey;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String stream = message.getStream();
        RecordId id = message.getId();
        String messageId = id.toString();
        if (!messageQueueIdempotentHandler.isMessageProcessed(messageId)) {
            if (messageQueueIdempotentHandler.isAccomplish(messageId)) {
                acknowledgeAndDelete(stream, message);
                return;
            }
            throw new ServiceException("消息未完成流程，需要消息队列重试");
        }
        try {
            Map<String, String> values = message.getValue();
            ShortLinkStatsRecordDTO statsRecord = JSONUtil.toBean(
                    values.get("statsRecord"), ShortLinkStatsRecordDTO.class);
            String fullShortUrl = values.get("fullShortUrl");
            if (StrUtil.isBlank(fullShortUrl) && statsRecord != null) {
                fullShortUrl = statsRecord.getFullShortUrl();
            }
            if (StrUtil.isBlank(fullShortUrl) || statsRecord == null) {
                log.warn("忽略无效的短链接统计消息, messageId={}, values={}", id, values);
            } else {
                actualSaveShortLinkStats(fullShortUrl, statsRecord);
            }
            messageQueueIdempotentHandler.setAccomplish(messageId);
        } catch (Throwable ex) {
            messageQueueIdempotentHandler.delMessageProcessed(messageId);
            log.error("Redis Stream 短链接统计消费异常, messageId={}", id, ex);
            return;
        }
        try {
            acknowledgeAndDelete(stream, message);
        } catch (Throwable ex) {
            // 保留“已完成”标记，消息再次投递时只补做 ACK 和删除。
            log.error("Redis Stream 短链接统计确认异常, messageId={}", id, ex);
        }
    }

    private void acknowledgeAndDelete(String stream, MapRecord<String, String, String> message) {
        stringRedisTemplate.opsForStream().acknowledge(group, message);
        stringRedisTemplate.opsForStream().delete(
                Objects.requireNonNull(stream), message.getId().getValue());
    }

    private void actualSaveShortLinkStats(String fullShortUrl, ShortLinkStatsRecordDTO statsRecord) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(
                String.format(LOCK_GID_UPDATE_KEY, fullShortUrl));
        RLock readLock = readWriteLock.readLock();
        if (!readLock.tryLock()) {
            delayShortLinkStatsProducer.send(statsRecord);
            return;
        }
        try {
            ShortLinkGotoDO gotoRecord = shortLinkGotoMapper.selectOne(
                    Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                            .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl));
            if (gotoRecord == null) {
                return;
            }
            persistShortLinkStats(fullShortUrl, gotoRecord.getGid(), statsRecord);
        } finally {
            readLock.unlock();
        }
    }

    private void persistShortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO statsRecord) {
        LocalDate currentDate = LocalDate.now();
        linkAccessStatsMapper.shortLinkStats(LinkAccessStatsDO.builder()
                .fullShortUrl(fullShortUrl).gid(gid).date(currentDate)
                .pv(1)
                .uv(Boolean.TRUE.equals(statsRecord.getUvFirstFlag()) ? 1 : 0)
                .uip(Boolean.TRUE.equals(statsRecord.getUipFirstFlag()) ? 1 : 0)
                .hour(LocalDateTime.now().getHour())
                .weekday(currentDate.getDayOfWeek().getValue())
                .build());

        String actualProvince = "未知";
        String actualCity = "未知";
        Map<String, Object> localeParams = new HashMap<>();
        localeParams.put("key", statsLocaleAmapKey);
        localeParams.put("ip", statsRecord.getRemoteAddr());
        JSONObject localeObject = JSONUtil.parseObj(HttpUtil.get(AMAP_REMOTE_URL, localeParams));
        if (StrUtil.equals(localeObject.getStr("infocode"), "10000")) {
            String province = localeObject.getStr("province");
            boolean unknown = StrUtil.equals(province, "[]");
            actualProvince = unknown ? actualProvince : province;
            actualCity = unknown ? actualCity : localeObject.getStr("city");
            linkLocaleStatsMapper.shortLinkLocaleState(LinkLocaleStatsDO.builder()
                    .fullShortUrl(fullShortUrl).province(actualProvince).city(actualCity)
                    .adcode(unknown ? "未知" : localeObject.getStr("adcode"))
                    .cnt(1).country("china").gid(gid).date(currentDate).build());
        }

        linkOsStatsMapper.shortLinkOsStats(LinkOsStatsDO.builder()
                .os(statsRecord.getOs()).cnt(1).gid(gid)
                .date(currentDate).fullShortUrl(fullShortUrl).build());
        linkBrowserStatsMapper.shortLinkBrowserStats(LinkBrowserStatsDO.builder()
                .browser(statsRecord.getBrowser()).cnt(1).gid(gid)
                .date(currentDate).fullShortUrl(fullShortUrl).build());
        linkDeviceStatsMapper.shortLinkDeviceStats(LinkDeviceStatsDO.builder()
                .device(statsRecord.getDevice()).cnt(1).gid(gid)
                .date(currentDate).fullShortUrl(fullShortUrl).build());
        linkNetworkStatsMapper.shortLinkNetworkStats(LinkNetworkStatsDO.builder()
                .network(statsRecord.getNetwork()).cnt(1).gid(gid)
                .date(currentDate).fullShortUrl(fullShortUrl).build());

        linkAccessLogsMapper.insert(LinkAccessLogsDO.builder()
                .ip(statsRecord.getRemoteAddr()).browser(statsRecord.getBrowser())
                .user(statsRecord.getUv()).os(statsRecord.getOs())
                .network(statsRecord.getNetwork()).device(statsRecord.getDevice())
                .locale(StrUtil.join("-", "中国", actualProvince, actualCity))
                .gid(gid).fullShortUrl(fullShortUrl).build());

        int uv = Boolean.TRUE.equals(statsRecord.getUvFirstFlag()) ? 1 : 0;
        int uip = Boolean.TRUE.equals(statsRecord.getUipFirstFlag()) ? 1 : 0;
        shortLinkMapper.incrementStats(gid, fullShortUrl, 1, uv, uip);
        linkStatsTodayMapper.shortLinkStatsToday(LinkStatsTodayDO.builder()
                .todayPv(1).todayUv(uv).todayUIp(uip)
                .gid(gid).fullShortUrl(fullShortUrl).date(currentDate).build());
    }
}
