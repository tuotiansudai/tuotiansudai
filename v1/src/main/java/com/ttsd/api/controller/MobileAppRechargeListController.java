package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.LoanListRequestDto;
import com.ttsd.api.dto.RechargeListRequestDto;
import com.ttsd.api.service.MobileAppLoanListService;
import com.ttsd.api.service.MobileAppRechargeListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppRechargeListController {
    @Resource
    private MobileAppRechargeListService rechargeListService;

    @RequestMapping(value="/get/userrecharges",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryList(@RequestBody RechargeListRequestDto requestDto){
        return rechargeListService.generateRechargeList(requestDto);
    }
}
