package com.esoft.jdp2p.schedule.job;

import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.loan.service.LoanService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LoanOutSuccessfulNotificationJob implements Job {

    public static final String LOAN_ID = "loanId";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LoanService loanService = (LoanService) SpringBeanUtil.getBeanByName("loanService");
        String loanId = jobExecutionContext.getJobDetail().getJobDataMap().getString(LOAN_ID);
        loanService.notifyInvestorsLoanOutSuccessful(loanId);
    }
}
