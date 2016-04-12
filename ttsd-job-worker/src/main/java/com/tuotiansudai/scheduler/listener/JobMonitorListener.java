package com.tuotiansudai.scheduler.listener;

import com.tuotiansudai.scheduler.service.JobMonitorService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobMonitorListener implements JobListener {

    static Logger logger = Logger.getLogger(JobMonitorListener.class);

    @Autowired
    private JobMonitorService jobMonitorService;

    @Override
    public String getName() {
        return JobMonitorListener.class.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        try {
            jobMonitorService.onJobToBeExecuted(context);
        } catch (Exception e) {
            logger.warn("log job execution info failed", e);
        }
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        try {
            jobMonitorService.onJobExecuted(context, jobException);
        } catch (Exception e) {
            logger.warn("update job execution info failed", e);
        }
    }
}
