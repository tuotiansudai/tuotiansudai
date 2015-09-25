package com.tuotiansudai.service;


public interface SmsCaptchaService {

    boolean sendRegisterCaptcha(String mobile, String requestIP);

    boolean verifyRegisterCaptcha(String mobile, String captcha);

    boolean sendRetrievePasswordCaptcha(String mobile, String requestIP);

    boolean verifyMobileCaptcha(String mobile, String captcha);

}
