package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppTransferApplicationService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppTransferApplicationControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppTransferApplicationController controller;

    @Mock
    private MobileAppTransferApplicationService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGenerateTransferApplicationIsSuccess() throws Exception{
        when(service.generateTransferApplication(any(TransferApplicationRequestDto.class))).thenReturn(successResponseDto);

        doRequestWithServiceMockedTest("/get/transferrer-transfer-application-list",
                new TransferApplicationRequestDto());
    }
    @Test
    public void shouldGenerateTransfereeApplicationIsSuccess() throws Exception{
        when(service.generateTransfereeApplication(any(PaginationRequestDto.class))).thenReturn(successResponseDto);

        doRequestWithServiceMockedTest("/get/transferee-transfer-application-list",
                new PaginationRequestDto());
    }

    @Test
    public void shouldTransferApplyIsBadRequest() throws Exception{
        TransferApplyRequestDto transferApplyRequestDto =new TransferApplyRequestDto();
        transferApplyRequestDto.setTransferInvestId("123");
        when(service.transferApply(any(TransferApplyRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/transfer-apply", transferApplyRequestDto)
                .andExpect(jsonPath("$.code").value("0023"));
    }

    @Test
    public void shouldTransferApplyIsOk() throws Exception{
        TransferApplyRequestDto transferApplyRequestDto =new TransferApplyRequestDto();
        transferApplyRequestDto.setTransferAmount("1.00");
        transferApplyRequestDto.setTransferInvestId("123");
        when(service.transferApply(any(TransferApplyRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/transfer-apply", transferApplyRequestDto);
    }


    @Test
    public void shouldTransferApplyQueryIsOk() throws Exception{
        TransferApplyQueryRequestDto transferApplyQueryRequestDto = new TransferApplyQueryRequestDto();
        transferApplyQueryRequestDto.setInvestId("123");
        when(service.transferApplyQuery(any(TransferApplyQueryRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/transfer-apply", transferApplyQueryRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldTransferApplicationCancelIsOk() throws Exception{
        TransferCancelRequestDto transferCancelRequestDto = new TransferCancelRequestDto();
        transferCancelRequestDto.setTransferApplicationId(1000000L);
        when(service.transferApplicationCancel(any(TransferCancelRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/transfer-cancel", transferCancelRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldTransferPurchaseIsOk() throws Exception {
        TransferPurchaseRequestDto transferPurchaseRequestDto = new TransferPurchaseRequestDto();
        transferPurchaseRequestDto.setTransferApplicationId("123");
        when(service.transferPurchase(any(TransferPurchaseRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/transfer-purchase", transferPurchaseRequestDto);
    }

    @Test
    public void shouldTransferApplicationListIsOk() throws Exception{
        TransferApplicationListRequestDto transferApplicationListRequestDto = new TransferApplicationListRequestDto();
        when(service.transferApplicationList(any(TransferApplicationListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/transfer-application-list", transferApplicationListRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldTransferApplicationDetailIsOk() throws Exception{
        TransferApplicationDetailRequestDto transferApplicationDetailRequestDto = new TransferApplicationDetailRequestDto();
        when(service.transferApplicationById(any(TransferApplicationDetailRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/transfer-application", transferApplicationDetailRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

}
