package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;
import com.tuotiansudai.api.service.impl.MobileAppRetrievePasswordServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class MobileAppRetrievePasswordServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppRetrievePasswordServiceImpl mobileAppRetrievePasswordService;

    @Mock
    private UserService userService;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Test
    public void shouldSendSMS() {
        String mobileNumber = "13800138000";
        String remoteIp = "114.114.114.114";
        RetrievePasswordRequestDto requestDto = new RetrievePasswordRequestDto();
        requestDto.setAuthType("find_login_password_by_mobile");
        requestDto.setPassword(mobileNumber);

        BaseDto<SmsDataDto> smsDto = new BaseDto<>();
        SmsDataDto successSmsDataDto = new SmsDataDto();
        smsDto.setData(successSmsDataDto);
        successSmsDataDto.setStatus(true);

        when(smsCaptchaService.sendRetrievePasswordCaptcha(anyString(), anyString())).thenReturn(smsDto);
        when(userService.mobileIsExist(anyString())).thenReturn(true);

        BaseResponseDto responseDto = mobileAppRetrievePasswordService.sendSMS(requestDto, remoteIp);

        assert responseDto.isSuccess();
    }

    @Test
    public void validateAuthCode() {
        RetrievePasswordRequestDto requestDto = new RetrievePasswordRequestDto();
        requestDto.setPhoneNum("13800138000");
        requestDto.setValidateCode("123456");
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), eq(CaptchaType.RETRIEVE_PASSWORD_CAPTCHA))).thenReturn(true);
        mobileAppRetrievePasswordService.sendSMS(requestDto, "114.114.114.114");
        BaseResponseDto responseDto = mobileAppRetrievePasswordService.validateAuthCode(requestDto);
        assert responseDto.isSuccess();
    }
}
