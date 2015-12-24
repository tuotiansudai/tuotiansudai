package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ChangePasswordRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.impl.MobileAppChangePasswordServiceImpl;
import com.tuotiansudai.service.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppChangePasswordServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppChangePasswordServiceImpl mobileAppChangePasswordService;

    @Mock
    private UserService userService;

    @Test
    public void shouldChangePasswordSuccess() {
        when(userService.changePassword(anyString(), anyString(), anyString())).thenReturn(true);

        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        requestDto.setOriginPassword("123456");
        requestDto.setNewPassword("123abc");
        BaseResponseDto responseDto = mobileAppChangePasswordService.changePassword(requestDto);

        assertEquals(true, responseDto.isSuccess());
    }

    @Test
    public void shouldChangePasswordFail() {
        when(userService.changePassword(anyString(), anyString(), anyString())).thenReturn(false);

        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        requestDto.setOriginPassword("123456");
        requestDto.setNewPassword("123abc");
        BaseResponseDto responseDto = mobileAppChangePasswordService.changePassword(requestDto);

        assertEquals(false, responseDto.isSuccess());
        assertEquals(ReturnMessage.CHANGEPASSWORD_INVALID_PASSWORD.getCode(), responseDto.getCode());
    }
}
