package com.nageoffer.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 短链接监控响应参数。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsAccessRecordRespDTO {

    /** 访客类型*/
    private String uvType;

    /** 浏览器 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** IP */
    private String ip;

    /** 网络 */
    private String network;

    /** 设备 */
    private String device;

    /** 地区 */
    private String locale;

    /** 时间*/

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    private String user;
}
