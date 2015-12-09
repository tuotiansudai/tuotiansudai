package com.tuotiansudai.coupon.aspect;

import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.RepayDto;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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

    @Pointcut("execution(* com.tuotiansudai.service.LoanService.getLoanDetail(*))")
    public void getLoanDetailPointcut() {
    }

    @Pointcut("execution(* com.tuotiansudai.service.InvestService.invest(*))")
    public void investPointcut() {
    }

    @Pointcut("execution(* com.tuotiansudai.service.RepayService.repay(*))")
    public void repayPointcut() {
    }

    @After("registerUserPointcut()")
    public void afterUserRegistered(JoinPoint joinPoint) {
        logger.debug("after registerUser pointcut");
        try {
            RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
            couponService.afterUserRegistered(registerUserDto.getLoginName());
        } catch (Exception e) {
            logger.error("after user registered aspect fail ", e);
        }
    }

    @After("getLoanDetailPointcut()")
    public void afterGetLoanDetail(JoinPoint joinPoint) {
        logger.debug("after get loan detail pointcut");
        try {
            String loginName = String.valueOf(joinPoint.getArgs()[0]);
            long loanId = (Long) joinPoint.getArgs()[1];

            couponService.afterGetLoanDetail(loginName, loanId);
        } catch (Exception e) {
            logger.error("after user registered aspect fail ", e);
        }
    }

    @After("investPointcut()")
    public void afterInvest(JoinPoint joinPoint) {
        logger.debug("after registerUser pointcut");
        try {
            InvestDto investDto = (InvestDto) joinPoint.getArgs()[0];
            couponService.afterInvest(investDto.getLoginName(), investDto.getLoanIdLong());
        } catch (Exception e) {
            logger.error("after invest aspect fail ", e);
        }
    }

    @After("repayPointcut()")
    public void afterRepay(JoinPoint joinPoint) {
        logger.debug("after repay pointcut");
        try {
            RepayDto repayDto = (RepayDto) joinPoint.getArgs()[0];
            couponService.afterRepay(repayDto.getLoanId(), repayDto.isAdvanced());
        } catch (Exception e) {
            logger.error("after repay aspect fail ", e);
        }
    }
}
