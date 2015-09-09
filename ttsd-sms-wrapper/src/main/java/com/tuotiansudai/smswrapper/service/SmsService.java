package com.tuotiansudai.smswrapper.service;

import com.tuotiansudai.dto.InvestSmsNotifyDto;

public interface SmsService {

    boolean sendRegisterCaptcha(String mobile, String captcha);

    boolean sendInvestNotify(InvestSmsNotifyDto dto);
}
