package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppHelpCenterSearchController;
import com.tuotiansudai.api.dto.v1_0.HelpCenterCategoryRequestDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterSearchRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppHelpCenterSearchService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppHelpCenterSearchResultControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppHelpCenterSearchController controller;

    @Mock
    private MobileAppHelpCenterSearchService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGenerateHelpCenterSearchRequestIsOk() throws Exception{
        when(service.getHelpCenterSearchResult(any(HelpCenterSearchRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/help-center-search", new HelpCenterCategoryRequestDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

}
