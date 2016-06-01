package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppLoanListController;
import com.tuotiansudai.api.dto.v1_0.LoanListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppLoanListService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


public class MobileAppLoanListControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppLoanListController controller;
    @Mock
    private MobileAppLoanListService service;


    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryLoanListIsOk() throws Exception {
        when(service.generateLoanList(any(LoanListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/loans",
                new LoanListRequestDto());
    }

}
