package com.tuotiansudai.paywrapper.membership.aspect;

import com.tuotiansudai.membership.service.MembershipInvestService;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MembershipAspect {

    private static Logger logger = Logger.getLogger(MembershipAspect.class);

    @Autowired
    MembershipInvestService membershipInvestService;


    /**
     * after invest success, add membership_point, create membership_experience_bill, upgrade if possible
     *
     * @param joinPoint
     */
    @After(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];

        logger.debug("into MembershipAspect afterReturningInvestSuccess, loginName: " + investModel.getLoginName());

        membershipInvestService.afterInvestSuccess(investModel.getLoginName(), investModel.getAmount());

        logger.debug("left MembershipAspect afterReturningInvestSuccess, loginName: " + investModel.getLoginName());
    }


}
