package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppNodeDetailController;
import com.tuotiansudai.api.dto.v1_0.NodeDetailRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNodeDetailService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppNodeDetailControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppNodeDetailController controller;

    @Mock
    private MobileAppNodeDetailService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryLoanDetailIsOk() throws Exception {

        when(service.generateNodeDetail(any(NodeDetailRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/node", new NodeDetailRequestDto());
    }

}
