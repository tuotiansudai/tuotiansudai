package com.tuotiansudai.point.aspect;

import com.tuotiansudai.dto.AgreementBusinessType;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PointTaskAspect {

    static Logger logger = Logger.getLogger(PointTaskAspect.class);

    @Autowired
    private PointTaskService pointTaskService;

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "execution(* *..AgreementService.postAgreementCallback(..))")
    public void afterReturningNoPasswordInvestAgreementCallback(JoinPoint joinPoint) {
        logger.info("after returning agreement, point task aspect starting...");

        String loginName = (String) joinPoint.getArgs()[0];
        AgreementBusinessType agreementBusinessType = (AgreementBusinessType) joinPoint.getArgs()[1];

        if (AgreementBusinessType.NO_PASSWORD_INVEST == agreementBusinessType) {
            pointTaskService.completeAdvancedTask(PointTask.FIRST_TURN_ON_NO_PASSWORD_INVEST, loginName);
        }

        logger.info("after returning agreement, point task aspect completed");
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "execution(* *..InvestService.switchNoPasswordInvest(..))")
    public void afterReturningTurnOnNoPasswordInvestCallback(JoinPoint joinPoint) {
        logger.info("after returning turn on no password invest, point task aspect starting...");

        String loginName = (String) joinPoint.getArgs()[0];
        boolean isTurn = (boolean) joinPoint.getArgs()[1];

        if (isTurn) {
            pointTaskService.completeAdvancedTask(PointTask.FIRST_TURN_ON_NO_PASSWORD_INVEST, loginName);
        }

        logger.info("after returning turn on no password invest, point task aspect completed");
    }

}
