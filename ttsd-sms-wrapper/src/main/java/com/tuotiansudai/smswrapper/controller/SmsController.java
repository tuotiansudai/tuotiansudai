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
        return smsService.sendRegisterCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.isVoice(), smsCaptchaDto.getIp());
    }

    @RequestMapping(value = "/register-success", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendRegisterSuccess(@Valid @RequestBody SmsRegisterSuccessNotifyDto notifyDto) {
        return smsService.sendRegisterSuccess(notifyDto);
    }

    @RequestMapping(value = "/no-password-invest-captcha", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        return smsService.sendNoPasswordInvestCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.isVoice(), smsCaptchaDto.getIp());
    }

    @RequestMapping(value = "/retrieve-password-captcha", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(@Valid @RequestBody SmsCaptchaDto smsCaptchaDto) {
        return smsService.sendRetrievePasswordCaptcha(smsCaptchaDto.getMobile(), smsCaptchaDto.getCaptcha(), smsCaptchaDto.isVoice(), smsCaptchaDto.getIp());
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/password-changed-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendPasswordChangedNotify(@PathVariable String mobile) {
        return smsService.sendPasswordChangedNotify(mobile);
    }

    @RequestMapping(value = "/fatal-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> fatalNotify(@Valid @RequestBody SmsFatalNotifyDto notify) {
        return smsService.sendFatalNotify(notify);
    }

    @RequestMapping(value = "/loan-repay-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> loanRepayNotify(@Valid @RequestBody RepayNotifyDto notifyDto) {
        return smsService.loanRepayNotify(notifyDto.getMobile(), notifyDto.getRepayAmount());
    }

    @RequestMapping(value = "/cancel-transfer-loan", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> cancelTransferLoan(@RequestBody SmsTransferLoanNotifyDto notifyDto) {
        return smsService.cancelTransferLoan(notifyDto.getMobile(), notifyDto.getTransferLoanName());
    }

    @RequestMapping(value = "/transfer-loan-success", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> transferLoanSuccess(@RequestBody SmsTransferLoanNotifyDto notifyDto) {
        return smsService.transferLoanSuccess(notifyDto.getMobile(), notifyDto.getTransferLoanName());
    }

    @RequestMapping(value = "/transfer-loan-overdue", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> transferLoanOverdue(@RequestBody SmsTransferLoanNotifyDto notifyDto) {
        return smsService.transferLoanOverdue(notifyDto.getMobile(), notifyDto.getTransferLoanName());
    }
//
//    @RequestMapping(value = "/import-user-receive-membership", method = RequestMethod.POST)
//    @ResponseBody
//    public BaseDto<SmsDataDto> importUserReceiveMembership(@RequestBody SmsUserReceiveMembershipDto notifyDto) {
//        return smsService.importUserGetGiveMembership(notifyDto.getMobile(), notifyDto.getLevel());
//    }

//    @RequestMapping(value = "/new-user-receive-membership", method = RequestMethod.POST)
//    @ResponseBody
//    public BaseDto<SmsDataDto> newUserReceiveMembership(@RequestBody SmsUserReceiveMembershipDto notifyDto) {
//        return smsService.newUserGetGiveMembership(notifyDto.getMobile(), notifyDto.getLevel());
//    }

    @RequestMapping(value = "/platform-balance-low-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> platformBalanceLowNotify(@RequestBody PlatformBalanceLowNotifyDto notifyDto) {
        return smsService.platformBalanceLowNotify(notifyDto.getMobiles(), notifyDto.getWarningLine());
    }

    @RequestMapping(value = "/generate-contract-error-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> generateContractErrorNotify(@RequestBody GenerateContractErrorNotifyDto notifyDto) {
        return smsService.generateContractNotify(notifyDto.getMobiles(), notifyDto.getBusinessId());
    }

    @RequestMapping(value = "/loan-raising-complete-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> loanRaisingCompleteNotify(@RequestBody LoanRaisingCompleteNotifyDto notifyDto) {
        return smsService.loanRaisingCompleteNotify(notifyDto);
    }

    @RequestMapping(value = "/loan-out-complete-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> loanOutCompleteNotify(@RequestBody LoanOutCompleteNotifyDto notifyDto) {
        return smsService.loanOutCompleteNotify(notifyDto);
    }

    @RequestMapping(value = "/coupon-assign-success", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> couponAssignSuccessNotify(@Valid @RequestBody SmsCouponNotifyDto notifyDto) {
        return smsService.assignCouponSuccessNotify(notifyDto);
    }

    @RequestMapping(value = "/coupon-expired-notify", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> couponExpiredNotify(@Valid @RequestBody SmsCouponNotifyDto notifyDto) {
        return smsService.couponExpiredNotify(notifyDto);
    }

    @RequestMapping(value = "/credit-loan-balance-alert", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> creditLoanBalanceAlert() {
        return smsService.creditLoanBalanceAlert();
    }


    @RequestMapping(value = "/advanced-repay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> advancedRepayNotify(@Valid @RequestBody InvestSmsNotifyDto dto) {
        return smsService.advancedRepay(dto);
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/membership-privilege-buy-success", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendMembershipPrivilegeBySuccessNotify(@PathVariable String mobile) {
        return smsService.membershipPrivilegeBySuccess(mobile);
    }

    @RequestMapping(value = "/payroll", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendPayrollNotify(@Valid @RequestBody SmsPayrollNotifyDto dto) {
        return smsService.payroll(dto);
    }

    @RequestMapping(value = "/use-point", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendUsePointNotify(@Valid @RequestBody SmsUsePointNotifyDto dto) {
        return smsService.usePoint(dto);
    }

    @RequestMapping(value = "/membership-upgrade", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<SmsDataDto> sendMembershipUpgradeNotify(@Valid @RequestBody SmsUserReceiveMembershipDto dto) {
        return smsService.membershipUpgrade(dto);
    }

}
