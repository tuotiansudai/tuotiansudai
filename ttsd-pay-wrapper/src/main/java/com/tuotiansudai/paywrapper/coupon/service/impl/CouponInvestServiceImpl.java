package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void invest(long investId, List<Long> userCouponIds) {
        if (CollectionUtils.isEmpty(userCouponIds)) {
            return;
        }

        for (Long userCouponId : userCouponIds) {
            UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
            userCouponModel.setStatus(InvestStatus.WAIT_PAY);
            userCouponModel.setInvestId(investId);
            userCouponModel.setLoanId(investMapper.findById(investId).getLoanId());
            userCouponModel.setUsedTime(new Date());
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
            long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(investModel.getAmount(), loanModel, couponModel);
            long expectedFee = InterestCalculator.estimateCouponExpectedFee(loanModel, couponModel, investModel.getAmount(), investModel.getInvestFeeRate());
            model.setExpectedInterest(expectedInterest);
            model.setExpectedFee(expectedFee);
            userCouponMapper.update(model);
        }

        couponAssignmentService.assignUserCoupon(investModel.getLoginName(), Lists.newArrayList(UserGroup.ALL_USER,
                UserGroup.INVESTED_USER,
                UserGroup.IMPORT_USER,
                UserGroup.AGENT,
                UserGroup.CHANNEL,
                UserGroup.STAFF,
                UserGroup.STAFF_RECOMMEND_LEVEL_ONE));
    }
}
