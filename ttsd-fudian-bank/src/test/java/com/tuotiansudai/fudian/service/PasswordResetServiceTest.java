package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.request.PasswordResetRequestDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class PasswordResetServiceTest {

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Mock
    private SignatureHelper signatureHelper;

    @Mock
    private InsertMapper insertMapper;

    @Mock
    private UpdateMapper updateMapper;

    @Test
    public void resetSuccess() {
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((PasswordResetRequestDto) o).setOrderNo("111111");
                ((PasswordResetRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));

        PasswordResetRequestDto dto = passwordResetService.reset(Source.WEB, mockBankBaseDto());

        verify(this.signatureHelper, times(1)).sign(any(), any());
        verify(this.insertMapper, times(1)).insertPasswordReset(any(PasswordResetRequestDto.class));
        assertThat(dto.getOrderNo(), is("111111"));
    }

    @Test
    public void resetFail() {
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((PasswordResetRequestDto) o).setOrderNo("111111");
                ((PasswordResetRequestDto) o).setRequestData(null);
                return false;
            }
        }));

        PasswordResetRequestDto dto = passwordResetService.reset(Source.WEB, mockBankBaseDto());

        verify(this.signatureHelper, times(1)).sign(any(), any());
        verify(this.insertMapper, times(0)).insertPasswordReset(any(PasswordResetRequestDto.class));
        assertNull(dto);
    }

    @Test
    public void notifyCallbackSuccess(){
        String responseData = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02724627346051001\",\"extMark\":\"{\\\"loginName\\\":\\\"loginName\\\",\\\"mobile\\\":\\\"18612801708\\\"}\",\"merchantNo\":\"M02689149095591001\",\"notifyUrl\":\"http://39.107.217.19:10003/callback/notify-url/register\",\"orderDate\":\"20180820\",\"orderNo\":\"20180820000000001351\",\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/register\",\"userName\":\"UU02724627342471001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        ResponseDto responseDto = passwordResetService.notifyCallback(responseData);
        verify(updateMapper, times(1)).updateNotifyResponseData(anyString(), any(ResponseDto.class));
        assertTrue(responseDto.isSuccess());
    }

    private BankBaseDto mockBankBaseDto() {
        return new BankBaseDto("loginName", "11111111111", "bankUserName", "bankAccountNo");
    }
}
