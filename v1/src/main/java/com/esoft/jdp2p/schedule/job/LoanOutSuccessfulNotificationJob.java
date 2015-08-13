package com.esoft.jdp2p.schedule.job;

import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoanOutSuccessfulNotificationJob implements Job {

    public static final String LOAN_ID = "loanId";

    @Resource
    LoanService loanService;

    @Resource
    UmPayNormalRepayOperation umPayNormalRepayOperation;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String loanId = jobExecutionContext.getJobDetail().getJobDataMap().getString(LOAN_ID);
        //此逻辑耗时较长，特放到此处以异步方式执行。
        umPayNormalRepayOperation.recommendedIncome(loanId);
        loanService.notifyInvestorsLoanOutSuccessful(loanId);
    }
}
