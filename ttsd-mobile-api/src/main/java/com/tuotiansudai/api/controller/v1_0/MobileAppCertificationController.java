package com.tuotiansudai.api.controller.v1_0;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuDataDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import com.tuotiansudai.util.IdentityNumberValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.tuotiansudai.api.dto.v1_0.ReturnMessage.IDENTITY_NUMBER_INVALID;

@RestController
@Api(description = "实名认证")
public class MobileAppCertificationController extends MobileAppBaseController {

    @Autowired
    private MobileAppCertificationService mobileAppCertificationService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/certificate", method = RequestMethod.POST)
    @ApiOperation("实名认证")
    public BaseResponseDto<CertificationResponseDataDto> userMobileCertification(@Valid @RequestBody CertificationRequestDto certificationRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            if (IdentityNumberValidator.validateIdentity(certificationRequestDto.getUserIdCardNumber())) {

                certificationRequestDto.getBaseParam().setUserId(getLoginName());
                RegisterAccountDto registerAccountDto = new RegisterAccountDto(getLoginName(),
                        certificationRequestDto.getBaseParam().getPhoneNum(),
                        certificationRequestDto.getUserRealName(),
                        certificationRequestDto.getUserIdCardNumber());
                BaseDto<HuiZuDataDto> baseDto = accountService.registerAccountFromHuiZu(registerAccountDto);
                if (baseDto.getData().getStatus()) {
                    CertificationResponseDataDto certificationResponseDataDto = new CertificationResponseDataDto();
                    certificationResponseDataDto.setUserIdCardNumber(certificationRequestDto.getUserIdCardNumber());
                    certificationResponseDataDto.setUserRealName(certificationRequestDto.getUserRealName());
                    BaseResponseDto<CertificationResponseDataDto> baseResponseDto = new BaseResponseDto<>();
                    baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
                    baseResponseDto.setMessage(baseDto.getData().getMessage());
                    baseResponseDto.setData(certificationResponseDataDto);

                    myAuthenticationUtil.createAuthentication(getLoginName(), Source.valueOf(certificationRequestDto.getBaseParam().getPlatform().toUpperCase()));
                    return baseResponseDto;
                }


                return new BaseResponseDto<>(baseDto.getData().getCode(), baseDto.getData().getMessage());
            }
            return new BaseResponseDto(IDENTITY_NUMBER_INVALID.getCode(), IDENTITY_NUMBER_INVALID.getMsg());
        }
    }

    @RequestMapping(value = "/common-certificate", method = RequestMethod.POST)
    @ApiOperation("公用实名认证")
    public BaseDto<HuiZuDataDto> commonCertification(@Valid @RequestBody CommonCertificationRequestDto commonCertificationRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseDto(true, new HuiZuDataDto(false, errorMessage, errorCode));
        } else {
            CertificationRequestDto certificationRequestDto = new CertificationRequestDto(commonCertificationRequestDto);
            BaseResponseDto<CertificationResponseDataDto> baseResponseDto = mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
            HuiZuDataDto huiZuDataDto = new HuiZuDataDto(baseResponseDto.isSuccess(), baseResponseDto.getMessage(), baseResponseDto.getCode());
            huiZuDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("userName", baseResponseDto.getData().getUserRealName())
                    .put("identityNumber", baseResponseDto.getData().getUserIdCardNumber())
                    .build()));

            return new BaseDto<>(true, huiZuDataDto);
        }
    }


}
