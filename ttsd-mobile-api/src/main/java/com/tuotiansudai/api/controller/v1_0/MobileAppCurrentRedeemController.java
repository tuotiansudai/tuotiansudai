package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
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
@Api(description = "活期提现申请")
public class MobileAppCurrentRedeemController extends MobileAppBaseController {

    @Autowired
    private MobileAppCurrentRedeemService mobileAppCurrentRedeemService;

    @RequestMapping(value = "/get/rxb/redeem", method = RequestMethod.POST)
    @ApiOperation("提现申请")
    public BaseResponseDto<CurrentRedeemResponseDataDto> withdraw(@RequestBody CurrentRedeemRequestDto redeemRequestDto) {
        String loginName = getLoginName();
        redeemRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppCurrentRedeemService.redeem(redeemRequestDto, loginName);
    }

}
