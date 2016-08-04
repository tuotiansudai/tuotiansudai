package com.tuotiansudai.jpush.job;

import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutoJPushWithDrawAlertJob implements Job {
    static Logger logger = Logger.getLogger(AutoJPushWithDrawAlertJob.class);

    public final static String WITHDRAW_ID_KEY = "WITHDRAW_ID";

    public final static int JPUSH_ALERT_WITHDRAW_DELAY_MINUTES = 2;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger send jPush alert withdraw apply job, prepare do job");
        long orderId = (long) context.getJobDetail().getJobDataMap().get(WITHDRAW_ID_KEY);
        jPushAlertService.autoJPushWithDrawAlert(orderId);
        logger.debug("trigger send jPush alert withdraw apply job, orderId : " + orderId);

    }
}
