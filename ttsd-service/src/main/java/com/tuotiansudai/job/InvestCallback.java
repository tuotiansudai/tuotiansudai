package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class InvestCallback implements Job {

    public static final int RUN_INTERVAL_SECONDS = 2;

    public static final String JOB_GROUP = "umpay";

    public static final String JOB_NAME = "invest_call_back";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        payWrapperClient.investCallback();
    }
}
