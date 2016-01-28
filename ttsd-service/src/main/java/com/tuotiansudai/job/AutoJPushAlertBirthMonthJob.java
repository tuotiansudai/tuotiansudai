package com.tuotiansudai.job;

import com.tuotiansudai.jpush.service.JPushAlertService;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoJPushAlertBirthMonthJob implements Job {
    static Logger logger = Logger.getLogger(AutoJPushAlertBirthMonthJob.class);

    @Autowired
    private JPushAlertService jPushAlertService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("AutoJPushAlertBirthMonthJob===========in");
        jPushAlertService.autoJPushAlertBirthMonth();
        logger.debug("AutoJPushAlertBirthMonthJob===========out");
    }
}
