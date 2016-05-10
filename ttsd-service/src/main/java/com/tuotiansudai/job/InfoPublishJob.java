package com.tuotiansudai.job;

import com.tuotiansudai.service.InfoPublishService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class InfoPublishJob implements Job {

    static Logger logger = Logger.getLogger(InfoPublishJob.class);

    @Autowired
    private InfoPublishService infoPublishService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger InfoPublish job start...");
        infoPublishService.createInfoPublishInvestDetail();

        logger.info("trigger InfoPublish job finish.");
    }

}
