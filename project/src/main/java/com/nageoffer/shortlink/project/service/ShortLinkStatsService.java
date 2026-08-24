package com.nageoffer.shortlink.project.service;

import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.nageoffer.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

/**
 * 短链接监控接口层。
 */
public interface ShortLinkStatsService {

    /**
     * 查询单个短链接在指定日期范围内的监控数据。
     *
     * @param requestParam 查询参数
     * @return 监控统计
     */
    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);
}
