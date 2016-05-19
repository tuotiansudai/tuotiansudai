package com.tuotiansudai.jpush.aspect;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.job.*;
import com.tuotiansudai.jpush.service.JPushAlertService;
import com.tuotiansudai.repository.mapper.InvestReferrerRewardMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
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

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Aspect
public class JPushAmountNotifyAspect {
    static Logger logger = Logger.getLogger(JPushAmountNotifyAspect.class);

    public final static String REPAY = "Repay-{0}";
    public final static String RECHARGE = "Recharge-{0}";
    public final static String WITHDRAW_APPLY = "WithDrawApply-{0}";
    public final static String WITHDRAW = "WithDraw-{0}";
    public final static String REFERRER_REWARD = "ReferrerReward-{0}";
    public final static String LOTTERYOBTAINCASH = "LotteryObtainCash-{0}";

    @Autowired
    private JobManager jobManager;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Pointcut("execution(* *..NormalRepayService.paybackInvest(..))")
    public void normalRepayPaybackInvestPointcut() {}

    @Pointcut("execution(* *..AdvanceRepayService.paybackInvest(..))")
    public void advanceRepayPaybackInvestPointcut() {}

    @Pointcut("execution(* *..RechargeService.rechargeCallback(..))")
    public void rechargeCallbackPointcut() {}

    @Pointcut("execution(* *..WithdrawService.withdrawCallback(..))")
    public void withdrawCallbackPointcut() {}

    @Pointcut("execution(* *..ReferrerRewardService.rewardReferrer(..))")
    public void rewardReferrerPointcut() {}

    @Pointcut("execution(* *..TransferCashService.transferCash(..))")
    public void transferCashPointcut() {}

    @AfterReturning(value = "normalRepayPaybackInvestPointcut() || advanceRepayPaybackInvestPointcut()", returning = "returnValue")
    public void afterReturningNormalRepayCallback(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after repay pointcut");
        try {
            long LoanRepayId = (long)joinPoint.getArgs()[0];
            if((boolean)returnValue){
                createAutoJPushRepayAlertJob(LoanRepayId, false);
            }
        } catch (Exception e) {
            logger.error("after repay aspect fail ", e);
        }
    }

    @AfterReturning(value = "advanceRepayPaybackInvestPointcut()", returning = "returnValue")
    public void afterReturningAdvanceRepayCallback(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after repay pointcut");
        try {
            long LoanRepayId = (long)joinPoint.getArgs()[0];
            if((boolean)returnValue){
                createAutoJPushRepayAlertJob(LoanRepayId, true);
            }
        } catch (Exception e) {
            logger.error("after repay aspect fail ", e);
        }
    }

    @AfterReturning(value = "rechargeCallbackPointcut()")
    public void afterReturningRechargeCallback(JoinPoint joinPoint) {
        logger.debug("after recharge pointcut");
        try {
            Map<String, String> paramsMap = (Map<String, String>) joinPoint.getArgs()[0];
            long orderId = Long.parseLong(paramsMap.get("order_id"));
            RechargeModel  rechargeModel = rechargeMapper.findById(orderId);
            if(rechargeModel != null && RechargeStatus.SUCCESS == rechargeModel.getStatus()){
                createAutoJPushRechargeAlertJob(orderId);
            }
        } catch (Exception e) {
            logger.error("after recharge aspect fail ", e);
        }
    }

    @AfterReturning(value = "withdrawCallbackPointcut()")
    public void afterReturningWithdrawCallback(JoinPoint joinPoint) {
        logger.debug("after Withdraw pointcut");
        try {
            Map<String, String> paramsMap = (Map<String, String>) joinPoint.getArgs()[0];
            long orderId = Long.parseLong(paramsMap.get("order_id"));
            WithdrawModel withdrawModel = withdrawMapper.findById(orderId);
            if(WithdrawStatus.APPLY_SUCCESS == withdrawModel.getStatus())
            {
                createAutoJPushWithDrawApplyAlertJob(orderId);
            }
            else if(WithdrawStatus.SUCCESS == withdrawModel.getStatus())
            {
                createAutoJPushWithDrawAlertJob(orderId);
            }
        } catch (Exception e) {
            logger.error("after Withdraw aspect fail ", e);
        }
    }

    @AfterReturning(value = "rewardReferrerPointcut()")
    public void afterReturningRewardReferrer(JoinPoint joinPoint) {
        logger.debug("after RewardReferrer pointcut");
        try {
            LoanModel loanModel = (LoanModel) joinPoint.getArgs()[0];
            createAutoJPushReferrerRewardAlertJob(loanModel.getId());
        } catch (Exception e) {
            logger.error("after RewardReferrer aspect fail ", e);
        }
    }

    @AfterReturning(value = "transferCashPointcut()", returning = "returnValue")
    public void afterReturningTransferCash(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after returning transferCash assign starting...");

        TransferCashDto transferCashDto = (TransferCashDto) joinPoint.getArgs()[0];

        BaseDto<PayDataDto> baseDto = (BaseDto<PayDataDto>) returnValue;
        try {
            if(baseDto.isSuccess()){
                jPushAlertService.autoJPushLotteryLotteryObtainCashAlert(transferCashDto);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        logger.debug("after returning transferCash assign completed");
    }

    private void createAutoJPushRepayAlertJob(long LoanRepayId, boolean isAdvanceRepay) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushRepayAlertJob.JPUSH_ALERT_REPAY_DELAY_MINUTES).toDate();
            jobManager.newJob(JobType.AutoJPushRepayAlert, AutoJPushRepayAlertJob.class)
                    .addJobData(AutoJPushRepayAlertJob.REPAY_ID_KEY, LoanRepayId)
                    .addJobData(AutoJPushRepayAlertJob.ADVANCE_REPAY_FLAG_KEY, isAdvanceRepay)
                    .withIdentity(JobType.AutoJPushRepayAlert.name(), formatMessage(REPAY, LoanRepayId))
                    .runOnceAt(triggerTime)
                    .replaceExistingJob(true)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushRepayAlert job for loanRepayId[" + LoanRepayId + "] fail", e);
        }
    }

    private void createAutoJPushRechargeAlertJob(long orderId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushRechargeAlertJob.JPUSH_ALERT_RECHARGE_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushRechargeAlert, AutoJPushRechargeAlertJob.class)
                    .addJobData(AutoJPushRechargeAlertJob.RECHARGE_ID_KEY, orderId)
                    .withIdentity(JobType.AutoJPushRechargeAlert.name(),  formatMessage(RECHARGE, orderId))
                    .runOnceAt(triggerTime)
                    .replaceExistingJob(true)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushRepayAlert job for loginName[" + orderId + "] fail", e);
        }
    }

    private void createAutoJPushWithDrawApplyAlertJob(long orderId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushWithDrawApplyAlertJob.JPUSH_ALERT_WITHDRAW_APPLY_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushWithDrawApplyAlert, AutoJPushWithDrawApplyAlertJob.class)
                    .addJobData(AutoJPushWithDrawApplyAlertJob.WITHDRAW_APPLY_ID_KEY, orderId)
                    .withIdentity(JobType.AutoJPushWithDrawApplyAlert.name(), formatMessage(WITHDRAW_APPLY, orderId))
                    .runOnceAt(triggerTime)
                    .replaceExistingJob(true)
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
                    .withIdentity(JobType.AutoJPushWithDrawAlert.name(), formatMessage(WITHDRAW, orderId))
                    .runOnceAt(triggerTime)
                    .replaceExistingJob(true)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushWithDrawAlert job for orderId[" + orderId + "] fail", e);
        }
    }

    private void createAutoJPushReferrerRewardAlertJob(long loanId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushReferrerRewardAlertJob.JPUSH_ALERT_REFERRER_REWARD_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushReferrerRewardAlert, AutoJPushReferrerRewardAlertJob.class)
                    .addJobData(AutoJPushReferrerRewardAlertJob.REFERRER_REWARD_ID_KEY, loanId)
                    .withIdentity(JobType.AutoJPushReferrerRewardAlert.name(), formatMessage(REFERRER_REWARD, loanId))
                    .runOnceAt(triggerTime)
                    .replaceExistingJob(true)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send AutoJPushReferrerRewardAlert job for orderId[" + loanId + "] fail", e);
        }
    }

    private String formatMessage(String message, Object object){
        return MessageFormat.format(message, String.valueOf(object));
    }
}
