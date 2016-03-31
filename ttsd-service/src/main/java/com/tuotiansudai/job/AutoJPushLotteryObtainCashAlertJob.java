package com.tuotiansudai.job;

import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoJPushLotteryObtainCashAlertJob implements Job {
    static Logger logger = Logger.getLogger(AutoJPushLotteryObtainCashAlertJob.class);

    public final static String LOTTERY_OBTAIN_CASH_ID_KEY = "LOTTERY_OBTAIN_CASH_ID";

    public final static int JPUSH_ALERT_LOTTERY_OBTAIN_CASH_DELAY_MINUTES = 2;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("trigger send jPush alert lottery obtain cash job, prepare do job");
        TransferCashDto transferCashDto = (TransferCashDto) context.getJobDetail().getJobDataMap().get(LOTTERY_OBTAIN_CASH_ID_KEY);
        jPushAlertService.autoJPushLotteryLotteryObtainCashAlert(transferCashDto);
        logger.debug("trigger send jPush alert lottery obtain job, lognName : " + transferCashDto.getLoginName());

    }
}

