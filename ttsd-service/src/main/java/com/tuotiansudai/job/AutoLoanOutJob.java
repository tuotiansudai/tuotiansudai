package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoLoanOutJob implements Job {

    static Logger logger = Logger.getLogger(AutoLoanOutJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    public final static String LOAN_OUT_IN_PROCESS_KEY = "job:loan-out-in-process:";

    public final static int AUTO_LOAN_OUT_DELAY_MINUTES = 1;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger auto loan out after raising complete job, prepare do job");
        redisWrapperClient.set("aaa", "1");
        long loanId;
        try {
            loanId = (long) context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY);
            logger.debug("trigger auto loan out after raising complete job, loanId : " + String.valueOf(loanId));

            if (redisWrapperClient.exists(AutoLoanOutJob.LOAN_OUT_IN_PROCESS_KEY + loanId)) {
                logger.debug("another loan out process is running, stop auto loan out. loanId : " + String.valueOf(loanId));
                return;
            }

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        BaseDto<PayDataDto> dto = payWrapperClient.autoLoanOutAfterRaisingComplete(loanId);
        if (!dto.getData().getStatus()) {
            throw new JobExecutionException();
        }
    }
}
