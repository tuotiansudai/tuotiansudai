package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestStatus;
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
    private InvestMapper investMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

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

}
