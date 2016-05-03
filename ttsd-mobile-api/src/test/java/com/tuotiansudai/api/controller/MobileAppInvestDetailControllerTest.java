package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppInvestDetailController;
import com.tuotiansudai.api.dto.v1_0.InvestDetailRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestDetailService;
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
