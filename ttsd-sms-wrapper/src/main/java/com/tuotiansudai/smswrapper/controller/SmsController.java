package com.tuotiansudai.smswrapper.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.smswrapper.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/sms", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/register-captcha", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendRegisterCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        return smsService.sendRegisterCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp());
    }

    @RequestMapping(value = "/retrieve-password-captcha", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        return smsService.sendRetrievePasswordCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp());
    }

    @RequestMapping(value = "/loan-out-investor-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendInvestNotify(@RequestBody InvestSmsNotifyDto investSmsNotifyDto) {
        return smsService.sendInvestNotify(investSmsNotifyDto);
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/password-changed-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendPasswordChangedNotify(@PathVariable String mobile) {
        return smsService.sendPasswordChangedNotify(mobile);
    }

    @RequestMapping(value = "/invest-fatal-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> investFatalNotify(@Valid @RequestBody SmsInvestFatalNotifyDto notifyDto) {
        return smsService.investFatalNotify(notifyDto.getMobile(), notifyDto.getErrMsg());
    }

    @RequestMapping(value = "/job-fatal-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> jobFatalNotify(@Valid @RequestBody SmsJobFatalNotifyDto notifyDto) {
        return smsService.jobFatalNotify(notifyDto.getMobile(), notifyDto.getErrMsg());
    }
}
