package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestRepayListRequestDto;
import com.ttsd.api.service.MobileAppInvestRepayListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppInvestRepayListController {

    @Resource
    private MobileAppInvestRepayListService mobileAppInvestRepayListService;

    @RequestMapping(value = "/get/investrepays", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryUserInvestList(@RequestBody InvestRepayListRequestDto requestDto) {
        BaseResponseDto dto = mobileAppInvestRepayListService.generateUserInvestRepayList(requestDto);
        return dto;
    }
}
