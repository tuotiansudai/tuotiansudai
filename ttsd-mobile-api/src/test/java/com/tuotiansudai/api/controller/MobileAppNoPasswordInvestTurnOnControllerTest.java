package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppNoPasswordInvestTurnOnController;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOnService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
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
        when(service.noPasswordInvestTurnOn(any(BaseParamDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/no-password-invest/turn-on", new BaseParamDto());
    }

}
