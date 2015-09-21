package com.tuotiansudai.smswrapper.service;

import com.tuotiansudai.dto.InvestSmsNotifyDto;

public interface SmsService {

    boolean sendRegisterCaptcha(String mobile, String captcha, String ip);

    boolean sendInvestNotify(InvestSmsNotifyDto dto);

    boolean sendRetrievePasswordCaptcha(String mobile, String captcha, String ip);

    boolean sendPasswordChangedNotify(String mobile);
}
