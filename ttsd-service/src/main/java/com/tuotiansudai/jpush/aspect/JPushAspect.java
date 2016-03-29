package com.tuotiansudai.jpush.aspect;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.job.*;
import com.tuotiansudai.repository.mapper.InvestReferrerRewardMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.model.*;
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
import java.util.Map;
import java.util.List;

/**
 * Created by gengbeijun on 16/3/18.
 */
@Component
@Aspect
public class JPushAspect {
    static Logger logger = Logger.getLogger(JPushAspect.class);

    @Autowired
    private JobManager jobManager;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Pointcut("execution(* *..RepayService.postRepayCallback(..))")
    public void postRepayCallbackPointcut() {}

    @Pointcut("execution(* *..RechargeService.rechargeCallback(..))")
    public void rechargeCallbackPointcut() {}

    @Pointcut("execution(* *..WithdrawService.withdrawCallback(..))")
    public void withdrawCallbackPointcut() {}

    @Pointcut("execution(* *..PayWrapperClient.transferCash(..))")
    public void transferCashPointcut() {}

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

    @AfterReturning(value = "withdrawCallbackPointcut()", returning = "returnValue")
    public void afterReturningWithdrawCallback(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after Withdraw pointcut");
        try {
            Map<String, String> paramsMap = (Map<String, String>) joinPoint.getArgs()[0];
            long orderId = Long.parseLong(paramsMap.get("order_id"));
            if(this.isApplyNotify(paramsMap)){
                if("0000".equals(paramsMap.get("ret_code").toString()))
                {
                    createAutoJPushWithDrawApplyAlertJob(orderId);
                }
            }
            else{
                if("0000".equals(paramsMap.get("ret_code").toString()))
                {
                    createAutoJPushWithDrawAlertJob(orderId);
                }
            }
        } catch (Exception e) {
            logger.error("after Withdraw aspect fail ", e);
        }
    }

    @AfterReturning(value = "rewardReferrerPointcut()")
    public void afterReturningRewardReferrer(JoinPoint joinPoint) {
        logger.debug("after RewardReferrer pointcut");
        try {
            List<InvestModel> successInvestList = (List<InvestModel>) joinPoint.getArgs()[1];
            for (InvestModel invest : successInvestList) {
                List<ReferrerRelationModel> referrerRelationList = referrerRelationMapper.findByLoginName(invest.getLoginName());
                for(ReferrerRelationModel referrerRelationModel : referrerRelationList){
                    InvestReferrerRewardModel investReferrerRewardModel = investReferrerRewardMapper.findByInvestIdAndReferrer(invest.getId(), referrerRelationModel.getReferrerLoginName());
                    createAutoJPushReferrerRewardAlertJob(investReferrerRewardModel.getId());
                }
            }
        } catch (Exception e) {
            logger.error("after RewardReferrer aspect fail ", e);
        }
    }

    @AfterReturning(value = "transferCashPointcut()", returning = "returnValue")
    public void afterReturningTransferCash(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after transferCash pointcut");
        try {
            TransferCashDto transferCashDto = (TransferCashDto) joinPoint.getArgs()[0];
            BaseDto<PayDataDto> payDataDtoBaseDto = (BaseDto<PayDataDto>) returnValue;
            if(payDataDtoBaseDto.isSuccess() && payDataDtoBaseDto != null){
                createAutoJPushTransferCashAlertJob(transferCashDto.getLoginName());
            }
        } catch (Exception e) {
            logger.error("after transferCash aspect fail ", e);
        }

    }


    private boolean isApplyNotify(Map<String, String> paramsMap) {
        return paramsMap.containsKey("service");
    }

    private void createAutoJPushRepayAlertJob(long loanId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushRepayAlertJob.JPUSH_ALERT_REPAY_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushRepayAlert, AutoJPushRepayAlertJob.class)
                    .addJobData(AutoJPushRepayAlertJob.REPAY_ID_KEY, loanId)
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

    private void createAutoJPushWithDrawApplyAlertJob(long orderId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushWithDrawApplyAlertJob.JPUSH_ALERT_WITHDRAW_APPLY_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushWithDrawApplyAlert, AutoJPushWithDrawApplyAlertJob.class)
                    .addJobData(AutoJPushWithDrawApplyAlertJob.WITHDRAW_APPLY_ID_KEY, orderId)
                    .withIdentity(JobType.AutoJPushWithDrawApplyAlert.name(), "WithDrawApply-" + orderId)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushWithDrawApplyAlert job for orderId[" + orderId + "] fail", e);
        }
    }

    private void createAutoJPushWithDrawAlertJob(long orderId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushWithDrawAlertJob.JPUSH_ALERT_WITHDRAW_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushWithDrawAlert, AutoJPushWithDrawAlertJob.class)
                    .addJobData(AutoJPushWithDrawAlertJob.WITHDRAW_ID_KEY, orderId)
                    .withIdentity(JobType.AutoJPushWithDrawAlert.name(), "WithDraw-" + orderId)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushWithDrawAlert job for orderId[" + orderId + "] fail", e);
        }
    }

    private void createAutoJPushReferrerRewardAlertJob(long orderId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushReferrerRewardAlertJob.JPUSH_ALERT_REFERRER_REWARD_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushReferrerRewardAlert, AutoJPushReferrerRewardAlertJob.class)
                    .addJobData(AutoJPushReferrerRewardAlertJob.REFERRER_REWARD_ID_KEY, orderId)
                    .withIdentity(JobType.AutoJPushReferrerRewardAlert.name(), "ReferrerReward-" + orderId)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushReferrerRewardAlert job for orderId[" + orderId + "] fail", e);
        }
    }

    private void createAutoJPushTransferCashAlertJob(String loginName) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushLotteryObtainCashAlertJob.JPUSH_ALERT_LOTTERY_OBTAIN_CASH_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushLotteryObtainCashAlert, AutoJPushLotteryObtainCashAlertJob.class)
                    .addJobData(AutoJPushLotteryObtainCashAlertJob.LOTTERY_OBTAIN_CASH_ID_KEY, loginName)
                    .withIdentity(JobType.AutoJPushLotteryObtainCashAlert.name(), "LotteryObtainCash-" + loginName)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushLotteryObtainCashAlert job for LotteryObtainCash[" + loginName + "] fail", e);
        }
    }

}
