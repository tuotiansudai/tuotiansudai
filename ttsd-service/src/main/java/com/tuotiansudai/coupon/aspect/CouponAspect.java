package com.tuotiansudai.coupon.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
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

import java.util.List;

@Component
@Aspect
public class CouponAspect {
    static Logger logger = Logger.getLogger(CouponAspect.class);

    private final static List<UserGroup> userGroups = Lists.newArrayList(UserGroup.ALL_USER,
            UserGroup.INVESTED_USER,
            UserGroup.REGISTERED_NOT_INVESTED_USER,
            UserGroup.AGENT,
            UserGroup.CHANNEL,
            UserGroup.STAFF,
            UserGroup.STAFF_RECOMMEND_LEVEL_ONE,
            UserGroup.IMPORT_USER,
            UserGroup.NOT_ACCOUNT_NOT_INVESTED_USER,
            UserGroup.MEMBERSHIP_V0,
            UserGroup.MEMBERSHIP_V1,
            UserGroup.MEMBERSHIP_V2,
            UserGroup.MEMBERSHIP_V3,
            UserGroup.MEMBERSHIP_V4,
            UserGroup.MEMBERSHIP_V5);

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Pointcut("execution(* com.tuotiansudai.service.UserService.registerUser(..))")
    public void registerUserPointcut() {
    }

    @Pointcut("execution(* com.tuotiansudai.client.SignInClient.sendSignIn(..))")
    public void loginSuccessPointcut() {
    }

    @Pointcut("execution(* *..MobileAppTokenProvider.refreshToken(..))")
    public void refreshTokenPointcut() {
    }

    @AfterReturning(value = "registerUserPointcut()", returning = "returnValue")
    public void afterReturningUserRegister(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after user register pointcut");
        try {
            if ((boolean) returnValue) {
                RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
                couponAssignmentService.assignUserCoupon(registerUserDto.getMobile(),
                        Lists.newArrayList(UserGroup.ALL_USER, UserGroup.NEW_REGISTERED_USER, UserGroup.NOT_ACCOUNT_NOT_INVESTED_USER));
            }
        } catch (Exception e) {
            logger.error("after user register aspect fail ", e);
        }
    }

    @AfterReturning(value = "loginSuccessPointcut()", returning = "returnValue")
    public void afterReturningUserLogin(JoinPoint joinPoint, BaseDto<LoginDto> returnValue) {
        logger.debug("assign coupon after user login");
        try {
            if(returnValue.getData().getStatus()) {
                SignInDto signInDto = (SignInDto) joinPoint.getArgs()[1];
                String loginName = signInDto.getUsername();
                couponAssignmentService.assignUserCoupon(loginName, userGroups);
            }
        } catch (Exception e) {
            logger.error("assign coupon after user login is failed ", e);
        }
    }

    @AfterReturning(value = "refreshTokenPointcut()")
    public void afterReturningRefreshTokenPointcut(JoinPoint joinPoint) {
        logger.debug("assign coupon after refresh token success");
        try {
            String loginName = (String) joinPoint.getArgs()[0];
            couponAssignmentService.assignUserCoupon(loginName, userGroups);
        } catch (Exception e) {
            logger.error("assign coupon after refresh token is failed", e);
        }
    }
}
