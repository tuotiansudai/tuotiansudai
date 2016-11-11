package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppUserTransferInvestRepayController;
import com.tuotiansudai.api.dto.v1_0.UserTransferInvestRepayRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserTransferInvestRepayService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


public class MobileAppUserTransferInvestRepayControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppUserTransferInvestRepayController controller;
    @Mock
    private MobileAppUserTransferInvestRepayService service;


    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldUserInvestRepayIsOk() throws Exception {
        when(service.userTransferInvestRepay(any(UserTransferInvestRepayRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/transfer-invest-repay", new UserTransferInvestRepayRequestDto());
    }

}
