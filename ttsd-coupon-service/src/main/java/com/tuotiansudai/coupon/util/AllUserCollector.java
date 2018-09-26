package com.tuotiansudai.coupon.util;

import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.springframework.stereotype.Service;

@Service
public class AllUserCollector implements UserCollector {

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        return couponModel != null && userModel != null && (couponModel.getCouponType() != CouponType.BIRTHDAY_COUPON || UserBirthdayUtil.isBirthMonth(userModel.getIdentityNumber()));
    }
}
