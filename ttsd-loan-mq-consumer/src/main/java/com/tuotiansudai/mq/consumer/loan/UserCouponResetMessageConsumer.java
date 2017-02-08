package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class UserCouponResetMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(UserCouponResetMessageConsumer.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.UserCouponReset;
    }

    @Override
    @Transactional
    public void consume(String message) {
        logger.info("[UserCouponReset] receive message {}.", message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[UserCouponReset] message is empty.");
            return;
        }

        UserCouponModel userCouponModel = userCouponMapper.findById(Long.parseLong(message));
        if (userCouponModel == null || userCouponModel.getStatus() != InvestStatus.SUCCESS) {
            logger.warn("[UserCouponReset] user coupon({}) is not found or status is not SUCCESS.", message);
            return;
        }

        LoanModel loanModel = loanMapper.findById(userCouponModel.getLoanId());

        if (loanModel == null || loanModel.getStatus() != LoanStatus.CANCEL) {
            logger.warn("[UserCouponReset] receive message {}, loan is not success or loan status is not cancel", message);
            return;
        }

        logger.info("[UserCouponReset] receive message {}, reset loanId {} investId {} usedTime {}",
                message,
                userCouponModel.getLoanId(),
                userCouponModel.getInvestId(),
                userCouponModel.getUsedTime());

        CouponModel couponModel = couponMapper.lockById(userCouponModel.getCouponId());
        couponModel.setUsedCount(couponModel.getUsedCount() - 1);
        couponMapper.updateCoupon(couponModel);

        userCouponModel.setLoanId(null);
        userCouponModel.setInvestId(null);
        userCouponModel.setUsedTime(null);
        userCouponModel.setStatus(null);
        userCouponModel.setExpectedInterest(0L);
        userCouponModel.setExpectedFee(0L);
        userCouponMapper.update(userCouponModel);
    }
}
