package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.BankRechargeDto;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.dto.RechargePayType;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.RechargeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankRechargeMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
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
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class RechargeServiceTest {
    @InjectMocks
    private RechargeService rechargeService;

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
    private QueryTradeService queryTradeService;

    @Test
    public void rechargeSuccess() {
        ArgumentCaptor<RechargeRequestDto> dtoCaptor = ArgumentCaptor.forClass(RechargeRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        BankRechargeDto dto = new BankRechargeDto("loginName", "mobile", "bankUserName", "bankAccountNo", 111l, 1000l, RechargePayType.FAST_PAY);

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((RechargeRequestDto) o).setOrderNo("111111");
                ((RechargeRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        RechargeRequestDto rechargeRequestDto = rechargeService.recharge(Source.WEB, dto);
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertRecharge(dtoCaptor.capture());
        assertNotNull(rechargeRequestDto);
    }

    @Test(expected = NullPointerException.class)
    public void rechargeFalse() {
        BankRechargeDto dto = new BankRechargeDto();
        ArgumentCaptor<RechargeRequestDto> dtoCaptor = ArgumentCaptor.forClass(RechargeRequestDto.class);
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        RechargeRequestDto rechargeRequestDto = rechargeService.recharge(Source.WEB, dto);
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.redisTemplate, times(0)).expire(any(), any(), any());
        verify(this.insertMapper, times(0)).insertRecharge(dtoCaptor.capture());
        assertNull(rechargeRequestDto);
    }

    @Test
    public void merchantRechargeSuccess() {
        when(bankConfig.getMerchantUserName()).thenReturn("12312");
        when(bankConfig.getMerchantAccountNo()).thenReturn("11111");
        ArgumentCaptor<RechargeRequestDto> dtoCaptor = ArgumentCaptor.forClass(RechargeRequestDto.class);
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((RechargeRequestDto) o).setOrderNo("111111");
                ((RechargeRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        RechargeRequestDto rechargeRequestDto = rechargeService.merchantRecharge(Source.WEB, "loginName", "mobile", 10000l, 1000l);
        verify(signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(insertMapper, times(1)).insertRecharge(dtoCaptor.capture());
        assertNotNull(rechargeRequestDto);
    }

    @Test
    public void merchantRechargeFalse() {
        when(bankConfig.getMerchantUserName()).thenReturn("12312");
        when(bankConfig.getMerchantAccountNo()).thenReturn("11111");
        ArgumentCaptor<RechargeRequestDto> dtoCaptor = ArgumentCaptor.forClass(RechargeRequestDto.class);
        RechargeRequestDto rechargeRequestDto = rechargeService.merchantRecharge(Source.WEB, "loginName", "mobile", 10000l, 1000l);
        verify(signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(insertMapper, times(0)).insertRecharge(dtoCaptor.capture());
        assertNull(rechargeRequestDto);
    }

    @Test
    public void returnCallback() {
        ResponseDto responseDto = new ResponseDto();
        rechargeService.returnCallback(responseDto);
        verify(updateMapper, times(1)).updateReturnResponse(any(String.class), any(ResponseDto.class));
    }

    @Test
    public void rechargeNotifyCallbackSuccess() {
        String responseData = "{\"certInfo\":\"cerInfo\",\"content\":{\"accountNo\":\"UA02685895452701001\",\"amount\":120.00,\"extMark\":\"{\\\"loginName\\\":\\\"njygcgwj\\\",\\\"mobile\\\":\\\"18897730001\\\"}\",\"fee\":0.00,\"merchantNo\":\"M02608959047521001\",\"notifyUrl\":\"http://qa.tuotiansudai.com:10003/callback/notify-url/recharge\",\"orderDate\":\"20180709\",\"orderNo\":\"20180709000000000001\",\"payType\":1,\"receivedAmount\":120.00,\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/recharge\",\"status\":1,\"userName\":\"UU02685895452641001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        ResponseDto<RechargeContentDto> responseDto = rechargeService.notifyCallback(responseData);
        verify(this.updateMapper, times(1)).updateNotifyResponseData(anyString(), any());
        assertNotNull(responseDto);
    }

    @Test
    public void rechargeNotifyCallbackFail() {
        ResponseDto<RechargeContentDto> responseDto = rechargeService.notifyCallback(null);
        verify(this.updateMapper, times(0)).updateNotifyResponseData(anyString(), any());
        assertNull(responseDto);
    }

    @Test
    public void scheduleSuccess() {
        BankRechargeMessage bankRegisterMessage = new BankRechargeMessage(1l, "loginName", "mobile", "bankUserName", "bankAccountNo", 1l, "bankOrderNo", "bankOrderDate");
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
        when(hashOperations.get(any(), any())).thenReturn(gson.toJson(bankRegisterMessage));
        //
        rechargeService.schedule();

        verify(redissonClient, times(1)).getLock("BANK_RECHARGE_QUERY_LOCK");
        verify(redisTemplate, times(1)).opsForHash();
        verify(selectMapper, times(1)).selectResponseInOneHour(any(String.class));
        verify(queryTradeService, times(rechargeRequests.size())).query(any(String.class), any(String.class), any(QueryTradeType.class));
        verify(updateMapper, times(rechargeRequests.size())).updateQueryResponse(any(String.class), any(ResponseDto.class));

        verify(messageQueueClient, times(rechargeRequests.size())).publishMessage(any(MessageTopic.Recharge.getClass()), any(Object.class));
    }

    @Test
    public void scheduleFalse() {
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
        when(hashOperations.get(any(), any())).thenReturn(null);
        //
        rechargeService.schedule();

        verify(redissonClient, times(1)).getLock("BANK_RECHARGE_QUERY_LOCK");
        verify(redisTemplate, times(1)).opsForHash();
        verify(selectMapper, times(1)).selectResponseInOneHour(any(String.class));
        verify(queryTradeService, times(rechargeRequests.size())).query(any(String.class), any(String.class), any(QueryTradeType.class));
        verify(updateMapper, times(0)).updateQueryResponse(any(String.class), any(ResponseDto.class));
        verify(messageQueueClient, times(0)).publishMessage(any(MessageTopic.Recharge.getClass()), any(Object.class));
    }

    private List<BaseRequestDto> getRequestData() {
        List<BaseRequestDto> rechargeRequests = new ArrayList<>();
        BaseRequestDto r1 = new BaseRequestDto();
        r1.setOrderNo("123123");
        r1.setOrderDate("20180809");
        BaseRequestDto r2 = new BaseRequestDto();
        r2.setOrderNo("123143");
        r2.setOrderDate("20180810");
        rechargeRequests.add(r1);
        rechargeRequests.add(r2);
        return rechargeRequests;
    }

}
