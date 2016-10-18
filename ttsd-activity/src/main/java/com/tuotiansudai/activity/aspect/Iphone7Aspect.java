package com.tuotiansudai.activity.aspect;

import com.tuotiansudai.activity.service.Iphone7LotteryService;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;

public class Iphone7Aspect {

    static Logger logger = Logger.getLogger(Iphone7Aspect.class);

    @Autowired
    private Iphone7LotteryService iphone7LotteryService;


    @AfterReturning(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        logger.info("after returning invest,iphone7 aspect starting...");

        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];
        String loginName = investModel.getLoginName();


        iphone7LotteryService.getLotteryNumber(investModel);

        logger.info("after returning invest, iphone7 aspect completed");
    }
}
