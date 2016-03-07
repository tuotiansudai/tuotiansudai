package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppUserCouponService;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppUserCouponServiceImpl implements MobileAppUserCouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserCouponService userCouponService;

    @Override
    public BaseResponseDto<UserCouponListResponseDataDto> getUserCoupons(UserCouponRequestDto requestDto) {
        List<UserCouponView> unUserCouponDtos = null;
        List<UserCouponView> useUserCouponDtos = null;
        List<UserCouponView> expiredUserDtos = null;

        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();

        if(requestDto.isUnused()){
            unUserCouponDtos = userCouponService.getUnusedUserCoupons(requestDto.getBaseParam().getUserId());
        }
        if(requestDto.isExpired()){
            userCouponDtos = userCouponService.getExpiredUserCoupons(requestDto.getBaseParam().getUserId());
        }

        if(requestDto.isUsed()){
            useRecords = userCouponService.findUseRecords(requestDto.getBaseParam().getUserId());
        }

        if(CollectionUtils.isNotEmpty(userCouponDtos)){
            coupons = Lists.transform(userCouponDtos, new Function<UserCouponDto, UserCouponResponseDataDto>() {
                @Override
                public UserCouponResponseDataDto apply(UserCouponDto userCouponDto) {
                    UserCouponModel userCouponModel = userCouponMapper.findById(userCouponDto.getId());
                    UserCouponResponseDataDto dataDto = new UserCouponResponseDataDto(couponMapper.findById(userCouponDto.getCouponId()), userCouponModel);
                    return dataDto;
                }
            });

        }

        if(CollectionUtils.isNotEmpty(useRecords)){
            coupons = Lists.transform(useRecords, new Function<CouponUseRecordView, UserCouponResponseDataDto>() {
                @Override
                public UserCouponResponseDataDto apply(CouponUseRecordView couponUseRecordView) {
                    return new UserCouponResponseDataDto(couponUseRecordView);
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
