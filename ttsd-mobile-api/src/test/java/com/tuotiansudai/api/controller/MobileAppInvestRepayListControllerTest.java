package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.InvestRepayListRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestRepayListService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppInvestRepayListControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppInvestRepayListController controller;

    @Mock
    private MobileAppInvestRepayListService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryInvestListIsOk() throws Exception {
        when(service.generateUserInvestRepayList(any(InvestRepayListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/investrepays",
                new InvestRepayListRequestDto());
    }
}
