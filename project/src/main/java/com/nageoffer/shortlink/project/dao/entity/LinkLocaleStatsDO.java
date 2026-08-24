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
 * 地区访问统计持久化实体，对应数据库表 {@code t_link_locale_stats}。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_link_locale_stats")
public class LinkLocaleStatsDO extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 完整短链接 */
    private String fullShortUrl;

    /** 分组标识 */
    private String gid;

    /** 日期 */
    @TableField("`date`")
    private LocalDate date;

    /** 访问量 */
    private Integer cnt;

    /** 省份名称 */
    private String province;

    /** 市名称 */
    private String city;

    /** 城市编码 */
    private String adcode;

    /** 国家标识 */
    private String country;
}
