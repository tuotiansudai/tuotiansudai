package com.tuotiansudai.smswrapper.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestSmsNotifyDto;
import com.tuotiansudai.dto.SmsCaptchaDto;
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
    public BaseDto<BaseDataDto> sendRegisterCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        BaseDataDto data = new BaseDataDto();
        dto.setData(data);
        data.setStatus(smsService.sendRegisterCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp()));
        return dto;
    }

    @RequestMapping(value = "/retrieve-password-captcha", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> sendRetrievePasswordCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        BaseDataDto data = new BaseDataDto();
        dto.setData(data);
        data.setStatus(smsService.sendRetrievePasswordCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp()));
        return dto;
    }

    @RequestMapping(value = "/invest_notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> sendInvestNotify(@RequestBody InvestSmsNotifyDto investSmsNotifyDto) {
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        BaseDataDto data = new BaseDataDto();
        dto.setData(data);
        data.setStatus(smsService.sendInvestNotify(investSmsNotifyDto));
        return dto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/password-changed-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> sendPasswordChangedNotify(@PathVariable String mobile) {
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        BaseDataDto data = new BaseDataDto();
        dto.setData(data);
        data.setStatus(smsService.sendPasswordChangedNotify(mobile));
        return dto;
    }
}
