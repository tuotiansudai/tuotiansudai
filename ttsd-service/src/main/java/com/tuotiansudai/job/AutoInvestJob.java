package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.util.JobManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class AutoInvestJob implements Job {

    static Logger logger = Logger.getLogger(AutoInvestJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    protected JobManager jobManager;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger auto invest job, prepare do job");
        String strLoanId = context.getJobDetail().getJobDataMap()
                .get(LOAN_ID_KEY).toString();

        logger.info("trigger auto invest job, loanId : " + strLoanId);

        long loanId = Long.parseLong(strLoanId);
        BaseDto<PayDataDto> response = payWrapperClient.autoInvest(loanId);
        if (!response.isSuccess()) {
            createRedoJob(loanId);
            throw new JobExecutionException("auto invest fail, cannot access to pay wrapper from job worker");
        }
        if (!response.getData().getStatus()) {
            createRedoJob(loanId);
            String message = MessageFormat.format("auto invest fail :{0}", response.getData().getMessage());
            throw new JobExecutionException(message);
        }
    }

    private void createRedoJob(long loanId){
        try {
            String jobName = MessageFormat.format("Loan-{0}-Redo-{1}", loanId, System.currentTimeMillis());
            jobManager.newJob(JobType.AutoInvest, AutoInvestJob.class)
                    .runOnceAt(new DateTime().plusHours(1).toDate())
                    .addJobData(AutoInvestJob.LOAN_ID_KEY, String.valueOf(loanId))
                    .withIdentity("AutoInvestJob", jobName)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create auto invest redo job for loan[" + loanId + "] fail", e);
        }
    }
}
