package com.tuotiansudai.api.service.v2_0;


import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.SendSmsCompositeRequestDto;

public interface MobileAppSendSmsV2Service {

    BaseResponseDto sendSms(SendSmsCompositeRequestDto sendSmsCompositeRequestDto, String remoteIp);
}
