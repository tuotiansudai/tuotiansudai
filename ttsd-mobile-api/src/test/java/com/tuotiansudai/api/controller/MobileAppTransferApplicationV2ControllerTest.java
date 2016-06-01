package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v2_0.MobileAppTransferApplicationV2Controller;
import com.tuotiansudai.api.dto.v1_0.PaginationRequestDto;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.UserInvestListRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppTransferApplicationV2Service;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppTransferApplicationV2ControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppTransferApplicationV2Controller controller;

    @Mock
    private MobileAppTransferApplicationV2Service service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }


    @Test
    public void shouldGenerateTransferableInvestIsSuccess() throws Exception{
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode("0000");
        when(service.generateTransferableInvest(any(UserInvestListRequestDto.class))).thenReturn(baseResponseDto);

        doRequestWithV2ServiceMockedTest("/get/userinvests",
                new PaginationRequestDto());
    }

}
