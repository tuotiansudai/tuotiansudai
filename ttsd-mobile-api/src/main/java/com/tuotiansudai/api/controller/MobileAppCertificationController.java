package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppCertificationController {

    @Autowired
    private MobileAppCertificationService mobileAppCertificationService;

    @RequestMapping(value = "/certificate", method = RequestMethod.POST)
    public BaseResponseDto userMobileCertification(@RequestBody CertificationRequestDto certificationRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

}
