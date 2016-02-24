package com.tuotiansudai.point.aspect;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.model.InvestModel;
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

    @AfterReturning(value = "execution(* *..RegisterService.register(..))", returning = "returnValue")
    public void afterReturningRegisterAccount(JoinPoint joinPoint, BaseDto<PayDataDto> returnValue) {
        logger.debug("after returning register account, point task aspect starting...");

        if (returnValue.getData().getStatus()) {
            RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
            pointTaskService.completeTask(PointTask.REGISTER, registerUserDto.getLoginName());
        }

        logger.debug("after returning register account, point task aspect completed");
    }

    @AfterReturning(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        logger.debug("after returning invest success, point task aspect starting...");

        InvestModel investModel = (InvestModel) joinPoint.getArgs()[1];
        pointTaskService.completeTask(PointTask.FIRST_INVEST, investModel.getLoginName());
        pointTaskService.completeTask(PointTask.SUM_INVEST_10000, investModel.getLoginName());

        logger.debug("after returning invest success, point task aspect completed");
    }

}
