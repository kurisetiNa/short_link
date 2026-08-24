package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkOsStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 操作系统访问统计持久层。
 */
public interface LinkOsStatsMapper extends BaseMapper<LinkOsStatsDO> {

    /**
     * 记录操作系统访问统计；同一短链接、分组、日期和操作系统已有记录时，累加访问量。
     *
     * @param linkOsStatsDO 操作系统访问统计数据
     */
    @Insert("INSERT INTO t_link_os_stats " +
            "(full_short_url, gid, `date`, cnt, os, create_time, update_time, del_flag) " +
            "VALUES (#{linkOsStats.fullShortUrl}, #{linkOsStats.gid}, #{linkOsStats.date}, " +
            "#{linkOsStats.cnt}, #{linkOsStats.os}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE " +
            "cnt = cnt + #{linkOsStats.cnt}, " +
            "update_time = NOW()")
    void shortLinkOsStats(@Param("linkOsStats") LinkOsStatsDO linkOsStatsDO);
}
