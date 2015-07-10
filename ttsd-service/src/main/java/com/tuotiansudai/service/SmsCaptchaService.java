package com.tuotiansudai.service;


public interface SmsCaptchaService {

    boolean sendRegisterCaptcha(String mobile);

    boolean verifyRegisterCaptcha(String mobile, String captcha);

}
