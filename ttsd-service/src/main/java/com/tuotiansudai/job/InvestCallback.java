package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class InvestCallback implements Job {

    public static final int RUN_INTERVAL_SECONDS = 2;

    public static final String JOB_GROUP = "umpay";

    public static final String JOB_NAME = "invest_call_back";

    public static final String JOB_TRIGGER_KEY = "invest_callback_job_trigger";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String trigger = redisWrapperClient.get(JOB_TRIGGER_KEY);
        if(trigger != null && Integer.valueOf(trigger) > 0) {
            payWrapperClient.investCallback();
        }
    }
}
