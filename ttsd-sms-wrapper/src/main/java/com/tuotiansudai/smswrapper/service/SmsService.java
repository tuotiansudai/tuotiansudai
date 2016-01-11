package com.tuotiansudai.smswrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestSmsNotifyDto;
import com.tuotiansudai.dto.SmsDataDto;

public interface SmsService {

    BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String captcha, String ip);

    BaseDto<SmsDataDto> sendInvestNotify(InvestSmsNotifyDto dto);

    BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String captcha, String ip);

    BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile);

    BaseDto<SmsDataDto> investFatalNotify(String mobile, String errMsg);

    BaseDto<SmsDataDto> jobFatalNotify(String mobile, String errMsg);

    BaseDto<SmsDataDto> loanRepayNotify(String mobile, String loanName, String repayAmount);
}
