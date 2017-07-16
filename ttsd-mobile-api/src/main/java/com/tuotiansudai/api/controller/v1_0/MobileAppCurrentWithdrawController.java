package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CurrentWithdrawRequestDto;
import com.tuotiansudai.api.dto.v1_0.CurrentWithdrawResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentWithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "活期提现申请")
public class MobileAppCurrentWithdrawController extends MobileAppBaseController {

    @Autowired
    private MobileAppCurrentWithdrawService mobileAppCurrentWithdrawService;

    @RequestMapping(value = "/get/rxb/redeem", method = RequestMethod.POST)
    @ApiOperation("提现申请")
    public BaseResponseDto<CurrentWithdrawResponseDataDto> withdraw(@RequestBody CurrentWithdrawRequestDto withdrawRequestDto) {
        String loginName = getLoginName();
        withdrawRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppCurrentWithdrawService.withdraw(withdrawRequestDto, loginName);
    }

}
