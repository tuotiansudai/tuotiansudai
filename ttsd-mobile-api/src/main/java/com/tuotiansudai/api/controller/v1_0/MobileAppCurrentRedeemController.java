package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CurrentRedeemLimitResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.CurrentRedeemRequestDto;
import com.tuotiansudai.api.dto.v1_0.CurrentRedeemResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentRedeemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "活期转出申请")
public class MobileAppCurrentRedeemController extends MobileAppBaseController {

    @Autowired
    private MobileAppCurrentRedeemService mobileAppCurrentRedeemService;

    @RequestMapping(value = "/get/rxb/redeem", method = RequestMethod.POST)
    @ApiOperation("转出申请")
    public BaseResponseDto<CurrentRedeemResponseDataDto> redeem(@RequestBody CurrentRedeemRequestDto redeemRequestDto) {
        String loginName = getLoginName();
        return mobileAppCurrentRedeemService.redeem(redeemRequestDto,loginName);
    }

    @RequestMapping(value = "/get/rxb/redeem/limits", method = RequestMethod.GET)
    @ApiOperation("当日可转出金额和最大转出金额")
    public BaseResponseDto<CurrentRedeemLimitResponseDataDto> limitRedeem() {
        String loginName = getLoginName();
        return mobileAppCurrentRedeemService.limitRedeem(loginName);
    }

}
