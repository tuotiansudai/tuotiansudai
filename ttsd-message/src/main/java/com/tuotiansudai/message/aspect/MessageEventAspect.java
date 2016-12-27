package com.tuotiansudai.message.aspect;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.message.util.UserMessageEventGenerator;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Map;

@Aspect
@Component
@Order(98)
public class MessageEventAspect {

    private static Logger logger = Logger.getLogger(MessageEventAspect.class);

    @Autowired
    private UserMessageEventGenerator userMessageEventGenerator;

    @Pointcut("execution(* *..UserService.registerUser(..))")
    public void registerUserPointcut() {
    }

    @Pointcut("execution(* *..AccountService.registerAccount(..))")
    public void registerAccountPointcut() {
    }

    @Pointcut("execution(* *..WithdrawService.withdrawCallback(..))")
    public void withdrawCallbackPointcut() {
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

    @Pointcut("execution(* *..InvestTransferService.cancelTransferApplication(..))")
    public void cancelInvestTrasnferPointcut() {
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
    @AfterReturning(value = "withdrawCallbackPointcut()")
    public void afterReturningWithdrawCallback(JoinPoint joinPoint) {
        Map<String, String> paramsMap = (Map<String, String>) joinPoint.getArgs()[0];
        long orderId = Long.parseLong(paramsMap.get("order_id"));
        logger.info(MessageFormat.format("[Message Event Aspect] after withdraw({0}) pointcut start", String.valueOf(orderId)));
        try {
            userMessageEventGenerator.generateWithdrawSuccessOrApplicationSuccessEvent(orderId);
            logger.info(MessageFormat.format("[Message Event Aspect] after withdraw({0}) pointcut finished", String.valueOf(orderId)));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after withdraw({0}) pointcut is fail", String.valueOf(orderId)), e);
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

    @AfterReturning(value = "loanOutSuccessPointcut()", returning = "baseDto")
    public void afterReturningLoanOutSuccess(JoinPoint joinPoint, BaseDto<PayDataDto> baseDto) {
        if (!baseDto.getData().getStatus()) {
            return;
        }
        long loanId = (Long) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after loan out success({0}) pointcut start", String.valueOf(loanId)));
        try {
            userMessageEventGenerator.generateLoanOutSuccessEvent(loanId);
            logger.info(MessageFormat.format("[Message Event Aspect] after loan out success({0}) pointcut finished", String.valueOf(loanId)));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after loan out success({0}) pointcut is fail", String.valueOf(loanId)), e);
        }
    }

    @AfterReturning(value = "normalRepaySuccessPointcut()", returning = "returnValue")
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

    @AfterReturning(value = "advanceRepaySuccessPointcut()", returning = "returnValue")
    public void afterReturningAdvancedRepaySuccess(JoinPoint joinPoint, boolean returnValue) {
        long loanRepayId = (long) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after advanced repay success({0}) pointcut start", String.valueOf(loanRepayId)));
        try {
            if (returnValue) {
                userMessageEventGenerator.generateAdvancedRepaySuccessEvent(loanRepayId);
                logger.info(MessageFormat.format("[Message Event Aspect] after advanced repay success({0}) pointcut finished", String.valueOf(loanRepayId)));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after advanced repay success({0}) pointcut is fail", String.valueOf(loanRepayId)), e);
        }
    }

    @AfterReturning("rewardReferrerSuccessPointcut()")
    public void afterReturningRewardReferrer(JoinPoint joinPoint) {
        try {
            long loanId = (long) joinPoint.getArgs()[0];
            userMessageEventGenerator.generateRecommendAwardSuccessEvent(loanId);
            logger.info(MessageFormat.format("[Message Event Aspect] after reward referrer success({0}) pointcut finished", String.valueOf(loanId)));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @AfterReturning(value = "loginSuccessPointcut() || refreshSuccessPointcut()", returning = "signInResult")
    public void afterReturningUserLogin(JoinPoint joinPoint, SignInResult signInResult) {
        logger.info("[Message Event Aspect] after returning user login start");
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

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "cancelInvestTrasnferPointcut()", returning = "returnValue")
    public void afterCancelInvestTrasnfer(JoinPoint joinPoint, boolean returnValue) {
        if (!returnValue) {
            return;
        }
        long transferApplicationId = (long) joinPoint.getArgs()[0];
        try {
            userMessageEventGenerator.generateTransferFailEvent(transferApplicationId);
            logger.info(MessageFormat.format("[Message Event Aspect] after transferApplication failed pointcut finished. transferApplicationId:{0}", transferApplicationId));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after transferApplication failed pointcut is fail. transferApplicationId:{0}", transferApplicationId), e);
        }
    }
}
