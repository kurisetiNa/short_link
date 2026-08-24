package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkAccessLogsDO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 短链接访问日志持久层。
 */
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogsDO> {

    /**
     * 记录短链接访问日志。
     *
     * @param linkAccessLogsDO 短链接访问日志数据
     */
    @Insert("INSERT INTO t_link_access_logs " +
            "(full_short_url, gid, `user`, browser, os, ip, create_time, update_time, del_flag) " +
            "VALUES (#{linkAccessLogs.fullShortUrl}, #{linkAccessLogs.gid}, " +
            "#{linkAccessLogs.user}, #{linkAccessLogs.browser}, #{linkAccessLogs.os}, " +
            "#{linkAccessLogs.ip}, NOW(), NOW(), 0)")
    void shortLinkAccessLogs(@Param("linkAccessLogs") LinkAccessLogsDO linkAccessLogsDO);

    @Select("SELECT ip, COUNT(*) `count` FROM t_link_access_logs " +
            "WHERE full_short_url = #{param.fullShortUrl} AND gid = #{param.gid} " +
            "AND create_time >= CONCAT(#{param.startDate}, ' 00:00:00') " +
            "AND create_time < DATE_ADD(#{param.endDate}, INTERVAL 1 DAY) " +
            "GROUP BY ip ORDER BY `count` DESC LIMIT 5")
    List<Map<String, Object>> listTopIpByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    @Select("SELECT " +
            "SUM(CASE WHEN first_access_date < #{param.startDate} THEN 1 ELSE 0 END) oldUserCnt, " +
            "SUM(CASE WHEN first_access_date BETWEEN #{param.startDate} AND #{param.endDate} THEN 1 ELSE 0 END) newUserCnt " +
            "FROM (SELECT `user`, MIN(DATE(create_time)) first_access_date FROM t_link_access_logs " +
            "WHERE full_short_url = #{param.fullShortUrl} AND gid = #{param.gid} GROUP BY `user`) access_users")
    Map<String, Object> findUvTypeByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
}
