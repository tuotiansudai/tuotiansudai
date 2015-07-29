package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseParamDto;
import com.ttsd.api.dto.CertificationRequestDto;
import com.ttsd.api.dto.CertificationResponseDto;
import com.ttsd.api.service.MobileAppCertificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by tuotian on 15/7/27.
 */
@Controller
@RequestMapping(value = "/mobileAppCertification")
public class MobileCertificationController {

    @Resource(name = "MobileAppCertificationServiceImpl")
    MobileAppCertificationService mobileAppCertificationService;

    @RequestMapping(value = "/certification",method = RequestMethod.POST)
    @ResponseBody
    public CertificationResponseDto userMobileCertification(@RequestBody CertificationRequestDto certificationRequestDto){
        return mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
    }

    public void setMobileAppCertificationService(MobileAppCertificationService mobileAppCertificationService) {
        this.mobileAppCertificationService = mobileAppCertificationService;
    }
}
