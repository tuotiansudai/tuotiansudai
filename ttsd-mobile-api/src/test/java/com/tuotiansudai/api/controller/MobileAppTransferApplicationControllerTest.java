package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.TransferApplicationRequestDto;
import com.tuotiansudai.api.dto.TransferApplyRequestDto;
import com.tuotiansudai.api.dto.UserBillDetailListRequestDto;
import com.tuotiansudai.api.service.MobileAppTransferApplicationService;
import com.tuotiansudai.api.service.MobileAppTransferApplyService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
    public void shoulGenerateTransferApplicationIsSuccess() throws Exception{
        when(service.generateTransferApplication(any(TransferApplicationRequestDto.class))).thenReturn(successResponseDto);

        doRequestWithServiceMockedTest("/get/transferrer-transfer-application-list",
                new TransferApplicationRequestDto());
    }



}
