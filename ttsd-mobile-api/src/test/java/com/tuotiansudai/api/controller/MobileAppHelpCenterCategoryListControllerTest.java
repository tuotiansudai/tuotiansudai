package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppHelpCenterCategoryListController;
import com.tuotiansudai.api.dto.v1_0.HelpCenterCategoryRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppHelpCenterCategoryService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppHelpCenterCategoryListControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppHelpCenterCategoryListController controller;

    @Mock
    private MobileAppHelpCenterCategoryService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGenerateHelpCenterCategoryListIsOk() throws Exception{
        when(service.generateHelpCenterCategoryList(any(HelpCenterCategoryRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/help-center-category-list", new HelpCenterCategoryRequestDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

}
