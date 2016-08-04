package com.tuotiansudai.aspect;

import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Aspect
@Component
public class RegisterUserLogAspect {

    private static Logger logger = Logger.getLogger(RegisterUserLogAspect.class);

    @Around(value = "execution(* com.tuotiansudai.service.UserService.registerUser(..))")
    public Object registerUserPointcut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DateTime startTime = new DateTime();
        RegisterUserDto dto = (RegisterUserDto) proceedingJoinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Register User {0}] starting...", dto.getMobile()));
        Object proceed;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error(throwable.getLocalizedMessage(), throwable);
            DateTime endTime = new DateTime();
            logger.info(MessageFormat.format("[Register User {0}] failed, total time {1} ", dto.getMobile(), String.valueOf(endTime.getMillis() - startTime.getMillis())));
            throw throwable;
        }
        DateTime endTime = new DateTime();
        logger.info(MessageFormat.format("[Register User {0}] completed, total time {1} ", dto.getMobile(), String.valueOf(endTime.getMillis() - startTime.getMillis())));
        return proceed;
    }

    @Around(value = "execution(* com.tuotiansudai.repository.mapper.UserMapper.create(..))")
    public Object createUserPointcut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DateTime startTime = new DateTime();
        UserModel model = (UserModel) proceedingJoinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Register User {0}] starting insert user...", model.getMobile()));
        Object proceed;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error(throwable.getLocalizedMessage(), throwable);
            DateTime endTime = new DateTime();
            logger.info(MessageFormat.format("[Register User {0}] insert user failed, total time {1} ", model.getMobile(), String.valueOf(endTime.getMillis() - startTime.getMillis())));
            throw throwable;
        }
        DateTime endTime = new DateTime();
        logger.info(MessageFormat.format("[Register User {0}] insert user completed, total time {1} ", model.getMobile(), String.valueOf(endTime.getMillis() - startTime.getMillis())));
        return proceed;
    }

    @SuppressWarnings(value = "unchecked")
    @Around(value = "execution(* com.tuotiansudai.repository.mapper.UserRoleMapper.create(..))")
    public Object createUserRolePointcut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DateTime startTime = new DateTime();
        List<UserRoleModel> models = (List<UserRoleModel>) proceedingJoinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Register User {0}] starting insert user role...", models.get(0).getLoginName()));
        Object proceed;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error(throwable.getLocalizedMessage(), throwable);
            DateTime endTime = new DateTime();
            logger.info(MessageFormat.format("[Register User {0}] insert user role failed, total time {1} ", models.get(0).getLoginName(), String.valueOf(endTime.getMillis() - startTime.getMillis())));
            throw throwable;
        }
        DateTime endTime = new DateTime();
        logger.info(MessageFormat.format("[Register User {0}] insert user role completed, total time {1} ", models.get(0).getLoginName(), String.valueOf(endTime.getMillis() - startTime.getMillis())));
        return proceed;
    }

    @SuppressWarnings(value = "unchecked")
    @Around(value = "execution(* com.tuotiansudai.service.ReferrerRelationService.generateRelation(..))")
    public Object createUserRelationPointcut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DateTime startTime = new DateTime();
        String loginName = (String) proceedingJoinPoint.getArgs()[1];
        logger.info(MessageFormat.format("[Register User {0}] starting insert user role...", loginName));
        Object proceed;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error(throwable.getLocalizedMessage(), throwable);
            DateTime endTime = new DateTime();
            logger.info(MessageFormat.format("[Register User {0}] insert user role failed, total time {1} ", loginName, String.valueOf(endTime.getMillis() - startTime.getMillis())));
            throw throwable;
        }
        DateTime endTime = new DateTime();
        logger.info(MessageFormat.format("[Register User {0}] insert user role completed, total time {1} ", loginName, String.valueOf(endTime.getMillis() - startTime.getMillis())));
        return proceed;
    }

    @SuppressWarnings(value = "unchecked")
    @Around(value = "execution(* com.tuotiansudai.membership.repository.mapper.UserMembershipMapper.create(..))")
    public Object createUserMemberPointcut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DateTime startTime = new DateTime();
        UserMembershipModel userMembershipModel = (UserMembershipModel) proceedingJoinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Register User {0}] starting insert user member...", userMembershipModel.getLoginName()));
        Object proceed;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error(throwable.getLocalizedMessage(), throwable);
            DateTime endTime = new DateTime();
            logger.info(MessageFormat.format("[Register User {0}] insert user member failed, total time {1} ", userMembershipModel.getLoginName(), String.valueOf(endTime.getMillis() - startTime.getMillis())));
            throw throwable;
        }
        DateTime endTime = new DateTime();
        logger.info(MessageFormat.format("[Register User {0}] insert user member completed, total time {1} ", userMembershipModel.getLoginName(), String.valueOf(endTime.getMillis() - startTime.getMillis())));
        return proceed;
    }
}
