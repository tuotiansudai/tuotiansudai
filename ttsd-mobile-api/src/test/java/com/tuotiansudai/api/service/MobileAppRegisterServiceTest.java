package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppRegisterServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppRegisterServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppRegisterServiceImpl mobileAppRegisterService;

    @Mock
    private UserService userService;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Mock
    private MobileAppChannelService channelService;

    @Mock
    private MyAuthenticationUtil myAuthenticationUtil;

    @Test
    public void shouldSendRegisterByMobileNumberSMS() {
        String mobileNumber = "13800138000";
        String remoteIp = "114.114.114.114";

        BaseDto<SmsDataDto> smsDto = new BaseDto<>();
        SmsDataDto successSmsDataDto = new SmsDataDto();
        smsDto.setData(successSmsDataDto);
        successSmsDataDto.setStatus(true);

        when(smsCaptchaService.sendRegisterCaptcha(anyString(), anyBoolean(), anyString())).thenReturn(smsDto);
        when(userService.mobileIsExist(anyString())).thenReturn(false);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);

        BaseResponseDto responseDto = mobileAppRegisterService.sendRegisterByMobileNumberSMS(mobileNumber, remoteIp);

        assert responseDto.isSuccess();

    }
    @Test
    public void shouldRegisterUserLoginNameIsExist() {

        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(SmsCaptchaType.class))).thenReturn(false);
        when(userService.loginNameIsExist(anyString())).thenReturn(true);
        when(userService.mobileIsExist(anyString())).thenReturn(false);
        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(false);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        BaseResponseDto baseResponseDto = mobileAppRegisterService.registerUser(registerRequestDto);
        assertEquals(ReturnMessage.USER_NAME_IS_EXIST.getCode(), baseResponseDto.getCode());
    }
    @Test
    public void shouldRegisterUserMobileIsExist() {

        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(SmsCaptchaType.class))).thenReturn(false);
        when(userService.loginNameIsExist(anyString())).thenReturn(false);
        when(userService.mobileIsExist(anyString())).thenReturn(true);
        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(false);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        BaseResponseDto baseResponseDto = mobileAppRegisterService.registerUser(registerRequestDto);
        assertEquals(ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode(),baseResponseDto.getCode());
    }
    @Test
    public void shouldRegisterUserCaptchaIsValid() {

        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(SmsCaptchaType.class))).thenReturn(false);
        when(userService.loginNameIsExist(anyString())).thenReturn(false);
        when(userService.mobileIsExist(anyString())).thenReturn(false);
        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(false);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        BaseResponseDto baseResponseDto = mobileAppRegisterService.registerUser(registerRequestDto);
        assertEquals(ReturnMessage.SMS_CAPTCHA_ERROR.getCode(),baseResponseDto.getCode());
    }

    public UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13" + RandomStringUtils.randomNumeric(9));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    public RegisterRequestDto getFakeRegisterRequestDto(){
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserName("userName");
        registerRequestDto.setCaptcha("123456");
        registerRequestDto.setPassword("password");
        registerRequestDto.setPhoneNum("13900000000");
        registerRequestDto.setBaseParam(BaseParamTest.getInstance());
        return registerRequestDto;
    }
    public RegisterHuizuRequestDto getFakeHuizuRegisterRequestDto(){
        RegisterHuizuRequestDto registerHuizuRequestDto = new RegisterHuizuRequestDto();
        registerHuizuRequestDto.setPassword("password");
        registerHuizuRequestDto.setPhoneNum("13900000000");
        return registerHuizuRequestDto;
    }
}
