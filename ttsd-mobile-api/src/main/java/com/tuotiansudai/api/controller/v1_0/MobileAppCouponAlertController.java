package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CouponAlertResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCouponAlertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "获取新手体验劵或者红包")
public class MobileAppCouponAlertController extends MobileAppBaseController {
    @Autowired
    private MobileAppCouponAlertService mobileAppCouponAlertService;

    @RequestMapping(value = "/get/coupon-alert", method = RequestMethod.POST)
    @ApiOperation("获取新手体验劵或者红包")
    public BaseResponseDto<CouponAlertResponseDataDto> getCouponAlert(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppCouponAlertService.getCouponAlert(baseParamDto);
    }

}
