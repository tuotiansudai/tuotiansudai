package com.tuotiansudai.paywrapper.coupon.aspect;


import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Aspect
public class CouponRepayAspect {

    private static Logger logger = Logger.getLogger(CouponRepayAspect.class);

    @Autowired
    private CouponRepayService couponRepayService;

    @AfterReturning(value = "execution(* *..LoanService.postLoanOut(*))", returning = "returnValue")
    public void afterReturningLoanOutGenerateCouponPayment(JoinPoint joinPoint, boolean returnValue) {
        final long loanId = (long) joinPoint.getArgs()[0];
        if (returnValue) {
            logger.info(MessageFormat.format("loan out : generate coupon payment , (loanId : {0}) ", String.valueOf(loanId)));
            try {
                couponRepayService.generateCouponRepay(loanId);
            } catch (Exception e) {
                logger.error(MessageFormat.format("loan out : generate coupon payment faild, (loanId : {0})", String.valueOf(loanId)), e);
                return;
            }
        }
        logger.info(MessageFormat.format("loan out : generate coupon payment success , (loanId : {0})", String.valueOf(loanId)));
    }
}
