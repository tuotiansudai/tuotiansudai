package com.tuotiansudai.job;

import com.tuotiansudai.service.LoanService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FundraisingStartJob implements Job{
    static Logger logger = Logger.getLogger(FundraisingStartJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    @Autowired
    private LoanService loanService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger FundraisingStartJob, prepare do job");
        String strLoanId = context.getJobDetail().getJobDataMap()
                .get(LOAN_ID_KEY).toString();
        logger.info("trigger FundraisingStartJob, loanId : " + strLoanId);

        long loanId = Long.parseLong(strLoanId);
        loanService.startFundraising(loanId);
    }
}
