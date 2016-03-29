package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.TransferApplyQueryRequestDto;
import com.tuotiansudai.api.service.MobileAppAgreementService;
import com.tuotiansudai.api.service.MobileAppTransferApplyQueryService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppTransferApplyQueryControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppTransferApplyQueryController controller;

    @Mock
    private MobileAppTransferApplyQueryService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldTransferApplyQueryIsOk() throws Exception{
        TransferApplyQueryRequestDto transferApplyQueryRequestDto = new TransferApplyQueryRequestDto();
        transferApplyQueryRequestDto.setInvestId("123");
        when(service.transferApplyQuery(any(TransferApplyQueryRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/transfer-apply", transferApplyQueryRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

}
