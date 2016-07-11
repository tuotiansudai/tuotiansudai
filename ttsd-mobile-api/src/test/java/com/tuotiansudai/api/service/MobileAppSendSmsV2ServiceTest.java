package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppSendSmsV2Service;
import com.tuotiansudai.repository.model.CaptchaType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.TestCase.assertEquals;

public class MobileAppSendSmsV2ServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppSendSmsV2Service mobileAppSendSmsV2Service;

    @Test
    public void shouldSendSmsIsOk(){
        SendSmsCompositeRequestDto sendSmsCompositeRequestDto = new SendSmsCompositeRequestDto();
        sendSmsCompositeRequestDto.setImageCaptcha("ABCDEF");
        sendSmsCompositeRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        sendSmsCompositeRequestDto.setPhoneNum("10002341");
        BaseResponseDto baseResponseDto = mobileAppSendSmsV2Service.sendSms(sendSmsCompositeRequestDto,"192.168.1.1");
        assertEquals(baseResponseDto.getCode(), ReturnMessage.SUCCESS.getCode());
    }

    @Test
    public void shouldSendSmsIsFail(){
        SendSmsCompositeRequestDto sendSmsCompositeRequestDto = new SendSmsCompositeRequestDto();
        sendSmsCompositeRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        sendSmsCompositeRequestDto.setPhoneNum("10002341");
        BaseResponseDto baseResponseDto = mobileAppSendSmsV2Service.sendSms(sendSmsCompositeRequestDto,"192.168.1.1");
        assertEquals(baseResponseDto.getCode(), ReturnMessage.NEED_IMAGE_CAPTCHA.getCode());
    }
}
