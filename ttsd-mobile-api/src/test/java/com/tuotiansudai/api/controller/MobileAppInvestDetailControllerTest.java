package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.InvestDetailRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestDetailService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppInvestDetailControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppInvestDetailController controller;

    @Mock
    private MobileAppInvestDetailService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void queryUserInvestList() throws Exception {
        when(service.generateUserInvestDetail(any(InvestDetailRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/userinvest",
                new InvestDetailRequestDto());
    }
}
