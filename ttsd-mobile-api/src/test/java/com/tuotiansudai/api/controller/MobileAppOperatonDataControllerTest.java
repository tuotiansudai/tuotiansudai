package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppOperationDataController;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.service.v1_0.MobileAppOperationDataService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppOperatonDataControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppOperationDataController controller;

    @Mock
    private MobileAppOperationDataService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void generatorOperationData() throws Exception {
        when(service.generatorOperationData(any(BaseParamDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/operation-data",
                new BaseParamDto());
    }
}
