package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.FundManagementRequestDto;
import com.tuotiansudai.api.service.MobileAppFundManagementService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


public class MobileAppFundManagementControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppFundManagementController controller;
    @Mock
    private MobileAppFundManagementService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void queryFundManagement() throws Exception {
        when(service.queryFundByUserId(anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/userfund",
                new FundManagementRequestDto()
        );
    }

}
