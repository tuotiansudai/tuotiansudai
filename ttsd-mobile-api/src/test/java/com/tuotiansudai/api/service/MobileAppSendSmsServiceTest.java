package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.dto.v1_0.VerifyCaptchaRequestDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppSendSmsServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class MobileAppSendSmsServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppSendSmsServiceImpl mobileAppSendSmsService;

    @Mock
    private UserService userService;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Test
    public void shouldSendSmsByRegisterCaptchaMobileIsDuplicate(){
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12312312312");
        sendSmsRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        when(userService.mobileIsExist(anyString())).thenReturn(true);
        BaseResponseDto baseResponseDto = mobileAppSendSmsService.sendSms(sendSmsRequestDto, "127.0.0.1");
        assertEquals(ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode(),baseResponseDto.getCode());

    }
    @Test
    public void shouldSendSmsByRetrievePasswordMobileNotExistCaptcha(){
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12312312312");
        sendSmsRequestDto.setType(CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        when(userService.mobileIsExist(anyString())).thenReturn(false);
        BaseResponseDto baseResponseDto = mobileAppSendSmsService.sendSms(sendSmsRequestDto, "127.0.0.1");
        assertEquals(ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getCode(),baseResponseDto.getCode());
    }

    @Test
    public void shouldSendSmsByRegisterCaptchaIsSuccess(){
        String ip = "127.0.0.1";
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12312312312");
        sendSmsRequestDto.setType(CaptchaType.REGISTER_CAPTCHA);
        BaseDto<SmsDataDto> smsDto = new BaseDto<>();
        SmsDataDto smsDataDto = new SmsDataDto();
        smsDataDto.setStatus(true);
        smsDto.setSuccess(true);
        smsDto.setData(smsDataDto);

        when(userService.mobileIsExist(anyString())).thenReturn(false);
        when(smsCaptchaService.sendRegisterCaptcha(anyString(), anyString())).thenReturn(smsDto);
        BaseResponseDto baseResponseDto = mobileAppSendSmsService.sendSms(sendSmsRequestDto, ip);

        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
    }

    @Test
    public void shouldSendSmsByRetrievePasswordCaptchaIsSuccess(){
        String ip = "127.0.0.1";
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12312312312");
        sendSmsRequestDto.setType(CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        BaseDto<SmsDataDto> smsDto = new BaseDto<>();
        SmsDataDto smsDataDto = new SmsDataDto();
        smsDataDto.setStatus(true);
        smsDto.setSuccess(true);
        smsDto.setData(smsDataDto);

        when(userService.mobileIsExist(anyString())).thenReturn(true);
        when(smsCaptchaService.sendRetrievePasswordCaptcha(anyString(), anyString())).thenReturn(smsDto);
        BaseResponseDto baseResponseDto = mobileAppSendSmsService.sendSms(sendSmsRequestDto, ip);

        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
    }

    @Test
    public void shouldSendSmsByTurnOffNoPasswordInvestIsSuccess(){
        String ip = "127.0.0.1";
        SendSmsCompositeRequestDto sendSmsRequestDto = new SendSmsCompositeRequestDto();
        sendSmsRequestDto.setPhoneNum("12312312312");
        sendSmsRequestDto.setType(CaptchaType.TURN_OFF_NO_PASSWORD_INVEST);
        BaseDto<SmsDataDto> smsDto = new BaseDto<>();
        SmsDataDto smsDataDto = new SmsDataDto();
        smsDataDto.setStatus(true);
        smsDto.setSuccess(true);
        smsDto.setData(smsDataDto);

        when(userService.mobileIsExist(anyString())).thenReturn(true);
        when(smsCaptchaService.sendNoPasswordInvestCaptcha(anyString(), anyString())).thenReturn(smsDto);
        BaseResponseDto baseResponseDto = mobileAppSendSmsService.sendSms(sendSmsRequestDto, ip);

        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
    }

    @Test
    public void shouldValidateCaptchaIsSuccess() {
        VerifyCaptchaRequestDto requestDto = new VerifyCaptchaRequestDto();
        requestDto.setPhoneNum("13800138000");
        requestDto.setCaptcha("123456");
        requestDto.setType(CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), eq(CaptchaType.RETRIEVE_PASSWORD_CAPTCHA))).thenReturn(true);
        BaseResponseDto responseDto = mobileAppSendSmsService.validateCaptcha(requestDto);
        assert responseDto.isSuccess();
    }





}
