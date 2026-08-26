package com.nageoffer.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 分组短链接访问记录分页请求参数。
 */
@Data
public class ShortLinkGroupStatsAccessRecordReqDTO extends Page<Object> {

    /** 分组标识 */
    private String gid;

    /** 开始日期 */
    private String startDate;

    /** 结束日期 */
    private String endDate;
}
