package com.tuotiansudai.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.Environment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EnvironmentAspect {

    @Value("${common.environment}")
    private Environment environment;

    @Value("${common.fake.captcha}")
    private String fakeCaptcha;


    @Around(value = "execution(* *..CaptchaHelper.captchaVerify(..))")
    public Object aroundCaptchaVerify(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (Environment.SMOKE == environment) {
            return true;
        }

        if(Environment.PRODUCTION != environment && proceedingJoinPoint.getArgs().length == 2
                && proceedingJoinPoint.getArgs()[1].equals(fakeCaptcha)){
            return true;
        }

        return proceedingJoinPoint.proceed();
    }

    @Around(value = "execution(public com.tuotiansudai.dto.BaseDto<com.tuotiansudai.dto.SmsDataDto> com.tuotiansudai.client.SmsWrapperClient.send*(..))")
    public Object aroundSmsSend(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (Lists.newArrayList(Environment.DEV, Environment.SMOKE).contains(environment)) {
            BaseDto<SmsDataDto> resultDto = new BaseDto<>();
            SmsDataDto dataDto = new SmsDataDto();
            dataDto.setStatus(true);
            resultDto.setData(dataDto);
            return resultDto;
        }
        return proceedingJoinPoint.proceed();
    }
}
