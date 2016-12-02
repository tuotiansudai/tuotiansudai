package com.tuotiansudai.api.controller.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.UserFundResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppUserFundV2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "V2.0用户资金汇总")
public class MobileAppUserFundV2Controller extends MobileAppBaseController {

    @Autowired
    private MobileAppUserFundV2Service mobileAppUserFundV2Service;

    @RequestMapping(value = "/get/user-fund", method = RequestMethod.POST)
    @ApiOperation("用户资金汇总")
    public BaseResponseDto<UserFundResponseDataDto> getUserFund() {
        return mobileAppUserFundV2Service.getUserFund(getLoginName());
    }
}
