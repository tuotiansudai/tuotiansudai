package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanTransferAgentMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanTransferAgentNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.impl.CreditLoanTransferAgentServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyMapOf;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CreditLoanTransferAgentServiceTest {

    @InjectMocks
    private CreditLoanTransferAgentServiceImpl creditLoanTransferAgentService;

    @Mock
    private CreditLoanBillMapper creditLoanBillMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.creditLoanTransferAgentService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.creditLoanTransferAgentService, this.redisWrapperClient);
        Field creditLoanAgentField = this.creditLoanTransferAgentService.getClass().getDeclaredField("creditLoanAgent");
        creditLoanAgentField.setAccessible(true);
        creditLoanAgentField.set(this.creditLoanTransferAgentService, "00000000000");
    }

    @Test
    public void shouldCreditLoanTransferAgentIsSuccess() throws Exception {

        when(creditLoanBillMapper.findSumAmountByInAndBusinessType()).thenReturn(100l);
        when(creditLoanBillMapper.findSumAmountByOutAndBusinessType()).thenReturn(0l);

        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(accountMapper.findByMobile(anyString())).thenReturn(accountModel);

        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        responseModel.setRetCode("0000");

        when(this.redisWrapperClient.hset(redisKeyCaptor.capture(), orderIdCaptor.capture(), statusCaptor.capture())).thenReturn(1L);
        when(this.paySyncClient.send(eq(CreditLoanTransferAgentMapper.class), requestModelCaptor.capture(), eq(ProjectTransferResponseModel.class))).thenReturn(responseModel);

        this.creditLoanTransferAgentService.creditLoanTransferAgent();

        verify(this.paySyncClient, times(1))
                .send(eq(CreditLoanTransferAgentMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class));
        verify(this.redisWrapperClient, times(2))
                .hset(anyString(), anyString(), anyString());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("CREDIT_LOAN_TRANSFER_AGENT_KEY"));
        assertNotNull(orderIdCaptor.getAllValues().get(0));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(redisKeyCaptor.getAllValues().get(1), is("CREDIT_LOAN_TRANSFER_AGENT_KEY"));
        assertNotNull(orderIdCaptor.getAllValues().get(1));
        assertThat(statusCaptor.getAllValues().get(1), is(SyncRequestStatus.SUCCESS.name()));

        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is("100"));
        assertNotNull(requestModelCaptor.getValue().getOrderId());
    }

    @Test
    public void creditLoanTransferAgentIsFailed() throws Exception {
        when(creditLoanBillMapper.findSumAmountByInAndBusinessType()).thenReturn(100l);
        when(creditLoanBillMapper.findSumAmountByOutAndBusinessType()).thenReturn(0l);

        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(accountMapper.findByMobile(anyString())).thenReturn(accountModel);

        when(this.redisWrapperClient.hset(redisKeyCaptor.capture(), orderIdCaptor.capture(), statusCaptor.capture())).thenReturn(1L);
        when(this.paySyncClient.send(eq(CreditLoanTransferAgentMapper.class), requestModelCaptor.capture(), eq(ProjectTransferResponseModel.class))).thenReturn(new ProjectTransferResponseModel());

        this.creditLoanTransferAgentService.creditLoanTransferAgent();

        verify(this.paySyncClient, times(1))
                .send(eq(CreditLoanTransferAgentMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class));
        verify(this.redisWrapperClient, times(2))
                .hset(anyString(), anyString(), anyString());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("CREDIT_LOAN_TRANSFER_AGENT_KEY"));
        assertNotNull(orderIdCaptor.getAllValues().get(0));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(redisKeyCaptor.getAllValues().get(1), is("CREDIT_LOAN_TRANSFER_AGENT_KEY"));
        assertNotNull(orderIdCaptor.getAllValues().get(1));
        assertThat(statusCaptor.getAllValues().get(1), is(SyncRequestStatus.FAILURE.name()));

        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is("100"));
        assertNotNull(requestModelCaptor.getValue().getOrderId());

    }

    @Test
    public void creditLoanTransferException() throws Exception {
        when(creditLoanBillMapper.findSumAmountByInAndBusinessType()).thenReturn(100l);
        when(creditLoanBillMapper.findSumAmountByOutAndBusinessType()).thenReturn(0l);

        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(accountMapper.findByMobile(anyString())).thenReturn(accountModel);

        when(this.redisWrapperClient.hset(redisKeyCaptor.capture(), orderIdCaptor.capture(), statusCaptor.capture())).thenReturn(1L);
        when(this.paySyncClient.send(eq(CreditLoanTransferAgentMapper.class), requestModelCaptor.capture(), eq(ProjectTransferResponseModel.class))).thenThrow(new PayException("error"));

        this.creditLoanTransferAgentService.creditLoanTransferAgent();

        verify(this.paySyncClient, times(1))
                .send(eq(CreditLoanTransferAgentMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class));
        verify(this.redisWrapperClient, times(1))
                .hset(anyString(), anyString(), anyString());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("CREDIT_LOAN_TRANSFER_AGENT_KEY"));
        assertNotNull(orderIdCaptor.getAllValues().get(0));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is("100"));
        assertNotNull(requestModelCaptor.getValue().getOrderId());
    }

    @Test
    public void callBackIsSuccess() throws Exception {

        String orderId = "1";

        ArgumentCaptor<String> redisKeyCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisKeyCaptor2 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);

        when(creditLoanBillMapper.findSumAmountByInAndBusinessType()).thenReturn(100l);
        when(creditLoanBillMapper.findSumAmountByOutAndBusinessType()).thenReturn(0l);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(accountMapper.findByMobile(anyString())).thenReturn(accountModel);

        BaseCallbackRequestModel callbackRequestModel = new BaseCallbackRequestModel();
        callbackRequestModel.setRetCode("0000");
        callbackRequestModel.setOrderId(orderId);

        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class),
                anyString(), eq(CreditLoanTransferAgentNotifyMapper.class), eq(ProjectTransferNotifyRequestModel.class)))
                .thenReturn(callbackRequestModel);

        when(this.redisWrapperClient.hset(redisKeyCaptor1.capture(), redisKeyCaptor2.capture(), statusCaptor.capture())).thenReturn(1L);
        String responseDate = this.creditLoanTransferAgentService.creditLoanTransferAgentCallback(Maps.newHashMap(), null);
        verify(this.redisWrapperClient, times(1)).hset(anyString(), anyString(), anyString());

        assertThat(redisKeyCaptor1.getAllValues().get(0), is("CREDIT_LOAN_TRANSFER_AGENT_IN_BALANCE:1"));
        assertThat(redisKeyCaptor2.getAllValues().get(0), is("CREDIT_LOAN_TRANSFER"));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SUCCESS.name()));

    }

    @Test
    public void callBackIsFailed() throws Exception {

        String orderId = "1";

        ArgumentCaptor<String> redisKeyCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisKeyCaptor2 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);

        when(creditLoanBillMapper.findSumAmountByInAndBusinessType()).thenReturn(100l);
        when(creditLoanBillMapper.findSumAmountByOutAndBusinessType()).thenReturn(0l);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(accountMapper.findByMobile(anyString())).thenReturn(accountModel);

        BaseCallbackRequestModel callbackRequestModel = new BaseCallbackRequestModel();
        callbackRequestModel.setRetCode("0000");
        callbackRequestModel.setOrderId(orderId);

        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class),
                anyString(), eq(CreditLoanTransferAgentNotifyMapper.class), eq(ProjectTransferNotifyRequestModel.class)))
                .thenReturn(callbackRequestModel);
        doThrow(new RuntimeException()).when(mqWrapperClient).sendMessage(any(MessageQueue.class), any(AmountTransferMessage.class));

        when(this.redisWrapperClient.hset(redisKeyCaptor1.capture(), redisKeyCaptor2.capture(), statusCaptor.capture())).thenReturn(1L);
        String responseDate = this.creditLoanTransferAgentService.creditLoanTransferAgentCallback(Maps.newHashMap(), null);
        verify(this.redisWrapperClient, times(1)).hset(anyString(), anyString(), anyString());

        assertThat(redisKeyCaptor1.getAllValues().get(0), is("CREDIT_LOAN_TRANSFER_AGENT_IN_BALANCE:1"));
        assertThat(redisKeyCaptor2.getAllValues().get(0), is("CREDIT_LOAN_TRANSFER"));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.FAILURE.name()));

    }
}
