package com.tuotiansudai.task.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class ApplicationAspect {

    @Around(value = "execution(* com.tuotiansudai.service.UserService.editUser(..))")
    public Object aroundEditUser(ProceedingJoinPoint proceedingJoinPoint, JoinPoint joinPoint) throws Throwable {
        
        return proceedingJoinPoint.proceed();
    }

}
