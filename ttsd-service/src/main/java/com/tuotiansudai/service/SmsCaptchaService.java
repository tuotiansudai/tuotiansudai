package com.tuotiansudai.service;


import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.model.SmsCaptchaModel;

public interface SmsCaptchaService {

    public boolean sendSmsByMobileNumberRegister(String mobileNumber);

    public boolean verifyCaptcha(String code, String mobile);


}
