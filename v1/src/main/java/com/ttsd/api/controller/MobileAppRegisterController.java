package com.ttsd.api.controller;

import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppRegisterService;
import com.ttsd.util.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MobileAppRegisterController {

    @Resource
    private MobileAppRegisterService mobileAppRegisterService;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        return mobileAppRegisterService.registerUser(registerRequestDto);
    }

    @RequestMapping(value = "/register/sendsms", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto sendRegisterByMobileNumberSMS(@RequestBody SendSmsRequestDto sendSmsRequestDto,HttpServletRequest request) {
        String mobileNumber = sendSmsRequestDto.getPhoneNum();
        String remoteIp = CommonUtils.getRemoteHost(request);
        return mobileAppRegisterService.sendRegisterByMobileNumberSMS(mobileNumber,remoteIp);
    }

}
