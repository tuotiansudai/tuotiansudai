package com.tuotiansudai.job;

import com.tuotiansudai.coupon.service.CouponAlertService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class BirthdayNotifyJob implements Job {

    static Logger logger = Logger.getLogger(BirthdayNotifyJob.class);

    @Autowired
    private CouponAlertService couponAlertServiceService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger BirthdayNotifyJob job");
        couponAlertServiceService.BirthdayNotify();
    }

}
