package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppNodeListController;
import com.tuotiansudai.api.dto.v1_0.NodeListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNodeListService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppNodeListControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppNodeListController controller;

    @Mock
    private MobileAppNodeListService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryLoanListIsOk() throws Exception {

        when(service.generateNodeList(any(NodeListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/nodes", new NodeListRequestDto());
    }

}
