package com.tuotiansudai.job;

import com.tuotiansudai.service.NewbieExperienceService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class NewbieExperienceRepayJob implements Job {
    static Logger logger = Logger.getLogger(LoanRepayNotifyJob.class);

    @Autowired
    private NewbieExperienceService newbieExperienceService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger NewbieExperienceRepayJob job");
        newbieExperienceService.sendCouplesDaily(new Date(), new Date());
    }
}
