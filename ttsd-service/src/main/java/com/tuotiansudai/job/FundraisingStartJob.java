package com.tuotiansudai.job;

import com.tuotiansudai.service.LoanService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FundraisingStartJob implements Job{
    public final static String LOAN_ID_KEY = "LOAN_ID";

    @Autowired
    LoanService loanService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String strLoanId = context.getJobDetail().getJobDataMap()
                .getString(LOAN_ID_KEY);

        long loanId = Long.parseLong(strLoanId);
        loanService.startFundraising(loanId);
    }
}
