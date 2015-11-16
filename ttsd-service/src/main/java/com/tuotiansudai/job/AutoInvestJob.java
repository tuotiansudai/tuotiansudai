package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoInvestJob implements Job {

    static Logger logger = Logger.getLogger(AutoInvestJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String strLoanId = context.getJobDetail().getJobDataMap()
                .get(LOAN_ID_KEY).toString();

        logger.info("trigger auto invest job, loanId : " + strLoanId);

        long loanId = Long.parseLong(strLoanId);
        payWrapperClient.autoInvest(loanId);
    }
}
