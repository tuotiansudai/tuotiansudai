package com.tuotiansudai.paywrapper.coupon.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
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
}

