package com.tuotiansudai.paywrapper.coupon.aspect;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
@Aspect
public class CouponAspect {
    static Logger logger = Logger.getLogger(CouponAspect.class);

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private CouponInvestService couponInvestService;

    @Around(value = "execution(* com.tuotiansudai.paywrapper.service.RepayService.postRepayCallback(*))")
    public Object aroundRepay(ProceedingJoinPoint proceedingJoinPoint) {
        logger.debug("after repay pointcut");
        List<Object> args = Lists.newArrayList(proceedingJoinPoint.getArgs());
        long loanRepayId = (long) args.get(0);
        try {
            boolean isSuccess = (boolean) proceedingJoinPoint.proceed();
            if (isSuccess) {
                couponRepayService.repay(loanRepayId);
            }
            return isSuccess;
        } catch (Throwable throwable) {
            logger.error(MessageFormat.format("Coupon repay aspect is failed (loanRepayId = {0})", String.valueOf(loanRepayId)), throwable);
        }

        return false;
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.invest(*))", returning = "returnValue")
    public void afterReturningInvest(JoinPoint joinPoint, Object returnValue) {
        InvestDto investDto = (InvestDto) joinPoint.getArgs()[0];
        BaseDto<PayFormDataDto> baseDto = (BaseDto<PayFormDataDto>) returnValue;
        if (baseDto.getData() != null && baseDto.getData().getStatus()) {
            long investId = Long.parseLong(baseDto.getData().getFields().get("order_id"));
            Long userCouponId = null;
            try {
                if (!Strings.isNullOrEmpty(investDto.getUserCouponId())) {
                    userCouponId = Long.parseLong(investDto.getUserCouponId());
                }
            } catch (NumberFormatException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            couponInvestService.invest(investId, userCouponId);
        }
    }

    @After(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[1];
        try {
            couponInvestService.investCallback(investModel.getId());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}

