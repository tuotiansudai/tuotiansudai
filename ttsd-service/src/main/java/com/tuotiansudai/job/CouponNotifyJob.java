package com.tuotiansudai.job;

import com.tuotiansudai.coupon.service.CouponActivationService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class CouponNotifyJob implements Job {

    static Logger logger = Logger.getLogger(CouponNotifyJob.class);

    public final static String COUPON_ID = "COUPON_ID";

    @Autowired
    private CouponActivationService couponActivationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long couponId = (long) context.getJobDetail().getJobDataMap().get(COUPON_ID);

        logger.info(MessageFormat.format("Send coupon notify (couponId = {0}) starting", String.valueOf(couponId)));

        couponActivationService.sendSms(couponId);

        logger.info(MessageFormat.format("Send coupon notify (couponId = {0}) finished", String.valueOf(couponId)));
    }
}
