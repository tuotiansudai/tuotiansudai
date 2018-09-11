package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanFullDto;
import com.tuotiansudai.fudian.dto.request.LoanFullRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanInvestRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.MessageFormat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class LoanFullServiceTest {

    @InjectMocks
    private LoanFullService loanFullService;

    @Mock
    private SignatureHelper signatureHelper;

    @Mock
    private RedisTemplate redisTemplate;

    @Mock
    private InsertMapper insertMapper;

    @Mock
    private BankClient bankClient;

    @Mock
    private UpdateMapper updateMapper;

    @Mock
    private MessageQueueClient messageQueueClient;

    @Test
    public void loanFullSuccess() {

        ArgumentCaptor<LoanFullRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanFullRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);

        doNothing().when(signatureHelper).sign(any(), argThat(o -> {
            ((LoanFullRequestDto) o).setOrderNo("111111");
            ((LoanFullRequestDto) o).setRequestData("requestData");
            return false;
        }));

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));

        String responseData = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02732393975741001\",\"amount\":3000.00,\"extMark\":\"{\\\"loginName\\\":\\\"loginName\\\",\\\"mobile\\\":\\\"11111111111\\\"}\",\"loanFee\":10.00,\"loanName\":\"房产抵押借款18010\",\"loanOrderDate\":\"20180810\",\"loanOrderNo\":\"111111\",\"loanTxNo\":\"LU02733521263371001\",\"merchantNo\":\"M02689149095591001\",\"notifyUrl\":\"http://39.107.217.19:10003/callback/notify-url/loan_full\",\"orderDate\":\"20180810\",\"orderNo\":\"111111\",\"status\":\"2\",\"userName\":\"UU02732393947841001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";

        when(bankClient.send(eq(ApiType.LOAN_FULL), anyString())).thenReturn(responseData);
        when(signatureHelper.verifySign(anyString())).thenReturn(true);
        when(updateMapper.updateNotifyResponseData(anyString(), any(ResponseDto.class))).thenReturn(1);
        Gson gson = new GsonBuilder().create();
        when(redisTemplate.opsForHash().get(anyString(), anyString())).thenReturn(gson.toJson(mockBankLoanFullMessage()));

        BankBaseMessage message = loanFullService.full(mockBankLoanFullDto());

        verify(insertMapper, times(1)).insertLoanFull(dtoCaptor.capture());
        verify(redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), anyString());
        verify(messageQueueClient, times(1)).publishMessage(eq(MessageTopic.LoanFullSuccess), any(BankLoanFullMessage.class));

        assertThat(dtoCaptor.getValue().getOrderNo(), is("111111"));
        assertThat(messageKeyCaptor.getValue(), is(MessageFormat.format("BANK_LOAN_FULL_MESSAGE_{0}", dtoCaptor.getValue().getOrderDate())));
        assertThat(messageHKeyCaptor.getValue(), is("111111"));
        assertTrue(message.isStatus());
    }

    @Test
    public void loanFullSignFail(){
        doNothing().when(signatureHelper).sign(any(), argThat(o -> {
            ((LoanFullRequestDto) o).setOrderNo("111111");
            ((LoanFullRequestDto) o).setRequestData(null);
            return false;
        }));
        BankBaseMessage message = loanFullService.full(mockBankLoanFullDto());
        assertFalse(message.isStatus());
        assertThat(message.getMessage(), is("签名失败"));
    }

    @Test
    public void loanFullVerifySignFail(){
        doNothing().when(signatureHelper).sign(any(), argThat(o -> {
            ((LoanFullRequestDto) o).setOrderNo("111111");
            ((LoanFullRequestDto) o).setRequestData("requestData");
            return false;
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(bankClient.send(eq(ApiType.LOAN_FULL), anyString())).thenReturn(null);
        BankBaseMessage message = loanFullService.full(mockBankLoanFullDto());
        assertFalse(message.isStatus());
        assertThat(message.getMessage(), is("验签失败"));
    }

    private BankLoanFullDto mockBankLoanFullDto() {
        return new BankLoanFullDto("loginName", "11111111111", "UU02732393947841001", "UA02732393975741001", 1, "LU02733521263371001", "111111", "20180810", "", "checkLoginName", "", 1000);
    }

    private BankLoanFullMessage mockBankLoanFullMessage() {
        return new BankLoanFullMessage(1, "loanTxNo", "checkLoginName", "111111", "20180810", "20180810");
    }
}
