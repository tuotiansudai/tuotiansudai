package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponType;
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

    @Override
    public List<String> collect(long couponId) {
        return Lists.newArrayList();
    }

    @Override
    public long count(long couponId) {
        return 0;
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        CouponModel couponModel = couponMapper.findById(couponId);
        return !(couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON && !userBirthdayUtil.isBirthMonth(loginName));
    }
}
