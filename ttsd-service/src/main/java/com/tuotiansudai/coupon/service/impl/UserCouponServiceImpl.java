package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserCouponServiceImpl implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public List<UserCouponDto> getUserCouponDtoByLoginName(String loginName) {
        List<UserCouponModel> modelList = userCouponMapper.findByLoginName(loginName);
        List<UserCouponDto> dtoList = Lists.transform(modelList, new Function<UserCouponModel, UserCouponDto>() {
            @Override
            public UserCouponDto apply(UserCouponModel userCoupon) {
                CouponModel coupon = couponMapper.findCouponById(userCoupon.getCouponId());
                return new UserCouponDto(coupon, userCoupon);
            }
        });
        return dtoList;
    }

}
