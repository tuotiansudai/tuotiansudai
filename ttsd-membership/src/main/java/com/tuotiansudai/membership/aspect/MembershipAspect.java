package com.tuotiansudai.membership.aspect;

import com.tuotiansudai.membership.service.MembershipInvestService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Aspect
public class MembershipAspect {

    private static Logger logger = Logger.getLogger(MembershipAspect.class);

    @Autowired
    private MembershipInvestService membershipInvestService;


    /**
     * after invest success, add membership_point, create membership_experience_bill, upgrade if possible
     *
     * @param joinPoint
     */
    @After(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        try {
            Object param = joinPoint.getArgs()[0];
            Class<?> aClass = param.getClass();
            String loginName = (String ) aClass.getMethod("getLoginName").invoke(param);
            long investId = (Long) aClass.getMethod("getId").invoke(param);
            long amount = (Long) aClass.getMethod("getAmount").invoke(param);
            logger.debug("into MembershipAspect afterReturningInvestSuccess, loginName: " + loginName);

            membershipInvestService.afterInvestSuccess(loginName, amount, investId);

            logger.debug("left MembershipAspect afterReturningInvestSuccess, loginName: " + loginName);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
