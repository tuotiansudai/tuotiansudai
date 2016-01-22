package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppCouponAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppCouponAlertController extends MobileAppBaseController {
    @Autowired
    private MobileAppCouponAlertService mobileAppCouponAlertService;

    @RequestMapping(value = "/get/coupon-alert", method = RequestMethod.POST)
    public BaseResponseDto getCouponAlert(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppCouponAlertService.getCouponAlert(baseParamDto);
    }

}
