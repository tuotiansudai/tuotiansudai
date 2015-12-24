package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.RegisterRequestDto;
import com.tuotiansudai.api.dto.SendSmsRequestDto;
import com.tuotiansudai.api.service.MobileAppRegisterService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppRegisterControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppRegisterController controller;

    @Mock
    private MobileAppRegisterService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void sendRegisterByMobileNumberSMS() throws Exception {
        when(service.sendRegisterByMobileNumberSMS(anyString(), anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/register/sendsms",
                new SendSmsRequestDto());
    }

    @Test
    public void shouldRegisterUserIsOk() throws Exception {
        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        when(service.registerUser(any(RegisterRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/register",
                registerRequestDto);
    }

    @Test
    public void shouldRegisterUserMobileIsValid() throws Exception {
        RegisterRequestDto registerRequestDto = getFakeRegisterRequestDto();
        registerRequestDto.setPhoneNum("1390000000k");
        when(service.registerUser(any(RegisterRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/register",
                registerRequestDto)
                .andExpect(jsonPath("$.code").value("0002"));
    }

    public RegisterRequestDto getFakeRegisterRequestDto(){
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUserName("loginName");
        registerRequestDto.setCaptcha("123456");
        registerRequestDto.setPassword("123ssword");
        registerRequestDto.setPhoneNum("13900000000");
        return registerRequestDto;
    }
}
