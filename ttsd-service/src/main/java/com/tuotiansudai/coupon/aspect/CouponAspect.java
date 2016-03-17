package com.tuotiansudai.coupon.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.RegisterUserDto;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class CouponAspect {
    static Logger logger = Logger.getLogger(CouponAspect.class);

    @Autowired
    private CouponActivationService couponActivationService;

    @Pointcut("execution(* com.tuotiansudai.service.UserService.registerUser(..))")
    public void registerUserPointcut() {
    }

    @Pointcut("execution(* com.tuotiansudai.security.MySimpleUrlAuthenticationSuccessHandler.onAuthenticationSuccess(..))")
    public void loginSuccessPointcut() {
    }

    @AfterReturning(value = "registerUserPointcut()", returning = "returnValue")
    public void afterReturningUserRegister(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after user register pointcut");
        try {
            if ((boolean) returnValue) {
                RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
                couponActivationService.assignUserCoupon(registerUserDto.getLoginName(), Lists.newArrayList(UserGroup.ALL_USER, UserGroup.NEW_REGISTERED_USER),null);
            }
        } catch (Exception e) {
            logger.error("after user register aspect fail ", e);
        }
    }

    @AfterReturning(value = "loginSuccessPointcut()")
    public void afterReturningUserLogin(JoinPoint joinPoint) {
        logger.debug("after user login pointcut");
        try {
            HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
            couponActivationService.assignUserCoupon(request.getParameter("username"), Lists.newArrayList(UserGroup.ALL_USER,
                    UserGroup.INVESTED_USER,
                    UserGroup.REGISTERED_NOT_INVESTED_USER,
                    UserGroup.IMPORT_USER),null);
        } catch (Exception e) {
            logger.error("after user login aspect fail ", e);
        }
    }
}
