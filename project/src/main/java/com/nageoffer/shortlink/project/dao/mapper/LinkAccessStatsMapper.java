package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkAccessStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 基础访问统计持久层。
 */
public interface LinkAccessStatsMapper extends BaseMapper<LinkAccessStatsDO> {

    /**
     * 新增短链接访问统计；同一短链接在同一分组、日期和小时已有记录时，累加访问数据。
     *
     * @param linkAccessStats 访问统计数据
     */
    @Insert("INSERT INTO t_link_access_stats " +
            "(full_short_url, gid, `date`, pv, uv, uip, `hour`, `weekday`, create_time, update_time, del_flag) " +
            "VALUES (#{linkAccessStats.fullShortUrl}, #{linkAccessStats.gid}, #{linkAccessStats.date}, " +
            "#{linkAccessStats.pv}, #{linkAccessStats.uv}, #{linkAccessStats.uip}, " +
            "#{linkAccessStats.hour}, #{linkAccessStats.weekday}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE " +
            "pv = pv + #{linkAccessStats.pv}, " +
            "uv = uv + #{linkAccessStats.uv}, " +
            "uip = uip + #{linkAccessStats.uip}, " +
            "update_time = NOW()")
    void shortLinkStats(@Param("linkAccessStats") LinkAccessStatsDO linkAccessStats);
}
