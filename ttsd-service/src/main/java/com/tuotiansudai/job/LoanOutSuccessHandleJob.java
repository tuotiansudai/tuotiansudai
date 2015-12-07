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
import java.util.Date;

@Component
public class LoanOutSuccessHandleJob implements Job {
    static Logger logger = Logger.getLogger(LoanOutSuccessHandleJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    public final static int HANDLE_DELAY_MINUTES = 3;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private JobManager jobManager;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger loan out success handle job, prepare do job");
        String strLoanId = context.getJobDetail().getJobDataMap()
                .get(LOAN_ID_KEY).toString();

        logger.info("trigger loan out success handle job, loanId : " + strLoanId);

        long loanId = Long.parseLong(strLoanId);
        BaseDto<PayDataDto> response = payWrapperClient.loanOutSuccessNotify(loanId);
        if (!response.isSuccess()) {
            createRedoJob(loanId);
            throw new JobExecutionException("loan out success handle fail, cannot access to pay wrapper from job worker");
        }
        if (!response.getData().getStatus()) {
            createRedoJob(loanId);
            String message = MessageFormat.format("loan out success handle fail :{0}", response.getData().getMessage());
            throw new JobExecutionException(message);
        }
    }

    private void createRedoJob(long loanId) {
        try {
            String jobName = MessageFormat.format("Loan-{0}-Redo-{1}", loanId, System.currentTimeMillis());
            jobManager.newJob(JobType.LoanOut, LoanOutSuccessHandleJob.class)
                    .addJobData(LoanOutSuccessHandleJob.LOAN_ID_KEY, loanId)
                    .withIdentity(JobType.LoanOut.name(), jobName)
                    .runOnceAt(new DateTime().plusHours(1).toDate())
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create loan out success handle Redo job for loan[" + loanId + "] fail", e);
        }
    }
}
