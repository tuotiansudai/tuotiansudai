package com.tuotiansudai.smswrapper.service;

public interface SmsService {

    boolean sendRegisterCaptcha(String mobile, String captcha);

    boolean sendRetrievePasswordCaptcha(String mobile, String captcha);

    boolean sendPasswordChangedNotify(String mobile);

}
