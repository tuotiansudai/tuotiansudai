package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppRetrievePasswordService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.bind.annotation.RequestBody;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppRetrievePasswordControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppRetrievePasswordController controller;

    @Mock
    private MobileAppRetrievePasswordService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void retrievePasswordIsOk() throws Exception {
        RetrievePasswordRequestDto retrievePasswordRequestDto = new RetrievePasswordRequestDto();
        retrievePasswordRequestDto.setPassword("123abc");
        retrievePasswordRequestDto.setValidateCode("123456");
        retrievePasswordRequestDto.setPhoneNum("12345678900");
        when(service.retrievePassword(any(RetrievePasswordRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/retrievepassword",
                retrievePasswordRequestDto);
    }

    @Test
    public void retrievePasswordIsValid() throws Exception {
        RetrievePasswordRequestDto retrievePasswordRequestDto = new RetrievePasswordRequestDto();
        retrievePasswordRequestDto.setPhoneNum("13800138000");
        retrievePasswordRequestDto.setValidateCode("123456");
        retrievePasswordRequestDto.setPassword("000000");
        when(service.retrievePassword(any(RetrievePasswordRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/retrievepassword",
                retrievePasswordRequestDto)
                .andExpect(jsonPath("$.code").value("0012"));
    }

    @Test
    public void validateAuthCode() throws Exception {
        when(service.validateAuthCode(any(RetrievePasswordRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/validatecaptcha",
                new RetrievePasswordRequestDto());
    }

    @Test
    public void sendSMS() throws Exception {
        when(service.sendSMS(any(RetrievePasswordRequestDto.class), anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/retrievepassword/sendsms",
                new RetrievePasswordRequestDto());
    }
}
