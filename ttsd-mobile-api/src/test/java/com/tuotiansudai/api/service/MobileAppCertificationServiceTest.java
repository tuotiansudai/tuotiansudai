package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CertificationRequestDto;
import com.tuotiansudai.api.dto.v1_0.CertificationResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppCertificationServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppCertificationServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppCertificationServiceImpl mobileAppCertificationService;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountService accountService;

    @Test
    public void shouldValidateUserCertificationInfoIsOk() {
        CertificationRequestDto certificationRequestDto = new CertificationRequestDto();
        certificationRequestDto.setUserIdCardNumber("370405199112286014");
        certificationRequestDto.setUserRealName("拓天");
        certificationRequestDto.setBaseParam(BaseParamTest.getInstance());
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setCode("0000");
        payDataDto.setMessage("");
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(true);

        when(accountService.registerAccount(any(RegisterAccountDto.class))).thenReturn(baseDto);

        BaseResponseDto<CertificationResponseDataDto> baseResponseDto = mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals("370405199112286014", baseResponseDto.getData().getUserIdCardNumber());

    }

    @Test
    public void shouldValidateUserCertificationInfoIsFailed() {
        CertificationRequestDto certificationRequestDto = new CertificationRequestDto();
        certificationRequestDto.setUserIdCardNumber("123456789012345678");
        certificationRequestDto.setUserRealName("拓天");
        certificationRequestDto.setBaseParam(BaseParamTest.getInstance());
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setCode("0001");
        payDataDto.setMessage("");
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(false);

        when(accountMapper.findByLoginName(anyString())).thenReturn(null);
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(accountService.registerAccount(any(RegisterAccountDto.class))).thenReturn(baseDto);
        when(userService.isIdentityNumberExist(anyString())).thenReturn(false);
        BaseResponseDto<CertificationResponseDataDto> baseResponseDto = mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
        assertEquals("0016", baseResponseDto.getCode());

    }
}
