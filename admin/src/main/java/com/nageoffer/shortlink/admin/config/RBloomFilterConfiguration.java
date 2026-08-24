package com.nageoffer.shortlink.admin.config;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dao.mapper.UserMapper;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置
 */
@Configuration
public class RBloomFilterConfiguration {

    /**
     * 防止用户注册查询数据库的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userRegisterCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(100000000, 0.001);
        return cachePenetrationBloomFilter;
    }

    /**
     * 应用启动时将数据库中的历史用户名同步到布隆过滤器。
     */
    @Bean
    public ApplicationRunner userRegisterBloomFilterInitializer(
            RBloomFilter<String> userRegisterCachePenetrationBloomFilter,
            UserMapper userMapper) {
        return args -> userMapper.selectList(
                        Wrappers.lambdaQuery(UserDO.class).select(UserDO::getUsername))
                .stream()
                .map(UserDO::getUsername)
                .filter(username -> username != null && !username.isBlank())
                .forEach(userRegisterCachePenetrationBloomFilter::add);
    }
}
