package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(description = "实名认证")
public class MobileAppCertificationController extends MobileAppBaseController {

    @Autowired
    private MobileAppCertificationService mobileAppCertificationService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @RequestMapping(value = "/certificate", method = RequestMethod.POST)
    @ApiOperation("实名认证")
    public BaseResponseDto<CertificationResponseDataDto> userMobileCertification(@Valid @RequestBody CertificationRequestDto certificationRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            certificationRequestDto.getBaseParam().setUserId(getLoginName());
            BaseResponseDto<CertificationResponseDataDto> baseResponseDto = mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
            myAuthenticationUtil.createAuthentication(getLoginName(), Source.valueOf(certificationRequestDto.getBaseParam().getPlatform().toUpperCase()));
            return baseResponseDto;
        }
    }

    @RequestMapping(value = "/common-certificate", method = RequestMethod.POST)
    @ApiOperation("公用实名认证")
    public BaseResponseDto<CertificationResponseDataDto> commonCertification(@Valid @RequestBody CommonCertificationRequestDto commonCertificationRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            CertificationRequestDto certificationRequestDto = new CertificationRequestDto(commonCertificationRequestDto);
            BaseResponseDto<CertificationResponseDataDto> baseResponseDto = mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
            return baseResponseDto;
        }
    }


}
