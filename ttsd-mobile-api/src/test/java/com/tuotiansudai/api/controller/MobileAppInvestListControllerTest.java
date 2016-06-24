package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppInvestListController;
import com.tuotiansudai.api.dto.v1_0.InvestListRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestListService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppInvestListControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppInvestListController controller;

    @Mock
    private MobileAppInvestListService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryInvestListIsOk() throws Exception {
        InvestListRequestDto investListRequestDto = new InvestListRequestDto();
        investListRequestDto.setLoanId("12345678");
        when(service.generateInvestList(any(InvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/invests",
                investListRequestDto);
    }
    @Test
    public void shouldQueryInvestListIsBadRequest() throws Exception {
        InvestListRequestDto investListRequestDto = new InvestListRequestDto();
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        investListRequestDto.setLoanId("abc123");
        when(service.generateInvestList(any(InvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/get/invests",
                investListRequestDto).andExpect(jsonPath("$.code").value("0023"));
    }

    @Test
    public void queryUserInvestList() throws Exception {
        when(service.generateUserInvestList(any(UserInvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/userinvests",
                new UserInvestListRequestDto());
    }
}
