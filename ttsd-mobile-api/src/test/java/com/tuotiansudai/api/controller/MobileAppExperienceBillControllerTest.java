package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppExperienceBillController;
import com.tuotiansudai.api.dto.v1_0.ExperienceBillRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppExperienceBillService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppExperienceBillControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppExperienceBillController mobileAppExperienceBillController;

    @Mock
    private MobileAppExperienceBillService mobileAppExperienceBillService;

    @Override
    protected Object getControllerObject() {
        return mobileAppExperienceBillController;
    }

    @Test
    public void shouldQueryExperienceBillIsOk() throws Exception{
        when(mobileAppExperienceBillService.queryExperienceBillList(any(ExperienceBillRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/experience-bill", new ExperienceBillRequestDto());
    }
}
