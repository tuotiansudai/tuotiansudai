package com.tuotiansudai.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.enums.SmsCaptchaType;

public interface SmsCaptchaService {

    BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, boolean isVoice, String requestIP);

    BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, boolean isVoice, String requestIP);

    boolean verifyMobileCaptcha(String mobile, String captcha, SmsCaptchaType smsCaptchaType);

    BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, boolean isVoice, String requestIP);

}
