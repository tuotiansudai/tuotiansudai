package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.NoPasswordInvestTurnOffRequestDto;
import com.tuotiansudai.api.service.MobileAppNoPasswordInvestTurnOffService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


public class MobileAppNoPasswordInvestTurnOffControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppNoPasswordInvestTurnOffController controller;
    @Mock
    private MobileAppNoPasswordInvestTurnOffService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldNoPasswordInvestTurnOffIsOk() throws Exception {
        when(service.noPasswordInvestTurnOff(any(NoPasswordInvestTurnOffRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/no-password-invest/turn-off", new BaseParamDto());
    }

}
