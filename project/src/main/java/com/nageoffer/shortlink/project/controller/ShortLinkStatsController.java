package com.nageoffer.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nageoffer.shortlink.project.common.convention.result.Result;
import com.nageoffer.shortlink.project.common.convention.result.Results;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.nageoffer.shortlink.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import com.nageoffer.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import com.nageoffer.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortlink/v1/stats")
public class ShortLinkStatsController {

    private final ShortLinkStatsService shortLinkStatsService;

    /**
     * 单个链接指定时间内监控数据
     */
    @GetMapping
    public Result<ShortLinkStatsRespDTO> oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return Results.success(shortLinkStatsService.oneShortLinkStats(requestParam));
    }

    /**
     * 分组内短链接指定时间内监控数据
     */
    @GetMapping("/group")
    public Result<ShortLinkStatsRespDTO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        return Results.success(shortLinkStatsService.groupShortLinkStats(requestParam));
    }
    /**
     * 单个链接指定时间内访问记录监控数据
     */
    @GetMapping("/accessRecord")
    public Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkAccessRecordStats(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return Results.success(shortLinkStatsService.shortLinkAccessRecordStats(requestParam));
    }
}
