package com.tuotiansudai.utils.quartz;

import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.ThreadPool;

public class ThreadPoolBuilder {
    public static ThreadPool buildMiniThreadPool() {
        return buildThreadPool(1, Thread.NORM_PRIORITY);
    }

    public static ThreadPool buildThreadPool(int threadCount, int threadPriority) {
        return new SimpleThreadPool(threadCount, threadPriority);
    }
}
