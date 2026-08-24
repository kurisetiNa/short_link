package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkNetworkStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 网络访问统计持久层。
 */
public interface LinkNetworkStatsMapper extends BaseMapper<LinkNetworkStatsDO> {

    /**
     * 记录网络访问统计；同一短链接、分组、日期和网络类型已有记录时，累加访问量。
     *
     * @param linkNetworkStatsDO 网络访问统计数据
     */
    @Insert("INSERT INTO t_link_network_stats " +
            "(full_short_url, gid, `date`, cnt, network, create_time, update_time, del_flag) " +
            "VALUES (#{linkNetworkStats.fullShortUrl}, #{linkNetworkStats.gid}, " +
            "#{linkNetworkStats.date}, #{linkNetworkStats.cnt}, #{linkNetworkStats.network}, " +
            "NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE " +
            "cnt = cnt + #{linkNetworkStats.cnt}, " +
            "update_time = NOW()")
    void shortLinkNetworkStats(@Param("linkNetworkStats") LinkNetworkStatsDO linkNetworkStatsDO);
}
