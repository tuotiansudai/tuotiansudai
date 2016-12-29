package com.tuotiansudai.paywrapper.coupon.aspect;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.SendRedEnvelopeJob;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.JobManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
@Aspect
public class CouponAspect {
    private static Logger logger = Logger.getLogger(CouponAspect.class);

    @Autowired
    private CouponInvestService couponInvestService;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestMapper investMapper;

//    @AfterReturning(value = "execution(* *..NormalRepayService.paybackInvest(*))", returning = "returnValue")
//    public void afterReturningNormalRepayPaybackInvest(JoinPoint joinPoint, boolean returnValue) {
//        long loanRepayId = (Long) joinPoint.getArgs()[0];
//        logger.info(MessageFormat.format("[normal repay Coupon Repay {0}] after returning payback invest({1}) aspect is starting...",
//                String.valueOf(loanRepayId), String.valueOf(returnValue)));
//
//        if (returnValue) {
//            couponRepayService.repay(loanRepayId, false);
//        }
//
//        logger.info(MessageFormat.format("[normal repay Coupon Repay {0}] after returning payback invest({1}) aspect is done",
//                String.valueOf(loanRepayId), String.valueOf(returnValue)));
//    }

//    @AfterReturning(value = "execution(* *..AdvanceRepayService.paybackInvest(*))", returning = "returnValue")
//    public void afterReturningAdvanceRepayPaybackInvest(JoinPoint joinPoint, boolean returnValue) {
//        long loanRepayId = (Long) joinPoint.getArgs()[0];
//        logger.info(MessageFormat.format("[advance repay Coupon Repay {0}] after returning payback invest({1}) aspect is starting...",
//                String.valueOf(loanRepayId), String.valueOf(returnValue)));
//
//        if (returnValue) {
//            couponRepayService.repay(loanRepayId, true);
//        }
//
//        logger.info(MessageFormat.format("[advance repay  Coupon Repay {0}] after returning payback invest({1}) aspect is done",
//                String.valueOf(loanRepayId), String.valueOf(returnValue)));
//    }

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

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "execution(* com.tuotiansudai.paywrapper.service.LoanService.postLoanOut(*))", returning = "returnValue")
    public void afterReturningLoanOut(JoinPoint joinPoint, Object returnValue) {
        final long loanId = (long) joinPoint.getArgs()[0];
        boolean isSuccess = (boolean) returnValue;
        if (isSuccess) {
            createSendRedEnvelopeJob(loanId);
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

    @AfterReturning(value = "execution(* com.tuotiansudai.paywrapper.service.LoanService.postLoanOut(*))", returning = "returnValue")
    public void afterReturningCreateInvestAchievementUserCoupon(JoinPoint joinPoint, boolean returnValue) {
        if (returnValue) {
            final long loanId = (long) joinPoint.getArgs()[0];
            LoanModel loanModel = loanMapper.findById(loanId);
            createUserCouponModel(loanModel.getFirstInvestAchievementId(), UserGroup.FIRST_INVEST_ACHIEVEMENT, loanId);
            createUserCouponModel(loanModel.getMaxAmountAchievementId(), UserGroup.MAX_AMOUNT_ACHIEVEMENT, loanId);
            createUserCouponModel(loanModel.getLastInvestAchievementId(), UserGroup.LAST_INVEST_ACHIEVEMENT, loanId);
        }
    }

    private void createUserCouponModel(Long investId, final UserGroup userGroup, long loanId) {
        if (investId == null || investId == 0) {
            logger.error(MessageFormat.format("loan id : {0} nothing {1}", String.valueOf(loanId), userGroup.name()));
            return;
        }

        List<CouponModel> couponModelList = couponMapper.findAllActiveCoupons();

        couponModelList.stream().filter(couponModel -> couponModel.getUserGroup().equals(userGroup)
                && DateTime.now().toDate().before(couponModel.getEndTime())
                && DateTime.now().toDate().after(couponModel.getStartTime()))
                .forEach(couponModel -> couponAssignmentService.assignInvestAchievementUserCoupon(loanId, investMapper.findById(investId).getLoginName(), couponModel.getId()));
    }

}

