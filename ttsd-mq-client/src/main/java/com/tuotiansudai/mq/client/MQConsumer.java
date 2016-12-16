package com.tuotiansudai.mq.client;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.config.setting.MessageConsumerSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class MQConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MQConsumer.class);
    private static final String HEALTH_REPORT_REDIS_KEY = "worker:health:report";

    protected volatile boolean continueRunning = true;

    private final Map<MessageQueue, Object> sleepingQueueLockMap = new HashMap<>();

    private final MessageConsumerSetting consumerSetting;
    private final Jedis jedis;

    public MQConsumer(Jedis jedis, MessageConsumerSetting consumerSetting) {
        this.jedis = jedis;
        this.consumerSetting = consumerSetting;
    }

    public abstract void subscribe(final MessageQueue queue, final Consumer<String> consumer);

    public final void stopSubscribe() {
        logger.info("[MQ] prepare to stop");
        continueRunning = false;
        stopAllSubscribeWaiting();
    }

    protected final void reportConsumerStatus(MessageQueue queue, boolean ok) {
        synchronized (jedis) {
            if (ok) {
                jedis.hset(HEALTH_REPORT_REDIS_KEY, queue.getQueueName(), String.valueOf(Clock.systemUTC().millis()));
            }
        }
    }

    // 失败超过阈值则暂停某队列的消费30分钟
    protected final void pauseSubscribeIfExceeds(MessageQueue queue, int errorCount) {
        // 计数超过阈值
        if (errorCount < consumerSetting.getErrorCountThreshold()) {
            return;
        }
        if (queue == null) {
            return;
        }
        lockQueue(queue, consumerSetting.getErrorSleepSeconds());
    }

    private void lockQueue(MessageQueue queue, int lockTimeSeconds) {
        Object lockObject = getQueueLockIfNotLocked(queue);
        if (lockObject == null) {
            return;
        }
        synchronized (lockObject) {
            try {
                logger.warn("[MQ] pause to listen queue {} ", queue.getQueueName());
                lockObject.wait(lockTimeSeconds * 1000L);
                if (continueRunning) {
                    logger.info("[MQ] resume to listen queue {}", queue.getQueueName());
                } else {
                    logger.info("[MQ] stop to listen queue {}", queue.getQueueName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        removeQueueLock(queue);
    }

    private void removeQueueLock(MessageQueue queue) {
        sleepingQueueLockMap.remove(queue);
    }

    private Object getQueueLockIfNotLocked(MessageQueue queue) {
        Object lockObject = new Object();
        synchronized (sleepingQueueLockMap) {
            if (!continueRunning) {
                return null;
            }
            if (sleepingQueueLockMap.containsKey(queue)) {
                return null;
            }
            sleepingQueueLockMap.put(queue, lockObject);
        }
        return lockObject;
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
