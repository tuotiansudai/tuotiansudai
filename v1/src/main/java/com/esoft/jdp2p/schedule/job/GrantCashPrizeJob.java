package com.esoft.jdp2p.schedule.job;

import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GrantCashPrizeJob implements Job {

    static Log log = LogFactory.getLog(GrantCashPrizeJob.class);

    public static final String LOAN_ID = "loanId";

//    @Resource
//    LoanService loanService;
//
//    @Resource
//    UmPayNormalRepayOperation umPayNormalRepayOperation;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("========GrantCashPrizeJob============");
    }
}
