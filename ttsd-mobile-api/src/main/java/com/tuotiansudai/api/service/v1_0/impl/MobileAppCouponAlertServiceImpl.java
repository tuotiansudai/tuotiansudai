package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CouponAlertResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppCouponAlertService;
import com.tuotiansudai.dto.CouponAlertDto;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.enums.CouponType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppCouponAlertServiceImpl implements MobileAppCouponAlertService {

    @Autowired
    private CouponAlertService couponAlertService;

    @Override
    public BaseResponseDto<CouponAlertResponseDataDto> getCouponAlert(BaseParamDto baseParamDto) {
        BaseResponseDto<CouponAlertResponseDataDto> baseDto = new BaseResponseDto();
        String loginName = baseParamDto.getBaseParam().getUserId();
        CouponAlertDto couponAlertDto = couponAlertService.getCouponAlert(loginName, Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE));
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        if(couponAlertDto != null){
            CouponAlertResponseDataDto responseDataDto = new CouponAlertResponseDataDto(couponAlertDto);
            baseDto.setData(responseDataDto);
        }

        return baseDto;
    }
}
