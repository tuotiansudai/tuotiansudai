package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.dto.RechargeListRequestDto;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import com.tuotiansudai.api.service.MobileAppRechargeListService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppRechargeListControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppRechargeListController controller;

    @Mock
    private MobileAppRechargeListService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldUserMobileCertificationIsOk() throws Exception {
        RechargeListRequestDto rechargeListRequestDto = new RechargeListRequestDto();
        rechargeListRequestDto.setPageSize(10);
        rechargeListRequestDto.setIndex(1);
        when(service.generateRechargeList(any(RechargeListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/userrecharges",
                rechargeListRequestDto
        );
    }

}
