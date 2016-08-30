package com.tuotiansudai.message.aspect;

import com.google.common.base.Strings;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.message.util.UserMessageEventGenerator;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Map;

@Aspect
@Component
public class MessageEventAspect {

    private static Logger logger = Logger.getLogger(MessageEventAspect.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

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

    @Pointcut("execution(* *..MySimpleUrlAuthenticationSuccessHandler.onAuthenticationSuccess(..))")
    public void loginSuccessPointcut() {
    }

    @Pointcut("execution(* *..CouponAssignmentService.assign(..))")
    public void assignCouponPointcut() {
    }


    @AfterReturning(value = "registerUserPointcut()", returning = "returnValue")
    public void afterReturningRegisterUser(JoinPoint joinPoint, boolean returnValue) {
        RegisterUserDto registerUserDto = (RegisterUserDto) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after register user({0}) pointcut start", registerUserDto.getLoginName()));
        try {
            if (returnValue) {
                userMessageEventGenerator.generateRegisterUserSuccessEvent(registerUserDto.getLoginName());
                logger.info(MessageFormat.format("[Message Event Aspect] after register user({0}) pointcut finished", registerUserDto.getLoginName()));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after register user({0}) pointcut is fail", registerUserDto.getLoginName()), e);
        }

    }

    @AfterReturning(value = "registerAccountPointcut()", returning = "returnValue")
    public void afterReturningRegisterAccount(JoinPoint joinPoint, BaseDto<PayDataDto> returnValue) {
        RegisterAccountDto registerAccountDto = (RegisterAccountDto) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after register account({0}) pointcut start", registerAccountDto.getLoginName()));
        try {
            if (returnValue.getData().getStatus()) {
                userMessageEventGenerator.generateRegisterAccountSuccessEvent(registerAccountDto.getLoginName());
                logger.info(MessageFormat.format("[Message Event Aspect] after register account({0}) pointcut finished", registerAccountDto.getLoginName()));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after register account({0}) pointcut is fail", registerAccountDto.getLoginName()), e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "rechargeCallbackPointcut()")
    public void afterReturningRechargeCallback(JoinPoint joinPoint) {
        Map<String, String> paramsMap = (Map<String, String>) joinPoint.getArgs()[0];
        long orderId = Long.parseLong(paramsMap.get("order_id"));
        logger.info(MessageFormat.format("[Message Event Aspect] after recharge({0}) pointcut start", String.valueOf(orderId)));
        try {
            RechargeModel rechargeModel = rechargeMapper.findById(orderId);
            if (rechargeModel != null && RechargeStatus.SUCCESS == rechargeModel.getStatus()) {
                userMessageEventGenerator.generateRechargeSuccessEvent(rechargeModel.getLoginName(), rechargeModel.getAmount());
                logger.info(MessageFormat.format("[Message Event Aspect] after recharge({0}) pointcut finished", String.valueOf(orderId)));
            }
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
            WithdrawModel withdrawModel = withdrawMapper.findById(orderId);
            if (withdrawModel != null && WithdrawStatus.SUCCESS == withdrawModel.getStatus()) {
                userMessageEventGenerator.generateWithdrawSuccessEvent(withdrawModel.getLoginName(), withdrawModel.getAmount());
                logger.info(MessageFormat.format("[Message Event Aspect] after withdraw({0}) pointcut finished", String.valueOf(orderId)));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after withdraw({0}) pointcut is fail", String.valueOf(orderId)), e);
        }
    }

    @AfterReturning(value = "investSuccessPointcut()")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];
        long investId = investModel.getId();
        logger.info(MessageFormat.format("[Message Event Aspect] after invest success({0}) pointcut start", String.valueOf(investId)));
        try {
            userMessageEventGenerator.generateInvestSuccessEvent(investModel.getLoginName(), investModel.getLoanId(), investModel.getAmount());
            logger.info(MessageFormat.format("[Message Event Aspect] after invest success({0}) pointcut finished", String.valueOf(investId)));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after invest success({0}) pointcut is fail", String.valueOf(investId)), e);
        }
    }

    @AfterReturning(value = "transferSuccessPointcut()")
    public void afterReturningTransferSuccess(JoinPoint joinPoint) {
        long investId = (Long) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after transfer success({0}) pointcut start", String.valueOf(investId)));
        try {
            TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(investId);
            userMessageEventGenerator.generateTransferSuccessEvent(transferApplicationModel.getLoginName(), transferApplicationModel.getId());
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
        LoanModel loanModel = (LoanModel) joinPoint.getArgs()[0];
        logger.info(MessageFormat.format("[Message Event Aspect] after reward referrer success({0}) pointcut start", String.valueOf(loanModel.getId())));
        try {
            userMessageEventGenerator.generateRecommendAwardSuccessEvent(loanModel.getId());
            logger.info(MessageFormat.format("[Message Event Aspect] after reward referrer success({0}) pointcut finished", String.valueOf(loanModel.getId())));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after reward referrer success({0}) pointcut is fail", String.valueOf(loanModel.getId())), e);
        }
    }

    @AfterReturning(value = "loginSuccessPointcut()")
    public void afterReturningUserLogin(JoinPoint joinPoint) {
        String loginName = LoginUserInfo.getLoginName();
        logger.info(MessageFormat.format("[Message Event Aspect] after login success({0}) pointcut start", loginName));
        try {
            userMessageEventGenerator.generateCouponExpiredAlertEvent(loginName);
            logger.info(MessageFormat.format("[Message Event Aspect] after login success({0}) pointcut finished", loginName));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after login success({0}) pointcut is fail", loginName), e);
        }
    }

    @AfterReturning(value = "assignCouponPointcut()", returning = "returnValue")
    public void afterReturningAssignCoupon(JoinPoint joinPoint, UserCouponModel returnValue) {
        logger.info(MessageFormat.format("[Message Event Aspect] after user({0}) assign user coupon({1}) pointcut start", returnValue.getLoginName(), String.valueOf(returnValue.getId())));
        try {
            userMessageEventGenerator.generateAssignCouponSuccessEvent(returnValue);
            logger.info(MessageFormat.format("[Message Event Aspect] after user({0}) assign user coupon({1}) pointcut finished", returnValue.getLoginName(), String.valueOf(returnValue.getId())));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after user({0}) assign user coupon({1}) pointcut is fail", returnValue.getLoginName(), String.valueOf(returnValue.getId())));
        }
    }
}
