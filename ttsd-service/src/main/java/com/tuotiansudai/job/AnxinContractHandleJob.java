package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnxinContractHandleJob implements Job {
    static Logger logger = Logger.getLogger(AnxinContractHandleJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    public final static int HANDLE_DELAY_MINUTES = 10;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger anxin contract handle job, prepare do job");

        long loanId;
        try {
            loanId = (long) context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY);
            logger.info("trigger anxin contract handle job, loanId : " + String.valueOf(loanId));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        BaseDto<PayDataDto> dto = payWrapperClient.anxinContractHandle(loanId);
        if (!dto.getData().getStatus()) {
            throw new JobExecutionException();
        }
    }
}
