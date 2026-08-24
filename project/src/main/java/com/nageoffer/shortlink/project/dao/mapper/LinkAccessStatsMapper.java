package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select("SELECT `date`, SUM(pv) pv, SUM(uv) uv, SUM(uip) uip " +
            "FROM t_link_access_stats WHERE full_short_url = #{param.fullShortUrl} " +
            "AND gid = #{param.gid} AND `date` BETWEEN #{param.startDate} AND #{param.endDate} " +
            "GROUP BY `date` ORDER BY `date`")
    List<LinkAccessStatsDO> listStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    @Select("SELECT `hour`, SUM(pv) pv FROM t_link_access_stats " +
            "WHERE full_short_url = #{param.fullShortUrl} AND gid = #{param.gid} " +
            "AND `date` BETWEEN #{param.startDate} AND #{param.endDate} GROUP BY `hour`")
    List<LinkAccessStatsDO> listHourStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    @Select("SELECT `weekday`, SUM(pv) pv FROM t_link_access_stats " +
            "WHERE full_short_url = #{param.fullShortUrl} AND gid = #{param.gid} " +
            "AND `date` BETWEEN #{param.startDate} AND #{param.endDate} GROUP BY `weekday`")
    List<LinkAccessStatsDO> listWeekdayStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
}
