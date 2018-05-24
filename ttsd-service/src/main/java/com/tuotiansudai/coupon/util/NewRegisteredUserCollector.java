package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class NewRegisteredUserCollector implements UserCollector {

    @Autowired
    private UserCouponMapper userCouponMapper;

    private static final long NEW_EXPERIENCE_COUPON_ID = 382L;

    private static final long OLD_EXPERIENCE_COUPON_ID = 100035L;

    private static final List<Long> NEW_RED_ENVELOPE_COUPONS = Lists.newArrayList(497L, 498L, 499L, 500L, 501L, 502L, 503L, 504L, 505L, 506L);

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        boolean isValidTime = couponModel != null && userModel != null && userModel.getRegisterTime().before(couponModel.getEndTime()) && userModel.getRegisterTime().after(couponModel.getStartTime());

        if (NEW_RED_ENVELOPE_COUPONS.contains(couponModel.getId())){


        }

        if (userModel == null || couponModel == null || couponModel.getId() != NEW_EXPERIENCE_COUPON_ID) {
            return isValidTime;
        }
        List<UserCouponModel> oldExperienceUserCouponModels = userCouponMapper.findByLoginNameAndCouponId(userModel.getLoginName(), OLD_EXPERIENCE_COUPON_ID);
        List<UserCouponModel> newExperienceUserCouponModels = userCouponMapper.findByLoginNameAndCouponId(userModel.getLoginName(), NEW_EXPERIENCE_COUPON_ID);
        return isValidTime && CollectionUtils.isEmpty(oldExperienceUserCouponModels) && CollectionUtils.isEmpty(newExperienceUserCouponModels);
    }

}
