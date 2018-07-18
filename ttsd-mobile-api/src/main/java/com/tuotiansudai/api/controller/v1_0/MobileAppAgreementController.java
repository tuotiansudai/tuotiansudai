package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppAgreementService;
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

@RestController
@Api(description = "开通自动投标")
public class MobileAppAgreementController extends MobileAppBaseController {

    private final MobileAppAgreementService mobileAppAgreementService;

    @Autowired
    public MobileAppAgreementController(MobileAppAgreementService mobileAppAgreementService) {
        this.mobileAppAgreementService = mobileAppAgreementService;
    }

    @RequestMapping(value = "/agreement", method = RequestMethod.POST)
    @ApiOperation("开通自动投标签约")
    public BaseResponseDto<BankAsynResponseDto> generateAgreementRequest(@RequestBody BaseParamDto baseParamDto, HttpServletRequest httpServletRequest) {
        return mobileAppAgreementService.generateAgreementRequest(LoginUserInfo.getLoginName(), LoginUserInfo.getMobile(), RequestIPParser.parse(httpServletRequest), baseParamDto);
    }

}
