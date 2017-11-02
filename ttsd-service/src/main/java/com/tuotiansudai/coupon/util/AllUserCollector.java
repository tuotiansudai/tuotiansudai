package com.tuotiansudai.coupon.util;

import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllUserCollector implements UserCollector {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        return userMapper.findAllLoginNames();
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        return couponModel != null && userModel != null && (couponModel.getCouponType() != CouponType.BIRTHDAY_COUPON || UserBirthdayUtil.isBirthMonth(userModel.getIdentityNumber()));
    }
}
