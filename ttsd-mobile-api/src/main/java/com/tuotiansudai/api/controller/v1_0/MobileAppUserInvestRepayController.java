package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestRepayRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestRepayResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestService;
import com.tuotiansudai.api.service.v1_0.MobileAppUserInvestRepayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(description = "出借回款")
public class MobileAppUserInvestRepayController extends MobileAppBaseController {
    @Autowired
    private MobileAppUserInvestRepayService mobileAppUserInvestRepayService;

    @RequestMapping(value = "/get/user-invest-repay", method = RequestMethod.POST)
    @ApiOperation("出借回款")
    public BaseResponseDto<UserInvestRepayResponseDataDto> userInvestRepay(@RequestBody UserInvestRepayRequestDto userInvestRepayRequestDto) {

        return mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);
    }
}
