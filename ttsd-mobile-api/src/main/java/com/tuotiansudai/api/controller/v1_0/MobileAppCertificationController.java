package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CertificationRequestDto;
import com.tuotiansudai.api.dto.v1_0.CertificationResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.CommonCertificationRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuDataDto;
import com.tuotiansudai.service.BankAccountService;
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

import static com.tuotiansudai.api.dto.v1_0.ReturnMessage.IDENTITY_NUMBER_INVALID;

@RestController
@Api(description = "实名认证")
public class MobileAppCertificationController extends MobileAppBaseController {

    @Autowired
    private MobileAppCertificationService mobileAppCertificationService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @Autowired
    private BankAccountService bankAccountService;

    @RequestMapping(value = "/certificate", method = RequestMethod.POST)
    @ApiOperation("实名认证")
    public BaseResponseDto<CertificationResponseDataDto> userMobileCertification(@Valid @RequestBody CertificationRequestDto certificationRequestDto, BindingResult bindingResult) {
        return new BaseResponseDto(IDENTITY_NUMBER_INVALID.getCode(), IDENTITY_NUMBER_INVALID.getMsg());
    }

    @RequestMapping(value = "/common-certificate", method = RequestMethod.POST)
    @ApiOperation("公用实名认证")
    public BaseDto<HuiZuDataDto> commonCertification(@Valid @RequestBody CommonCertificationRequestDto commonCertificationRequestDto, BindingResult bindingResult) {
        return new BaseDto<>(true, null);
    }


}
