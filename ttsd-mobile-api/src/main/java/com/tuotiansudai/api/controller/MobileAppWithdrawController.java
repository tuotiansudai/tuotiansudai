package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.WithdrawListRequestDto;
import com.tuotiansudai.api.dto.WithdrawOperateRequestDto;
import com.tuotiansudai.api.service.MobileAppWithdrawService;
import com.tuotiansudai.repository.mapper.BlacklistMapper;
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
    private BlacklistMapper blacklistMapper;

    @RequestMapping(value = "/get/userwithdrawlogs", method = RequestMethod.POST)
    public BaseResponseDto queryUserWithdrawLogs(@RequestBody WithdrawListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppWithDrawService.queryUserWithdrawLogs(requestDto);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public BaseResponseDto generateWithdrawRequest(@RequestBody WithdrawOperateRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        String loginName = getLoginName();
        if (blacklistMapper.userIsInBlacklist(loginName)) {
            BaseResponseDto<BaseResponseDataDto> baseResponseDto = new BaseResponseDto();
            baseResponseDto.setCode(BaseResponseDto.ACCESS_FAIL_CODE);
            baseResponseDto.setMessage("操作失败(错误代码:YM001), 请联系客服");
            return baseResponseDto;
        }
        return mobileAppWithDrawService.generateWithdrawRequest(requestDto);
    }

}
