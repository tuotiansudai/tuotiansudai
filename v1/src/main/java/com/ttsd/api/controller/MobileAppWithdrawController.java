package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.WithdrawListRequestDto;
import com.ttsd.api.dto.WithdrawOperateRequestDto;
import com.ttsd.api.service.MobileAppWithdrawService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppWithdrawController {
    @Resource
    private MobileAppWithdrawService mobileAppWithDrawService;

    @RequestMapping(value = "/get/userwithdrawlogs", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryUserWithdrawLogs(@RequestBody WithdrawListRequestDto requestDto) {
        return mobileAppWithDrawService.queryUserWithdrawLogs(requestDto);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto generateWithdrawRequest(@RequestBody WithdrawOperateRequestDto requestDto) {
        return mobileAppWithDrawService.generateWithdrawRequest(requestDto);
    }

}
