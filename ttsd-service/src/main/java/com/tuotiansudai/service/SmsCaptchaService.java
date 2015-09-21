package com.tuotiansudai.service;


import javax.servlet.http.HttpServletRequest;

public interface SmsCaptchaService {

    boolean sendRegisterCaptcha(String mobile, HttpServletRequest request);

    boolean verifyRegisterCaptcha(String mobile, String captcha);

    boolean sendMobileCaptcha(String mobile, HttpServletRequest request);

    boolean verifyMobileCaptcha(String mobile, String captcha);

}
