package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanOutSuccessHandleJob implements Job {
    static Logger logger = Logger.getLogger(LoanOutSuccessHandleJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    public final static int HANDLE_DELAY_MINUTES = 3;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger loan out success handle job, prepare do job");
        String strLoanId = context.getJobDetail().getJobDataMap()
                .get(LOAN_ID_KEY).toString();

        logger.info("trigger loan out success handle job, loanId : " + strLoanId);

        long loanId = Long.parseLong(strLoanId);

        payWrapperClient.loanOutSuccessNotify(loanId);
    }
}
