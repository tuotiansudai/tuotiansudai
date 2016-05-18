package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.controller.v2_0.MobileAppHomeV2Controller;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanListV2Service;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppHomeV2ControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppHomeV2Controller controller;
    @Mock
    private MobileAppLoanListV2Service service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryLoanListIsOk() throws Exception {
        when(service.generateIndexLoan(any(BaseParamDto.class))).thenReturn(null);
        //LoanListRequestDto requestDto = new LoanListRequestDto();
        //doRequestWithServiceMockedTest("/get/index", requestDto);
    }
}
