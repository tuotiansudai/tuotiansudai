package com.tuotiansudai.message.aspect;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.message.util.UserMessageEventGenerator;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Map;

@Aspect
@Component
public class MessageEventAspect {

    private static Logger logger = Logger.getLogger(MessageEventAspect.class);

    @Autowired
    private UserMessageEventGenerator userMessageEventGenerator;

    @Pointcut("execution(* *..UserService.registerUser(..))")
    public void registerUserPointcut() {
    }

    @Pointcut("execution(* *..UserService.registerAccount(..))")
    public void registerAccountPointcut() {
    }

    @Pointcut("execution(* *..RechargeService.rechargeCallback(..))")
    public void rechargeCallbackPointcut() {
    }

    @Pointcut("execution(* *..WithdrawService.withdrawCallback(..))")
    public void withdrawCallbackPointcut() {
    }

    @Pointcut("execution(* *..InvestService.investSuccess(..))")
    public void investSuccessPointcut() {
    }

    @Pointcut("execution(* *..InvestTransferPurchaseService.postPurchase(..))")
    public void transferSuccessPointcut() {
    }

    @Pointcut("execution(* *..paywrapper.service.LoanService.loanOut(..))")
    public void loanOutSuccessPointcut() {
    }

    @Pointcut("execution(* *..NormalRepayService.paybackInvest(..))")
    public void normalRepaySuccessPointcut() {
    }

    @Pointcut("execution(* *..AdvanceRepayService.paybackInvest(..))")
    public void advanceRepaySuccessPointcut() {
    }

    @Pointcut("execution(* *..ReferrerRewardService.rewardReferrer(..))")
    public void rewardReferrerSuccessPointcut() {
    }

    @Pointcut("execution(* *..SignInClient.login(..))")
    public void loginSuccessPointcut() {
    }

    @Pointcut("execution(* *..SignInClient.refresh(..))")
    public void refreshSuccessPointcut() {
    }

    @Pointcut("execution(* *..CouponAssignmentService.assign(..))")
    public void assignCouponPointcut() {
    }


    @AfterReturning(value = "registerUserPointcut()", returning = "returnValue")
    public void afterReturningRegisterUser(JoinPoint joinPoint, boolean returnValue) {
        Object registerUserDto = joinPoint.getArgs()[0];
        try {
            if (returnValue) {
                Class<?> aClass = registerUserDto.getClass();
                Method method = aClass.getMethod("getLoginName");
                String loginName = (String) method.invoke(registerUserDto);
                userMessageEventGenerator.generateRegisterUserSuccessEvent(loginName);
                logger.info(MessageFormat.format("[Message Event Aspect] after register user({0}) pointcut finished", loginName));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }

    @AfterReturning(value = "registerAccountPointcut()", returning = "returnValue")
    public void afterReturningRegisterAccount(JoinPoint joinPoint, BaseDto<PayDataDto> returnValue) {
        Object registerAccountDto = joinPoint.getArgs()[0];
        try {
            if (returnValue.getData().getStatus()) {
                Class<?> aClass = registerAccountDto.getClass();
                Method method = aClass.getMethod("getLoginName");
                String loginName = (String) method.invoke(registerAccountDto);
                userMessageEventGenerator.generateRegisterAccountSuccessEvent(loginName);
                logger.info(MessageFormat.format("[Message Event Aspect] after register account({0}) pointcut finished", loginName));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "rechargeCallbackPointcut()")
    public void afterReturningRechargeCallback(JoinPoint joinPoint) {
        Map<String, String> paramsMap = (Map<String, String>) joinPoint.getArgs()[0];
        long orderId = Long.parseLong(paramsMap.get("order_id"));
        logger.info(MessageFormat.format("[Message Event Aspect] after recharge({0}) pointcut start", String.valueOf(orderId)));
        try {
            userMessageEventGenerator.generateRechargeSuccessEvent(orderId);
            logger.info(MessageFormat.format("[Message Event Aspect] after recharge({0}) pointcut finished", String.valueOf(orderId)));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after recharge({0}) pointcut is fail", String.valueOf(orderId)), e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "withdrawCallbackPointcut()")
    public void afterReturningWithdrawCallback(JoinPoint joinPoint) {
        Map<String, String> paramsMap = (Map<String, String>) joinPoint.getArgs()[0];
        long orderId = Long.parseLong(paramsMap.get("order_id"));
        logger.info(MessageFormat.format("[Message Event Aspect] after withdraw({0}) pointcut start", String.valueOf(orderId)));
        try {
            userMessageEventGenerator.generateWithdrawSuccessEvent(orderId);
            logger.info(MessageFormat.format("[Message Event Aspect] after withdraw({0}) pointcut finished", String.valueOf(orderId)));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after withdraw({0}) pointcut is fail", String.valueOf(orderId)), e);
        }
    }

    @AfterReturning(value = "investSuccessPointcut()")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        Object investModel = joinPoint.getArgs()[0];
        try {
            Class<?> aClass = investModel.getClass();
            Method method = aClass.getMethod("getId");
            long investId = (long) method.invoke(investModel);
            userMessageEventGenerator.generateInvestSuccessEvent(investId);
            logger.info(MessageFormat.format("[Message Event Aspect] after invest success({0}) pointcut finished", String.valueOf(investId)));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @AfterReturning(value = "transferSuccessPointcut()")
    public void afterReturningTransferSuccess(JoinPoint joinPoint) {
        long investId = (Long) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after transfer success({0}) pointcut start", String.valueOf(investId)));
        try {
            userMessageEventGenerator.generateTransferSuccessEvent(investId);
            logger.info(MessageFormat.format("[Message Event Aspect] after transfer success({0}) pointcut finished", String.valueOf(investId)));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after transfer success({0}) pointcut is fail", String.valueOf(investId)), e);
        }
    }

    @AfterReturning(value = "loanOutSuccessPointcut()")
    public void afterReturningLoanOutSuccess(JoinPoint joinPoint) {
        long loanId = (Long) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after loan out success({0}) pointcut start", String.valueOf(loanId)));
        try {
            userMessageEventGenerator.generateLoanOutSuccessEvent(loanId);
            logger.info(MessageFormat.format("[Message Event Aspect] after loan out success({0}) pointcut finished", String.valueOf(loanId)));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after loan out success({0}) pointcut is fail", String.valueOf(loanId)), e);
        }
    }

    @AfterReturning(value = "normalRepaySuccessPointcut() || advanceRepaySuccessPointcut()", returning = "returnValue")
    public void afterReturningRepaySuccess(JoinPoint joinPoint, boolean returnValue) {
        long loanRepayId = (long) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after repay success({0}) pointcut start", String.valueOf(loanRepayId)));
        try {
            if (returnValue) {
                userMessageEventGenerator.generateRepaySuccessEvent(loanRepayId);
                logger.info(MessageFormat.format("[Message Event Aspect] after repay success({0}) pointcut finished", String.valueOf(loanRepayId)));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after repay success({0}) pointcut is fail", String.valueOf(loanRepayId)), e);
        }
    }

    @AfterReturning(value = "rewardReferrerSuccessPointcut()")
    public void afterReturningRewardReferrer(JoinPoint joinPoint) {
        Object loanModel = joinPoint.getArgs()[0];
        try {
            Class<?> aClass = loanModel.getClass();
            Method method = aClass.getMethod("getId");
            long loanId = (long) method.invoke(loanModel);
            userMessageEventGenerator.generateRecommendAwardSuccessEvent(loanId);
            logger.info(MessageFormat.format("[Message Event Aspect] after reward referrer success({0}) pointcut finished", String.valueOf(loanId)));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @AfterReturning(value = "loginSuccessPointcut() || refreshSuccessPointcut()", returning = "signInResult")
    public void afterReturningUserLogin(JoinPoint joinPoint, SignInResult signInResult) {
        try {
            if (signInResult != null && signInResult.isResult()) {
                userMessageEventGenerator.generateCouponExpiredAlertEvent(signInResult.getUserInfo().getLoginName());
                userMessageEventGenerator.generateMembershipExpiredEvent(signInResult.getUserInfo().getLoginName());
                logger.info(MessageFormat.format("[Message Event Aspect] after login success({0}) pointcut finished", signInResult.getUserInfo().getLoginName()));
            }
        } catch (Exception e) {
            logger.error("[Message Event Aspect] after login success({0}) pointcut is fail", e);
        }
    }

    @AfterReturning(value = "assignCouponPointcut()", returning = "returnValue")
    public void afterReturningAssignCoupon(JoinPoint joinPoint, Object returnValue) throws InvocationTargetException {
        if (returnValue == null) {
            return;
        }

        long userCouponId;
        try {
            Class<?> aClass = returnValue.getClass();
            Method method = aClass.getMethod("getId");
            userCouponId = (long) method.invoke(returnValue);
            userMessageEventGenerator.generateAssignCouponSuccessEvent(userCouponId);
            logger.info(MessageFormat.format("[Message Event Aspect] assign user coupon({0}) pointcut finished", String.valueOf(userCouponId)));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
