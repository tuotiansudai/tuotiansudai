package com.ttsd.api.controller;

import com.esoft.archer.user.model.ReferrerInvest;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.ReferrerInvestListRequestDto;
import com.ttsd.api.service.MobileAppReferrerInvestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppReferrerInvestListController {
    @Resource
    private MobileAppReferrerInvestService mobileAppReferrerInvestService;

    @RequestMapping(value="/get/referrerinvests",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryInvestList(@RequestBody ReferrerInvestListRequestDto referrerInvestListRequestDto){
        return mobileAppReferrerInvestService.generateReferrerInvestList(referrerInvestListRequestDto);
    }

}
