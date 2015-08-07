package com.tuotiansudai.smswrapper.service;

public interface SmsService {

    boolean sendRegisterCaptcha(String mobile, String captcha);

}
