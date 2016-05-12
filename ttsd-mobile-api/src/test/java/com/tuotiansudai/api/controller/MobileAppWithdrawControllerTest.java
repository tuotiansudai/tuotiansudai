package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppWithdrawController;
import com.tuotiansudai.api.dto.v1_0.WithdrawListRequestDto;
import com.tuotiansudai.api.dto.v1_0.WithdrawOperateRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppWithdrawService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppWithdrawControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppWithdrawController controller;
    @Mock
    private MobileAppWithdrawService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGenerateWithdrawRequestIsOk() throws Exception {
        when(service.generateWithdrawRequest(any(WithdrawOperateRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/withdraw",
                new WithdrawOperateRequestDto());
    }

    @Test
    public void shouldQueryUserWithdrawLogsIsOk() throws Exception {
        when(service.queryUserWithdrawLogs(any(WithdrawListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/userwithdrawlogs",
                new WithdrawListRequestDto());
    }



}
