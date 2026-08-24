package com.nageoffer.shortlink.admin.dto.req;


import lombok.Data;

/**
 * 短链接分组修改参数
 */
@Data
public class ShortLinkGroupUpdateReqDTO {
    private String name;
    private String gid;
}
