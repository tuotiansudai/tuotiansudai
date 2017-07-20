package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppCurrentInvestController;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.CurrentInvestRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentInvestService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppCurrentInvestControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppCurrentInvestController controller;

    @Mock
    private MobileAppCurrentInvestService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldInvest() throws Exception {
        when(service.invest(any(CurrentInvestRequestDto.class), anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/rxb/invest",
                new CurrentInvestRequestDto());
    }


    @Test
    public void shouldNoPasswordInvest() throws Exception {
        when(service.noPasswordInvest(any(CurrentInvestRequestDto.class), anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/rxb/no-password-invest",
                new CurrentInvestRequestDto());
    }
}
