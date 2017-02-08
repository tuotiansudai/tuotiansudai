package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserCouponService;
import com.tuotiansudai.repository.model.UserCouponView;
import com.tuotiansudai.coupon.service.UserCouponService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppUserCouponServiceImpl implements MobileAppUserCouponService {

    @Autowired
    private UserCouponService userCouponService;

    @Override
    public BaseResponseDto<UserCouponListResponseDataDto> getUserCoupons(UserCouponRequestDto requestDto) {
        List<UserCouponView> couponDtos = Lists.newArrayList();

        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();


        if(requestDto.isUnused()){
            couponDtos = userCouponService.getUnusedUserCoupons(requestDto.getBaseParam().getUserId());
        }
        if(requestDto.isExpired()){
            couponDtos = userCouponService.getExpiredUserCoupons(requestDto.getBaseParam().getUserId());
        }

        if(requestDto.isUsed()){
            couponDtos = userCouponService.findUseRecords(requestDto.getBaseParam().getUserId());
        }

        List<BaseCouponResponseDataDto> coupons = Lists.newArrayList();

        if(CollectionUtils.isNotEmpty(couponDtos)){
            coupons = Lists.transform(couponDtos, new Function<UserCouponView, BaseCouponResponseDataDto>() {
                @Override
                public BaseCouponResponseDataDto apply(UserCouponView userCouponView) {
                    return new UserCouponResponseDataDto(userCouponView);
                }
            });

        }

        if(CollectionUtils.isNotEmpty(coupons)){
            responseDto.setData(new UserCouponListResponseDataDto(coupons));
        }else{
            responseDto.setData(new UserCouponListResponseDataDto());
        }
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return responseDto;
    }
}
