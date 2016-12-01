package com.tuotiansudai.paywrapper.aspect;


import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.IPhone7LotteryConfigMapper;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.TransferStatus;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class ChristmasAspect {
    private static Logger logger = Logger.getLogger(ChristmasAspect.class);
    
    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.startTime}\")}")
    private Date activityChristmasStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date activityChristmasEndTime;

    final private static String LOAN_ACTIVITY_DESCRIPTION = "圣诞专享";

    final static private long INTEREST_COUPON_OF_ZERO_5_PERCENT_COUPON_ID = 324L;

    final static private long INVEST_LIMIT = 3000000L;

    @Autowired
    CouponAssignmentService couponAssignmentService;

    @AfterReturning(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        logger.debug("after returning invest,christmas aspect starting...");
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];
        Date nowDate = DateTime.now().toDate();

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
        if (loanDetailsModel == null) {
            logger.error("christmas activity loanDetails is null");
            return;
        }

        if (investModel.getTransferStatus() != TransferStatus.SUCCESS
                && (activityChristmasStartTime.before(nowDate) && activityChristmasEndTime.after(nowDate))
                && loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals(LOAN_ACTIVITY_DESCRIPTION)
                && investModel.getAmount() >= INVEST_LIMIT) {
            couponAssignmentService.assignUserCoupon(investModel.getLoginName(), INTEREST_COUPON_OF_ZERO_5_PERCENT_COUPON_ID);
        }

        logger.debug("after returning invest, iphone7 aspect completed");
    }

}
