package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppUserBillListController;
import com.tuotiansudai.api.dto.v1_0.UserBillDetailListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserBillListService;
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
