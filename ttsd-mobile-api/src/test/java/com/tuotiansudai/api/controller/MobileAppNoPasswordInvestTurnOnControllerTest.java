package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.service.MobileAppNoPasswordInvestTurnOnService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


public class MobileAppNoPasswordInvestTurnOnControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppNoPasswordInvestTurnOnController controller;
    @Mock
    private MobileAppNoPasswordInvestTurnOnService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldNoPasswordInvestTurnOnIsOk() throws Exception {
        when(service.noPasswordInvestTurnOn(any(BaseParamDto.class), anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/no-password-invest/turn-on", new BaseParamDto());
    }

}
