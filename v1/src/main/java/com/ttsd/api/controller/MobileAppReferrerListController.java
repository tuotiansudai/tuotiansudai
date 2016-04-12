package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestListRequestDto;
import com.ttsd.api.dto.ReferrerListRequestDto;
import com.ttsd.api.service.MobileAppReferrerListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppReferrerListController {
    @Resource
    private MobileAppReferrerListService mobileAppReferrerListService;

    @RequestMapping(value="/get/referrers",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryInvestList(@RequestBody ReferrerListRequestDto referrerListRequestDto){
        return mobileAppReferrerListService.generateReferrerList(referrerListRequestDto);
    }

}
