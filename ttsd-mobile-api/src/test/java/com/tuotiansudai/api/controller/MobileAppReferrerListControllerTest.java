package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.ReferrerListRequestDto;
import com.tuotiansudai.api.service.MobileAppReferrerListService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


public class MobileAppReferrerListControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppReferrerListController controller;

    @Mock
    private MobileAppReferrerListService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldQueryInvestListIsOk() throws Exception {
        ReferrerListRequestDto referrerListRequestDto = new ReferrerListRequestDto();
        referrerListRequestDto.setIndex(1);
        referrerListRequestDto.setPageSize(2);
        referrerListRequestDto.setReferrerId("loginName");
        when(service.generateReferrerList(any(ReferrerListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/referrers",referrerListRequestDto);
    }

}
