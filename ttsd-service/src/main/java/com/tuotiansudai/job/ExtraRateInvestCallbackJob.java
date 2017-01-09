package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class ExtraRateInvestCallbackJob implements Job {

    static Logger logger = Logger.getLogger(ExtraRateInvestCallbackJob.class);

    public static final int RUN_INTERVAL_SECONDS = 2;

    public static final String JOB_GROUP = "umpay";

    public static final String JOB_NAME = "repay_extra_rate_invest_call_back";

    public static final String REPAY_EXTRA_RATE_JOB_TRIGGER_KEY = "job:repay:extra_rate_invest_callback_job_trigger";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger ExtraRateInvestCallbackJob job start... ");
        String trigger = redisWrapperClient.get(REPAY_EXTRA_RATE_JOB_TRIGGER_KEY);
        if (trigger != null && Integer.valueOf(trigger) > 0) {
            payWrapperClient.extraRateInvestCallback();
        }
        logger.info("trigger ExtraRateInvestCallbackJob job end... ");
    }
}
