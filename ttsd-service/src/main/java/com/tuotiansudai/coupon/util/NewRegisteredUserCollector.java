package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewRegisteredUserCollector implements UserCollector {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        CouponModel couponModel = couponMapper.findById(couponId);
        List<UserModel> usersByRegisterTimeOrReferrer = userMapper.findUsersByRegisterTimeOrReferrer(couponModel.getStartTime(), couponModel.getEndTime(), null);
        return Lists.transform(usersByRegisterTimeOrReferrer, userModel -> userModel.getLoginName());
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        CouponModel couponModel = couponMapper.findById(couponId);
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel != null && userModel.getRegisterTime().before(couponModel.getEndTime()) && userModel.getRegisterTime().after(couponModel.getStartTime());
    }
}
