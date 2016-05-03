package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppNoPasswordInvestController;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


public class MobileAppNoPasswordInvestControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppNoPasswordInvestController controller;
    @Mock
    private MobileAppNoPasswordInvestService service;


    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetNoPasswordInvestDataIsOk() throws Exception {
        when(service.getNoPasswordInvestData(any(BaseParamDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/no-password-invest",
                new BaseParamDto());
    }

}
