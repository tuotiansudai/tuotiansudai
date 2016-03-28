package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.TransferCashDto;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LotteryTransferCashJob implements Job{

    static Logger logger = Logger.getLogger(LotteryTransferCashJob.class);

    public final static int TRANSFER_CASH_DELAY_MINUTES = 1;

    public final static String TRANSFER_CASH_KEY = "TRANSFER_CASH";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger transfer cash job, prepare do job");
        TransferCashDto transferCashDto = (TransferCashDto)context.getJobDetail().getJobDataMap().get(TRANSFER_CASH_KEY);
        logger.info("trigger transfer cash job, login name : " + transferCashDto.getLoginName());
        payWrapperClient.transferCash(transferCashDto);
        logger.info("trigger transfer cash job over, login name : " + transferCashDto.getLoginName());
    }

}
