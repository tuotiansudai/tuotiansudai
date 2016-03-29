package com.tuotiansudai.job;

import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoJPushRechargeAlertJob implements Job {
    static Logger logger = Logger.getLogger(AutoJPushRechargeAlertJob.class);

    public final static String RECHARGE_ID_KEY = "RECHARGE_ID";

    public final static int JPUSH_ALERT_RECHARGE_DELAY_MINUTES = 2;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger send jPush alert recharge job, prepare do job");
        long orderId = (long) context.getJobDetail().getJobDataMap().get(RECHARGE_ID_KEY);
        jPushAlertService.autoJPushRechargeAlert(orderId);
        logger.debug("trigger send jPush alert recharge job, orderId : " + orderId);

    }
}
