package com.tuotiansudai.job;

import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendCouponIncomeJob implements Job {

    static Logger logger = Logger.getLogger(SendCouponIncomeJob.class);

    public final static String LOAN_REPAY_ID_KEY = "LOAN_REPAY_ID";

    public final static int SEND_COUPON_INCOME_DELAY_MINUTES = 1;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger send coupon income job, prepare do job");
        long loanRepayId = (long) context.getJobDetail().getJobDataMap().get(LOAN_REPAY_ID_KEY);
        jPushAlertService.autoJPushCouponIncomeAlert(loanRepayId);
        logger.debug("trigger send coupon income job, loanRepayId : " + String.valueOf(loanRepayId));

    }
}
