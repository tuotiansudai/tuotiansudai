package com.tuotiansudai.jpush.aspect;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.job.AutoJPushRepayAlertJob;
import com.tuotiansudai.job.AutoJPushRechargeAlertJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.util.JobManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by gengbeijun on 16/3/18.
 */
@Component
@Aspect
public class JPushAspect {
    static Logger logger = Logger.getLogger(JPushAspect.class);

    @Autowired
    private JobManager jobManager;

    @Pointcut("execution(* *..RepayService.postRepayCallback(..)")
    public void postRepayCallbackPointcut() {}

    @Pointcut("execution(* *..RechargeService.rechargeCallback(..)")
    public void rechargeCallbackPointcut() {}

    @AfterReturning(value = "postRepayCallbackPointcut()", returning = "returnValue")
    public void afterReturningPostRepayCallback(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after repay pointcut");
        try {
            RepayDto repayDto = (RepayDto)joinPoint.getArgs()[0];
            if((boolean)returnValue){
                createAutoJPushRepayAlertJob(repayDto.getLoanId());
            }
        } catch (Exception e) {
            logger.error("after repay aspect fail ", e);
        }
    }

    @AfterReturning(value = "rechargeCallbackPointcut()", returning = "returnValue")
    public void afterReturningRechargeCallback(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after recharge pointcut");
        try {
            RechargeDto rechargeDto = (RechargeDto)joinPoint.getArgs()[0];
            BaseDto<PayFormDataDto> baseDto = (BaseDto<PayFormDataDto>) returnValue;
            if(baseDto != null && baseDto.isSuccess()){
                createAutoJPushRechargeAlertJob(rechargeDto);
            }
        } catch (Exception e) {
            logger.error("after recharge aspect fail ", e);
        }
    }


    private void createAutoJPushRepayAlertJob(long loanId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushRepayAlertJob.JPUSH_ALERT_REPAY_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushRepayAlert, AutoJPushRepayAlertJob.class)
                    .addJobData(AutoJPushRepayAlertJob.LOAN_ID_KEY, loanId)
                    .withIdentity(JobType.AutoJPushRepayAlert.name(), "Repay-" + loanId)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushRepayAlert job for loan[" + loanId + "] fail", e);
        }
    }

    private void createAutoJPushRechargeAlertJob(RechargeDto rechargeDto) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushRechargeAlertJob.JPUSH_ALERT_RECHARGE_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushRechargeAlert, AutoJPushRechargeAlertJob.class)
                    .addJobData(AutoJPushRechargeAlertJob.RECHARGE_ID_KEY, rechargeDto)
                    .withIdentity(JobType.AutoJPushRechargeAlert.name(), "Recharge-" + rechargeDto)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushRepayAlert job for loginName[" + rechargeDto.getLoginName() + "] fail", e);
        }
    }

}
