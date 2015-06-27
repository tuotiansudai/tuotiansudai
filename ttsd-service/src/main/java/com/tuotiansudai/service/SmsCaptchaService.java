package com.tuotiansudai.service;


public interface SmsCaptchaService {

    boolean sendSmsByMobileNumberRegister(String mobileNumber);

    boolean verifyCaptcha(String code, String mobile);

}
