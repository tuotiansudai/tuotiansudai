package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private UserBirthdayUtil userBirthdayUtil;

    @Override
    @Transactional
    public void invest(long investId, List<Long> userCouponIds) {
        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        List<UserCouponModel> notSharedCoupons = Lists.newArrayList();
        for (Long userCouponId : userCouponIds) {
            UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
            if (userCouponModel != null) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                if (InvestStatus.SUCCESS == userCouponModel.getStatus()
                        || (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON && !userBirthdayUtil.isBirthMonth(investModel.getLoginName()))
                        || userCouponModel.getEndTime().before(new Date())
                        || !couponModel.getProductTypes().contains(loanModel.getProductType())
                        || (couponModel.getInvestLowerLimit() > 0 && investModel.getAmount() < couponModel.getInvestLowerLimit())
                        || (couponModel.getInvestUpperLimit() > 0 && investModel.getAmount() > couponModel.getInvestUpperLimit())) {
                    logger.error(MessageFormat.format("user coupon ({0}) is unusable!", String.valueOf(userCouponId)));
                    return;
                }
                if (!couponModel.isShared()) {
                    notSharedCoupons.add(userCouponModel);
                }
            }
        }

        if (notSharedCoupons.size() > 1) {
            String notSharedUserCouponIds = Joiner.on(",").join(Lists.transform(notSharedCoupons, new Function<UserCouponModel, String>() {
                @Override
                public String apply(UserCouponModel input) {
                    return String.valueOf(input.getId());
                }
            }));
            logger.error(MessageFormat.format("user({0}) used more then one not shared coupons({1}) when invested({2})!", investModel.getLoginName(), notSharedUserCouponIds, String.valueOf(investId)));
            return;
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
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanId, null);
        for (UserCouponModel userCouponModel : userCouponModels) {
            if (InvestStatus.SUCCESS.equals(userCouponModel.getStatus())) {
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
            long expectedFee = InterestCalculator.estimateCouponExpectedFee(loanModel, couponModel, investModel.getAmount());
            model.setExpectedInterest(expectedInterest);
            model.setExpectedFee(expectedFee);
            userCouponMapper.update(model);
        }
    }
}
