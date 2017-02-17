package com.tuotiansudai.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class AnxinCreateContractJob implements Job {
    static Logger logger = Logger.getLogger(AnxinCreateContractJob.class);

    public final static String BUSINESS_ID = "BUSINESS_ID";

    public final static int HANDLE_DELAY_MINUTES = 1;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            long businessId = (long) context.getJobDetail().getJobDataMap().get(BUSINESS_ID);

            logger.info(MessageFormat.format("trigger anxin create contract job, prepare do job. businessId:{0}", String.valueOf(businessId)));
//            anxinSignService.createLoanContracts(businessId, true);
            ////TODO anxinsign
            logger.info(MessageFormat.format("trigger anxin create contract job, execute job end. businessId:{0}", String.valueOf(businessId)));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
