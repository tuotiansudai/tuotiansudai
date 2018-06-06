package com.tuotiansudai.smswrapper.controller;

import com.tuotiansudai.smswrapper.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sms", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
public class SmsController {

    @Autowired
    private SmsService smsService;

//    @RequestMapping(method = RequestMethod.POST)
//    @ResponseBody
//    public BaseDto<SmsDataDto> sendSms(@Valid @RequestBody SmsNotifyDto smsNotifyDto) {
//        return smsService.sendSms(smsNotifyDto);
//    }
//
//    @RequestMapping(value = "/fatal-notify", method = RequestMethod.POST)
//    @ResponseBody
//    public BaseDto<SmsDataDto> fatalNotify(@Valid @RequestBody SmsFatalNotifyDto notify) {
//        return smsService.sendFatalNotify(notify);
//    }
}
