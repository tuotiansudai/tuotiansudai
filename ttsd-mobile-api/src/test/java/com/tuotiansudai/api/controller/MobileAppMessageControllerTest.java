package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.controller.v1_0.MobileAppMessageController;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserMessagesRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppMessageControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppMessageController controller;

    @Mock
    private MobileAppUserMessageService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetMessages() throws Exception {
        when(service.getUserMessages(any(UserMessagesRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/messages", new UserMessagesRequestDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldGetUnreadMessageCount() throws Exception {
        when(service.getUnreadMessageCount(any(BaseParamDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/unread-message-count", new BaseParamDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

}
