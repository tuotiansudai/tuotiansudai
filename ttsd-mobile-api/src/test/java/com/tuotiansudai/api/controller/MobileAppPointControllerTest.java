package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.dto.PointBillRequestDto;
import com.tuotiansudai.api.dto.PointTaskRequestDto;
import com.tuotiansudai.api.service.MobileAppPointService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppPointControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppPointController mobileAppPointController;

    @Mock
    private MobileAppPointService mobileAppPointService;

    @Override
    protected Object getControllerObject() {
        return mobileAppPointController;
    }

    @Test
    public void shouldQueryPointBillIsOk() throws Exception{
        when(mobileAppPointService.queryPointBillList(any(PointBillRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/point-bill", new PointBillRequestDto());
    }
    @Test
    public void shouldQueryPointTaskIsOk() throws Exception{
        when(mobileAppPointService.queryPointTaskList(any(PointTaskRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/point-task", new PointTaskRequestDto());
    }
}
