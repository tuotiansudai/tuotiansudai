package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WinnerNotifyCollector implements UserCollector {

    @Override
    public List<String> collect(long couponId) {
        return Lists.newArrayList();
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        return true;
    }
}
