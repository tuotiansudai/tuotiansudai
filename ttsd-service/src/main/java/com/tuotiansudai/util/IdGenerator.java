package com.tuotiansudai.util;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class IdGenerator {

    static Logger logger = Logger.getLogger(IdGenerator.class);

    // 纪元开始时间
    private final static long twEpoch = new DateTime().withDate(2015, 4, 13).withTime(8, 0, 0, 0).toDate().getTime();

    // 机器ID所占的位数
    private final static long workerIdBits = 4L;

    // 机器ID的最大值
    public final static long maxWorkerId = ~(-1L << workerIdBits);

    // Sequence所占的位数
    private final static long sequenceBits = 10L;

    // 机器ID的偏移量
    private final static long workerIdShift = sequenceBits;

    // 时间戳的偏移量
    private final static long timestampLeftShift = sequenceBits + workerIdBits;

    // Sequence的屏蔽位
    public final static long sequenceMask = ~(-1L << sequenceBits);

    // 机器ID
    private final long workerId = 0L;

    // Sequence从0开始
    private long sequence = 0L;

    // 上一个毫秒数
    private long lastTimestamp = -1L;

    public IdGenerator() {
        // 最大16个节点
//        if (workerId > maxWorkerId || workerId < 0) {
//            throw new IllegalArgumentException(MessageFormat.format("IdGenerator's id can't be greater than {0} or less than 0", String.valueOf(maxWorkerId)));
//        }
//        this.workerId = workerId;
    }

    public synchronized long generate() {
        long timestamp = System.currentTimeMillis();
        if (this.lastTimestamp == timestamp) {
            // 在统一毫秒内产生
            this.sequence = (this.sequence + 1) & this.sequenceMask;
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
        return ((timestamp - twEpoch << timestampLeftShift)) | (workerId << workerIdShift) | (this.sequence);
    }

    private long tilNextMillis(final long lastTimestamp) {
        // 等待到下一个毫秒
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}