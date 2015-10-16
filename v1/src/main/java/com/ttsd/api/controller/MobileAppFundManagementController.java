package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.FundManagementRequestDto;
import com.ttsd.api.service.MobileAppFundManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MobileAppFundManagementController {
    @Autowired
    private MobileAppFundManagementService mobileAppFundManagementService;

    @ResponseBody
    @RequestMapping(value = "/get/userfund",method = RequestMethod.POST)
    public BaseResponseDto queryFundManagement(@RequestBody FundManagementRequestDto fundManagementRequestDto){
        return mobileAppFundManagementService.queryFundByUserId(fundManagementRequestDto.getUserId());
    }

}
