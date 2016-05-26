package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserCouponRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppUserCouponController extends MobileAppBaseController {

    @Autowired
    private MobileAppUserCouponService mobileAppUserCouponService;

    @RequestMapping(value = "/get/userCoupons", method = RequestMethod.POST)
    public BaseResponseDto getCoupons(@RequestBody UserCouponRequestDto dto) {
        dto.getBaseParam().setUserId(getLoginName());
        return mobileAppUserCouponService.getUserCoupons(dto);
    }
}
