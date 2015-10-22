package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhoubx on 15/10/22.
 */
public class InvestCallback implements Job {

    @Autowired
    PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        payWrapperClient.investCallback();
    }
}
