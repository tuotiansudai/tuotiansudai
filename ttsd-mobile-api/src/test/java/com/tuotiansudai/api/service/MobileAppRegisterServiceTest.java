package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.impl.MobileAppRegisterServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppRegisterServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppRegisterServiceImpl mobileAppRegisterService;

    @Mock
    private UserService userService;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Test
    public void shouldSendRegisterByMobileNumberSMS() {
        String mobileNumber = "13800138000";
        String remoteIp = "114.114.114.114";

        BaseDto<SmsDataDto> smsDto = new BaseDto<>();
        SmsDataDto successSmsDataDto = new SmsDataDto();
        smsDto.setData(successSmsDataDto);
        successSmsDataDto.setStatus(true);

        when(smsCaptchaService.sendRegisterCaptcha(anyString(), anyString())).thenReturn(smsDto);
        when(userService.mobileIsExist(anyString())).thenReturn(false);

        BaseResponseDto responseDto = mobileAppRegisterService.sendRegisterByMobileNumberSMS(mobileNumber, remoteIp);

        assert responseDto.isSuccess();
    }
}
