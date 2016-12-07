package com.tuotiansudai.coupon.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.CouponInvestService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.InterestCalculator;
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
    private CouponAssignmentService couponAssignmentService;

    @Override
    @Transactional
    public void updateCouponAndAssign(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(investId);

        for (UserCouponModel userCoupon : userCouponModels) {
            if (userCoupon.getStatus() == InvestStatus.SUCCESS) {
                // 如果用户优惠券状态已经被更新为Success，则不再重复处理。
                logger.info(MessageFormat.format("coupon status is already SUCCESS, won't update again. investId:{0}, userCouponId:{1}",
                        String.valueOf(investId), String.valueOf(userCoupon.getId())));
                continue;
            }
            CouponModel couponModel = couponMapper.lockById(userCoupon.getCouponId());
            couponModel.setUsedCount(couponModel.getUsedCount() + 1);
            couponMapper.updateCoupon(couponModel);

            userCoupon.setLoanId(loanModel.getId());
            userCoupon.setInvestId(investId);
            userCoupon.setUsedTime(new Date());
            userCoupon.setStatus(InvestStatus.SUCCESS);
            long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(investModel.getAmount(), loanModel, couponModel);
            long expectedFee = InterestCalculator.estimateCouponExpectedFee(loanModel, couponModel, investModel.getAmount(), investModel.getInvestFeeRate());
            userCoupon.setExpectedInterest(expectedInterest);
            userCoupon.setExpectedFee(expectedFee);
            userCouponMapper.update(userCoupon);
        }

        couponAssignmentService.asyncAssignUserCoupon(investModel.getLoginName(), Lists.newArrayList(UserGroup.ALL_USER,
                UserGroup.INVESTED_USER,
                UserGroup.IMPORT_USER,
                UserGroup.AGENT,
                UserGroup.CHANNEL,
                UserGroup.STAFF,
                UserGroup.STAFF_RECOMMEND_LEVEL_ONE));
    }
}
