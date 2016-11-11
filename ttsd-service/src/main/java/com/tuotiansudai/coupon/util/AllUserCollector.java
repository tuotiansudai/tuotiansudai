package com.tuotiansudai.coupon.util;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllUserCollector implements UserCollector {

    @Autowired
    private UserBirthdayUtil userBirthdayUtil;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        return userMapper.findAllLoginNames();
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        CouponModel couponModel = couponMapper.findById(couponId);
        return couponModel.getCouponType() != CouponType.BIRTHDAY_COUPON || userBirthdayUtil.isBirthMonth(loginName);
    }
}
