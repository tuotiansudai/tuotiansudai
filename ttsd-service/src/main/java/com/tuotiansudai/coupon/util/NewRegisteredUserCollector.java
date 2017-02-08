package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
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
        return Lists.transform(usersByRegisterTimeOrReferrer, UserModel::getLoginName);
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        return couponModel != null && userModel != null && userModel.getRegisterTime().before(couponModel.getEndTime()) && userModel.getRegisterTime().after(couponModel.getStartTime());
    }
}
