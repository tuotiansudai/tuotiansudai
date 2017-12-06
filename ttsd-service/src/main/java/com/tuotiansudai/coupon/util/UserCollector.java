package com.tuotiansudai.coupon.util;

import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;

import java.util.List;

public interface UserCollector {

    boolean contains(CouponModel couponModel, UserModel userModel);
}
