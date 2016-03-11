package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.PointBillRequestDto;
import com.tuotiansudai.api.dto.PointExchangeRequestDto;
import com.tuotiansudai.api.service.MobileAppPointExchangeListService;
import com.tuotiansudai.api.service.MobileAppPointExchangeService;
import com.tuotiansudai.api.dto.PointExchangeListRequestDto;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by gengbeijun on 16/3/11.
 */
public class MobileAppPointExchangeListControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppPointExchangeListController mobileAppPointExchangeListController;
    @Mock
    private MobileAppPointExchangeListService mobileAppPointExchangeListService;
    @Mock
    private MobileAppPointExchangeService mobileAppPointExchangeService;

    @Override
    protected Object getControllerObject() {
        return mobileAppPointExchangeListController;
    }

    @Test
    public void shouldQueryPointExchangeListIsOk() throws Exception{
        when(mobileAppPointExchangeListService.generatePointExchangeList(any(PointExchangeListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/point-exchange", new PointExchangeListRequestDto());
    }

    @Test
    public void shouldPointExchangeIsOk() throws Exception{
        when(mobileAppPointExchangeService.generatePointExchange(any(PointExchangeRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/point-exchange", new PointExchangeRequestDto());
    }

}
