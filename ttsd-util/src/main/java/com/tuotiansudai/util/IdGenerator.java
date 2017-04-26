package com.tuotiansudai.util;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.text.MessageFormat;

/**
 * https://github.com/killme2008/Metamorphosis/blob/master/metamorphosis-commons/src/main/java/com/taobao/metamorphosis/utils/IdWorker.java
 * DO NOT MODIFY!!!
 */

public class IdGenerator {

    private final static Logger logger = Logger.getLogger(IdGenerator.class);

    private final static String ID_GENERATOR_REDIS_KEY = "common:id";

    // 纪元开始时间
    private final static long TW_EPOCH = new DateTime().withDate(2015, 4, 13).withTimeAtStartOfDay().getMillis();

    // 机器ID所占的位数
    private final static long WORKER_ID_BITS = 6L;

    // 机器ID的最大值
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    // Sequence所占的位数
    private final static long SEQUENCE_BITS = 4L;

    // 机器ID的偏移量
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

    // 时间戳的偏移量
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // Sequence的屏蔽位
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // 机器ID
    private final static long workerId = RedisWrapperClient.getInstance().incr(ID_GENERATOR_REDIS_KEY).intValue() % MAX_WORKER_ID;

    // Sequence从0开始
    private static long sequence = 0L;

    // 上一个毫秒数
    private static long lastTimestamp = -1L;

    public static synchronized long generate() {
        long timestamp = System.currentTimeMillis();
        if (lastTimestamp == timestamp) {
            // 在统一毫秒内产生
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 同一毫秒内的ID已经用光了，等到下一毫秒才能继续产生
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 上一次的毫秒已经过去了，现在进入下一个毫秒，重置Sequence
            sequence = 0;
        }

        // 如果系统时间发生了更改，而且更改到了一个过去的时间
        if (timestamp < lastTimestamp) {
            logger.error(MessageFormat.format("Clock moved backwards. Refusing to generate id for {0} milliseconds", String.valueOf(lastTimestamp - timestamp)));
        }

        // 保存上次的毫秒
        lastTimestamp = timestamp;

        // 毫秒数 ------> 机器ID ------> 毫秒内的Sequence
        return  timestamp - TW_EPOCH << TIMESTAMP_LEFT_SHIFT | workerId << WORKER_ID_SHIFT | sequence;
    }

    private static long tilNextMillis(final long lastTimestamp) {
        // 等待到下一个毫秒
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        // return System.nanoTime() / 1000000;
        return System.currentTimeMillis();
    }
}