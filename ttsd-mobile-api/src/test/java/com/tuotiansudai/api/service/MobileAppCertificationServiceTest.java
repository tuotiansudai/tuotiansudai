package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppCertificationServiceImpl;
import com.tuotiansudai.api.service.impl.MobileAppRegisterServiceImpl;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppCertificationServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppCertificationServiceImpl mobileAppCertificationService;
    @Mock
    private UserService userService;

    @Test
    public void shouldValidateUserCertificationInfoIsOk(){
        CertificationRequestDto certificationRequestDto = new CertificationRequestDto();
        certificationRequestDto.setUserIdCardNumber("123456789012345678");
        certificationRequestDto.setUserRealName("拓天");
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setCode("0000");
        payDataDto.setMessage("");
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(true);

        when(userService.registerAccount(any(RegisterAccountDto.class))).thenReturn(baseDto);

        BaseResponseDto<CertificationResponseDataDto> baseResponseDto = mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
        assertEquals("123456789012345678",baseResponseDto.getData().getUserIdCardNumber());

    }

    @Test
    public void shouldValidateUserCertificationInfoIsFailed(){
        CertificationRequestDto certificationRequestDto = new CertificationRequestDto();
        certificationRequestDto.setUserIdCardNumber("123456789012345678");
        certificationRequestDto.setUserRealName("拓天");
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setCode("0001");
        payDataDto.setMessage("");
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(false);

        when(userService.registerAccount(any(RegisterAccountDto.class))).thenReturn(baseDto);

        BaseResponseDto<CertificationResponseDataDto> baseResponseDto = mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
        assertEquals("0001",baseResponseDto.getCode());

    }
}
