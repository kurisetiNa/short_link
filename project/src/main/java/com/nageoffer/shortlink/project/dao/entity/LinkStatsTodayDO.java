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
 * 短链接今日访问统计持久化实体，对应数据库表 {@code t_link_stats_today_0}。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_link_stats_today")
public class LinkStatsTodayDO extends BaseDO implements Serializable {

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

    /** 今日访问量 */
    private Integer todayPv;

    /** 今日独立访客数 */
    private Integer todayUv;

    /** 今日独立 IP 数 */
    private Integer todayIpCount;
}
