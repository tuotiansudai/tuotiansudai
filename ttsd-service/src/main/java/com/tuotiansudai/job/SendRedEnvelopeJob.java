package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendRedEnvelopeJob implements Job {

    static Logger logger = Logger.getLogger(SendRedEnvelopeJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    public final static int SEND_RED_ENVELOPE_DELAY_MINUTES = 1;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger send red envelope job, prepare do job");
        long loanId = (long) context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY);
        logger.debug("trigger send red envelope job, loanId : " + String.valueOf(loanId));

        payWrapperClient.sendRedEnvelopeAfterLoanOut(loanId);
    }
}
