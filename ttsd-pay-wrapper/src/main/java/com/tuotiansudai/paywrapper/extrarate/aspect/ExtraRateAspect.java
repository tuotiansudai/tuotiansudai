package com.tuotiansudai.paywrapper.extrarate.aspect;

import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.loanout.LoanOutInvestCalculationService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Aspect
public class ExtraRateAspect {

//    private static Logger logger = Logger.getLogger(ExtraRateAspect.class);
//
//    @Autowired
//    private ExtraRateService extraRateService;
//
//    @AfterReturning(value = "execution(* *..NormalRepayService.paybackInvest(*))", returning = "returnValue")
//    public void afterReturningNormalRepayPaybackInvest(JoinPoint joinPoint, boolean returnValue) {
//        long loanRepayId = (Long) joinPoint.getArgs()[0];
//        logger.info(MessageFormat.format("[Normal Repay {0}] extra rate after returning payback invest({1}) aspect is starting...",
//                String.valueOf(loanRepayId), String.valueOf(returnValue)));
//
//        if (returnValue) {
//            extraRateService.normalRepay(loanRepayId);
//        }
//
//        logger.info(MessageFormat.format("[Normal Repay {0}] extra rate after returning payback invest({1}) aspect is done",
//                String.valueOf(loanRepayId), String.valueOf(returnValue)));
//
//    }
//
//    @AfterReturning(value = "execution(* *..AdvanceRepayService.paybackInvest(*))", returning = "returnValue")
//    public void afterReturningAdvanceRepayPaybackInvest(JoinPoint joinPoint, boolean returnValue) {
//        long loanRepayId = (Long) joinPoint.getArgs()[0];
//        logger.info(MessageFormat.format("[Advance Repay {0}] extra rate after returning payback invest({1}) aspect is starting...",
//                String.valueOf(loanRepayId), String.valueOf(returnValue)));
//
//        if (returnValue) {
//            extraRateService.advanceRepay(loanRepayId);
//        }
//
//        logger.info(MessageFormat.format("[Advance Repay {0}] extra rate after returning payback invest({1}) aspect is done",
//                String.valueOf(loanRepayId), String.valueOf(returnValue)));
//    }

}

