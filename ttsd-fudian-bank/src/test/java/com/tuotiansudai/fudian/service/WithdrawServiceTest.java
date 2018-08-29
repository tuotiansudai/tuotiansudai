package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.BankRechargeDto;
import com.tuotiansudai.fudian.dto.BankWithdrawDto;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.dto.RechargePayType;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.request.WithdrawRequestDto;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.RechargeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.dto.response.WithdrawContentDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankRechargeMessage;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by qduljs2011 on 2018/8/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class WithdrawServiceTest {

    @InjectMocks
    private WithdrawService withdrawService;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private RedissonClient redissonClient;
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
    public void withdrawSuccess() {
        ArgumentCaptor<WithdrawRequestDto> dtoCaptor = ArgumentCaptor.forClass(WithdrawRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);


        BankWithdrawDto bankWithdrawDto = new BankWithdrawDto(1l, "loginName", "mobile", "bankUserName", "bankAccountNo", 1l, true, "openId");

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((WithdrawRequestDto) o).setOrderNo("111111");
                ((WithdrawRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        WithdrawRequestDto withdrawRequestDto = withdrawService.withdraw(Source.WEB, bankWithdrawDto);
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertWithdraw(dtoCaptor.capture());
        assertNotNull(withdrawRequestDto);
    }

    @Test(expected = NullPointerException.class)
    public void rechargeFalse() {
        BankWithdrawDto dto = new BankWithdrawDto();
        ArgumentCaptor<WithdrawRequestDto> dtoCaptor = ArgumentCaptor.forClass(WithdrawRequestDto.class);
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        WithdrawRequestDto withdrawRequestDto = withdrawService.withdraw(Source.WEB, dto);
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.redisTemplate, times(0)).expire(any(), any(), any());
        verify(this.insertMapper, times(0)).insertWithdraw(dtoCaptor.capture());
        assertNull(withdrawRequestDto);
    }

    @Test
    public void rechargeNotifyCallbackSuccess() {
        String responseData = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02688444138861001\",\"amount\":50.00,\"bankCardNo\":\"6213314405378127356\",\"bankCode\":\"102\",\"bankName\":\"中国工商银行\",\"extMark\":\"{\\\"loginName\\\":\\\"krfvjxrq\\\",\\\"mobile\\\":\\\"18897730004\\\"}\",\"fee\":2.00,\"merchantNo\":\"M02608959047521001\",\"notifyUrl\":\"http://qa.tuotiansudai.com:10003/callback/notify-url/withdraw\",\"orderDate\":\"20180709\",\"orderNo\":\"20180709000000000041\",\"realName\":\"朱坤\",\"receivedAmount\":48.00,\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/withdraw\",\"status\":\"1\",\"userName\":\"UU02688444138801001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        ResponseDto<WithdrawContentDto> responseDto = withdrawService.notifyCallback(responseData);
        verify(this.updateMapper, times(1)).updateNotifyResponseData(anyString(), any());
        assertNotNull(responseDto);
    }

    @Test
    public void rechargeNotifyCallbackFail() {
        ResponseDto<WithdrawContentDto> responseDto = withdrawService.notifyCallback(null);
        verify(this.updateMapper, times(0)).updateNotifyResponseData(anyString(), any());
        assertNull(responseDto);
    }

    @Test
    public void scheduleSuccess() {
        BankWithdrawMessage bankRechargeMessage = new BankWithdrawMessage(1l, "loginName", "mobile", "bankUserName", "bankAccountNo", 2l, 1l, "bankOrderNo", "bankOrderDate");
        Gson gson = new GsonBuilder().create();
        //
        List<BaseRequestDto> rechargeRequests = getRequestData();
        //
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
        when(hashOperations.get(any(), any())).thenReturn(gson.toJson(bankRechargeMessage));
        //
        withdrawService.schedule();

        verify(redissonClient, times(1)).getLock("BANK_WITHDRAW_QUERY_LOCK");
        verify(redisTemplate, times(1)).opsForHash();
        verify(selectMapper, times(1)).selectResponseInOneHour(any(String.class));
        verify(queryTradeService, times(rechargeRequests.size())).query(any(String.class), any(String.class), any(QueryTradeType.class));
        verify(updateMapper, times(rechargeRequests.size())).updateQueryResponse(any(String.class), any(ResponseDto.class));

        verify(messageQueueClient, times(rechargeRequests.size())).sendMessage(any(MessageQueue.Withdraw_Success.getClass()), any(Object.class));
    }

    @Test
    public void scheduleFalse() {
        //
        List<BaseRequestDto> withdrawRequest = getRequestData();
        //
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
        when(selectMapper.selectResponseInOneHour(any(String.class))).thenReturn(withdrawRequest);
        when(queryTradeService.query(any(String.class), any(String.class), any(QueryTradeType.class))).thenReturn(query);
        when(hashOperations.get(any(), any())).thenReturn(null);
        //
        withdrawService.schedule();

        verify(redissonClient, times(1)).getLock("BANK_WITHDRAW_QUERY_LOCK");
        verify(redisTemplate, times(1)).opsForHash();
        verify(selectMapper, times(1)).selectResponseInOneHour(any(String.class));
        verify(queryTradeService, times(0)).query(any(String.class), any(String.class), any(QueryTradeType.class));
        verify(updateMapper, times(0)).updateQueryResponse(any(String.class), any(ResponseDto.class));
        verify(messageQueueClient, times(0)).sendMessage(any(MessageQueue.Withdraw_Success.getClass()), any(Object.class));
    }

    private List<BaseRequestDto> getRequestData() {
        List<BaseRequestDto> withdrawRequests = new ArrayList<>();
        BaseRequestDto w1 = new BaseRequestDto();
        w1.setOrderNo("123123");
        w1.setOrderDate("20180809");
        BaseRequestDto w2 = new BaseRequestDto();
        w2.setOrderNo("123143");
        w2.setOrderDate("20180810");
        withdrawRequests.add(w1);
        withdrawRequests.add(w2);
        return withdrawRequests;
    }
}
