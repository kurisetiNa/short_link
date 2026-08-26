package com.nageoffer.shortlink.admin.remote.dto;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.nageoffer.shortlink.admin.remote.dto.req.*;
import com.nageoffer.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.nageoffer.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.nageoffer.shortlink.admin.remote.dto.resp.ShortLinkStatsRespDTO;
import com.nageoffer.shortlink.admin.remote.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ShortLinkRemoteService {


    /**
     * 创建短链接
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建响应
     */
    default Result<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO requestParam){
        String resultBodyStr=HttpUtil.post("http://127.0.0.1:8001/api/shortlink/v1/create",JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }


    /**
     * 修改短链接
     * @param requestParam 修改短链接请求参数
     * @return 短链接修改响应
     */
    default Result<Void> updateShortLink(ShortLinkUpdateReqDTO requestParam){
        String resultBodyStr = HttpUtil.post(
                "http://127.0.0.1:8001/api/shortlink/v1/update",
                JSON.toJSONString(requestParam)
        );
        return JSON.parseObject(resultBodyStr, new TypeReference<Result<Void>>() {
        });
    }

    /**
     * 分页查询短链接
     * @param requestParam 分页短链接请求参数
     * @return 分页短链接响应
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("orderTag", requestParam.getOrderTag());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());

        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/shortlink/v1/page", requestMap);

        return JSON.parseObject(resultPageStr, new TypeReference<Result<IPage<ShortLinkPageRespDTO>>>() {
        });
    }

    /**
     * 查询单个短链接监控数据。
     *
     * @param requestParam 监控查询参数
     * @return 监控统计响应
     */
    default Result<ShortLinkStatsRespDTO> oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("fullShortUrl", requestParam.getFullShortUrl());
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("startDate", requestParam.getStartDate());
        requestMap.put("endDate", requestParam.getEndDate());
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/shortlink/v1/stats", requestMap);
        return JSON.parseObject(resultStr, new TypeReference<Result<ShortLinkStatsRespDTO>>() {
        });
    }

    /**
     * 分页查询单个短链接访问记录。
     */
    default Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(
            ShortLinkStatsAccessRecordReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("fullShortUrl", requestParam.getFullShortUrl());
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("startDate", requestParam.getStartDate());
        requestMap.put("endDate", requestParam.getEndDate());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/shortlink/v1/stats/accessRecord", requestMap);
        return JSON.parseObject(resultStr,
                new TypeReference<Result<IPage<ShortLinkStatsAccessRecordRespDTO>>>() {
                });
    }


    /**
     * 查询分组短链接总量
     *
     * @param requestParam 分组短链接总量请求参数
     * @return 查询分组短链接总量响应
     */
    default Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(List<String> requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requestParam", requestParam);

        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/shortlink/v1/count", requestMap);

        return JSON.parseObject(resultPageStr, new TypeReference<>() {
        });
    }

    /**
     * 根据Url获取标题
     * @param url 目标网站地址
     * @return 网站标题
     */
    default Result<String> getTitleByUrl(@RequestParam("url") String url){
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/shortlink/v1/title?url=" + url);
        return JSON.parseObject(resultStr, new TypeReference<>() {
        });
    }

    /**
     * 保存回收站
     * @param requestParam
     */
    default void saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam){
        HttpUtil.post(
                "http://127.0.0.1:8001/api/shortlink/v1/recycle_bin/save",
                JSON.toJSONString(requestParam)
        );
    }

    /**
     * 分页查询短链接
     * @param requestParam 分页短链接请求参数
     * @return 分页短链接响应
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gidList",requestParam.getGidList());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());

        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/shortlink/v1/recycle_bin/page", requestMap);

        return JSON.parseObject(resultPageStr, new TypeReference<Result<IPage<ShortLinkPageRespDTO>>>() {
        });
    }

    /**
     * 恢复短链接
     * @param requestParam
     */
    default void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam){
        HttpUtil.post(
                "http://127.0.0.1:8001/api/shortlink/v1/recycle_bin/recover",
                JSON.toJSONString(requestParam)
        );
    }

    default  void removeRecycleBin(RecycleBinRemoveReqDTO requestParam){
        HttpUtil.post(
                "http://127.0.0.1:8001/api/shortlink/v1/recycle_bin/remove",
                JSON.toJSONString(requestParam)
        );
    }
}
