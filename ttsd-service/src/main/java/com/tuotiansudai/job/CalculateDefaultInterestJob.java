package com.tuotiansudai.job;

import com.tuotiansudai.service.LoanRepayService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CalculateDefaultInterestJob implements Job{

    static Logger logger = Logger.getLogger(CalculateDefaultInterestJob.class);

    @Autowired
    private LoanRepayService loanRepayService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger CalculateDefaultInterestJob job");
        loanRepayService.calculateDefaultInterest();
    }

}
