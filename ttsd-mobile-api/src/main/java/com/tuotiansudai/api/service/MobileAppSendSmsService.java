package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.SendSmsCompositeRequestDto;

public interface MobileAppSendSmsService {

    BaseResponseDto sendSms(SendSmsCompositeRequestDto sendSmsCompositeRequestDto, String remoteIp);

}
