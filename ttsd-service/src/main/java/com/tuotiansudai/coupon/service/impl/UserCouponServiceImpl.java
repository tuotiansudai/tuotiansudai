package com.tuotiansudai.coupon.service.impl;

import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.service.InvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCouponServiceImpl implements UserCouponService{

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private InvestService investService;

    @Override
    public long findExpectedTotalByCouponId(long couponId, long couponAmount) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(couponId);
        long returnAmount = 0;
        for (UserCouponModel userCouponModel : userCouponModels) {
            returnAmount += investService.estimateInvestIncome(userCouponModel.getLoanId(), couponAmount);
        }
        return returnAmount;
    }

}
