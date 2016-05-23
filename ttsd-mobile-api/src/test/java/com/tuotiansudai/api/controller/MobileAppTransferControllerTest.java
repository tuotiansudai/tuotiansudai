package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.dto.TransferTransfereeRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.service.MobileAppTransferService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppTransferControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppTransferController mobileAppTransferController;

    @Override
    protected Object getControllerObject() {
        return mobileAppTransferController;
    }

    @Mock
    private MobileAppTransferService mobileAppTransferService;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGetTransfereeSuccess() throws Exception{
        TransferTransfereeRequestDto transferTransfereeRequestDto = new TransferTransfereeRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        transferTransfereeRequestDto.setBaseParam(baseParam);
        transferTransfereeRequestDto.setPageSize(10);
        transferTransfereeRequestDto.setIndex(1);
        transferTransfereeRequestDto.setTransferApplicationId(idGenerator.generate());
        when(mobileAppTransferService.getTransferee(any(TransferTransfereeRequestDto.class))).thenReturn(successResponseDto);

        doRequestWithServiceMockedTest("/get/transferee", transferTransfereeRequestDto);
    }

}
