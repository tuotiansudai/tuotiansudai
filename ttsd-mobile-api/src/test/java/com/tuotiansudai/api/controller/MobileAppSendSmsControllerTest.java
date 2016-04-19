package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppSendSmsService;
import com.tuotiansudai.repository.model.CaptchaType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppSendSmsControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppSendSmsController controller;

    @Mock
    private MobileAppSendSmsService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldSendSmsIsOk() throws Exception {
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12345678900");
        sendSmsRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        when(service.sendSms(any(SendSmsCompositeRequestDto.class), anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/sms-captcha/send", sendSmsRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldSendSmsTypeIsValid() throws Exception {
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12345678900");
        when(service.sendSms(any(SendSmsCompositeRequestDto.class), anyString())).thenReturn(successResponseDto);

        doRequestWithServiceIsOkMockedTest("/sms-captcha/send", sendSmsRequestDto)
                .andExpect(jsonPath("$.code").value("0022"));
    }

    @Test
    public void shouldSendSmsMobileIsValid() throws Exception{
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12345A78900");
        sendSmsRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        when(service.sendSms(any(SendSmsCompositeRequestDto.class), anyString())).thenReturn(successResponseDto);

        doRequestWithServiceIsOkMockedTest("/sms-captcha/send", sendSmsRequestDto)
                .andExpect(jsonPath("$.code").value("0002"));
    }

    @Test
    public void shouldSendSmsMobileIsNull() throws Exception{
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        when(service.sendSms(any(SendSmsCompositeRequestDto.class), anyString())).thenReturn(successResponseDto);

        doRequestWithServiceIsOkMockedTest("/sms-captcha/send", sendSmsRequestDto)
                .andExpect(jsonPath("$.code").value("0001"));
    }

    @Test
    public void shouldValidateCaptchaIsSuccess() throws Exception {
        VerifyCaptchaRequestDto verifyCaptchaRequestDto = new VerifyCaptchaRequestDto();
        verifyCaptchaRequestDto.setCaptcha("123456");
        verifyCaptchaRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        verifyCaptchaRequestDto.setPhoneNum("13800000098");
        when(service.validateCaptcha(any(VerifyCaptchaRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/validatecaptcha",
                verifyCaptchaRequestDto);
    }
    @Test
    public void shouldValidateCaptchaIsValid() throws Exception {
        VerifyCaptchaRequestDto verifyCaptchaRequestDto = new VerifyCaptchaRequestDto();
        verifyCaptchaRequestDto.setCaptcha("1234");
        verifyCaptchaRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        when(service.validateCaptcha(any(VerifyCaptchaRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/validatecaptcha", verifyCaptchaRequestDto)
                .andExpect(jsonPath("$.code").value("0001"));
    }



}
