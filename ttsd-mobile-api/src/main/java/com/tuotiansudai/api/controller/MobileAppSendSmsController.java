package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppSendSmsService;
import com.tuotiansudai.api.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class MobileAppSendSmsController extends MobileAppBaseController{

    @Autowired
    private MobileAppSendSmsService mobileAppSendSmsService;

    @RequestMapping(value = "/sms-captcha/send", method = RequestMethod.POST)
    public BaseResponseDto sendSms(@Valid @RequestBody SendSmsCompositeRequestDto sendSmsCompositeRequestDto,BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            sendSmsCompositeRequestDto.getBaseParam().setUserId(getLoginName());
            String remoteIp = CommonUtils.getRemoteHost(request);
            return mobileAppSendSmsService.sendSms(sendSmsCompositeRequestDto,remoteIp);
        }
    }
    @RequestMapping(value = "/validatecaptcha", method = RequestMethod.POST)
    public BaseResponseDto validateAuthCode(@Valid @RequestBody VerifyCaptchaRequestDto verifyCaptchaRequestDto,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            return mobileAppSendSmsService.validateCaptcha(verifyCaptchaRequestDto);
        }
    }
}
