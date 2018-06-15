package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CertificationRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(description = "实名认证")
public class MobileAppCertificationController extends MobileAppBaseController {

    private final MobileAppCertificationService mobileAppCertificationService;

    @Autowired
    public MobileAppCertificationController(MobileAppCertificationService mobileAppCertificationService) {
        this.mobileAppCertificationService = mobileAppCertificationService;
    }

    @RequestMapping(value = "/certificate", method = RequestMethod.POST)
    @ApiOperation("实名认证")
    public BaseResponseDto<BankAsynResponseDto> certification(@Valid @RequestBody CertificationRequestDto certificationRequestDto,
                                                              BindingResult bindingResult,
                                                              HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            return new BaseResponseDto<>(ReturnMessage.REQUEST_PARAM_IS_WRONG);
        }

        certificationRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppCertificationService.certificate(LoginUserInfo.getLoginName(),
                LoginUserInfo.getMobile(),
                certificationRequestDto.getUserRealName(),
                certificationRequestDto.getUserIdCardNumber(),
                certificationRequestDto.getBaseParam().getToken(),
                RequestIPParser.parse(httpServletRequest),
                certificationRequestDto.getBaseParam().getDeviceId());
    }
}
