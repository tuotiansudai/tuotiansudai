package com.tuotiansudai.sms.service;

public interface SmsService {

    boolean sendRegisterCaptcha(String mobile, String captcha);

}
