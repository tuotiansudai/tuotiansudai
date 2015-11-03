package com.tuotiansudai.scheduler;

import java.util.ResourceBundle;

public class JobConfig {
    public static final String schedulerNames;
    public static final int threadCount;
    public static final int threadPriority;
    public static final int misfireThreshold;
    public static final int maxMisfiresToHandleAtATime;
    public static final boolean isClustered;
    public static final int clusterCheckinInterval;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("job-worker");
        schedulerNames = rb.getString("org.quartz.scheduler.names");
        threadCount = Integer.parseInt(rb.getString("org.quartz.threadPool.threadCount"));
        threadPriority = Integer.parseInt(rb.getString("org.quartz.threadPool.threadPriority"));
        misfireThreshold = Integer.parseInt(rb.getString("org.quartz.jobStore.misfireThreshold"));
        maxMisfiresToHandleAtATime = Integer.parseInt(rb.getString("org.quartz.jobStore.maxMisfiresToHandleAtATime"));
        isClustered = "true".equalsIgnoreCase(rb.getString("org.quartz.jobStore.isClustered"));
        clusterCheckinInterval = Integer.parseInt(rb.getString("org.quartz.jobStore.clusterCheckinInterval"));
    }
}
