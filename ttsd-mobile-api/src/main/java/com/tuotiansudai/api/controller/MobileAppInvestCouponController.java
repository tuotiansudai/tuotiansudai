package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MobileAppInvestCouponController extends MobileAppBaseController {

    @Autowired
    private MobileAppInvestCouponService mobileAppInvestCouponService;

    @RequestMapping(value = "/get/investCoupons", method = RequestMethod.POST)
    public BaseResponseDto getCoupons( @RequestBody InvestRequestDto dto) {
        dto.getBaseParam().setUserId(getLoginName());
        return mobileAppInvestCouponService.getInvestCoupons(dto);
    }
}
