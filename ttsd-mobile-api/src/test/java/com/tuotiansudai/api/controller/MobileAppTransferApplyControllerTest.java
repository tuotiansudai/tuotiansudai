package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.TransferApplicationRequestDto;
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
        TransferApplicationRequestDto transferApplicationRequestDto =new TransferApplicationRequestDto();
        transferApplicationRequestDto.setTransferInterest(true);
        transferApplicationRequestDto.setTransferInvestId("123");
        when(service.transferApply(any(TransferApplicationRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/transfer-apply", transferApplicationRequestDto)
                .andExpect(jsonPath("$.code").value("0023"));
    }

    @Test
    public void shouldTransferApplyIsOk() throws Exception{
        TransferApplicationRequestDto transferApplicationRequestDto =new TransferApplicationRequestDto();
        transferApplicationRequestDto.setTransferInterest(true);
        transferApplicationRequestDto.setTransferAmount("1.00");
        transferApplicationRequestDto.setTransferInvestId("123");
        when(service.transferApply(any(TransferApplicationRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/transfer-apply", transferApplicationRequestDto);
    }

}
