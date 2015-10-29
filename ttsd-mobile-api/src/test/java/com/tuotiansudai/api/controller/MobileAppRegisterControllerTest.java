package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.SendSmsRequestDto;
import com.tuotiansudai.api.service.MobileAppRegisterService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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
}
