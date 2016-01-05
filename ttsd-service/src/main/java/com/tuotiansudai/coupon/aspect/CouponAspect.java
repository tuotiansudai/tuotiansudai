package com.tuotiansudai.coupon.aspect;

import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.RegisterUserDto;
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
    CouponService couponService;

    @Pointcut("execution(* com.tuotiansudai.service.UserService.registerUser(*))")
    public void registerUserPointcut() {
    }

    @AfterReturning(value = "registerUserPointcut()", returning = "returnValue")
    public void afterReturningUserRegistered(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after registerUser pointcut");
        try {
            boolean userRegisterFlag = (boolean) returnValue;
            if (userRegisterFlag) {
                RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
                couponService.assignNewbieCoupon(registerUserDto.getLoginName());
            }
        } catch (Exception e) {
            logger.error("after user registered aspect fail ", e);
        }
    }

}
