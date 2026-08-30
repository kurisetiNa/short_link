package com.nageoffer.shortlink.project.mq.idempotent;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/** Redis Stream 消息幂等处理器。 */
@Component
@RequiredArgsConstructor
public class MessageQueueIdempotentHandler {

    private static final String IDEMPOTENT_KEY_PREFIX = "short-link:idempotent:";
    private static final long IDEMPOTENT_KEY_TIMEOUT_MINUTES = 2L;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 尝试将消息标记为正在处理。
     *
     * @return true 表示本次成功获得处理权；false 表示标记已存在
     */
    public boolean isMessageProcessed(String messageId) {
        String key = buildKey(messageId);
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(
                key, "0", IDEMPOTENT_KEY_TIMEOUT_MINUTES, TimeUnit.MINUTES));
    }

    /** 判断消息是否已经完成整个消费流程。 */
    public boolean isAccomplish(String messageId) {
        return Objects.equals(stringRedisTemplate.opsForValue().get(buildKey(messageId)), "1");
    }

    /** 将消息标记为处理完成。 */
    public void setAccomplish(String messageId) {
        stringRedisTemplate.opsForValue().set(
                buildKey(messageId), "1", IDEMPOTENT_KEY_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    /** 处理失败时删除占位标记，允许后续重试。 */
    public void delMessageProcessed(String messageId) {
        stringRedisTemplate.delete(buildKey(messageId));
    }

    private String buildKey(String messageId) {
        return IDEMPOTENT_KEY_PREFIX + messageId;
    }
}
