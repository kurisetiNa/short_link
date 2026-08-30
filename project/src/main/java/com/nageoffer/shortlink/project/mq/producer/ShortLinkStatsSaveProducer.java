package com.nageoffer.shortlink.project.mq.producer;

import cn.hutool.json.JSONUtil;
import com.nageoffer.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.nageoffer.shortlink.project.common.constant.RedisKeyConstant.SHORT_LINK_STATS_STREAM_TOPIC_KEY;

/** 短链接访统计 Redis Stream 生产者。 */
@Component
@RequiredArgsConstructor
public class ShortLinkStatsSaveProducer {

    private final StringRedisTemplate stringRedisTemplate;

    public void send(String fullShortUrl, ShortLinkStatsRecordDTO statsRecord) {
        Map<String, String> message = new HashMap<>();
        message.put("fullShortUrl", fullShortUrl);
        message.put("statsRecord", JSONUtil.toJsonStr(statsRecord));
        stringRedisTemplate.opsForStream().add(SHORT_LINK_STATS_STREAM_TOPIC_KEY, message);
    }
}
