package com.tuotiansudai.aspect;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.job.AutoJPushLotteryObtainCashAlertJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.impl.RankingActivityServiceImpl;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Aspect
@Component
public class JPushLotteryObtainCashAspect {

    private static Logger logger = Logger.getLogger(JPushLotteryObtainCashAspect.class);

    public final static String LOTTERYOBTAINCASH = "LotteryObtainCash-{0}";

    @Autowired
    private JobManager jobManager;

    @Pointcut("execution(* *..TransferCashService.transferCash(..))")
    public void transferCashPointcut() {}

    @AfterReturning(value = "transferCashPointcut()", returning = "returnValue")
    public void afterReturningTransferCash(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after returning transferCash assign starting...");

        TransferCashDto transferCashDto = (TransferCashDto) joinPoint.getArgs()[0];

        BaseDto<PayDataDto> baseDto = (BaseDto<PayDataDto>) returnValue;
        try {
            if(baseDto.isSuccess()){
                createAutoJPushLotteryObtainCashAlertJob(transferCashDto);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        logger.debug("after returning transferCash assign completed");
    }

    private void createAutoJPushLotteryObtainCashAlertJob(TransferCashDto transferCashDto) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushLotteryObtainCashAlertJob.JPUSH_ALERT_LOTTERY_OBTAIN_CASH_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushLotteryObtainCashAlert, AutoJPushLotteryObtainCashAlertJob.class)
                    .addJobData(AutoJPushLotteryObtainCashAlertJob.LOTTERY_OBTAIN_CASH_ID_KEY, transferCashDto)
                    .withIdentity(JobType.AutoJPushLotteryObtainCashAlert.name(), MessageFormat.format(LOTTERYOBTAINCASH, String.valueOf(transferCashDto.getOrderId())))
                    .runOnceAt(triggerTime)
                    .replaceExistingJob(true)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushLotteryObtainCashAlert job for orderId[" + transferCashDto.getOrderId() + "] fail", e);
        }
    }

}
