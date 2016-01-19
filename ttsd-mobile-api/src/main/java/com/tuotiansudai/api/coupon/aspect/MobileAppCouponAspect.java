package com.tuotiansudai.api.coupon.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MobileAppCouponAspect {
    static Logger logger = Logger.getLogger(MobileAppCouponAspect.class);

    @Autowired
    CouponActivationService couponActivationService;

    @Pointcut("execution(* com.tuotiansudai.api.security.MobileAppTokenProvider.refreshToken(..))")
    public void refreshTokenPointcut() {
    }

    @AfterReturning(value = "refreshTokenPointcut()")
    public void afterReturningRefreshTokenPointcut(JoinPoint joinPoint) {
        logger.debug("after user login pointcut");
        try {
            String loginName = (String) joinPoint.getArgs()[0];
            couponActivationService.assignUserCoupon(loginName, Lists.newArrayList(UserGroup.ALL_USER,
                    UserGroup.INVESTED_USER,
                    UserGroup.REGISTERED_NOT_INVESTED_USER,
                    UserGroup.IMPORT_USER));
        } catch (Exception e) {
            logger.error("after user login aspect fail ", e);
        }
    }
}