package com.esoft.jdp2p.schedule.job;

import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.apache.commons.logging.Log;
import javax.annotation.Resource;

@Component
public class LoanOutSuccessfulNotificationJob implements Job {

    static Log log = LogFactory.getLog(LoanOutSuccessfulNotificationJob.class);

    public static final String LOAN_ID = "loanId";

    @Resource
    LoanService loanService;

    @Resource
    UmPayNormalRepayOperation umPayNormalRepayOperation;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("log begin-----------------------------------------------------------------");
        String loanId = jobExecutionContext.getJobDetail().getJobDataMap().getString(LOAN_ID);
        //此逻辑耗时较长，特放到此处以异步方式执行。
        log.debug("loan_Id = " + loanId);
        log.debug("begin referrer reward--------------------------------------------------------");
        umPayNormalRepayOperation.recommendedIncome(loanId);
        log.debug("end referrer reward--------------------------------------------------------");
        log.debug("begin sms and email--------------------------------------------------------");
        loanService.notifyInvestorsLoanOutSuccessful(loanId);
        log.debug("end sms and email--------------------------------------------------------");
    }
}
