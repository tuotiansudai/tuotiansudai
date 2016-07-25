package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.controller.v1_0.MobileAppMembershipController;
import com.tuotiansudai.api.dto.v1_0.MembershipRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppMembershipControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppMembershipController controller;

    @Mock
    private MobileAppMembershipService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetMembershipExperienceBill() throws Exception{
        when(service.getMembershipExperienceBill(any(MembershipRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/membership-experience-bill", new MembershipRequestDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }
}
