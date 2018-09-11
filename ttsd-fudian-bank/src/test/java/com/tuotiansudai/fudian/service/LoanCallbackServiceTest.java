package com.tuotiansudai.fudian.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.dto.BankLoanRepayInvestDto;
import com.tuotiansudai.fudian.dto.request.LoanCallbackInvestItemRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanCallbackRequestDto;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanCallbackInvestItemContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class LoanCallbackServiceTest {

    @InjectMocks
    private LoanCallbackService loanCallbackService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private RedissonClient redissonClient;

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
    public void sendScheduleSuccess() {

        ArgumentCaptor<LoanCallbackRequestDto> loanCallbackRequestDtoCaptor = ArgumentCaptor.forClass(LoanCallbackRequestDto.class);

        RLock lock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        ListOperations listOperations = mock(ListOperations.class);
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        Gson gson = new GsonBuilder().create();
        when(lock.tryLock()).thenReturn(true);
        when(listOperations.rightPop(eq("BANK_LOAN_CALLBACK_QUEUE"))).thenReturn(gson.toJson(mockBankLoanRepayDto()));
        ValueOperations operations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(operations);
        when(operations.increment(anyString(), anyInt())).thenReturn(1l);

        doNothing().when(signatureHelper).sign(any(), argThat(o -> {
            ((LoanCallbackRequestDto) o).setOrderNo("111111");
            ((LoanCallbackRequestDto) o).setRequestData("requestData");
            return false;
        }));

        doNothing().when(insertMapper).insertLoanCallback(any(LoanCallbackRequestDto.class));
        doNothing().when(insertMapper).insertLoanCallbackInvestItems(anyList());

        String responseData = "{\"certInfo\":\"certInfo\",\"content\":{\"extMark\":\"{\\\"loginName\\\":\\\"loginName1\\\",\\\"mobile\\\":\\\"11111111111\\\"}\",\"investList\":[{\"capital\":10.00,\"interest\":0.01,\"interestFee\":0.01,\"investAccountNo\":\"bankAccountNo1\",\"investOrderDate\":\"20180810\",\"investOrderNo\":\"111111111111\",\"investUserName\":\"bankUserName1\",\"merchantName\":\"拓天速贷\",\"orderDate\":\"20180810\",\"orderNo\":\"111111\",\"rateInterest\":0.00,\"retCode\":\"0000\",\"retMsg\":\"操作成功\"},{\"capital\":10.00,\"interest\":0.01,\"interestFee\":0.01,\"investAccountNo\":\"bankAccountNo2\",\"investOrderDate\":\"20180810\",\"investOrderNo\":\"222222222222\",\"investUserName\":\"bankUserName\",\"merchantName\":\"拓天速贷\",\"orderDate\":\"20180810\",\"orderNo\":\"222222\",\"rateInterest\":0.00,\"retCode\":\"0000\",\"retMsg\":\"操作成功\"}],\"loanTxNo\":\"LU02688449311831001\",\"merchantNo\":\"M02608959047521001\",\"orderDate\":\"20180810\",\"orderNo\":\"333333\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        when(bankClient.send(eq(ApiType.LOAN_CALLBACK), anyString())).thenReturn(responseData);
        when(signatureHelper.verifySign(anyString())).thenReturn(true);

        when(updateMapper.updateNotifyResponseData(anyString(), any(ResponseDto.class))).thenReturn(1);
        doNothing().when(updateMapper).updateLoanCallbackInvestItems(anyList());

        loanCallbackService.sendSchedule();

        verify(signatureHelper, times(1)).sign(eq(ApiType.LOAN_CALLBACK), any(LoanCallbackRequestDto.class));
        verify(insertMapper, times(1)).insertLoanCallback(loanCallbackRequestDtoCaptor.capture());
        verify(insertMapper, times(1)).insertLoanCallbackInvestItems(anyList());
        verify(bankClient, times(1)).send(eq(ApiType.LOAN_CALLBACK), anyString());
        verify(signatureHelper, times(1)).verifySign(anyString());
        verify(updateMapper, times(1)).updateNotifyResponseData(anyString(), any(ResponseDto.class));

        assertThat(loanCallbackRequestDtoCaptor.getValue().getLoanTxNo(), is("loanTxNo"));
        assertThat(loanCallbackRequestDtoCaptor.getValue().getOrderNo(), is("111111"));
        assertThat(loanCallbackRequestDtoCaptor.getValue().getInvestList().size(), is(2));
        assertThat(loanCallbackRequestDtoCaptor.getValue().getInvestList().get(0).getInvestOrderNo(), is("investOrderNo1"));
        assertThat(loanCallbackRequestDtoCaptor.getValue().getInvestList().get(0).getCapital(), is("10.00"));
        assertThat(loanCallbackRequestDtoCaptor.getValue().getInvestList().get(0).getInvestUserName(), is("bankUserName1"));
        assertThat(loanCallbackRequestDtoCaptor.getValue().getInvestList().get(1).getInvestOrderNo(), is("investOrderNo2"));
        assertThat(loanCallbackRequestDtoCaptor.getValue().getInvestList().get(1).getCapital(), is("10.00"));
        assertThat(loanCallbackRequestDtoCaptor.getValue().getInvestList().get(1).getInvestUserName(), is("bankUserName2"));
    }

    @Test
    public void sendScheduleFail(){
        RLock lock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        ListOperations listOperations = mock(ListOperations.class);
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(lock.tryLock()).thenReturn(true);
        when(listOperations.rightPop(eq("BANK_LOAN_CALLBACK_QUEUE"))).thenReturn(null);
        loanCallbackService.sendSchedule();
        verify(signatureHelper, times(0)).sign(any(), any());
    }

    @Test
    public void loanCallbackSignFail(){
        RLock lock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        ListOperations listOperations = mock(ListOperations.class);
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        Gson gson = new GsonBuilder().create();
        when(lock.tryLock()).thenReturn(true);
        when(listOperations.rightPop(eq("BANK_LOAN_CALLBACK_QUEUE"))).thenReturn(gson.toJson(mockBankLoanRepayDto()));
        ValueOperations operations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(operations);
        when(operations.increment(anyString(), anyInt())).thenReturn(1l);

        doNothing().when(signatureHelper).sign(any(), argThat(o -> {
            ((LoanCallbackRequestDto) o).setOrderNo("111111");
            ((LoanCallbackRequestDto) o).setRequestData(null);
            return false;
        }));

        loanCallbackService.sendSchedule();
        verify(signatureHelper, times(1)).sign(any(), any());
        verify(insertMapper, times(0)).insertLoanCallback(any());
    }

    private BankLoanRepayDto mockBankLoanRepayDto() {
        return new BankLoanRepayDto("loginName", "mobile", "bankUserName", "bankAccountNo", 1, 1, "loanTxNo", true, mockBankLoanRepayInvestDto());
    }

    private List<BankLoanRepayInvestDto> mockBankLoanRepayInvestDto() {
        BankLoanRepayInvestDto dto1 = new BankLoanRepayInvestDto("invest1", "11111111111", "bankUserName1", "bankAccountNo1", 1, 1, "loanTxNo", 1000, 1000, 0, 100, "investOrderNo1", "investOrderDate1");
        BankLoanRepayInvestDto dto2 = new BankLoanRepayInvestDto("invest2", "22222222222", "bankUserName2", "bankAccountNo2", 2, 1, "loanTxNo", 1000, 1000, 0, 100, "investOrderNo2", "investOrderDate2");
        return Lists.newArrayList(dto1, dto2);
    }
}
