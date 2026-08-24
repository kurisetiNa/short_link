package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkLocaleStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 地区访问统计持久层。
 */
public interface LinkLocaleStatsMapper extends BaseMapper<LinkLocaleStatsDO> {

    /**
     * 记录地区访问统计；同一短链接、分组、日期和地区已有记录时，累加访问量。
     *
     * @param linkLocaleStatsDO 地区访问统计数据
     */
    @Insert("INSERT INTO t_link_locale_stats " +
            "(full_short_url, gid, `date`, cnt, province, city, adcode, country, " +
            "create_time, update_time, del_flag) " +
            "VALUES (#{linkLocaleStats.fullShortUrl}, #{linkLocaleStats.gid}, " +
            "#{linkLocaleStats.date}, #{linkLocaleStats.cnt}, #{linkLocaleStats.province}, " +
            "#{linkLocaleStats.city}, #{linkLocaleStats.adcode}, #{linkLocaleStats.country}, " +
            "NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE " +
            "cnt = cnt + #{linkLocaleStats.cnt}, " +
            "city = #{linkLocaleStats.city}, " +
            "country = #{linkLocaleStats.country}, " +
            "update_time = NOW()")
    void shortLinkLocaleState(@Param("linkLocaleStats") LinkLocaleStatsDO linkLocaleStatsDO);
}
