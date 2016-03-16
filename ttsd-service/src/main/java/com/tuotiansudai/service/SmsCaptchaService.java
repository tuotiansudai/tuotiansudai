package com.tuotiansudai.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.model.CaptchaType;

public interface SmsCaptchaService {

    BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String requestIP);

    BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String requestIP);

    boolean verifyMobileCaptcha(String mobile, String captcha, CaptchaType captchaType);

    BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, String requestIP) ;

}
