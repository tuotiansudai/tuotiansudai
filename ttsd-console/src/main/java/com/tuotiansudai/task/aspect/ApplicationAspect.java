package com.tuotiansudai.task.aspect;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.EditUserDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class ApplicationAspect {

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Around(value = "execution(* com.tuotiansudai.service.UserService.editUser(..))")
    public Object aroundEditUser(ProceedingJoinPoint proceedingJoinPoint, JoinPoint joinPoint) throws Throwable {
        EditUserDto editUserDto = (EditUserDto)joinPoint.getArgs()[1];
        return proceedingJoinPoint.proceed();
    }

}
