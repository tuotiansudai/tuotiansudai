package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.dto.RegisterRequestDto;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    public void shouldUserMobileCertificationIsOk() throws Exception {
        CertificationRequestDto certificationRequestDto = new CertificationRequestDto();
        certificationRequestDto.setUserIdCardNumber("123456789012345678");
        certificationRequestDto.setUserRealName("拓天");
        when(service.validateUserCertificationInfo(any(CertificationRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/certificate",
                certificationRequestDto
        );
    }
    @Test
    public void shouldUserMobileCertificationIsBadRequest() throws Exception {
        CertificationRequestDto requestDto = new CertificationRequestDto();
        requestDto.setUserRealName("拓天");
        doRequestWithServiceIsOkMockedTest("/certificate",requestDto)
                .andExpect(jsonPath("$.code").value("0013"));
    }
}
