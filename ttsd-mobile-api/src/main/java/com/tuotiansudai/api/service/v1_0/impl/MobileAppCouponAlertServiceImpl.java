package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CouponAlertResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppCouponAlertService;
import com.tuotiansudai.coupon.dto.CouponAlertDto;
import com.tuotiansudai.coupon.service.CouponAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppCouponAlertServiceImpl implements MobileAppCouponAlertService {

    @Autowired
    private CouponAlertService couponAlertService;

    @Override
    public BaseResponseDto getCouponAlert(BaseParamDto baseParamDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        String loginName = baseParamDto.getBaseParam().getUserId();
        CouponAlertDto couponAlertDto = couponAlertService.getCouponAlert(loginName);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        if(couponAlertDto != null){
            CouponAlertResponseDataDto responseDataDto = new CouponAlertResponseDataDto(couponAlertDto);
            baseDto.setData(responseDataDto);
        }

        return baseDto;
    }
}
