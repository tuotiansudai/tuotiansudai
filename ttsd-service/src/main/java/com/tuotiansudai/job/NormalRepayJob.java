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

import java.text.MessageFormat;

@Component
public class NormalRepayJob implements Job {

    static Logger logger = Logger.getLogger(NormalRepayJob.class);

    public final static String LOAN_REPAY_ID = "LOAN_REPAY_ID";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long loanRepayId = (Long) context.getJobDetail().getJobDataMap().get(LOAN_REPAY_ID);

        logger.info(MessageFormat.format("[Normal Repay {0}] invest payback job is starting...", String.valueOf(loanRepayId)));

        BaseDto<PayDataDto> dto;

        try {
            dto = payWrapperClient.postNormalRepay(loanRepayId);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Normal Repay {0}] invest payback job execution is failed", String.valueOf(loanRepayId)));
            return;
        }

        logger.info(MessageFormat.format("[Normal Repay {0}] invest payback job is done", String.valueOf(loanRepayId)));

        if (!dto.getData().getStatus()) {
            logger.error(MessageFormat.format("[Normal Repay {0}] invest payback job result is {1}",
                    String.valueOf(loanRepayId), String.valueOf(dto.getData().getStatus())));
            throw new JobExecutionException();
        }
    }
}
