package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppNewBieCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppNewBieCouponController extends MobileAppBaseController {
    @Autowired
    private MobileAppNewBieCouponService mobileAppNewBieCouponService;

    @RequestMapping(value = "/get/newbiecoupon", method = RequestMethod.POST)
    public BaseResponseDto getNewBieCoupon(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNewBieCouponService.getNewBieCoupon(baseParamDto);
    }

}
