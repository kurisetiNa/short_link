package com.nageoffer.shortlink.admin.remote;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.nageoffer.shortlink.admin.remote.dto.req.*;
import com.nageoffer.shortlink.admin.remote.dto.resp.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 短链接中台远程调用服务。 */
@FeignClient("short-link-project")
public interface ShortLinkActualRemoteService {
    @PostMapping("/api/shortlink/v1/create")
    Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam);

    @PostMapping("/api/shortlink/v1/create/batch")
    Result<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam);

    @PostMapping("/api/shortlink/v1/update")
    Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam);

    @GetMapping("/api/shortlink/v1/page")
    Result<Page<ShortLinkPageRespDTO>> pageShortLink(@RequestParam("gid") String gid,
                                                     @RequestParam("orderTag") String orderTag,
                                                     @RequestParam("current") Long current,
                                                     @RequestParam("size") Long size);

    @GetMapping("/api/shortlink/v1/count")
    Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam);

    @GetMapping("/api/shortlink/v1/title")
    Result<String> getTitleByUrl(@RequestParam("url") String url);

    @PostMapping("/api/shortlink/v1/recycle_bin/save")
    void saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam);

    @GetMapping("/api/shortlink/v1/recycle_bin/page")
    Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(@RequestParam("gidList") List<String> gidList,
                                                               @RequestParam("current") Long current,
                                                               @RequestParam("size") Long size);

    @PostMapping("/api/shortlink/v1/recycle_bin/recover")
    void recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam);

    @PostMapping("/api/shortlink/v1/recycle_bin/remove")
    void removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam);

    @GetMapping("/api/shortlink/v1/stats")
    Result<ShortLinkStatsRespDTO> oneShortLinkStats(@RequestParam("fullShortUrl") String fullShortUrl,
                                                    @RequestParam("gid") String gid,
                                                    @RequestParam("startDate") String startDate,
                                                    @RequestParam("endDate") String endDate);

    @GetMapping("/api/shortlink/v1/stats/group")
    Result<ShortLinkStatsRespDTO> groupShortLinkStats(@RequestParam("gid") String gid,
                                                      @RequestParam("startDate") String startDate,
                                                      @RequestParam("endDate") String endDate);

    @GetMapping("/api/shortlink/v1/stats/accessRecord")
    Result<Page<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(
            @RequestParam("fullShortUrl") String fullShortUrl, @RequestParam("gid") String gid,
            @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
            @RequestParam("current") Long current, @RequestParam("size") Long size);

    @GetMapping("/api/shortlink/v1/stats/accessRecord/group")
    Result<Page<ShortLinkStatsAccessRecordRespDTO>> groupShortLinkStatsAccessRecord(
            @RequestParam("gid") String gid, @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate, @RequestParam("current") Long current,
            @RequestParam("size") Long size);
}
