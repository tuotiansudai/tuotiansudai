package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.InvestListRequestDto;
import com.tuotiansudai.api.dto.UserInvestListRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestListService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
        when(service.generateInvestList(any(InvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/invests",
                new InvestListRequestDto());
    }
    @Test
    public void shouldQueryInvestListIsBadRequest() throws Exception {
        InvestListRequestDto investListRequestDto = new InvestListRequestDto();
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        investListRequestDto.setLoanId("abc123");
        when(service.generateInvestList(any(InvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/invests",
                investListRequestDto);
    }

    @Test
    public void queryUserInvestList() throws Exception {
        when(service.generateUserInvestList(any(UserInvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/userinvests",
                new UserInvestListRequestDto());
    }
}
