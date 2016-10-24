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

    @RequestMapping(value = "/register-captcha", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendRegisterCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        BaseDto<SmsDataDto> smsDateDto = smsService.sendRegisterCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp());
        return smsDateDto;

    }

    @RequestMapping(value = "/no-password-invest-captcha", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        return smsService.sendNoPasswordInvestCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp());
    }

    @RequestMapping(value = "/retrieve-password-captcha", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        return smsService.sendRetrievePasswordCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.getIp());
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/password-changed-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendPasswordChangedNotify(@PathVariable String mobile) {
        return smsService.sendPasswordChangedNotify(mobile);
    }

    @RequestMapping(value = "/loan-out-investor-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendInvestNotify(@RequestBody InvestSmsNotifyDto investSmsNotifyDto) {
        return smsService.sendInvestNotify(investSmsNotifyDto);
    }

    @RequestMapping(value = "/fatal-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> fatalNotify(@Valid @RequestBody SmsFatalNotifyDto notify) {
        return smsService.sendFatalNotify(notify);
    }

    @RequestMapping(value = "/coupon-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> couponNotify(@Valid @RequestBody SmsCouponNotifyDto notifyDto) {
        return smsService.couponNotifyByMd(notifyDto);
    }

    @RequestMapping(value = "/birthday-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> birthdayNotify(@RequestBody SmsCouponNotifyDto notifyDto) {
        return smsService.birthdayNotify(notifyDto);
    }

    @RequestMapping(value = "/loan-repay-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> loanRepayNotify(@Valid @RequestBody RepayNotifyDto notifyDto) {
        return smsService.loanRepayNotify(notifyDto.getMobile(), notifyDto.getRepayAmount());
    }

    @RequestMapping(value = "/experience-repay-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> experienceRepayNotify(@Valid @RequestBody ExperienceRepayNotifyDto notifyDto) {
        return smsService.experienceRepayNotify(notifyDto.getMobiles(), notifyDto.getRepayAmount());
    }

    @RequestMapping(value = "/cancel-transfer-loan", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> cancelTransferLoan(@RequestBody SmsCancelTransferLoanNotifyDto notifyDto) {
        return smsService.cancelTransferLoan(notifyDto.getMobile(), notifyDto.getTransferLoanName());
    }

    @RequestMapping(value = "/import-user-receive-membership", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> importUserReceiveMembership(@RequestBody SmsUserReceiveMembershipDto notifyDto) {
        return smsService.importUserGetGiveMembership(notifyDto.getMobile(), notifyDto.getLevel());
    }

    @RequestMapping(value = "/new-user-receive-membership", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> newUserReceiveMembership(@RequestBody SmsUserReceiveMembershipDto notifyDto) {
        return smsService.newUserGetGiveMembership(notifyDto.getMobile(), notifyDto.getLevel());
    }
}
