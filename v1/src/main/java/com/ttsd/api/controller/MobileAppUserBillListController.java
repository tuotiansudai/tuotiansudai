package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.UserBillDetailListRequestDto;
import com.ttsd.api.service.MobileAppUserBillListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MobileAppUserBillListController {
    @Autowired
    private MobileAppUserBillListService mobileAppUserBillListService;

    @ResponseBody
    @RequestMapping(value = "/get/userbills",method = RequestMethod.POST)
    public BaseResponseDto queryFundManagement(@RequestBody UserBillDetailListRequestDto userBillDetailListRequestDto){
        return mobileAppUserBillListService.queryUserBillList(userBillDetailListRequestDto);
    }

}
