package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppInvestController;
import com.tuotiansudai.api.dto.v1_0.InvestListRequestDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppInvestControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppInvestController controller;

    @Mock
    private MobileAppInvestService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void createInvestTest() throws Exception {
        when(service.invest(any(InvestRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/create/invest",
                new InvestListRequestDto());
    }
}
