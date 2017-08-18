package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v2_0.MobileAppSendSmsV2Controller;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppSendSmsV2Service;
import com.tuotiansudai.enums.SmsCaptchaType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppSendSmsV2ControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppSendSmsV2Controller controller;

    @Mock
    private MobileAppSendSmsV2Service service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldSendSmsIsOk() throws Exception {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12345678900");
        sendSmsRequestDto.setType(SmsCaptchaType.REGISTER_CAPTCHA);
        when(service.sendSms(any(SendSmsCompositeRequestDto.class), anyString())).thenReturn(baseResponseDto);
        BaseParamDto baseParamDto = new BaseParamDto();
        doRequestWithServiceV2IsOkMockedTest("/sms-captcha/send", baseParamDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }
}
