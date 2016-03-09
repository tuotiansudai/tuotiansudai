package com.tuotiansudai.point.aspect;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.RechargeModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Aspect
public class PointTaskAspect {

    static Logger logger = Logger.getLogger(PointTaskAspect.class);

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private RechargeMapper rechargeMapper;

    @AfterReturning(value = "execution(* *..RegisterService.register(..))", returning = "returnValue")
    public void afterReturningRegisterAccount(JoinPoint joinPoint, BaseDto<PayDataDto> returnValue) {
        logger.debug("after returning register account, point task aspect starting...");

        if (returnValue.getData().getStatus()) {
            RegisterAccountDto registerAccountDto = (RegisterAccountDto) joinPoint.getArgs()[0];
            pointTaskService.completeTask(PointTask.REGISTER, registerAccountDto.getLoginName());
        }

        logger.debug("after returning register account, point task aspect completed");
    }

    @AfterReturning(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        logger.debug("after returning invest, point task aspect starting...");

        InvestModel investModel = (InvestModel) joinPoint.getArgs()[1];
        pointTaskService.completeTask(PointTask.FIRST_INVEST, investModel.getLoginName());
        pointTaskService.completeTask(PointTask.SUM_INVEST_10000, investModel.getLoginName());

        logger.debug("after returning invest, point task aspect completed");
    }

    @AfterReturning(value = "execution(* *..BindEmailService.verifyEmail(..))")
    public void afterReturningVerifyEmail(JoinPoint joinPoint) {
        logger.debug("after returning bind email, point task aspect starting...");

        String loginName = (String) joinPoint.getArgs()[0];
        pointTaskService.completeTask(PointTask.BIND_EMAIL, loginName);

        logger.debug("after returning bind email, point task aspect completed");
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "execution(* *..RechargeService.rechargeCallback(..))")
    public void afterReturningRechargeCallback(JoinPoint joinPoint) {
        logger.debug("after returning recharge, point task aspect starting...");

        Map<String, String> paramsMap = (Map<String, String>) joinPoint.getArgs()[0];
        RechargeModel rechargeModel = rechargeMapper.findById(Long.parseLong(paramsMap.get("order_id")));
        pointTaskService.completeTask(PointTask.FIRST_RECHARGE, rechargeModel.getLoginName());

        logger.debug("after returning recharge, point task aspect completed");
    }
}
