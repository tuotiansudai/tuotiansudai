package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.NodeDetailRequestDto;
import com.tuotiansudai.api.dto.NodeListRequestDto;
import com.tuotiansudai.api.service.MobileAppNodeDetailService;
import com.tuotiansudai.api.service.MobileAppNodeListService;
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
