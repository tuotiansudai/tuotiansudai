package com.tuotiansudai.paywrapper.extrarate.aspect;

import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.extrarate.service.LoanOutInvestCalculationService;
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

    private static Logger logger = Logger.getLogger(ExtraRateAspect.class);

    @Autowired
    private ExtraRateService extraRateService;

    @Autowired
    private LoanOutInvestCalculationService investExtraRateService;

    @After(value = "execution(* *..InvestTransferPurchaseService.postPurchase(*))")
    public void afterReturningInvestTransferPurchase(JoinPoint joinPoint) {
        long investId = (Long) joinPoint.getArgs()[0];
        extraRateService.transferPurchase(investId);
    }

    @AfterReturning(value = "execution(* *..LoanService.postLoanOut(*))", returning = "returnValue")
    public void afterReturningLoanOutInvestCalculation(JoinPoint joinPoint, boolean returnValue) {
        if (returnValue) {
            final long loanId = (long) joinPoint.getArgs()[0];
            logger.info(MessageFormat.format("[extra rate loan:{0}] aspect is starting",loanId));
            investExtraRateService.rateIncreases(loanId);
            logger.info(MessageFormat.format("[extra rate loan:{0}] aspect is end", loanId));
        }

    }
}

