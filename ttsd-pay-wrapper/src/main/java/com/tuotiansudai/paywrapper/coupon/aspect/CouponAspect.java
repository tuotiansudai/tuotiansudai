package com.tuotiansudai.paywrapper.coupon.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.paywrapper.coupon.service.UserCouponService;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
@Aspect
public class CouponAspect {
    static Logger logger = Logger.getLogger(CouponAspect.class);

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private UserCouponService userCouponService;

    @Around(value = "execution(* com.tuotiansudai.paywrapper.service.RepayService.postRepayCallback(*))")
    public Object aroundRepay(ProceedingJoinPoint proceedingJoinPoint) {
        logger.debug("after repay pointcut");
        List<Object> args = Lists.newArrayList(proceedingJoinPoint.getArgs());
        long loanRepayId = (long) args.get(0);
        try {
            boolean isSuccess = (boolean) proceedingJoinPoint.proceed();
            if (isSuccess) {
                couponRepayService.repay(loanRepayId);
            }
            return isSuccess;
        } catch (Throwable throwable) {
            logger.error(MessageFormat.format("Coupon repay aspect is failed (loanRepayId = {0})", String.valueOf(loanRepayId)), throwable);
        }

        return false;
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.invest(*))", returning = "returnValue")
    public void afterReturningInvest(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after invest");
        BaseDto<PayFormDataDto> baseDto= (BaseDto)returnValue;
        if (baseDto.getData() != null && baseDto.getData().getStatus()) {
            InvestDto investDto = (InvestDto) joinPoint.getArgs()[0];
            long investId = Long.parseLong(baseDto.getData().getFields().get("order_id").toString());
            userCouponService.afterReturningInvest(investDto, investId);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.investSuccess(*))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        logger.debug("after invest success");
        InvestModel investModel = (InvestModel)joinPoint.getArgs()[1];
        UserCouponModel userCouponModel = userCouponMapper.findByInvestId(investModel.getId());
        logger.debug("after invest success invest id is " + investModel.getId());
        if (userCouponModel != null) {
            userCouponModel.setStatus(InvestStatus.SUCCESS);
            userCouponMapper.update(userCouponModel);
            userCouponService.recordUsedCount(userCouponModel.getCouponId());
        }
        logger.debug("can not find user coupon by invest id , invest id is " + investModel.getId());
    }

}

