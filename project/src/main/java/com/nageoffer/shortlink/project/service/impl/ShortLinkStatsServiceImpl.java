package com.nageoffer.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.shortlink.project.common.convention.exception.ClientException;
import com.nageoffer.shortlink.project.dao.entity.LinkAccessLogsDO;
import com.nageoffer.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.nageoffer.shortlink.project.dao.entity.LinkDeviceStatsDO;
import com.nageoffer.shortlink.project.dao.entity.LinkLocaleStatsDO;
import com.nageoffer.shortlink.project.dao.entity.LinkNetworkStatsDO;
import com.nageoffer.shortlink.project.dao.mapper.LinkAccessLogsMapper;
import com.nageoffer.shortlink.project.dao.mapper.LinkAccessStatsMapper;
import com.nageoffer.shortlink.project.dao.mapper.LinkBrowserStatsMapper;
import com.nageoffer.shortlink.project.dao.mapper.LinkDeviceStatsMapper;
import com.nageoffer.shortlink.project.dao.mapper.LinkLocaleStatsMapper;
import com.nageoffer.shortlink.project.dao.mapper.LinkNetworkStatsMapper;
import com.nageoffer.shortlink.project.dao.mapper.LinkOsStatsMapper;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.nageoffer.shortlink.project.dto.resp.*;
import com.nageoffer.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 短链接监控实现层。
 */
@Service
@RequiredArgsConstructor
public class ShortLinkStatsServiceImpl implements ShortLinkStatsService {

    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;

    @Override
    public ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return buildStats(requestParam);
    }

    @Override
    public ShortLinkStatsRespDTO groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        ShortLinkStatsReqDTO statsRequestParam = new ShortLinkStatsReqDTO();
        statsRequestParam.setGid(requestParam.getGid());
        statsRequestParam.setStartDate(requestParam.getStartDate());
        statsRequestParam.setEndDate(requestParam.getEndDate());
        return buildStats(statsRequestParam);
    }

    private ShortLinkStatsRespDTO buildStats(ShortLinkStatsReqDTO requestParam) {
        LocalDate startDate = parseDate(requestParam.getStartDate(), "开始日期");
        LocalDate endDate = parseDate(requestParam.getEndDate(), "结束日期");
        if (endDate.isBefore(startDate)) {
            throw new ClientException("结束日期不能早于开始日期");
        }

        List<LinkAccessStatsDO> accessStats = linkAccessStatsMapper.listStatsByShortLink(requestParam);
        int pv = accessStats.stream().map(LinkAccessStatsDO::getPv).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
        int uv = accessStats.stream().map(LinkAccessStatsDO::getUv).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
        int uip = accessStats.stream().map(LinkAccessStatsDO::getUip).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();

        List<ShortLinkStatsAccessDailyRespDTO> daily = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> accessStats.stream()
                        .filter(item -> Objects.equals(date, item.getDate()))
                        .findFirst()
                        .map(item -> ShortLinkStatsAccessDailyRespDTO.builder()
                                .date(date.toString()).pv(value(item.getPv())).uv(value(item.getUv())).uip(value(item.getUip())).build())
                        .orElseGet(() -> ShortLinkStatsAccessDailyRespDTO.builder()
                                .date(date.toString()).pv(0).uv(0).uip(0).build()))
                .toList();

        List<LinkLocaleStatsDO> localeRows = linkLocaleStatsMapper.listLocaleByShortLink(requestParam);
        int localeTotal = localeRows.stream().map(LinkLocaleStatsDO::getCnt).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
        List<ShortLinkStatsLocaleCNRespDTO> localeCnStats = localeRows.stream()
                .map(item -> ShortLinkStatsLocaleCNRespDTO.builder().locale(item.getProvince())
                        .cnt(value(item.getCnt())).ratio(ratio(value(item.getCnt()), localeTotal)).build())
                .toList();

        List<LinkAccessStatsDO> hourRows = linkAccessStatsMapper.listHourStatsByShortLink(requestParam);
        List<Integer> hourStats = new ArrayList<>(24);
        for (int hour = 0; hour < 24; hour++) {
            int currentHour = hour;
            hourStats.add(hourRows.stream().filter(item -> Objects.equals(item.getHour(), currentHour))
                    .findFirst().map(LinkAccessStatsDO::getPv).map(ShortLinkStatsServiceImpl::value).orElse(0));
        }

        List<ShortLinkStatsTopIpRespDTO> topIpStats = linkAccessLogsMapper.listTopIpByShortLink(requestParam).stream()
                .map(item -> ShortLinkStatsTopIpRespDTO.builder().ip(string(item.get("ip")))
                        .cnt(number(item.get("count"))).build())
                .toList();

        List<LinkAccessStatsDO> weekdayRows = linkAccessStatsMapper.listWeekdayStatsByShortLink(requestParam);
        List<Integer> weekdayStats = new ArrayList<>(7);
        for (int weekday = 1; weekday <= 7; weekday++) {
            int currentWeekday = weekday;
            weekdayStats.add(weekdayRows.stream().filter(item -> Objects.equals(item.getWeekday(), currentWeekday))
                    .findFirst().map(LinkAccessStatsDO::getPv).map(ShortLinkStatsServiceImpl::value).orElse(0));
        }

        List<Map<String, Object>> browserRows = linkBrowserStatsMapper.listBrowserStatsByShortLink(requestParam);
        int browserTotal = sumCount(browserRows);
        List<ShortLinkStatsBrowserRespDTO> browserStats = browserRows.stream()
                .map(item -> ShortLinkStatsBrowserRespDTO.builder().browser(string(item.get("browser")))
                        .cnt(number(item.get("count"))).ratio(ratio(number(item.get("count")), browserTotal)).build())
                .toList();

        List<Map<String, Object>> osRows = linkOsStatsMapper.listOsStatsByShortLink(requestParam);
        int osTotal = sumCount(osRows);
        List<ShortLinkStatsOsRespDTO> osStats = osRows.stream()
                .map(item -> ShortLinkStatsOsRespDTO.builder().os(string(item.get("os")))
                        .cnt(number(item.get("count"))).ratio(ratio(number(item.get("count")), osTotal)).build())
                .toList();

        Map<String, Object> uvTypes = linkAccessLogsMapper.findUvTypeByShortLink(requestParam);
        int oldUserCnt = uvTypes == null ? 0 : number(uvTypes.get("oldUserCnt"));
        int newUserCnt = uvTypes == null ? 0 : number(uvTypes.get("newUserCnt"));
        int uvTotal = oldUserCnt + newUserCnt;
        List<ShortLinkStatsUvRespDTO> uvTypeStats = List.of(
                ShortLinkStatsUvRespDTO.builder().uvType("newUser").cnt(newUserCnt).ratio(ratio(newUserCnt, uvTotal)).build(),
                ShortLinkStatsUvRespDTO.builder().uvType("oldUser").cnt(oldUserCnt).ratio(ratio(oldUserCnt, uvTotal)).build());

        List<LinkDeviceStatsDO> deviceRows = linkDeviceStatsMapper.listDeviceStatsByShortLink(requestParam);
        int deviceTotal = deviceRows.stream().map(LinkDeviceStatsDO::getCnt).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
        List<ShortLinkStatsDeviceRespDTO> deviceStats = deviceRows.stream()
                .map(item -> ShortLinkStatsDeviceRespDTO.builder().device(item.getDevice()).cnt(value(item.getCnt()))
                        .ratio(ratio(value(item.getCnt()), deviceTotal)).build()).toList();

        List<LinkNetworkStatsDO> networkRows = linkNetworkStatsMapper.listNetworkStatsByShortLink(requestParam);
        int networkTotal = networkRows.stream().map(LinkNetworkStatsDO::getCnt).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
        List<ShortLinkStatsNetworkRespDTO> networkStats = networkRows.stream()
                .map(item -> ShortLinkStatsNetworkRespDTO.builder().network(item.getNetwork()).cnt(value(item.getCnt()))
                        .ratio(ratio(value(item.getCnt()), networkTotal)).build()).toList();

        return ShortLinkStatsRespDTO.builder().pv(pv).uv(uv).uip(uip).daily(daily)
                .localeCnStats(localeCnStats).hourStats(hourStats).topIpStats(topIpStats)
                .weekdayStats(weekdayStats).browserStats(browserStats).osStats(osStats)
                .uvTypeStats(uvTypeStats).deviceStats(deviceStats).networkStats(networkStats).build();
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkAccessRecordStats(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return accessRecordStats(requestParam, requestParam.getGid(), requestParam.getFullShortUrl(),
                requestParam.getStartDate(), requestParam.getEndDate());
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> groupShortLinkAccessRecordStats(
            ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        return accessRecordStats(requestParam, requestParam.getGid(), null,
                requestParam.getStartDate(), requestParam.getEndDate());
    }

    private IPage<ShortLinkStatsAccessRecordRespDTO> accessRecordStats(
            Page<LinkAccessLogsDO> pageParam, String gid, String fullShortUrl, String startDate, String endDate) {
        LambdaQueryWrapper<LinkAccessLogsDO> queryWrapper = Wrappers.lambdaQuery(LinkAccessLogsDO.class)
                .eq(LinkAccessLogsDO::getGid, gid)
                .eq(fullShortUrl != null && !fullShortUrl.isBlank(), LinkAccessLogsDO::getFullShortUrl, fullShortUrl)
                .ge(LinkAccessLogsDO::getCreateTime, parseDate(startDate, "开始日期").atStartOfDay())
                .lt(LinkAccessLogsDO::getCreateTime, parseDate(endDate, "结束日期").plusDays(1).atStartOfDay())
                .eq(LinkAccessLogsDO::getDelFlag, 0)
                .orderByDesc(LinkAccessLogsDO::getCreateTime);
        IPage<LinkAccessLogsDO> linkAccessLogsDOIPage = linkAccessLogsMapper.selectPage(pageParam, queryWrapper);
        IPage<ShortLinkStatsAccessRecordRespDTO> actualResult = linkAccessLogsDOIPage.convert(
                each -> BeanUtil.toBean(each, ShortLinkStatsAccessRecordRespDTO.class));
        List<String> userAccessLogsList = actualResult.getRecords().stream()
                .map(ShortLinkStatsAccessRecordRespDTO::getUser)
                .toList();
        if (userAccessLogsList.isEmpty()) {
            return actualResult;
        }
        ShortLinkStatsReqDTO statsRequestParam = new ShortLinkStatsReqDTO();
        statsRequestParam.setFullShortUrl(fullShortUrl);
        statsRequestParam.setGid(gid);
        statsRequestParam.setStartDate(startDate);
        statsRequestParam.setEndDate(endDate);
        List<Map<String, Object>> uvTypeList = linkAccessLogsMapper.selectUvTypeByUsers(
                statsRequestParam, userAccessLogsList);
        actualResult.getRecords().forEach(each -> {
            String uvType = uvTypeList.stream()
                    .filter(item -> Objects.equals(each.getUser(), item.get("user")))
                    .findFirst()
                    .map(item -> item.get("uvType"))
                    .map(Object::toString)
                    .orElse("旧访客");
            each.setUvType(uvType);
        });

        return actualResult;
    }

    private static LocalDate parseDate(String date, String fieldName) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException | NullPointerException ex) {
            throw new ClientException(fieldName + "格式应为 yyyy-MM-dd");
        }
    }

    private static int sumCount(List<Map<String, Object>> rows) {
        return rows.stream().mapToInt(item -> number(item.get("count"))).sum();
    }

    private static int value(Integer value) {
        return value == null ? 0 : value;
    }

    private static int number(Object value) {
        return value instanceof Number number ? number.intValue() : value == null ? 0 : Integer.parseInt(value.toString());
    }

    private static String string(Object value) {
        return value == null ? "未知" : value.toString();
    }

    private static double ratio(int count, int total) {
        return total == 0 ? 0D : Math.round((double) count / total * 100D) / 100D;
    }
}
