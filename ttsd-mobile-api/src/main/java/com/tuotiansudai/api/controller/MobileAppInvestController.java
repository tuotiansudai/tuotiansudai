package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MobileAppInvestController extends MobileAppBaseController {
    @Autowired
    private MobileAppInvestService mobileAppInvestService;

    @RequestMapping(value = "/create/invest", method = RequestMethod.POST)
    public BaseResponseDto invest(@RequestBody InvestRequestDto investRequestDto) {
        investRequestDto.setUserId(getLoginName());
        investRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppInvestService.invest(investRequestDto);
    }

    @RequestMapping(value = "/no-password-invest", method = RequestMethod.POST)
    public BaseResponseDto noPasswordInvest(@RequestBody InvestRequestDto investRequestDto) {
        investRequestDto.setUserId(getLoginName());
        investRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppInvestService.noPasswordInvest(investRequestDto);
    }

}
