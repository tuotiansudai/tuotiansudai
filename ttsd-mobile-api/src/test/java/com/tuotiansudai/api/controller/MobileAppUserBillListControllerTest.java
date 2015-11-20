package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;
import com.tuotiansudai.api.dto.UserBillDetailListRequestDto;
import com.tuotiansudai.api.service.MobileAppUserBillListService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppUserBillListControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppUserBillListController controller;

    @Mock
    private MobileAppUserBillListService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }
    @Test
    public void shouldQueryFundManagementIsOk() throws Exception {
        when(service.queryUserBillList(any(UserBillDetailListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/userbills",
                new UserBillDetailListRequestDto());
    }


}
