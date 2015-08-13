package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestListRequestDto;
import com.ttsd.api.dto.UserInvestListRequestDto;
import com.ttsd.api.service.MobileAppInvestListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppInvestListController {
    @Resource
    private MobileAppInvestListService mobileAppInvestListService;

    @RequestMapping(value="/get/invests",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryInvestList(@RequestBody InvestListRequestDto investListRequestDto){
        return mobileAppInvestListService.generateInvestList(investListRequestDto);
    }

    @RequestMapping(value = "/get/userinvests", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryUserInvestList(@RequestBody UserInvestListRequestDto requestDto) {
        return mobileAppInvestListService.generateUserInvestList(requestDto);
    }
}
