package com.tuotiansudai.job;

import com.oracle.tools.packager.Log;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class CouponRepayNotifyCallbackJob implements Job {

    static Logger logger = Logger.getLogger(CouponRepayNotifyCallbackJob.class);

    public static final int RUN_INTERVAL_SECONDS = 2;

    public static final String JOB_GROUP = "umpay";

    public static final String JOB_NAME = "coupon_repay_call_back";

    public static final String COUPON_REPAY_JOB_TRIGGER_KEY = "job:repay:coupon_repay_callback_job_trigger";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger CouponRepayNotifyCallbackJob job start...");
        String trigger = redisWrapperClient.get(COUPON_REPAY_JOB_TRIGGER_KEY);
        if (trigger != null && Integer.valueOf(trigger) > 0) {
            payWrapperClient.couponRepayCallback();
        }
        logger.info("trigger CouponRepayNotifyCallbackJob job end...");
    }
}
