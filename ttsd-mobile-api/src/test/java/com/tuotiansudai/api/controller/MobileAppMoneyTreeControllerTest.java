package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.controller.v1_0.MobileAppMoneyTreeController;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppMoneyTreeService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppMoneyTreeControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppMoneyTreeController controller;

    @Mock
    private MobileAppMoneyTreeService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetMoneyTreeLeftChance() throws Exception {
        when(service.generateLeftCount(anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/money-tree-left-chance",new BaseParamDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldMoneyTreeAllPrizeList() throws Exception {
        when(service.generatePrizeListTop10()).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/money-tree-all-prize-list", new BaseParamDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldMoneyTreeMyPrizeList() throws Exception {
        when(service.generateMyPrizeList(anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/money-tree-my-prize-list", new BaseParamDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldMoneyTreePrize() throws Exception {
        when(service.generatePrize(anyString())).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/money-tree-prize", new BaseParamDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }



}
