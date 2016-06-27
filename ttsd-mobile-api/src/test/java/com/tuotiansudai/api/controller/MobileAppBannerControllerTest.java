package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppBannerController;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.service.v1_0.MobileAppBannerService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppBannerControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppBannerController controller;

    @Mock
    private MobileAppBannerService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void getBannerTest() throws Exception {
        when(service.generateBannerList(any(BaseParam.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/banner");
    }
}
