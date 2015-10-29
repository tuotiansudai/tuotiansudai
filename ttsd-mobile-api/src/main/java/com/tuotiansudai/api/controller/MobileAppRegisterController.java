package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RegisterRequestDto;
import com.tuotiansudai.api.dto.SendSmsRequestDto;
import com.tuotiansudai.api.service.MobileAppRegisterService;
import com.tuotiansudai.api.util.CommonUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MobileAppRegisterController {

    @Autowired
    private MobileAppRegisterService mobileAppRegisterService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseResponseDto registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @RequestMapping(value = "/register/sendsms", method = RequestMethod.POST)
    public BaseResponseDto sendRegisterByMobileNumberSMS(@RequestBody SendSmsRequestDto sendSmsRequestDto, HttpServletRequest request) {
        String mobileNumber = sendSmsRequestDto.getPhoneNum();
        String remoteIp = CommonUtils.getRemoteHost(request);
        return mobileAppRegisterService.sendRegisterByMobileNumberSMS(mobileNumber, remoteIp);
    }
}
