package com.tuotiansudai.jpush.aspect;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.job.AutoJPushRepayAlertJob;
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
public class JpushAspect {
    static Logger logger = Logger.getLogger(JpushAspect.class);

    @Autowired
    private JobManager jobManager;

    @Pointcut("execution(* com.tuotiansudai.service.RepayService.repay(..)")
    public void repayPointcut() {}

    @AfterReturning(value = "repayPointcut()", returning = "returnValue")
    public void afterReturningRepay(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after repay pointcut");
        try {
            RepayDto repayDto = (RepayDto)joinPoint.getArgs()[0];
            BaseDto<PayFormDataDto> baseDto = (BaseDto<PayFormDataDto>) returnValue;
            if(baseDto != null && baseDto.isSuccess()){
                createAutoJPushAlertJob(repayDto.getLoanId());
            }
        } catch (Exception e) {
            logger.error("after repay aspect fail ", e);
        }
    }


    private void createAutoJPushAlertJob(long loanId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushRepayAlertJob.JPUSH_ALERT_REPAY_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushRepayAlert, AutoJPushRepayAlertJob.class)
                    .addJobData(AutoJPushRepayAlertJob.LOAN_ID_KEY, loanId)
                    .withIdentity(JobType.AutoJPushRepayAlert.name(), "Loan-" + loanId)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushRepayAlert job for loan[" + loanId + "] fail", e);
        }
    }

}
