package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.LoanListRequestDto;
import com.tuotiansudai.api.service.MobileAppLoanListService;
import com.tuotiansudai.api.service.MobileAppNoPasswordInvestService;
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
