package com.tuotiansudai.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class AutoReFreshAreaByMobileJob implements Job {
    static Logger logger = Logger.getLogger(AutoReFreshAreaByMobileJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("AutoReFleshAreaByMobileJob===========in");

//        userService.refreshAreaByMobileInJob();

        logger.info("AutoReFleshAreaByMobileJob===========out");
    }
}
