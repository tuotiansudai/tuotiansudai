package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BankCardRequestDto;
import com.tuotiansudai.api.dto.WithdrawOperateRequestDto;
import com.tuotiansudai.api.service.MobileAppRechargeService;
import com.tuotiansudai.api.service.MobileAppWithdrawService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppWithdrawControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppWithdrawController controller;
    @Mock
    private MobileAppWithdrawService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldWithdrawIsOk() throws Exception {
        when(service.generateWithdrawRequest(any(WithdrawOperateRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/withdraw",
                new BankCardRequestDto());
    }


}
