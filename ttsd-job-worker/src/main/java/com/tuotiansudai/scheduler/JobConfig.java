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
        schedulerNames = rb.getString("org.quartz.scheduler.names").trim();
        threadCount = Integer.parseInt(rb.getString("org.quartz.threadPool.threadCount").trim());
        threadPriority = Integer.parseInt(rb.getString("org.quartz.threadPool.threadPriority").trim());
        misfireThreshold = Integer.parseInt(rb.getString("org.quartz.jobStore.misfireThreshold").trim());
        maxMisfiresToHandleAtATime = Integer.parseInt(rb.getString("org.quartz.jobStore.maxMisfiresToHandleAtATime").trim());
        isClustered = "true".equalsIgnoreCase(rb.getString("org.quartz.jobStore.isClustered").trim());
        clusterCheckinInterval = Integer.parseInt(rb.getString("org.quartz.jobStore.clusterCheckinInterval").trim());
    }
}
