package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestDetailRequestDto;
import com.ttsd.api.service.MobileAppInvestDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppInvestDetailController {

    @Resource
    private MobileAppInvestDetailService mobileAppMyInvestDetailService;

    @RequestMapping(value = "/get/userinvest", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryUserInvestList(@RequestBody InvestDetailRequestDto requestDto) {
        return mobileAppMyInvestDetailService.generateUserInvestDetail(requestDto);
    }
}
