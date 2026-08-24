package com.nageoffer.shortlink.admin.remote.dto.resp;

import lombok.Data;

import java.util.List;

/**
 * 短链接监控响应参数。
 */
@Data
public class ShortLinkStatsRespDTO {
    private Integer pv;
    private Integer uv;
    private Integer uip;
    private List<AccessDaily> daily;
    private List<LocaleStats> localeCnStats;
    private List<Integer> hourStats;
    private List<TopIpStats> topIpStats;
    private List<Integer> weekdayStats;
    private List<BrowserStats> browserStats;
    private List<OsStats> osStats;
    private List<UvStats> uvTypeStats;
    private List<DeviceStats> deviceStats;
    private List<NetworkStats> networkStats;

    @Data
    public static class AccessDaily {
        private String date;
        private Integer pv;
        private Integer uv;
        private Integer uip;
    }

    @Data
    public static class LocaleStats {
        private String locale;
        private Integer cnt;
        private Double ratio;
    }

    @Data
    public static class TopIpStats {
        private String ip;
        private Integer cnt;
    }

    @Data
    public static class BrowserStats {
        private String browser;
        private Integer cnt;
        private Double ratio;
    }

    @Data
    public static class OsStats {
        private String os;
        private Integer cnt;
        private Double ratio;
    }

    @Data
    public static class UvStats {
        private String uvType;
        private Integer cnt;
        private Double ratio;
    }

    @Data
    public static class DeviceStats {
        private String device;
        private Integer cnt;
        private Double ratio;
    }

    @Data
    public static class NetworkStats {
        private String network;
        private Integer cnt;
        private Double ratio;
    }
}
