package com.tuotiansudai.scheduler.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface JobMonitorService {
    void onJobToBeExecuted(JobExecutionContext context);

    void onJobExecuted(JobExecutionContext context, JobExecutionException exception);
}
