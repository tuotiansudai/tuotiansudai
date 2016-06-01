package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.dto.v1_0.VerifyCaptchaRequestDto;

public interface MobileAppSendSmsService {

    BaseResponseDto sendSms(SendSmsCompositeRequestDto sendSmsCompositeRequestDto, String remoteIp);

    BaseResponseDto validateCaptcha(VerifyCaptchaRequestDto verifyCaptchaRequestDto);

}
