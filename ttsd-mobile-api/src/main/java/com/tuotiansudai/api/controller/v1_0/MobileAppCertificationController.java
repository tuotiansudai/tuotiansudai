package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppCertificationController extends MobileAppBaseController {
    @Autowired
    private MobileAppCertificationService mobileAppCertificationService;

    @RequestMapping(value = "/certificate", method = RequestMethod.POST)
    public BaseResponseDto userMobileCertification(@Valid @RequestBody CertificationRequestDto certificationRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            certificationRequestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
        }
    }

}
