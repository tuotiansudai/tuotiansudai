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
import java.util.List;

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

    @Override
    @Transactional
    public void invest(long investId, List<Long> userCouponIds) {
        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        for (Long userCouponId : userCouponIds) {
            UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
            if (userCouponModel != null) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                if (InvestStatus.SUCCESS == userCouponModel.getStatus()
                        || new DateTime(couponModel.getEndTime()).plusDays(1).withTimeAtStartOfDay().isBeforeNow()
                        || !couponModel.getProductTypes().contains(loanModel.getProductType())
                        || (couponModel.getInvestLowerLimit() > 0 && investModel.getAmount() < couponModel.getInvestLowerLimit())
                        || (couponModel.getInvestUpperLimit() > 0 && investModel.getAmount() > couponModel.getInvestUpperLimit())) {
                    logger.debug(MessageFormat.format("user coupon ({0}) is unusable!", String.valueOf(userCouponId)));
                    return;
                }
            }
        }

        for (Long userCouponId : userCouponIds) {
            UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
            userCouponModel.setStatus(InvestStatus.WAIT_PAY);
            userCouponModel.setInvestId(investId);
            userCouponModel.setLoanId(loanModel.getId());
            userCouponMapper.update(userCouponModel);
        }
    }

    @Override
    @Transactional
    public void cancelUserCoupon(long loanId) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findSuccessfulUserCouponByLoanId(loanId);
        for(UserCouponModel userCouponModel : userCouponModels){
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

    @Override
    @Transactional
    public void investCallback(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(investId);

        for (UserCouponModel model : userCouponModels) {
            CouponModel couponModel = couponMapper.lockById(model.getCouponId());
            couponModel.setUsedCount(couponModel.getUsedCount() + 1);
            couponMapper.updateCoupon(couponModel);

            model.setLoanId(loanModel.getId());
            model.setInvestId(investId);
            model.setUsedTime(new Date());
            model.setStatus(InvestStatus.SUCCESS);
            long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(loanModel, couponModel, investModel.getAmount());
            long expectedFee = new BigDecimal(expectedInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
            model.setExpectedInterest(expectedInterest);
            model.setExpectedFee(expectedFee);
            userCouponMapper.update(model);
        }
    }
}
