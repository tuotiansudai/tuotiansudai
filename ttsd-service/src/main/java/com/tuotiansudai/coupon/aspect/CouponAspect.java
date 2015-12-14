package com.tuotiansudai.coupon.aspect;

import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
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

    @Pointcut("execution(* com.tuotiansudai.service.InvestService.invest(*))")
    public void investPointcut() {
    }

    @AfterReturning(value = "registerUserPointcut()", returning = "returnValue")
    public void afterReturningUserRegistered(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after registerUser pointcut");
        try {
            boolean userRegisterFlag = (boolean) returnValue;
            if (userRegisterFlag) {
                RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
                couponService.afterReturningUserRegistered(registerUserDto.getLoginName());
            }
        } catch (Exception e) {
            logger.error("after user registered aspect fail ", e);
        }
    }

    @AfterReturning(value = "investPointcut()", returning = "returnValue")
    public void afterReturningInvest(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after invest aspect");
        try {
            BaseDto<PayFormDataDto> baseDto= (BaseDto)returnValue;
            if(baseDto.getData() != null && baseDto.getData().getStatus()){
                InvestDto investDto = (InvestDto) joinPoint.getArgs()[0];
                couponService.afterReturningInvest(investDto);
            }
        } catch (Exception e) {
            logger.error("after invest aspect fail ", e);
        }
    }
}
