package com.tuotiansudai.membership.aspect;

import com.tuotiansudai.membership.service.MembershipGiveService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.MessageFormat;

@Aspect
@Component
public class ReceiveMembershipAspect {

    static Logger logger = Logger.getLogger(ReceiveMembershipAspect.class);

    @Autowired
    MembershipGiveService membershipGiveService;

    @AfterReturning(value = "execution(public boolean *.registerUser(..))", returning = "returnValue")
    public void registerUserPointcut(JoinPoint joinPoint, boolean returnValue) throws Throwable {
        if (!returnValue) {
            return;
        }
        Object dto = joinPoint.getArgs()[0];
        String loginName = "";
        try {
            Method getLoginName = dto.getClass().getMethod("getLoginName");
            loginName = (String) getLoginName.invoke(dto);
            membershipGiveService.newUserReceiveMembership(loginName);
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0} does not receive Membership.", loginName));
        }
    }
}
