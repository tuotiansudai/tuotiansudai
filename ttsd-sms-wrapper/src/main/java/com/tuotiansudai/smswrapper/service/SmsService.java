package com.tuotiansudai.smswrapper.service;

public interface SmsService {

    boolean sendRegisterCaptcha(String mobile, String captcha);

    boolean sendMobileCaptcha(String mobile, String captcha);

}
