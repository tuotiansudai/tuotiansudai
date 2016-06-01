package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppChangePasswordController;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.ChangePasswordRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppChangePasswordService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppChangePasswordControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppChangePasswordController controller;

    @Mock
    private MobileAppChangePasswordService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void changePasswordIsOk() throws Exception {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        requestDto.setOriginPassword("123");
        requestDto.setNewPassword("123abc");
        when(service.changePassword(any(ChangePasswordRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/changepassword", requestDto);
    }

    @Test
    public void changePasswordValidFail() throws Exception {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        requestDto.setOriginPassword("123");
        requestDto.setNewPassword("12bc");
        when(service.changePassword(any(ChangePasswordRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceIsOkMockedTest("/changepassword", requestDto)
                .andExpect(jsonPath("$.code").value(ReturnMessage.PASSWORD_IS_INVALID.getCode()));
    }
}
