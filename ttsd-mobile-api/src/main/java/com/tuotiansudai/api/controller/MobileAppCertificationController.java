package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import com.tuotiansudai.api.service.impl.MobileAppRegisterServiceImpl;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

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
