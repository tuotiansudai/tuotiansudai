package com.tuotiansudai.job;

import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoJPushReferrerRewardAlertJob implements Job {
    static Logger logger = Logger.getLogger(AutoJPushReferrerRewardAlertJob.class);

    public final static String REFERRER_REWARD_ID_KEY = "REFERRER_REWARD_ID";

    public final static int JPUSH_ALERT_REFERRER_REWARD_DELAY_MINUTES = 2;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger send jPush alert referrer reward job, prepare do job");
        long orderId = (long) context.getJobDetail().getJobDataMap().get(REFERRER_REWARD_ID_KEY);
        jPushAlertService.autoJPushReferrerRewardAlert(orderId);
        logger.debug("trigger send jPush alert referrer reward job, lognName : " + orderId);

    }
}
