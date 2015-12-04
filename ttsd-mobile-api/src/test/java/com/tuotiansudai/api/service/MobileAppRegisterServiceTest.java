package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppRegisterServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
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
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);

        BaseResponseDto responseDto = mobileAppRegisterService.sendRegisterByMobileNumberSMS(mobileNumber, remoteIp);

        assert responseDto.isSuccess();

    }
    @Test
    public void shouldRegisterUserLoginNameIsExist() throws ReferrerRelationException {

        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(CaptchaType.class))).thenReturn(false);
        when(userService.loginNameIsExist(anyString())).thenReturn(true);
        when(userService.mobileIsExist(anyString())).thenReturn(false);
        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(false);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        BaseResponseDto baseResponseDto = mobileAppRegisterService.registerUser(registerRequestDto);
        assertEquals(ReturnMessage.USER_NAME_IS_EXIST.getCode(), baseResponseDto.getCode());
    }
    @Test
    public void shouldRegisterUserMobileIsExist() throws ReferrerRelationException {

        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(CaptchaType.class))).thenReturn(false);
        when(userService.loginNameIsExist(anyString())).thenReturn(false);
        when(userService.mobileIsExist(anyString())).thenReturn(true);
        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(false);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        BaseResponseDto baseResponseDto = mobileAppRegisterService.registerUser(registerRequestDto);
        assertEquals(ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode(),baseResponseDto.getCode());
    }
    @Test
    public void shouldRegisterUserCaptchaIsValid() throws ReferrerRelationException {

        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(CaptchaType.class))).thenReturn(false);
        when(userService.loginNameIsExist(anyString())).thenReturn(false);
        when(userService.mobileIsExist(anyString())).thenReturn(false);
        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(false);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        BaseResponseDto baseResponseDto = mobileAppRegisterService.registerUser(registerRequestDto);
        assertEquals(ReturnMessage.SMS_CAPTCHA_ERROR.getCode(),baseResponseDto.getCode());
    }
    @Test
    public void shouldRegisterUserIsOk() throws ReferrerRelationException {
        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(CaptchaType.class))).thenReturn(true);
        when(userService.loginNameIsExist(anyString())).thenReturn(false);
        when(userService.mobileIsExist(anyString())).thenReturn(false);
        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(true);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        BaseResponseDto baseResponseDto = mobileAppRegisterService.registerUser(registerRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
        assertEquals("13900000000",((RegisterResponseDataDto)baseResponseDto.getData()).getPhoneNum());
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
        registerRequestDto.setUserName("loginName");
        registerRequestDto.setCaptcha("123456");
        registerRequestDto.setPassword("password");
        registerRequestDto.setPhoneNum("13900000000");
        registerRequestDto.setBaseParam(BaseParamTest.getInstance());
        return registerRequestDto;
    }
}
