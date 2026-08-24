package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkAccessLogsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

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
}
