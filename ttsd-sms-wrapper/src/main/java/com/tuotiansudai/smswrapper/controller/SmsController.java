package com.tuotiansudai.smswrapper.controller;

import com.tuotiansudai.dto.SmsCaptchaDto;
import com.tuotiansudai.smswrapper.dto.SmsResultDataDto;
import com.tuotiansudai.smswrapper.dto.SmsResultDto;
import com.tuotiansudai.smswrapper.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/sms", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/mobile/captcha/register", method = RequestMethod.POST)
    @ResponseBody
    public SmsResultDto sendRegisterCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        SmsResultDataDto data = new SmsResultDataDto();
        data.setStatus(smsService.sendRegisterCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp()));
        return new SmsResultDto(data);
    }

    @RequestMapping(value = "/mobile/captcha/retrieve", method = RequestMethod.POST)
    @ResponseBody
    public SmsResultDto sendRetrievePasswordCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        SmsResultDataDto data = new SmsResultDataDto();
        data.setStatus(smsService.sendRetrievePasswordCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp()));
        return new SmsResultDto(data);
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/password-changed-notify", method = RequestMethod.GET)
    @ResponseBody
    public SmsResultDto sendPasswordChangedNotify(@PathVariable String mobile) {
        SmsResultDataDto data = new SmsResultDataDto();
        data.setStatus(smsService.sendPasswordChangedNotify(mobile));
        return new SmsResultDto(data);
    }
}
