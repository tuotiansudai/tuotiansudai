package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppWithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "提现-提现记录")
public class MobileAppWithdrawController extends MobileAppBaseController {
    @Autowired
    private MobileAppWithdrawService mobileAppWithDrawService;

    @RequestMapping(value = "/get/userwithdrawlogs", method = RequestMethod.POST)
    @ApiOperation("提现-提现记录")
    public BaseResponseDto<WithdrawListResponseDataDto> queryUserWithdrawLogs(@RequestBody WithdrawListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppWithDrawService.queryUserWithdrawLogs(requestDto);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    @ApiOperation("提现")
    public BaseResponseDto<WithdrawOperateResponseDataDto> generateWithdrawRequest(@RequestBody WithdrawOperateRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppWithDrawService.generateWithdrawRequest(requestDto);
    }

}
