package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class AdvanceRepayCallbackJob implements Job {

    static Logger logger = Logger.getLogger(AdvanceRepayCallbackJob.class);

    public static final int RUN_INTERVAL_SECONDS = 2;

    public static final String JOB_GROUP = "umpay";

    public static final String JOB_NAME = "advance_repay_call_back";

    public static final String ADVANCE_REPAY_JOB_TRIGGER_KEY = "job:repay:advance_repay_callback_job_trigger";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger AdvanceRepayCallbackJob job start... ");
        payWrapperClient.advanceRepayInvestPayback();
        logger.info("trigger AdvanceRepayCallbackJob job end...  ");
    }
}
