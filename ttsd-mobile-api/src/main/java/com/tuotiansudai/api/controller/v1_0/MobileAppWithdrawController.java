package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppWithdrawService;
import com.tuotiansudai.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppWithdrawController extends MobileAppBaseController {
    @Autowired
    private MobileAppWithdrawService mobileAppWithDrawService;

    @Autowired
    private BlacklistService blacklistService;

    @RequestMapping(value = "/get/userwithdrawlogs", method = RequestMethod.POST)
    public BaseResponseDto queryUserWithdrawLogs(@RequestBody WithdrawListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppWithDrawService.queryUserWithdrawLogs(requestDto);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public BaseResponseDto generateWithdrawRequest(@RequestBody WithdrawOperateRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        String loginName = getLoginName();
        if (blacklistService.userIsInBlacklist(loginName)) {
            BaseResponseDto<BaseResponseDataDto> baseResponseDto = new BaseResponseDto<>();
            baseResponseDto.setCode(ReturnMessage.WITHDRAW_IN_BLACKLIST.getCode());
            baseResponseDto.setMessage(ReturnMessage.WITHDRAW_IN_BLACKLIST.getMsg());
            return baseResponseDto;
        }
        return mobileAppWithDrawService.generateWithdrawRequest(requestDto);
    }

}
