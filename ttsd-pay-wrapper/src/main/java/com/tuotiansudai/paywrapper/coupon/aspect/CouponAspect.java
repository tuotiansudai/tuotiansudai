package com.tuotiansudai.paywrapper.coupon.aspect;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.SendRedEnvelopeJob;
import com.tuotiansudai.jpush.job.AutoJPushAlertLoanOutJob;
import com.tuotiansudai.jpush.job.SendCouponIncomeJob;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Component
@Aspect
public class CouponAspect {
    private static Logger logger = Logger.getLogger(CouponAspect.class);

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private CouponInvestService couponInvestService;

    @Autowired
    private JobManager jobManager;

    @AfterReturning(value = "execution(* *..NormalRepayService.paybackInvest(*)) || execution(* *..AdvanceRepayService.paybackInvest(*))", returning = "returnValue")
    public void afterReturningPaybackInvest(JoinPoint joinPoint, boolean returnValue) {
        long loanRepayId = (Long) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Coupon Repay {0}] after returning payback invest({1}) aspect is starting...",
                String.valueOf(loanRepayId), String.valueOf(returnValue)));

        if (returnValue) {
            couponRepayService.repay(loanRepayId);
            createSendCouponIncomeJob(loanRepayId);
        }

        logger.info(MessageFormat.format("[Coupon Repay {0}] after returning payback invest({1}) aspect is done",
                String.valueOf(loanRepayId), String.valueOf(returnValue)));
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.invest(*))", returning = "returnValue")
    public void afterReturningInvest(JoinPoint joinPoint, Object returnValue) {
        InvestDto investDto = (InvestDto) joinPoint.getArgs()[0];
        BaseDto<PayFormDataDto> baseDto = (BaseDto<PayFormDataDto>) returnValue;
        if (baseDto.getData() != null && baseDto.getData().getStatus()) {
            long investId = Long.parseLong(baseDto.getData().getFields().get("order_id"));
            try {
                if (CollectionUtils.isNotEmpty(investDto.getUserCouponIds())) {
                    couponInvestService.invest(investId, investDto.getUserCouponIds());
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "execution(* com.tuotiansudai.paywrapper.service.LoanService.cancelLoan(*))", returning = "returnValue")
    public void afterReturningCancelLoan(JoinPoint joinPoint, Object returnValue) {
        long loanId = (long) joinPoint.getArgs()[0];
        BaseDto<PayDataDto> baseDto = (BaseDto<PayDataDto>) returnValue;
        if (baseDto.getData() != null && baseDto.getData().getStatus()) {
            try {
                couponInvestService.cancelUserCoupon(loanId);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }


    @After(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];
        try {
            couponInvestService.investCallback(investModel.getId());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }


    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "execution(* com.tuotiansudai.paywrapper.service.LoanService.loanOut(*))", returning = "returnValue")
    public void afterReturningLoanOut(JoinPoint joinPoint, Object returnValue) {
        final long loanId = (long) joinPoint.getArgs()[0];
        BaseDto<PayDataDto> baseDto = (BaseDto<PayDataDto>) returnValue;
        if (baseDto.getData() != null && baseDto.getData().getStatus()) {
            createSendRedEnvelopeJob(loanId);
            createAutoJPushAlertLoanOutJob(loanId);
        }
    }

    private void createSendRedEnvelopeJob(long loanId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(SendRedEnvelopeJob.SEND_RED_ENVELOPE_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.SendRedEnvelope, SendRedEnvelopeJob.class)
                    .addJobData(SendRedEnvelopeJob.LOAN_ID_KEY, loanId)
                    .withIdentity(JobType.SendRedEnvelope.name(), "Loan-" + loanId)
                    .replaceExistingJob(true)
                    .runOnceAt(triggerTime)
                    .replaceExistingJob(true)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send red envelope job for loan[" + loanId + "] fail", e);
        }
    }

    private void createAutoJPushAlertLoanOutJob(long loanId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AutoJPushAlertLoanOutJob.JPUSH_ALERT_LOAN_OUT_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.AutoJPushAlertLoanOut, AutoJPushAlertLoanOutJob.class)
                    .addJobData(AutoJPushAlertLoanOutJob.LOAN_ID_KEY, loanId)
                    .withIdentity(JobType.AutoJPushAlertLoanOut.name(), "Loan-" + loanId)
                    .replaceExistingJob(true)
                    .runOnceAt(triggerTime)
                    .replaceExistingJob(true)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send red AutoJPushAlertLoanOut job for loan[" + loanId + "] fail", e);
        }
    }

    private void createSendCouponIncomeJob(long loanRepayId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(SendCouponIncomeJob.SEND_COUPON_INCOME_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.SendCouponIncome, SendCouponIncomeJob.class)
                    .addJobData(SendCouponIncomeJob.LOAN_REPAY_ID_KEY, loanRepayId)
                    .withIdentity(JobType.SendCouponIncome.name(), "LoanRepayId-" + loanRepayId)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create send coupon income job for loanRepayId[" + loanRepayId + "] fail", e);
        }
    }
}

