package com.tuotiansudai.job;

import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoJPushRepayAlertJob implements Job {
    static Logger logger = Logger.getLogger(AutoJPushRepayAlertJob.class);

    public final static String REPAY_ID_KEY = "REPAY_ID";

    public final static int JPUSH_ALERT_REPAY_DELAY_MINUTES = 2;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger send jPush alert repay job, prepare do job");
        long loanId = (long) context.getJobDetail().getJobDataMap().get(REPAY_ID_KEY);
        jPushAlertService.autoJPushRepayAlert(loanId);
        logger.debug("trigger send jPush alert repay job, loanId : " + String.valueOf(loanId));

    }
}
