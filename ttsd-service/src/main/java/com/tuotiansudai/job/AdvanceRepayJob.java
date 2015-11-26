package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdvanceRepayJob implements Job {

    static Logger logger = Logger.getLogger(AutoInvestJob.class);

    public final static String LOAN_REPAY_ID_KEY = "LOAN_REPAY_ID";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String loanRepayId = context.getJobDetail().getJobDataMap().get(LOAN_REPAY_ID_KEY).toString();

        logger.info("trigger advance repay, loanRepayId = " + loanRepayId);

        payWrapperClient.postAdvanceRepay(Long.parseLong(loanRepayId));
    }
}
