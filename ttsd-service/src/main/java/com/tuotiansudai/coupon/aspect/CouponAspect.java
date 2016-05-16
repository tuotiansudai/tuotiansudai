package com.tuotiansudai.coupon.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.SignInDto;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CouponAspect {
    static Logger logger = Logger.getLogger(CouponAspect.class);

    @Autowired
    private CouponActivationService couponActivationService;

    @Pointcut("execution(* com.tuotiansudai.service.UserService.registerUser(..))")
    public void registerUserPointcut() {
    }

    @Pointcut("execution(* com.tuotiansudai.client.SignInClient.sendSignIn(..))")
    public void loginSuccessPointcut() {
    }

    @AfterReturning(value = "registerUserPointcut()", returning = "returnValue")
    public void afterReturningUserRegister(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after user register pointcut");
        try {
            if ((boolean) returnValue) {
                RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
                couponActivationService.assignUserCoupon(registerUserDto.getLoginName(), Lists.newArrayList(UserGroup.ALL_USER, UserGroup.NEW_REGISTERED_USER),null, null);
            }
        } catch (Exception e) {
            logger.error("after user register aspect fail ", e);
        }
    }

    @AfterReturning(value = "loginSuccessPointcut()", returning = "returnValue")
    public void afterReturningUserLogin(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after user login pointcut");
        BaseDto<LoginDto> baseDto = (BaseDto<LoginDto>)returnValue;
        if (baseDto.isSuccess()) {
            try {
                SignInDto signInDto = (SignInDto) joinPoint.getArgs()[1];
                couponActivationService.assignUserCoupon(signInDto.getUsername(), Lists.newArrayList(UserGroup.ALL_USER,
                        UserGroup.INVESTED_USER,
                        UserGroup.REGISTERED_NOT_INVESTED_USER,
                        UserGroup.AGENT,
                        UserGroup.CHANNEL,
                        UserGroup.STAFF,
                        UserGroup.STAFF_RECOMMEND_LEVEL_ONE,
                        UserGroup.IMPORT_USER), null, null);
            } catch (Exception e) {
                logger.error("after user login aspect fail ", e);
            }
        }
    }

}
