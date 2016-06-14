package com.tuotiansudai.jpush.job;

import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoJPushAlertLoanOutJob implements Job {

    static Logger logger = Logger.getLogger(AutoJPushAlertLoanOutJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    public final static int JPUSH_ALERT_LOAN_OUT_DELAY_MINUTES = 2;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger send jPush alert loan out job, prepare do job");
        long loanId = (long) context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY);
        jPushAlertService.autoJPushLoanAlert(loanId);
        logger.debug("trigger send jPush alert loan out job, loanId : " + String.valueOf(loanId));


    }
}
