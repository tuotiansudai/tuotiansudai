package com.tuotiansudai.api.aspect;

import com.tuotiansudai.api.dto.AutoInvestPlanRequestDto;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.repository.mapper.UserOpLogMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserOpLogModel;
import com.tuotiansudai.repository.model.UserOpType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

@Aspect
@Component
public class UserOpLogAspect {

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    @AfterReturning(value = "execution(* com.tuotiansudai.api.service.MobileAppAutoInvestPlanService.buildAutoInvestPlan(..))")
    public void afterBuildAutoInvestPlan(JoinPoint joinPoint) {
        AutoInvestPlanRequestDto dto = (AutoInvestPlanRequestDto)joinPoint.getArgs()[0];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(dto.getBaseParam().getUserId());
        logModel.setIp(dto.getIp());
        logModel.setDeviceId(dto.getBaseParam().getDeviceId());
        logModel.setSource(Source.valueOf(dto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.AUTO_INVEST);
        logModel.setCreatedTime(new Date());
        logModel.setDescription(dto.isEnabled() ? "Turn On" : "Turn Off");

        userOpLogMapper.create(logModel);
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.api.service.MobileAppNoPasswordInvestTurnOnService.noPasswordInvestTurnOn(..))")
    public void afterNoPasswordInvestTurnOn(JoinPoint joinPoint) {
        BaseParamDto dto = (BaseParamDto)joinPoint.getArgs()[0];
        String ip = (String)joinPoint.getArgs()[1];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(dto.getBaseParam().getUserId());
        logModel.setIp(ip);
        logModel.setDeviceId(dto.getBaseParam().getDeviceId());
        logModel.setSource(Source.valueOf(dto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.INVEST_NO_PASSWORD);
        logModel.setCreatedTime(new Date());
        logModel.setDescription("Turn On");

        userOpLogMapper.create(logModel);
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.api.service.MobileAppNoPasswordInvestTurnOffService.noPasswordInvestTurnOff(..))", returning = "returnValue")
    public void afterNoPasswordInvestTurnOff(JoinPoint joinPoint, Object returnValue) {
        BaseParamDto dto = (BaseParamDto)joinPoint.getArgs()[0];
        String ip = (String)joinPoint.getArgs()[1];

        BaseResponseDto retDto = (BaseResponseDto) returnValue;

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(dto.getBaseParam().getUserId());
        logModel.setIp(ip);
        logModel.setDeviceId(dto.getBaseParam().getDeviceId());
        logModel.setSource(Source.valueOf(dto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.INVEST_NO_PASSWORD);
        logModel.setCreatedTime(new Date());
        logModel.setDescription("Turn Off. " + (retDto.isSuccess() ? "Success" : "Fail"));

        userOpLogMapper.create(logModel);
    }
}
