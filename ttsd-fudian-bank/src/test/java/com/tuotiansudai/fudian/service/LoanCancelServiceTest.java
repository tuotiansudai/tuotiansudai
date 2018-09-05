package com.tuotiansudai.fudian.service;

/**
 * Created by qduljs2011 on 2018/8/30.
 */

import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanCancelDto;
import com.tuotiansudai.fudian.dto.request.LoanCancelRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanCreateRequestDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanCancelMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class LoanCancelServiceTest {
    @InjectMocks
    private LoanCancelService loanCancelService;
    @Mock
    private MessageQueueClient messageQueueClient;
    @Mock
    private SignatureHelper signatureHelper;
    @Mock
    private BankClient bankClient;
    @Mock
    private InsertMapper insertMapper;
    @Mock
    private UpdateMapper updateMapper;

    @Test
    public void cancelSuccess() {
        ArgumentCaptor<LoanCancelRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanCancelRequestDto.class);
        String responseStr = "{\"certInfo\":\"certInfo\",\"content\":{\"extMark\":\"\",\"loanOrderDate\":\"20180710\",\"loanOrderNo\":\"20180710000000000979\",\"loanTxNo\":\"LU02689298262601001\",\"merchantNo\":\"M02608959047521001\",\"orderDate\":\"20180710\",\"orderNo\":\"20180710000000007393\",\"status\":\"5\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<LoanCancelRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanCancelRequestDto) o).setOrderNo("111111");
                ((LoanCancelRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(signatureHelper.verifySign(eq(responseStr))).thenReturn(true);
        when(bankClient.send(any(ApiType.class), anyString())).thenReturn(responseStr);

        BankLoanCancelMessage bankLoanCancelMessage = loanCancelService.cancel(new BankLoanCancelDto());

        verify(insertMapper, times(1)).insertLoanCancel(dtoCaptor.capture());
        verify(updateMapper, times(1)).updateNotifyResponseData(anyString(), any(ResponseDto.class));
        verify(messageQueueClient, times(1)).sendMessage(eq(MessageQueue.LoanCancel_Success), any(BankLoanCancelMessage.class));
        assertNotNull(bankLoanCancelMessage);
        assertEquals(true, bankLoanCancelMessage.isStatus());
    }

    @Test
    public void cancelFalseSign() {
        BankLoanCancelMessage bankLoanCancelMessage = loanCancelService.cancel(new BankLoanCancelDto());
        verify(insertMapper, times(0)).insertLoanCancel(any(LoanCancelRequestDto.class));
        assertNotNull(bankLoanCancelMessage);
        assertFalse(bankLoanCancelMessage.isStatus());
    }

    @Test
    public void cancelFalseVerifySign() {
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanCancelRequestDto) o).setOrderNo("111111");
                ((LoanCancelRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(bankClient.send(any(ApiType.class), anyString())).thenReturn(null);
        BankLoanCancelMessage bankLoanCancelMessage = loanCancelService.cancel(new BankLoanCancelDto());
        verify(insertMapper, times(1)).insertLoanCancel(any(LoanCancelRequestDto.class));
        verify(updateMapper, times(0)).updateNotifyResponseData(anyString(), any(ResponseDto.class));
        assertNotNull(bankLoanCancelMessage);
        assertFalse(bankLoanCancelMessage.isStatus());
    }
}
