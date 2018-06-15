package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppBankCardService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(description = "解绑卡")
public class MobileAppBankCardController extends MobileAppBaseController {

    private MobileAppBankCardService mobileAppBankCardService;

    @Autowired
    public MobileAppBankCardController(MobileAppBankCardService mobileAppBankCardService){
        this.mobileAppBankCardService = mobileAppBankCardService;
    }

    @RequestMapping(path = "/bankcard/bind", method = RequestMethod.POST)
    @ApiOperation("绑卡")
    public BaseResponseDto<BankAsynResponseDto> bindBankCard(@Valid @RequestBody BaseParam baseParam, HttpServletRequest httpServletRequest) {
        return mobileAppBankCardService.bindBankCard(LoginUserInfo.getLoginName(), RequestIPParser.parse(httpServletRequest), baseParam.getDeviceId());
    }


    @RequestMapping(path = "/bankcard/unbind", method = RequestMethod.POST)
    @ApiOperation("解绑")
    public BaseResponseDto<BankAsynResponseDto> unbindBankCard(@Valid @RequestBody BaseParam baseParam, HttpServletRequest httpServletRequest) {
        return mobileAppBankCardService.unBindBankCard(LoginUserInfo.getLoginName(), RequestIPParser.parse(httpServletRequest), baseParam.getDeviceId());
    }
}
