package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkDeviceStatsDO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设备访问统计持久层。
 */
public interface LinkDeviceStatsMapper extends BaseMapper<LinkDeviceStatsDO> {

    /**
     * 记录设备访问统计；同一短链接、分组、日期和设备已有记录时，累加访问量。
     *
     * @param linkDeviceStatsDO 设备访问统计数据
     */
    @Insert("INSERT INTO t_link_device_stats " +
            "(full_short_url, gid, `date`, cnt, device, create_time, update_time, del_flag) " +
            "VALUES (#{linkDeviceStats.fullShortUrl}, #{linkDeviceStats.gid}, " +
            "#{linkDeviceStats.date}, #{linkDeviceStats.cnt}, #{linkDeviceStats.device}, " +
            "NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE " +
            "cnt = cnt + #{linkDeviceStats.cnt}, " +
            "update_time = NOW()")
    void shortLinkDeviceStats(@Param("linkDeviceStats") LinkDeviceStatsDO linkDeviceStatsDO);

    @Select("<script>SELECT device, SUM(cnt) cnt FROM t_link_device_stats " +
            "WHERE gid = #{param.gid} " +
            "<if test=\"param.fullShortUrl != null and param.fullShortUrl != ''\">" +
            "AND full_short_url = #{param.fullShortUrl} </if>" +
            "AND `date` BETWEEN #{param.startDate} AND #{param.endDate} " +
            "GROUP BY device ORDER BY cnt DESC</script>")
    List<LinkDeviceStatsDO> listDeviceStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
}
