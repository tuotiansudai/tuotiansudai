package com.tuotiansudai.smswrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.*;
import com.tuotiansudai.smswrapper.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/sms", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendSms(@Valid @RequestBody SmsDto smsDto) {
        return smsService.sendSms(smsDto.getMobiles(), smsDto.getJianZhouSmsTemplate(), smsDto.isVoice(), smsDto.getParams(), smsDto.getRequestIP());
    }

    @RequestMapping(value = "/fatal-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> fatalNotify(@Valid @RequestBody SmsFatalNotifyDto notify) {
        return smsService.sendFatalNotify(notify);
    }
}
