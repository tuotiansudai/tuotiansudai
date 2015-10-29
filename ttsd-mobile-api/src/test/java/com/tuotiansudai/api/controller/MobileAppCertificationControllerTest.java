package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MobileAppCertificationControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppCertificationController controller;

    @Mock
    private MobileAppCertificationService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void userMobileCertification() throws Exception {
        doRequestWithServiceMockedTest("/certificate",
                new CertificationRequestDto()
        );
    }
}
