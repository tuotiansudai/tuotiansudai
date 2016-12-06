package com.tuotiansudai.message.job;

import com.tuotiansudai.message.util.UserMessageEventGenerator;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class BirthdayMessageSendJob implements Job {

    static Logger logger = Logger.getLogger(BirthdayMessageSendJob.class);

    @Autowired
    private UserMessageEventGenerator userMessageEventGenerator;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger birthday message send job, prepare do job");
        try {
            userMessageEventGenerator.generateBirthdayEvent();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        logger.info("birthday message send job done!");
    }
}
