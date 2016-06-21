package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppRechargeController;
import com.tuotiansudai.api.dto.v1_0.BankCardRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppRechargeControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppRechargeController controller;
    @Mock
    private MobileAppRechargeService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldRechargeIsOk() throws Exception {
        when(service.recharge(any(BankCardRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/recharge",
                new BankCardRequestDto());
    }


}
