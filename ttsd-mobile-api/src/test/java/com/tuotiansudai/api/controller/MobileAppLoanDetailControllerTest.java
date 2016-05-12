package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppLoanDetailController;
import com.tuotiansudai.api.dto.v1_0.LoanDetailRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppLoanDetailService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class MobileAppLoanDetailControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppLoanDetailController controller;
    @Mock
    private MobileAppLoanDetailService service;


    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryLoanListIsOk() throws Exception {
        LoanDetailRequestDto loanDetailRequestDto = new LoanDetailRequestDto();
        loanDetailRequestDto.setLoanId("1111");
        when(service.generateLoanDetail(any(LoanDetailRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/loan",
                loanDetailRequestDto);
    }
    @Test
    public void shouldQueryLoanListIsBadRequest() throws Exception {
        LoanDetailRequestDto loanDetailRequestDto = new LoanDetailRequestDto();
        loanDetailRequestDto.setLoanId("111a");
        when(service.generateLoanDetail(any(LoanDetailRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/get/loan",
                loanDetailRequestDto)
                .andExpect(jsonPath("$.code").value("0024"));
    }

}
