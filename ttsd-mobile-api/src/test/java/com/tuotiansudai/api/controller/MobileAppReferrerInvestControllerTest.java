package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.dto.ReferrerInvestListRequestDto;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import com.tuotiansudai.api.service.MobileAppReferrerInvestService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppReferrerInvestControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppReferrerInvestListController controller;

    @Mock
    private MobileAppReferrerInvestService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryInvestListIsOk() throws Exception {
        ReferrerInvestListRequestDto referrerInvestListRequestDto = new ReferrerInvestListRequestDto();
        referrerInvestListRequestDto.setIndex(1);
        referrerInvestListRequestDto.setPageSize(10);
        referrerInvestListRequestDto.setReferrerId("loginName");
        when(service.generateReferrerInvestList(any(ReferrerInvestListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/referrerinvests",
                referrerInvestListRequestDto
        );
    }

}
