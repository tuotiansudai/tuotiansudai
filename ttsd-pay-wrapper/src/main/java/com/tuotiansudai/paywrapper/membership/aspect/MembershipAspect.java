package com.tuotiansudai.paywrapper.membership.aspect;

import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.membership.service.MembershipInvestService;
import com.tuotiansudai.repository.model.InvestModel;
import org.aopalliance.intercept.Joinpoint;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MembershipAspect {

    private static Logger logger = Logger.getLogger(MembershipAspect.class);

    @Autowired
    MembershipInvestService membershipInvestService;
    @Autowired
    private UserMembershipService UserMembershipService;


    /**
     * after invest success, add membership_point, create membership_experience_bill, upgrade if possible
     *
     * @param joinPoint
     */
    @After(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];

        logger.debug("into MembershipAspect afterReturningInvestSuccess, loginName: " + investModel.getLoginName());

        membershipInvestService.afterInvestSuccess(investModel.getLoginName(), investModel.getAmount(), investModel.getId());

        logger.debug("left MembershipAspect afterReturningInvestSuccess, loginName: " + investModel.getLoginName());
    }
    @AfterReturning(value = "execution(* com.tuotiansudai.service.UserService.registerUser(..))",returning = "returnValue")
    public void afterReturningUserRegister(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after user register pointcut membership");
        if ((boolean) returnValue) {
            RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
            UserMembershipService.createUserMemberByLevel(0,registerUserDto.getLoginName());
        }

    }




}
