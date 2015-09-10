package com.tuotiansudai.smswrapper.controller;

import com.tuotiansudai.smswrapper.dto.SmsResultDataDto;
import com.tuotiansudai.smswrapper.dto.SmsResultDto;
import com.tuotiansudai.smswrapper.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sms", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/register", method = RequestMethod.GET)
    @ResponseBody
    public SmsResultDto sendRegisterCaptcha(@PathVariable String mobile, @PathVariable String captcha) {
        SmsResultDataDto data = new SmsResultDataDto();
        data.setStatus(smsService.sendRegisterCaptcha(mobile, captcha));
        return new SmsResultDto(data);
    }
}
