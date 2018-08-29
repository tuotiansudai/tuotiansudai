package com.tuotiansudai.fudian.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.dto.BankLoanRepayInvestDto;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanRepayRequestDto;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanRepayMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
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
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class NormalLoanRepayServiceTest {
    @InjectMocks
    private LoanRepayService loanRepayService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private MessageQueueClient messageQueueClient;

    @Mock
    private BankConfig bankConfig;

    @Mock
    private SignatureHelper signatureHelper;

    @Mock
    private SelectMapper selectMapper;

    @Mock
    private InsertMapper insertMapper;

    @Mock
    private UpdateMapper updateMapper;

    @Mock
    private LoanCallbackService loanCallbackService;

    @Mock
    private QueryTradeService queryTradeService;

    @Test
    public void loanRepaySuccess() {
        ArgumentCaptor<LoanRepayRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanRepayRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanRepayRequestDto) o).setOrderNo("111111");
                ((LoanRepayRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
        LoanRepayRequestDto loanRepayRequestDto = loanRepayService.repay(Source.WEB, mockBankLoanRepayDto());
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate.opsForValue(), times(1)).set(anyString(), any(), anyLong(), eq(TimeUnit.DAYS));
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertLoanRepay(dtoCaptor.capture());
        assertThat(dtoCaptor.getValue().getCapital(), is("20.00"));
        assertThat(dtoCaptor.getValue().getInterest(), is("20.00"));
    }

    @Test
    public void loanRepayFail() {
        BankLoanRepayDto dto = new BankLoanRepayDto("loginName", "mobile", "bankUserName", "bankAccountNo", 111l, 1000l, "loanTxNo", true, mockBankLoanRepayInvestDto());
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
        LoanRepayRequestDto loanRepayRequestDto = loanRepayService.repay(Source.WEB, dto);
        verify(this.signatureHelper, times(1)).sign(any(), any());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.redisTemplate, times(0)).expire(any(), anyLong(), eq(TimeUnit.DAYS));
        verify(this.insertMapper, times(0)).insertLoanRepay(any());
        assertNull(loanRepayRequestDto);
    }

    @Test
    public void loanRepayNotifyCallbackSuccess() {
        String responseData = "{\"certInfo\":\"cerInfo\",\"content\":{\"accountNo\":\"UA02724627346051001\",\"capital\":5500.00,\"extMark\":\"{\\\"loginName\\\":\\\"kgprvktg\\\",\\\"mobile\\\":\\\"18612801708\\\"}\",\"interest\":1.20,\"loanFee\":0.00,\"loanTxNo\":\"LU02728262870471001\",\"merchantNo\":\"M02689149095591001\",\"notifyUrl\":\"http://39.107.217.19:10003/callback/notify-url/loan_repay\",\"orderDate\":\"20180824\",\"orderNo\":\"20180824000000000784\",\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/loan_repay\",\"userName\":\"UU02724627342471001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        ResponseDto dto = loanRepayService.notifyCallback(responseData);
        verify(this.updateMapper, times(1)).updateNotifyResponseData(anyString(), any());
        assertNotNull(dto);
    }

    @Test
    public void loanRepayNotifyCallbackFail() {
        ResponseDto dto = loanRepayService.notifyCallback(null);
        verify(this.updateMapper, times(0)).updateNotifyResponseData(anyString(), any());
        assertNull(dto);
    }

    @Test
    public void scheduleSuccess() {
        ArgumentCaptor<BankLoanRepayMessage> loanRepayMessageCaptor = ArgumentCaptor.forClass(BankLoanRepayMessage.class);
        ArgumentCaptor<String> loanCallbackDataKeyCaptor = ArgumentCaptor.forClass(String.class);

        Gson gson = new GsonBuilder().create();
        List<BaseRequestDto> rechargeRequests = mockRequestData();

        ResponseDto<QueryTradeContentDto> query = new ResponseDto<QueryTradeContentDto>();
        QueryTradeContentDto dto = new QueryTradeContentDto();
        dto.setQueryType("04");
        dto.setQueryState("1");
        query.setContent(dto);
        query.setRetCode("0000");

        RLock lock = mock(RLock.class);
        when(redissonClient.getLock(any())).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);

        HashOperations hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(selectMapper.selectResponseInOneHour(any(String.class))).thenReturn(rechargeRequests);
        when(queryTradeService.query(any(String.class), any(String.class), any(QueryTradeType.class))).thenReturn(query);
        when(hashOperations.get(eq("BANK_LOAN_REPAY_MESSAGE_20180810"), eq("111111"))).thenReturn(null);
        when(hashOperations.get(eq("BANK_LOAN_REPAY_MESSAGE_20180810"), eq("222222"))).thenReturn(gson.toJson(mockBankLoanRepayMessage()));
        when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
        when(redisTemplate.opsForValue().get(anyString())).thenReturn(gson.toJson(mockBankLoanRepayDto()));
        doNothing().when(loanCallbackService).pushLoanCallbackQueue(any(BankLoanRepayDto.class));

        loanRepayService.schedule();
        verify(redissonClient, times(1)).getLock("BANK_LOAN_REPAY_QUERY_LOCK");
        verify(redisTemplate, times(1)).opsForHash();
        verify(selectMapper, times(1)).selectResponseInOneHour(anyString());
        verify(queryTradeService, times(2)).query(anyString(), anyString(), eq(QueryTradeType.LOAN_REPAY));
        verify(updateMapper, times(2)).updateQueryResponse(anyString(), any(ResponseDto.class));
        verify(redisTemplate.opsForHash(), times(2)).get(anyString(), anyString());
        verify(messageQueueClient, times(1)).sendMessage(any(MessageQueue.LoanRepay_Success.getClass()), loanRepayMessageCaptor.capture());
        verify(loanCallbackService, times(1)).pushLoanCallbackQueue(any(BankLoanRepayDto.class));
        verify(redisTemplate.opsForValue(), times(1)).get(loanCallbackDataKeyCaptor.capture());
        assertThat(loanCallbackDataKeyCaptor.getValue(), is("BANK_LOAN_CALLBACK_DATA_222222"));
        assertThat(loanRepayMessageCaptor.getValue().getBankOrderNo(), is("222222"));
        assertTrue(loanRepayMessageCaptor.getValue().isStatus());
    }

    private BankLoanRepayDto mockBankLoanRepayDto() {
        return new BankLoanRepayDto("loginName", "mobile", "bankUserName", "bankAccountNo", 111l, 1000l, "loanTxNo", true, mockBankLoanRepayInvestDto());
    }

    private List<BankLoanRepayInvestDto> mockBankLoanRepayInvestDto() {
        BankLoanRepayInvestDto dto1 = new BankLoanRepayInvestDto("invest1", "11111111111", "bankUserName", "bankAccountNo", 1, 1, "loanTxNo", 1000, 1000, 0, 100, "investOrderNo1", "investOrderDate1");
        BankLoanRepayInvestDto dto2 = new BankLoanRepayInvestDto("invest2", "22222222222", "bankUserName", "bankAccountNo", 2, 1, "loanTxNo", 1000, 1000, 0, 100, "investOrderNo2", "investOrderDate2");
        return Lists.newArrayList(dto1, dto2);
    }

    private List<BaseRequestDto> mockRequestData() {
        BaseRequestDto r1 = new BaseRequestDto();
        r1.setOrderNo("111111");
        r1.setOrderDate("20180810");
        BaseRequestDto r2 = new BaseRequestDto();
        r2.setOrderNo("222222");
        r2.setOrderDate("20180810");
        return Lists.newArrayList(r1, r2);
    }


    private BankLoanRepayMessage mockBankLoanRepayMessage() {
        return new BankLoanRepayMessage(1, 1, 2000, 2000, true, "loginName", "11111111111", "222222", "20180810");
    }
}
