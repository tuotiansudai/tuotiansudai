package com.tuotiansudai.paywrapper.extrarate.aspect;


import com.tuotiansudai.paywrapper.extrarate.service.CouponPaymentService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Aspect
public class CouponAspect {

    private static Logger logger = Logger.getLogger(CouponAspect.class);

    @Autowired
    private CouponPaymentService couponPaymentService;

    @AfterReturning(value = "execution(* *..LoanService.postLoanOut(*))")
    public void afterReturningLoanOutGenerateCouponPayment(JoinPoint joinPoint) {
        logger.debug("标的放款：生成优惠券还款计划，标的ID: + loanId");
        final long loanId = (long) joinPoint.getArgs()[0];
        try {
            couponPaymentService.generateCouponPayment(loanId);
        } catch (Exception e) {
            logger.error(MessageFormat.format("生成优惠券还款计划失败 (loanId = {0})", String.valueOf(loanId)), e);
        }
    }
}
