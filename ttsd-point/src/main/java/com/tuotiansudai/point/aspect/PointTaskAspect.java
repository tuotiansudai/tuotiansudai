package com.tuotiansudai.point.aspect;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
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

    @AfterReturning(value = "execution(* com.tuotiansudai.service.BindEmailService.verifyEmail(..))", returning = "returnValue")
    public void afterReturningVerifyEmail(JoinPoint joinPoint, Object returnValue) {
        if (returnValue != null) {
            logger.debug("after returning bind email success, point task aspect starting...");

            String loginName = (String) joinPoint.getArgs()[0];
            pointTaskService.completeTask(PointTask.BIND_EMAIL, loginName);

            logger.debug("after returning bind email success, point task aspect completed");
        }
    }

    @AfterReturning(value = "execution(* *..RechargeService.rechargeCallback(..))")
    public void afterReturningRechargeCallback(JoinPoint joinPoint) {
        Map<String, String> paramsMap = (Map<String, String>)joinPoint.getArgs()[0];
        if ("0000".equals(String.valueOf(paramsMap.get("ret_code"))) && paramsMap.get("order_id") != null) {
            long orderId = Long.parseLong(paramsMap.get("order_id"));
            RechargeModel rechargeModel = rechargeMapper.findById(orderId);
            if (!rechargeModel.isPublicPay()) {
                logger.debug("after returning recharge success, point task aspect starting...");

                pointTaskService.completeTask(PointTask.FIRST_RECHARGE, rechargeModel.getLoginName());

                logger.debug("after returning recharge success, point task aspect completed");
            }
        }
    }

}
