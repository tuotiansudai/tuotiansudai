package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankInvestDto;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanInvestRequestDto;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.LoanInvestContentDto;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanInvestMessage;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class LoanInvestServiceTest {
    @InjectMocks
    private LoanInvestService loanInvestService;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private BankClient bankClient;
    @Mock
    private MessageQueueClient messageQueueClient;
    @Mock
    private SignatureHelper signatureHelper;
    @Mock
    private QueryTradeService queryTradeService;
    @Mock
    private InsertMapper insertMapper;
    @Mock
    private UpdateMapper updateMapper;
    @Mock
    private SelectMapper selectMapper;

    @Test
    public void investSuccess() {
        ArgumentCaptor<LoanInvestRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanInvestRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);
        BankInvestDto dto = new BankInvestDto("loginName", "mobile", "bankUserName", "bankAccountNo", 2l, 2l, "loanTxNo", 2l, "loanName");

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanInvestRequestDto) o).setOrderNo("111111");
                ((LoanInvestRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        LoanInvestRequestDto loanInvestRequestDto = loanInvestService.invest(Source.WEB, dto);
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertLoanInvest(dtoCaptor.capture());
        assertNotNull(loanInvestRequestDto);
    }

    @Test
    public void investFail() {
        ArgumentCaptor<LoanInvestRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanInvestRequestDto.class);
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        LoanInvestRequestDto loanInvestRequestDto = loanInvestService.invest(Source.WEB, new BankInvestDto());
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.redisTemplate, times(0)).expire(anyString(), anyInt(), any(TimeUnit.DAYS.getClass()));
        verify(this.insertMapper, times(0)).insertLoanInvest(dtoCaptor.capture());
        assertNull(loanInvestRequestDto);
    }

    @Test
    public void fastInvestSuccess() {
        String responseStr = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02688390602211001\",\"amount\":100.00,\"award\":0.00,\"extMark\":\"{\\\"loginName\\\":\\\"skinxosu\\\",\\\"mobile\\\":\\\"18897730003\\\"}\",\"loanTxNo\":\"LU02688394699271001\",\"merchantNo\":\"M02608959047521001\",\"notifyUrl\":\"http://qa.tuotiansudai.com:10003/callback/notify-url/loan_invest\",\"orderDate\":\"20180709\",\"orderNo\":\"20180709000000000022\",\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/loan_invest\",\"userName\":\"UU02688390602151001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        ArgumentCaptor<LoanInvestRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanInvestRequestDto.class);
        BankInvestDto dto = new BankInvestDto("loginName", "mobile", "bankUserName", "bankAccountNo", 2l, 2l, "loanTxNo", 2l, "loanName");
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanInvestRequestDto) o).setOrderNo("111111");
                ((LoanInvestRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(signatureHelper.verifySign(anyString())).thenReturn(true);
        when(bankClient.send(eq(ApiType.LOAN_FAST_INVEST), anyString())).thenReturn(responseStr);
        BankReturnCallbackMessage bankReturnCallbackMessage = loanInvestService.fastInvest(Source.WEB, dto);

        verify(insertMapper, times(1)).insertLoanInvest(dtoCaptor.capture());
        verify(bankClient, times(1)).send(eq(ApiType.LOAN_FAST_INVEST), anyString());
        assertNotNull(bankReturnCallbackMessage);
        assertEquals(true, bankReturnCallbackMessage.isStatus());
    }

    @Test
    public void fastInvestDataFail() {
        BankReturnCallbackMessage bankReturnCallbackMessage = loanInvestService.fastInvest(Source.WEB, new BankInvestDto());
        verify(insertMapper, times(0)).insertLoanInvest(any(LoanInvestRequestDto.class));
        assertNotNull(bankReturnCallbackMessage);
        assertEquals(false, bankReturnCallbackMessage.isStatus());
    }

    @Test
    public void fastInvestClientFail() {
        ArgumentCaptor<LoanInvestRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanInvestRequestDto.class);
        BankInvestDto dto = new BankInvestDto("loginName", "mobile", "bankUserName", "bankAccountNo", 2l, 2l, "loanTxNo", 2l, "loanName");
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanInvestRequestDto) o).setOrderNo("111111");
                ((LoanInvestRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        BankReturnCallbackMessage bankReturnCallbackMessage = loanInvestService.fastInvest(Source.WEB, dto);
        verify(insertMapper, times(1)).insertLoanInvest(any(LoanInvestRequestDto.class));
        assertNotNull(bankReturnCallbackMessage);
        assertEquals(false, bankReturnCallbackMessage.isStatus());
    }

    @Test
    public void rnotifyCallbackSuccess() {
        String responseStr = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02688390602211001\",\"amount\":100.00,\"award\":0.00,\"extMark\":\"{\\\"loginName\\\":\\\"skinxosu\\\",\\\"mobile\\\":\\\"18897730003\\\"}\",\"loanTxNo\":\"LU02688394699271001\",\"merchantNo\":\"M02608959047521001\",\"notifyUrl\":\"http://qa.tuotiansudai.com:10003/callback/notify-url/loan_invest\",\"orderDate\":\"20180709\",\"orderNo\":\"20180709000000000022\",\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/loan_invest\",\"userName\":\"UU02688390602151001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        ResponseDto<LoanInvestContentDto> responseDto = loanInvestService.notifyCallback(responseStr);
        verify(this.updateMapper, times(1)).updateNotifyResponseData(anyString(), any());
        assertNotNull(responseDto);
    }

    @Test
    public void notifyCallbackFail() {
        ResponseDto<LoanInvestContentDto> responseDto = loanInvestService.notifyCallback(null);
        verify(this.updateMapper, times(0)).updateNotifyResponseData(anyString(), any());
        assertNull(responseDto);
    }

    @Test
    public void scheduleSuccess() {
        BankLoanInvestMessage bankLoanInvestMessage = new BankLoanInvestMessage(1l, "loanName", 1l, 2l, "loginName", "mobile", "bankUserName", "bankAccountNo", "bankOrderNo", "bankOrderDate");
        Gson gson = new GsonBuilder().create();
        List<BaseRequestDto> loanInvestRequests = getBaseRequestData();
        ResponseDto<QueryTradeContentDto> query = new ResponseDto<QueryTradeContentDto>();
        QueryTradeContentDto dto = new QueryTradeContentDto();
        dto.setQueryState("1");
        query.setContent(dto);
        query.setRetCode("0000");
        //
        RLock lock = mock(RLock.class);
        HashOperations hashOperations = mock(HashOperations.class);
        when(redissonClient.getLock(any())).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(selectMapper.selectResponseInOneHour(any(String.class))).thenReturn(loanInvestRequests);
        when(queryTradeService.query(any(String.class), any(String.class), any(QueryTradeType.class))).thenReturn(query);
        when(hashOperations.get(any(), any())).thenReturn(gson.toJson(bankLoanInvestMessage));
        //
        loanInvestService.schedule();
        //
        verify(redissonClient, times(1)).getLock("BANK_LOAN_INVEST_QUERY_LOCK");
        verify(redisTemplate, times(1)).opsForHash();
        verify(selectMapper, times(1)).selectResponseInOneHour(any(String.class));
        verify(queryTradeService, times(loanInvestRequests.size())).query(any(String.class), any(String.class), any(QueryTradeType.class));
        verify(updateMapper, times(loanInvestRequests.size())).updateQueryResponse(any(String.class), any(ResponseDto.class));
        verify(messageQueueClient, times(loanInvestRequests.size())).publishMessage(eq(MessageTopic.InvestSuccess), any(BankLoanInvestMessage.class));
    }

    @Test
    public void scheduleFalse() {
        List<BaseRequestDto> rechargeRequests = getBaseRequestData();
        ResponseDto<QueryTradeContentDto> query = new ResponseDto<QueryTradeContentDto>();
        QueryTradeContentDto dto = new QueryTradeContentDto();
        dto.setQueryState("1");
        query.setContent(dto);
        query.setRetCode("0000");
        //
        RLock lock = mock(RLock.class);
        HashOperations hashOperations = mock(HashOperations.class);
        when(redissonClient.getLock(any())).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(selectMapper.selectResponseInOneHour(any(String.class))).thenReturn(rechargeRequests);
        when(queryTradeService.query(any(String.class), any(String.class), any(QueryTradeType.class))).thenReturn(query);
        when(hashOperations.get(any(), any())).thenReturn(null);
        //
        loanInvestService.schedule();
        //
        verify(redissonClient, times(1)).getLock("BANK_LOAN_INVEST_QUERY_LOCK");
        verify(redisTemplate, times(1)).opsForHash();
        verify(selectMapper, times(1)).selectResponseInOneHour(any(String.class));
        verify(queryTradeService, times(0)).query(any(String.class), any(String.class), any(QueryTradeType.class));
        verify(updateMapper, times(0)).updateQueryResponse(any(String.class), any(ResponseDto.class));
        verify(messageQueueClient, times(0)).sendMessage(any(MessageQueue.Withdraw_Success.getClass()), any(Object.class));
    }

    private List<BaseRequestDto> getBaseRequestData() {
        List<BaseRequestDto> withdrawRequests = new ArrayList<>();
        BaseRequestDto i1 = new BaseRequestDto();
        i1.setOrderNo("123123");
        i1.setOrderDate("20180809");
        BaseRequestDto i2 = new BaseRequestDto();
        i2.setOrderNo("123143");
        i2.setOrderDate("20180810");
        withdrawRequests.add(i1);
        withdrawRequests.add(i2);
        return withdrawRequests;
    }

}
