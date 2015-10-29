package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestDetailRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppInvestDetailController {

    @Autowired
    private MobileAppInvestDetailService mobileAppMyInvestDetailService;

    @RequestMapping(value = "/get/userinvest", method = RequestMethod.POST)
    public BaseResponseDto queryUserInvestList(@RequestBody InvestDetailRequestDto requestDto) {
        return mobileAppMyInvestDetailService.generateUserInvestDetail(requestDto);
    }
}
