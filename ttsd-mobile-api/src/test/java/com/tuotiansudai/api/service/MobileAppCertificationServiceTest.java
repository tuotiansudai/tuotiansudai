package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.dto.CertificationResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.impl.MobileAppCertificationServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.service.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
