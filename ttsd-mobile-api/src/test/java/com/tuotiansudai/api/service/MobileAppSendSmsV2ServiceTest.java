package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.service.v2_0.impl.MobileAppSendSmsV2ServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.CaptchaHelper;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppSendSmsV2ServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppSendSmsV2ServiceImpl mobileAppSendSmsV2Service;
    @Mock
    private CaptchaHelper captchaHelper;
    @Mock
    private UserService userService;
    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Test
    public void shouldSendSmsIsOk(){
        SendSmsCompositeRequestDto sendSmsCompositeRequestDto = new SendSmsCompositeRequestDto();
        sendSmsCompositeRequestDto.setImageCaptcha("ABCDEF");
        sendSmsCompositeRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        sendSmsCompositeRequestDto.setPhoneNum("10002341");
        mockMethod(false);
        BaseResponseDto baseResponseDto = mobileAppSendSmsV2Service.sendSms(sendSmsCompositeRequestDto,"192.168.1.1");
        assertEquals(baseResponseDto.getCode(), ReturnMessage.SUCCESS.getCode());
    }

    @Test
    public void shouldSendSmsIsFail(){
        SendSmsCompositeRequestDto sendSmsCompositeRequestDto = new SendSmsCompositeRequestDto();
        sendSmsCompositeRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        sendSmsCompositeRequestDto.setPhoneNum("10002341");
        BaseParam baseParam = new BaseParam();
        baseParam.setDeviceId("1234443");
        sendSmsCompositeRequestDto.setBaseParam(baseParam);
        mockMethod(true);
        BaseResponseDto baseResponseDto = mobileAppSendSmsV2Service.sendSms(sendSmsCompositeRequestDto,"192.168.1.1");
        assertEquals(baseResponseDto.getCode(), ReturnMessage.NEED_IMAGE_CAPTCHA.getCode());
    }

    private void mockMethod(boolean captchaVerify){
        when(captchaHelper.isNeedImageCaptcha(anyString(),anyString())).thenReturn(captchaVerify);
        when(userService.mobileIsExist(anyString())).thenReturn(false);
        BaseDto<SmsDataDto> sms = new BaseDto<>();
        sms.setSuccess(true);
        SmsDataDto smsDataDto = new SmsDataDto();
        smsDataDto.setStatus(true);
        sms.setData(smsDataDto);
        when(smsCaptchaService.sendRegisterCaptcha(anyString(),anyString())).thenReturn(sms);
    }
}
