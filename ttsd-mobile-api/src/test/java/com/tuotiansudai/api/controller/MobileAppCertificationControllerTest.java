package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppCertificationController;
import com.tuotiansudai.api.dto.v1_0.CertificationRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppCertificationControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppCertificationController controller;

    @Mock
    private MobileAppCertificationService service;

    @Mock
    private MyAuthenticationUtil myAuthenticationUtil;

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
        when(myAuthenticationUtil.createAuthentication(anyString(), any(Source.class))).thenReturn("token");
        doRequestWithServiceMockedTest("/certificate", certificationRequestDto);
    }
    @Test
    public void shouldUserMobileCertificationIsBadRequest() throws Exception {
        CertificationRequestDto requestDto = new CertificationRequestDto();
        requestDto.setUserRealName("拓天");
        doRequestWithServiceIsOkMockedTest("/certificate",requestDto)
                .andExpect(jsonPath("$.code").value("0013"));
    }
}
