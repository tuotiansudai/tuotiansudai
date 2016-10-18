package com.tuotiansudai.job;

import com.tuotiansudai.service.LoanCreateService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class FundraisingStartJob implements Job{
    static Logger logger = Logger.getLogger(FundraisingStartJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    @Autowired
    private LoanCreateService loanCreateService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger FundraisingStartJob, prepare do job");
        long loanId = Long.parseLong(String.valueOf(context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY)));
        logger.info(MessageFormat.format("trigger FundraisingStartJob, loanId = {0}", String.valueOf(loanId)));

        loanCreateService.startRaising(loanId);
    }
}
