package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppMembershipPerceptionController;
import com.tuotiansudai.api.dto.v1_0.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPerceptionService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppMembershipPerceptionControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppMembershipPerceptionController controller;

    @Mock
    private MobileAppMembershipPerceptionService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetMembershipPerceptionIsOk() throws Exception{
        when(service.getMembershipPerception(any(MembershipPerceptionRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/membership-perception", new AgreementOperateRequestDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

}
