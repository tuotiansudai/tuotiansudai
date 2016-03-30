package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.TransferApplyRequestDto;
import com.tuotiansudai.api.service.MobileAppTransferApplyService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppTransferApplyControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppTransferApplyController controller;

    @Mock
    private MobileAppTransferApplyService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldTransferApplyIsBadRequest() throws Exception{
        TransferApplyRequestDto transferApplyRequestDto =new TransferApplyRequestDto();
        transferApplyRequestDto.setTransferInterest(true);
        transferApplyRequestDto.setTransferInvestId("123");
        when(service.transferApply(any(TransferApplyRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/transfer-apply", transferApplyRequestDto)
                .andExpect(jsonPath("$.code").value("0023"));
    }

    @Test
    public void shouldTransferApplyIsOk() throws Exception{
        TransferApplyRequestDto transferApplyRequestDto =new TransferApplyRequestDto();
        transferApplyRequestDto.setTransferInterest(true);
        transferApplyRequestDto.setTransferAmount("1.00");
        transferApplyRequestDto.setTransferInvestId("123");
        when(service.transferApply(any(TransferApplyRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/transfer-apply", transferApplyRequestDto);
    }

}
