package com.nageoffer.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nageoffer.shortlink.project.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短链接持久化实体，对应数据库表 {@code t_link}。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_link")
public class ShortLinkDO extends BaseDO implements Serializable {

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

    /** 点击次数 */
    private Integer clickNum;

    /** 分组标识 */
    private String gid;

    /** 启用标识：0-启用，1-未启用 */
    private Integer enableStatus;

    /** 创建类型：0-接口创建，1-管理后台创建 */
    private Integer createdType;

    /** 有效期类型：0-永久有效，1-用户自定义 */
    private Integer validDateType;

    /** 有效期 */
    private LocalDateTime validDate;

    /** 描述 */
    @TableField("`describe`")
    private String describe;

    /**
     * 网站标识
     */
    private String favicon;

    /**
     * 历史参数
     */
    private Integer totalPv;
    private Integer totalUv;
    private Integer totalUIp;
}
