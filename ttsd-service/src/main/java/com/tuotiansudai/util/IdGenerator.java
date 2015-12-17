package com.tuotiansudai.util;

import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * https://github.com/killme2008/Metamorphosis/blob/master/metamorphosis-commons/src/main/java/com/taobao/metamorphosis/utils/IdWorker.java
 */

@Component
public class IdGenerator {

    static Logger logger = Logger.getLogger(IdGenerator.class);

    private final static String ID_GENERATOR_REDIS_KEY = "common:id";

    // 纪元开始时间
    private final static long TW_EPOCH = new DateTime().withDate(2015, 4, 13).withTimeAtStartOfDay().getMillis();

    // 机器ID所占的位数
    private final static long WORKER_ID_BITS = 10L;

    // 机器ID的最大值
    public final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    // Sequence所占的位数
    private final static long SEQUENCE_BITS = 12L;

    // 机器ID的偏移量
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

    // 时间戳的偏移量
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // Sequence的屏蔽位
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // 机器ID
    private long workerId;

    // Sequence从0开始
    private long sequence = 0L;

    // 上一个毫秒数
    private long lastTimestamp = -1L;

    @Autowired
    public IdGenerator(RedisWrapperClient redisWrapperClient) {
        this.workerId = redisWrapperClient.incr(ID_GENERATOR_REDIS_KEY).intValue() % MAX_WORKER_ID;
        // 最大1024个节点
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(MessageFormat.format("IdGenerator's id can't be greater than {0} or less than 0", String.valueOf(MAX_WORKER_ID)));
        }
    }

    public synchronized long generate() {
        long timestamp = System.currentTimeMillis();
        if (this.lastTimestamp == timestamp) {
            // 在统一毫秒内产生
            this.sequence = (this.sequence + 1) & SEQUENCE_MASK;
            if (this.sequence == 0) {
                // 同一毫秒内的ID已经用光了，等到下一毫秒才能继续产生
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            // 上一次的毫秒已经过去了，现在进入下一个毫秒，重置Sequence
            this.sequence = 0;
        }

        // 如果系统时间发生了更改，而且更改到了一个过去的时间
        if (timestamp < this.lastTimestamp) {
            logger.error(MessageFormat.format("Clock moved backwards. Refusing to generate id for {0} milliseconds", String.valueOf(this.lastTimestamp - timestamp)));
        }

        // 保存上次的毫秒
        this.lastTimestamp = timestamp;

        // 毫秒数 ------> 机器ID ------> 毫秒内的Sequence
        return  timestamp - TW_EPOCH << TIMESTAMP_LEFT_SHIFT | this.workerId << WORKER_ID_SHIFT | this.sequence;
    }

    private long tilNextMillis(final long lastTimestamp) {
        // 等待到下一个毫秒
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.nanoTime() / 1000000;
    }
}