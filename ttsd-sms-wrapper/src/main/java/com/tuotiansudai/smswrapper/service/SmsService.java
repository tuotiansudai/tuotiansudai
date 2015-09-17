package com.tuotiansudai.smswrapper.service;

public interface SmsService {

    boolean sendRegisterCaptcha(String mobile, String captcha, String ip);

    boolean sendRetrievePasswordCaptcha(String mobile, String captcha, String ip);

}
