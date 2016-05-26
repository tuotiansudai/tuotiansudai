package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RetrievePasswordRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppRetrievePasswordServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppRetrievePasswordServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppRetrievePasswordServiceImpl mobileAppRetrievePasswordService;

    @Mock
    private UserService userService;

    @Mock
    private SmsCaptchaService smsCaptchaService;
    @Mock
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;
    @Mock
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Test
    public void shouldRetrievePasswordIsOk(){
        UserModel userModel = getFakeUser();
        RetrievePasswordRequestDto retrievePasswordRequestDto = new RetrievePasswordRequestDto();
        retrievePasswordRequestDto.setPassword("123abc");
        retrievePasswordRequestDto.setPhoneNum("13900000000");
        retrievePasswordRequestDto.setValidateCode("123456");
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(CaptchaType.class))).thenReturn(true);
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(myShaPasswordEncoder.encodePassword(anyString(),anyString())).thenReturn("123456789");
        BaseResponseDto baseResponseDto = mobileAppRetrievePasswordService.retrievePassword(retrievePasswordRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
    }
    @Test
    public void shouldRetrievePasswordCaptchaIsValid(){
        UserModel userModel = getFakeUser();
        RetrievePasswordRequestDto retrievePasswordRequestDto = new RetrievePasswordRequestDto();
        retrievePasswordRequestDto.setPassword("123abc");
        retrievePasswordRequestDto.setPhoneNum("13900000000");
        retrievePasswordRequestDto.setValidateCode("123456");
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(CaptchaType.class))).thenReturn(false);
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(myShaPasswordEncoder.encodePassword(anyString(),anyString())).thenReturn("123456789");
        BaseResponseDto baseResponseDto = mobileAppRetrievePasswordService.retrievePassword(retrievePasswordRequestDto);
        assertEquals(ReturnMessage.SMS_CAPTCHA_ERROR.getCode(),baseResponseDto.getCode());
    }

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


    private UserModel getFakeUser(){
        UserModel user1 = new UserModel();
        user1.setId(idGenerator.generate());
        user1.setLoginName("test1");
        user1.setPassword("123");
        user1.setMobile("13900000000");
        user1.setRegisterTime(new Date());
        user1.setLastModifiedTime(new Date());
        user1.setStatus(UserStatus.ACTIVE);
        user1.setSalt("123");
        return user1;
    }
}
