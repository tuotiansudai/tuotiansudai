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
    public void queryInvestList() throws Exception {
        when(service.generateInvestList(any(InvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/invests",
                new InvestListRequestDto());
    }

    @Test
    public void queryUserInvestList() throws Exception {
        when(service.generateUserInvestList(any(UserInvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/userinvests",
                new UserInvestListRequestDto());
    }
}
