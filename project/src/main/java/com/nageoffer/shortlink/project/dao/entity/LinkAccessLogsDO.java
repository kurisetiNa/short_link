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

/**
 * 短链接访问日志持久化实体，对应数据库表 {@code t_link_access_logs}。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_link_access_logs")
public class LinkAccessLogsDO extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 完整短链接 */
    private String fullShortUrl;

    /** 分组标识 */
    private String gid;

    /** 用户信息 */
    @TableField("`user`")
    private String user;

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
}
