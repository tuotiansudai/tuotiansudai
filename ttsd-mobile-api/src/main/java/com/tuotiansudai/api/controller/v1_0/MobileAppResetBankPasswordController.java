package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.spring.LoginUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "重置银行交易密码")
public class MobileAppResetBankPasswordController extends MobileAppBaseController {

    private final MobileAppCertificationService mobileAppCertificationService;

    @Autowired
    public MobileAppResetBankPasswordController(MobileAppCertificationService mobileAppCertificationService) {
        this.mobileAppCertificationService = mobileAppCertificationService;
    }

    @RequestMapping(value = "/reset-bank-password", method = RequestMethod.POST)
    @ApiOperation("重置银行交易密码")
    public BaseResponseDto<BankAsynResponseDto> resetBankPassword() {
        return mobileAppCertificationService.resetBankPassword(LoginUserInfo.getLoginName());
    }
}
