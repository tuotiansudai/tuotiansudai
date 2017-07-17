package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CurrentInvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.InvestNoPassResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.InvestResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentInvestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "活期转入")
public class MobileAppCurrentInvestController extends MobileAppBaseController {

    @Autowired
    private MobileAppCurrentInvestService mobileAppCurrentInvestService;

    @RequestMapping(value = "/rxb/invest", method = RequestMethod.POST)
    @ApiOperation("验密转入")
    public BaseResponseDto<InvestResponseDataDto> invest(@RequestBody CurrentInvestRequestDto investRequestDto) {
        String loginName = getLoginName();
        investRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppCurrentInvestService.invest(investRequestDto, loginName);
    }

    @RequestMapping(value = "/rxb/no-password-invest", method = RequestMethod.POST)
    @ApiOperation("免密转入")
    public BaseResponseDto<InvestNoPassResponseDataDto> noPasswordInvest(@RequestBody CurrentInvestRequestDto investRequestDto) {
        String loginName = getLoginName();
        investRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppCurrentInvestService.noPasswordInvest(investRequestDto, loginName);
    }
}
