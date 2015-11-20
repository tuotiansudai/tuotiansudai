package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.InvestListRequestDto;
import com.tuotiansudai.api.dto.InvestRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestService;
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
