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
public class NormalRepayJob implements Job {

    static Logger logger = Logger.getLogger(NormalRepayJob.class);

    public final static String LOAN_REPAY_ID = "LOAN_REPAY_ID";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger normal repay, prepare do job");

        long loanRepayId = (Long) context.getJobDetail().getJobDataMap().get(LOAN_REPAY_ID);

        logger.info("trigger normal repay, loanRepayId = " + String.valueOf(loanRepayId));

        BaseDto<PayDataDto> dto = payWrapperClient.postNormalRepay(loanRepayId);

        if (!dto.getData().getStatus()) {
            throw new JobExecutionException();
        }
    }
}
