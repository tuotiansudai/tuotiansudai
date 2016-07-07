package com.tuotiansudai.api.service.v2_0.impl;


import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppSendSmsService;

public class MobileAppSendSmsServiceImpl implements MobileAppSendSmsService {

    @Override
    public BaseResponseDto sendSms(SendSmsCompositeRequestDto sendSmsCompositeRequestDto, String remoteIp) {
        if(Strings.isNullOrEmpty(sendSmsCompositeRequestDto.getImageCaptcha())){

        }
        return null;
    }
}
