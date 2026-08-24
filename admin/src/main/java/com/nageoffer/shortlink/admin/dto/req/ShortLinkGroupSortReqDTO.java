package com.nageoffer.shortlink.admin.dto.req;


import lombok.Data;

/**
 * 短链接分组排序参数
 */
@Data
public class ShortLinkGroupSortReqDTO {
    private String gid;
    private Integer sortOrder;

}
