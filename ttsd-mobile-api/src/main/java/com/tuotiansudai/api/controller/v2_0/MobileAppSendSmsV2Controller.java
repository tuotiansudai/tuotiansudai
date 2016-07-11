package com.tuotiansudai.api.controller.v2_0;


import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppSendSmsV2Service;
import com.tuotiansudai.api.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public class MobileAppSendSmsV2Controller extends MobileAppBaseController {

    @Autowired
    private MobileAppSendSmsV2Service mobileAppSendSmsV2Service;

    @RequestMapping(value = "/sms-captcha/send", method = RequestMethod.POST)
    public BaseResponseDto sendSms(@Valid @RequestBody SendSmsCompositeRequestDto sendSmsCompositeRequestDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            sendSmsCompositeRequestDto.getBaseParam().setUserId(getLoginName());
            String remoteIp = CommonUtils.getRemoteHost(request);
            return mobileAppSendSmsV2Service.sendSms(sendSmsCompositeRequestDto,remoteIp);
        }
    }
}
