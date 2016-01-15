package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanPeriodUnit;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

@Service
public class CouponInvestServiceImpl implements CouponInvestService {

    static Logger logger = Logger.getLogger(CouponInvestServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponService couponService;

    @Override
    @Transactional
    public void invest(long investId, Long userCouponId) {
        if (userCouponId == null || userCouponMapper.findById(userCouponId) == null) {
            return;
        }

        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
        CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());

        if (InvestStatus.SUCCESS != userCouponModel.getStatus()
                && new DateTime(couponModel.getEndTime()).plusDays(1).withTimeAtStartOfDay().isAfterNow()
                && couponModel.getProductTypes().contains(loanModel.getProductType())
                && investModel.getAmount() >= couponModel.getInvestLowerLimit()) {
            userCouponModel.setStatus(InvestStatus.WAIT_PAY);
            userCouponModel.setInvestId(investId);
            userCouponModel.setLoanId(loanModel.getId());
            userCouponMapper.update(userCouponModel);
        } else {
            logger.debug(MessageFormat.format("user coupon ({0}) is unusable!", String.valueOf(userCouponId)));
        }
    }

    @Override
    @Transactional
    public void investCallback(long investId) {
        UserCouponModel userCouponModel = userCouponMapper.findByInvestId(investId);
        if (userCouponModel == null) {
            return;
        }
        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        userCouponModel.setLoanId(loanModel.getId());
        userCouponModel.setInvestId(investId);
        userCouponModel.setUsedTime(new Date());
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        long expectedInterest = couponService.estimateCouponExpectedInterest(loanModel.getId(), userCouponModel.getCouponId(), investModel.getAmount());
        long expectedFee = new BigDecimal(expectedInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        userCouponModel.setExpectedInterest(expectedInterest);
        userCouponModel.setExpectedFee(expectedFee);
        userCouponMapper.update(userCouponModel);

        CouponModel couponModel = couponMapper.lockById(userCouponModel.getCouponId());
        couponModel.setUsedCount(couponModel.getUsedCount() + 1);
        couponMapper.updateCoupon(couponModel);
    }
}
