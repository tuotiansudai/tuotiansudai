package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestRequestDto;
import com.ttsd.api.service.MobileAppUmPayInvestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppUmPayInvestController {
    @Resource
    private MobileAppUmPayInvestService mobileAppUmPayInvestService;
    @ResponseBody
    @RequestMapping(value = "/create/invest", method = RequestMethod.POST)
    public BaseResponseDto invest(@RequestBody InvestRequestDto investRequestDto){
        return mobileAppUmPayInvestService.invest(investRequestDto);
    }

}
