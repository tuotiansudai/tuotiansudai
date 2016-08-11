package com.tuotiansudai.smswrapper.service;

import com.tuotiansudai.dto.*;

import java.util.List;

public interface SmsService {

    BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String captcha, String ip);

    BaseDto<SmsDataDto> sendInvestNotify(InvestSmsNotifyDto dto);

    BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String captcha, String ip);

    BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile);

    BaseDto<SmsDataDto> sendFatalNotify(SmsFatalNotifyDto notify);

    BaseDto<SmsDataDto> loanRepayNotify(String mobile, String repayAmount);

    BaseDto<SmsDataDto> couponNotify(SmsCouponNotifyDto notifyDto);

    BaseDto<SmsDataDto> birthdayNotify(SmsCouponNotifyDto notifyDto);

    BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, String captcha, String ip) ;

    BaseDto<SmsDataDto> experienceRepayNotify(List<String> mobiles, String repayAmount);

    BaseDto<SmsDataDto> cancelTransferLoan(String mobile, String TransferLoanName);
}
