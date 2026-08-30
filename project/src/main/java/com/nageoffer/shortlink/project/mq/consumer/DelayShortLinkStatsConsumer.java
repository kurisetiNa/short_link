package com.nageoffer.shortlink.project.mq.consumer;

import com.nageoffer.shortlink.project.common.convention.exception.ServiceException;
import com.nageoffer.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.nageoffer.shortlink.project.mq.idempotent.MessageQueueIdempotentHandler;
import com.nageoffer.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static com.nageoffer.shortlink.project.common.constant.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;

/** 短链接统计延迟队列消费者。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsConsumer implements InitializingBean {

    private final RedissonClient redissonClient;
    private final ShortLinkService shortLinkService;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    public void onMessage() {
        Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("delay_short-link_stats_consumer");
            thread.setDaemon(true);
            return thread;
        }).execute(() -> {
            RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque =
                    redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
            RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue =
                    redissonClient.getDelayedQueue(blockingDeque);
            for (;;) {
                try {
                    ShortLinkStatsRecordDTO statsRecord = delayedQueue.poll();
                    if (statsRecord == null) {
                        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(500));
                        continue;
                    }
                    consume(statsRecord);
                } catch (Throwable ex) {
                    log.error("延迟记录短链接统计消费异常", ex);
                    LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
                }
            }
        });
    }

    private void consume(ShortLinkStatsRecordDTO statsRecord) {
        String messageKey = statsRecord.getKeys();
        if (!messageQueueIdempotentHandler.isMessageProcessed(messageKey)) {
            if (messageQueueIdempotentHandler.isAccomplish(messageKey)) {
                return;
            }
            throw new ServiceException("消息未完成流程，需要消息队列重试");
        }
        try {
            // 重新投递到 Redis Stream，由主消费者再次尝试获取读锁。
            shortLinkService.shortLinkStats(null, null, statsRecord);
            messageQueueIdempotentHandler.setAccomplish(messageKey);
        } catch (Throwable ex) {
            messageQueueIdempotentHandler.delMessageProcessed(messageKey);
            throw ex;
        }
    }

    @Override
    public void afterPropertiesSet() {
        onMessage();
    }
}
