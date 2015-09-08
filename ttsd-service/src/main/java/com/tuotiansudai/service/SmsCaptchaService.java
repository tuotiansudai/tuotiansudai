package com.tuotiansudai.service;


public interface SmsCaptchaService {

    boolean sendRegisterCaptcha(String mobile);

    boolean verifyRegisterCaptcha(String mobile, String captcha);

    boolean sendMobileCaptcha(String mobile);

    boolean verifyMobileCaptcha(String mobile, String captcha);

}
