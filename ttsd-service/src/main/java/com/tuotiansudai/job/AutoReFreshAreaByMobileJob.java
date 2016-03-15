package com.tuotiansudai.job;

import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AutoReFreshAreaByMobileJob implements Job {
    static Logger logger = Logger.getLogger(AutoReFreshAreaByMobileJob.class);

    @Autowired
    private UserService userService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("AutoReFleshAreaByMobileJob===========in");

        userService.refreshAreaByMobileInJob();

        logger.debug("AutoReFleshAreaByMobileJob===========out");
    }
}
