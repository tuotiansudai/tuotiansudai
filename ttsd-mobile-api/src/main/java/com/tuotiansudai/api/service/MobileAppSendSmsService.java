package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;
import com.tuotiansudai.api.dto.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.dto.VerifyCaptchaRequestDto;

public interface MobileAppSendSmsService {

    BaseResponseDto sendSms(SendSmsCompositeRequestDto sendSmsCompositeRequestDto, String remoteIp);

    BaseResponseDto validateCaptcha(VerifyCaptchaRequestDto verifyCaptchaRequestDto);

}
