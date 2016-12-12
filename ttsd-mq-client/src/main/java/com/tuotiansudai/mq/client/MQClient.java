package com.tuotiansudai.mq.client;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.tools.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class MQClient {
    private static final Logger logger = LoggerFactory.getLogger(MQClient.class);
    private static final int ERROR_COUNT_THRESHOLD = 10; // 连续失败10次则认为异常
    protected static final int TIME_SLICE_SECONDS = 10; // 每次取消息最多等10秒
    public static final String HEALTH_REPORT_REDIS_KEY = "worker:health:report";

    protected final RedisClient redisClient;
    protected final Jedis sharedJedis;

    protected volatile boolean continueRunning = true;

    private final Map<MessageQueue, Object> sleepingQueueLockMap = new HashMap<>();

    public MQClient(RedisClient redisClient) {
        this.redisClient = redisClient;
        this.sharedJedis = redisClient.newJedis();
    }

    public abstract void publishMessage(final MessageTopic topic, final String message);

    public abstract void sendMessage(final MessageQueue queue, final String message);

    public abstract void subscribe(final MessageQueue queue, final Consumer<String> consumer);

    public final void stopSubscribe() {
        logger.info("[MQ] prepare to stop");
        continueRunning = false;
        stopAllSubscribeWaiting();
    }

    protected final void reportConsumerStatus(MessageQueue queue, boolean ok) {
        synchronized (sharedJedis) {
            if (ok) {
                sharedJedis.hset(HEALTH_REPORT_REDIS_KEY, queue.getQueueName(), String.valueOf(Clock.systemUTC().millis()));
            }
        }
    }

    // 失败超过阈值则暂停某队列的消费30分钟
    protected final void pauseSubscribeIfExceeds(MessageQueue queue, int errorCount) {
        // 计数超过阈值
        if (errorCount < ERROR_COUNT_THRESHOLD) {
            return;
        }
        if (queue == null) {
            return;
        }
        Object lockObject = new Object();
        synchronized (sleepingQueueLockMap) {
            if (!continueRunning) {
                return;
            }
            if (sleepingQueueLockMap.containsKey(queue)) {
                return;
            }
            sleepingQueueLockMap.put(queue, lockObject);
        }
        synchronized (lockObject) {
            try {
                logger.warn("[MQ] pause to listen queue {} ", queue.getQueueName());
                lockObject.wait(TIME_SLICE_SECONDS * 6 * 30 * 1000L);
                if (continueRunning) {
                    logger.info("[MQ] resume to listen queue {}", queue.getQueueName());
                } else {
                    logger.info("[MQ] stop to listen queue {}", queue.getQueueName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sleepingQueueLockMap.remove(queue);
    }

    private void stopAllSubscribeWaiting() {
        synchronized (sleepingQueueLockMap) {
            sleepingQueueLockMap.keySet().forEach(queue -> {
                Object lockObject = sleepingQueueLockMap.get(queue);
                synchronized (lockObject) {
                    logger.info("[MQ] break thread wait of queue consumer {}", queue.getQueueName());
                    lockObject.notify();
                }
            });
        }
    }
}
