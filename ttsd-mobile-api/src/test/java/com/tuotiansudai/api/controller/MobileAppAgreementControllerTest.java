package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppAgreementController;
import com.tuotiansudai.api.dto.v1_0.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppAgreementService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppAgreementControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppAgreementController controller;

    @Mock
    private MobileAppAgreementService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGenerateAgreementRequestIsOk() throws Exception{
        when(service.generateAgreementRequest(any(AgreementOperateRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/agreement", new AgreementOperateRequestDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

}
