package com.tuotiansudai.service;


import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.model.SmsCaptchaModel;

public interface SmsCaptchaService {

    public boolean sendSmsbyMobileNumberRegister(String mobileNumber) throws Exception;

    public boolean verifyMobileNumber(String mobileNumber);

    public SmsCaptchaModel createSmsCaptcha(String mobileNumber);

    public boolean verifyCaptcha(String code, String mobile);

    public boolean sendRegisterCaptcha(String mobile, String captcha) throws Exception;

}
