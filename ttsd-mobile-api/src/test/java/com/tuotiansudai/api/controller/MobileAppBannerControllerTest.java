package com.tuotiansudai.api.controller;

import org.junit.Test;
import org.mockito.InjectMocks;

public class MobileAppBannerControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppBannerController controller;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void getBannerTest() throws Exception {
        doRequestWithServiceMockedTest("/get/banner");
    }
}
