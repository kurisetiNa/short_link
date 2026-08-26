package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkLocaleStatsDO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select("<script>SELECT province, SUM(cnt) cnt FROM t_link_locale_stats " +
            "WHERE gid = #{param.gid} " +
            "<if test=\"param.fullShortUrl != null and param.fullShortUrl != ''\">" +
            "AND full_short_url = #{param.fullShortUrl} </if>" +
            "AND `date` BETWEEN #{param.startDate} AND #{param.endDate} " +
            "GROUP BY province ORDER BY cnt DESC</script>")
    List<LinkLocaleStatsDO> listLocaleByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
}
