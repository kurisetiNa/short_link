package com.nageoffer.shortlink.project.dto.biz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 短链接访问统计记录。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsRecordDTO {

    private String fullShortUrl;
    private String remoteAddr;
    private String os;
    private String browser;
    private String device;
    private String network;
    private String uv;
    private Boolean uvFirstFlag;
    private Boolean uipFirstFlag;

    /** 延迟队列消息唯一标识。 */
    private String keys;
}
