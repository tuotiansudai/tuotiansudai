package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.credit.CreditLoanOutService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanOutProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanOutProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CreditLoanOutServiceTest {

    @InjectMocks
    private CreditLoanOutService creditLoanOutService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.creditLoanOutService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.creditLoanOutService, this.redisWrapperClient);
    }

    @Test
    public void shouldLoanOutFailedWhenInvalidParameters() {
        int orderId = 1;
        String mobile = "13900000000";
        BaseDto<PayDataDto> dto = this.creditLoanOutService.loanOut(orderId, mobile, 0);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.BAD_REQUEST)));
        assertThat(dto.getData().getMessage(), is("贷款金额必须大于零"));

        when(accountMapper.findByMobile(mobile)).thenReturn(null);
        dto = this.creditLoanOutService.loanOut(orderId, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.BAD_REQUEST)));
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));

        when(accountMapper.findByMobile(mobile)).thenReturn(new AccountModel());
        when(redisWrapperClient.hget("credit:loan:out", String.valueOf(orderId))).thenReturn(SyncRequestStatus.SUCCESS.name());
        dto = this.creditLoanOutService.loanOut(orderId, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.BAD_REQUEST)));
        assertThat(dto.getData().getMessage(), is("放款正在交易中，或已放款，请勿重复放款"));
    }

    @Test
    public void shouldLoanOutSuccess() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        int amount = 888;
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        responseModel.setRetCode("0000");
        when(this.paySyncClient.send(eq(CreditLoanOutProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class))).thenReturn(responseModel);
        when(this.redisWrapperClient.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        BaseDto<PayDataDto> dto = this.creditLoanOutService.loanOut(orderId, mobile, amount);
        verify(this.redisWrapperClient, times(2))
                .hset(redisKeyCaptor.capture(), orderIdCaptor.capture(), statusCaptor.capture());
        verify(this.paySyncClient,times(1))
                .send(eq(CreditLoanOutProjectTransferMapper.class), requestModelCaptor.capture(), eq(ProjectTransferResponseModel.class));

        assertThat(redisKeyCaptor.getAllValues().get(0), is("credit:loan:out"));
        assertThat(orderIdCaptor.getAllValues().get(0), is(String.valueOf(1)));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(redisKeyCaptor.getAllValues().get(1), is("credit:loan:out"));
        assertThat(orderIdCaptor.getAllValues().get(1), is(String.valueOf(1)));
        assertThat(statusCaptor.getAllValues().get(1), is(SyncRequestStatus.SUCCESS.name()));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(orderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.OK)));
    }

    @Test
    public void shouldLoanOutFailed() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        int amount = 888;
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(this.paySyncClient.send(eq(CreditLoanOutProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class))).thenReturn(new ProjectTransferResponseModel());
        when(this.redisWrapperClient.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        BaseDto<PayDataDto> dto = this.creditLoanOutService.loanOut(orderId, mobile, amount);
        verify(this.redisWrapperClient, times(2))
                .hset(redisKeyCaptor.capture(), orderIdCaptor.capture(), statusCaptor.capture());
        verify(this.paySyncClient,times(1))
                .send(eq(CreditLoanOutProjectTransferMapper.class), requestModelCaptor.capture(), eq(ProjectTransferResponseModel.class));

        assertThat(redisKeyCaptor.getAllValues().get(0), is("credit:loan:out"));
        assertThat(orderIdCaptor.getAllValues().get(0), is(String.valueOf(orderId)));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(redisKeyCaptor.getAllValues().get(1), is("credit:loan:out"));
        assertThat(orderIdCaptor.getAllValues().get(1), is(String.valueOf(orderId)));
        assertThat(statusCaptor.getAllValues().get(1), is(SyncRequestStatus.FAILURE.name()));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(orderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Test
    public void shouldLoanOutException() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        int amount = 888;
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(this.paySyncClient.send(eq(CreditLoanOutProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class))).thenThrow(new PayException("error"));
        when(this.redisWrapperClient.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        BaseDto<PayDataDto> dto = this.creditLoanOutService.loanOut(orderId, mobile, amount);
        verify(this.redisWrapperClient, times(1))
                .hset(redisKeyCaptor.capture(), orderIdCaptor.capture(), statusCaptor.capture());
        verify(this.paySyncClient,times(1))
                .send(eq(CreditLoanOutProjectTransferMapper.class), requestModelCaptor.capture(), eq(ProjectTransferResponseModel.class));

        assertThat(redisKeyCaptor.getAllValues().get(0), is("credit:loan:out"));
        assertThat(orderIdCaptor.getAllValues().get(0), is(String.valueOf(orderId)));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(orderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Test
    public void shouldLoanOutCallbackSuccess() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MessageQueue> queueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        BaseCallbackRequestModel callbackRequestModel = new BaseCallbackRequestModel();
        callbackRequestModel.setOrderId(String.valueOf(orderId) + "X");
        callbackRequestModel.setRetCode("0000");
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class),
                anyString(), eq(CreditLoanOutProjectTransferNotifyMapper.class), eq(ProjectTransferNotifyRequestModel.class)))
        .thenReturn(callbackRequestModel);

        when(this.redisWrapperClient.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        doNothing().when(this.mqWrapperClient).sendMessage(any(MessageQueue.class), anyMapOf(String.class, Object.class));

        this.creditLoanOutService.loanOutCallback(Maps.newHashMap(), null);
        verify(this.redisWrapperClient, times(1))
                .hset(redisKeyCaptor.capture(), orderIdCaptor.capture(), statusCaptor.capture());

        verify(this.mqWrapperClient, times(1))
                .sendMessage(queueCaptor.capture(), mapCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("credit:loan:out"));
        assertThat(orderIdCaptor.getAllValues().get(0), is(String.valueOf(orderId)));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SUCCESS.name()));

        assertThat(queueCaptor.getValue(), is(MessageQueue.CreditLoanOutQueue));
        assertThat(mapCaptor.getValue().get("order_id"), is(String.valueOf(orderId)));
        assertThat(mapCaptor.getValue().get("success"), is(true));
    }


    @Test
    public void shouldLoanOutCallbackFail() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MessageQueue> queueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        BaseCallbackRequestModel callbackRequestModel = new BaseCallbackRequestModel();
        callbackRequestModel.setOrderId(String.valueOf(orderId) + "X");
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class),
                anyString(), eq(CreditLoanOutProjectTransferNotifyMapper.class), eq(ProjectTransferNotifyRequestModel.class)))
                .thenReturn(callbackRequestModel);

        when(this.redisWrapperClient.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        doNothing().when(this.mqWrapperClient).sendMessage(any(MessageQueue.class), anyMapOf(String.class, Object.class));

        this.creditLoanOutService.loanOutCallback(Maps.newHashMap(), null);
        verify(this.redisWrapperClient, times(1))
                .hset(redisKeyCaptor.capture(), orderIdCaptor.capture(), statusCaptor.capture());

        verify(this.mqWrapperClient, times(1))
                .sendMessage(queueCaptor.capture(), mapCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("credit:loan:out"));
        assertThat(orderIdCaptor.getAllValues().get(0), is(String.valueOf(orderId)));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.FAILURE.name()));

        assertThat(queueCaptor.getValue(), is(MessageQueue.CreditLoanOutQueue));
        assertThat(mapCaptor.getValue().get("order_id"), is(String.valueOf(orderId)));
        assertThat(mapCaptor.getValue().get("success"), is(false));
    }
}
