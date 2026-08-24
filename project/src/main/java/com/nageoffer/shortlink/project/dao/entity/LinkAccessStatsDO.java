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
import java.time.LocalDate;

/**
 * 基础访问统计持久化实体，对应数据库表 {@code t_link_access_stats}。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_link_access_stats")
public class LinkAccessStatsDO extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分组标识 */
    private String gid;

    /** 完整短链接 */
    private String fullShortUrl;

    /** 日期 */
    @TableField("`date`")
    private LocalDate date;

    /** 访问量 */
    private Integer pv;

    /** 独立访问数 */
    private Integer uv;

    /** 独立 IP 数 */
    private Integer uip;

    /** 小时 */
    @TableField("`hour`")
    private Integer hour;

    /** 星期 */
    @TableField("`weekday`")
    private Integer weekday;
}
