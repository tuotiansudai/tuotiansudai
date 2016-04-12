package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.WithdrawListRequestDto;
import com.tuotiansudai.api.dto.WithdrawOperateRequestDto;
import com.tuotiansudai.api.service.MobileAppWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppWithdrawController extends MobileAppBaseController {
    @Autowired
    private MobileAppWithdrawService mobileAppWithDrawService;

    @RequestMapping(value = "/get/userwithdrawlogs", method = RequestMethod.POST)
    public BaseResponseDto queryUserWithdrawLogs(@RequestBody WithdrawListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppWithDrawService.queryUserWithdrawLogs(requestDto);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public BaseResponseDto generateWithdrawRequest(@RequestBody WithdrawOperateRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppWithDrawService.generateWithdrawRequest(requestDto);
    }

}
