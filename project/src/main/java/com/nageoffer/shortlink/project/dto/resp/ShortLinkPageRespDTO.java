package com.nageoffer.shortlink.project.dto.resp;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
public class ShortLinkPageRespDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 域名 */
    private String domain;

    /** 短链接 URI */
    private String shortUri;

    /** 完整短链接 */
    private String fullShortUrl;

    /** 原始链接 */
    private String originUrl;

    /** 分组标识 */
    private String gid;

    /** 有效期类型：0-永久有效，1-用户自定义 */
    private Integer validDateType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    /** 有效期 */
    private LocalDateTime validDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String createTime;

    /** 描述 */
    @TableField("`describe`")
    private String describe;

    /**
     * 网站标识
     */
    private String favicon;
}
