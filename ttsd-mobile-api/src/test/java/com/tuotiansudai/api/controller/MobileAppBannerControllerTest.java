package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.service.MobileAppBannerService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
        when(service.generateBannerList()).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/banner");
    }
}
