package com.tuotiansudai.fudian.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.BankLoanCreditInvestDto;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanCreditInvestMessage;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class LoanCreditInvestServiceTest {

    @InjectMocks
    private LoanCreditInvestService loanCreditInvestService;

    @Mock
    private SignatureHelper signatureHelper;

    @Mock
    private InsertMapper insertMapper;

    @Mock
    private UpdateMapper updateMapper;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private MessageQueueClient messageQueueClient;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private QueryTradeService queryTradeService;

    @Mock
    private SelectMapper selectMapper;

    @Test
    public void investSuccess(){

        ArgumentCaptor<LoanCreditInvestRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanCreditInvestRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanCreditInvestRequestDto) o).setOrderNo("111111");
                ((LoanCreditInvestRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        loanCreditInvestService.invest(Source.WEB, mockBankLoanCreditInvestDto());
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertLoanCreditInvest(dtoCaptor.capture());
        assertThat(dtoCaptor.getValue().getAmount(), is("9.99"));
        assertThat(dtoCaptor.getValue().getCreditAmount(), is("10.00"));
        assertThat(dtoCaptor.getValue().getCreditFee(), is("0.01"));
        assertThat(messageHKeyCaptor.getValue(), is("111111"));
        assertThat(messageTimeoutCaptor.getValue(), is(30L));
        assertThat(messageTimeUnitCaptor.getValue(), is(TimeUnit.DAYS));
    }

    @Test
    public void investFail(){
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanCreditInvestRequestDto) o).setOrderNo("111111");
                ((LoanCreditInvestRequestDto) o).setRequestData(null);
                return false;
            }
        }));

        LoanCreditInvestRequestDto dto = loanCreditInvestService.invest(Source.WEB, mockBankLoanCreditInvestDto());
        verify(signatureHelper, times(1)).sign(any(), any());
        assertNull(dto);
    }

    @Test
    public void notifyCallbackSuccess(){
        String responseData = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02733512006601001\",\"amount\":1300.00,\"creditAmount\":1300.00,\"creditFee\":13.00,\"creditFeeType\":1,\"creditNo\":\"3434\",\"creditNoAmount\":1300.00,\"extMark\":\"{\\\"loginName\\\":\\\"hypakxih\\\",\\\"mobile\\\":\\\"13260124014\\\"}\",\"investOrderDate\":\"20180830\",\"investOrderNo\":\"20180830000000000123\",\"loanTxNo\":\"LU02733521263371001\",\"merchantNo\":\"M02689149095591001\",\"notifyUrl\":\"http://39.107.217.19:10003/callback/notify-url/loan_credit_invest\",\"orderDate\":\"20180830\",\"orderNo\":\"20180830000000000147\",\"oriOrderDate\":\"20180830\",\"oriOrderNo\":\"20180830000000000147\",\"repayedAmount\":0.00,\"returnUrl\":\"http://qa.tuotiansudai.com:10004/callback/return-url/loan_credit_invest\",\"userName\":\"UU02733512002791001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        ResponseDto dto = loanCreditInvestService.notifyCallback(responseData);
        verify(this.updateMapper, times(1)).updateNotifyResponseData(anyString(), any());
        assertNotNull(dto);
    }

    @Test
    public void scheduleSuccess(){
        ArgumentCaptor<String> queryOrderNoCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bankLoanCreditInvestMessageCaptor = ArgumentCaptor.forClass(String.class);

        Gson gson = new GsonBuilder().create();
        RLock lock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);

        HashOperations hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(selectMapper.selectResponseInOneHour(anyString())).thenReturn(mockRequestData());
        when(hashOperations.get(eq("BANK_LOAN_CREDIT_INVEST_MESSAGE_KEY_20180810"), anyString())).thenReturn(gson.toJson(mockBankLoanCreditInvestMessage()));

        ResponseDto<QueryTradeContentDto> query = new ResponseDto<QueryTradeContentDto>();
        QueryTradeContentDto dto = new QueryTradeContentDto();
        dto.setQueryType("06");
        dto.setQueryState("1");
        query.setContent(dto);
        query.setRetCode("0000");
        when(queryTradeService.query(anyString(), anyString(), eq(QueryTradeType.LOAN_CREDIT_INVEST))).thenReturn(query);
        loanCreditInvestService.schedule();

        verify(queryTradeService, times(1)).query(queryOrderNoCaptor.capture(), anyString(), eq(QueryTradeType.LOAN_CREDIT_INVEST));
        verify(updateMapper, times(1)).updateQueryResponse(anyString(), any(ResponseDto.class));
        verify(messageQueueClient, times(1)).sendMessage(eq(MessageQueue.LoanCreditInvest_Success), bankLoanCreditInvestMessageCaptor.capture());
        assertThat(queryOrderNoCaptor.getValue(), is("111111"));
        BankLoanCreditInvestMessage message = gson.fromJson(bankLoanCreditInvestMessageCaptor.getValue(), BankLoanCreditInvestMessage.class);
        assertThat(message.getAmount(), is(1000L));
        assertThat(message.getInvestId(), is(1L));
        assertThat(message.getLoginName(), is("loginName"));
    }

    @Test
    public void notifyCallbackFail(){
        ResponseDto dto = loanCreditInvestService.notifyCallback(null);
        verify(this.updateMapper, times(0)).updateNotifyResponseData(anyString(), any());
        assertNull(dto);
    }

    private BankLoanCreditInvestDto mockBankLoanCreditInvestDto(){
        return new BankLoanCreditInvestDto("loginName", "11111111111", "bankUserName", "bankAccountNo", 1, 1, 1000, 999, 1, "investOrderDate", "investOrderNo", "loanTxNo");
    }

    private List<BaseRequestDto> mockRequestData() {
        BaseRequestDto dto = new BaseRequestDto();
        dto.setOrderNo("111111");
        dto.setOrderDate("20180810");
        return Lists.newArrayList(dto);
    }

    private BankLoanCreditInvestMessage mockBankLoanCreditInvestMessage(){
        return new BankLoanCreditInvestMessage(1,1,1000, "loginName", "11111111111", "bankUserName", "bankAccountNo", "111111", "20180810");
    }
}
