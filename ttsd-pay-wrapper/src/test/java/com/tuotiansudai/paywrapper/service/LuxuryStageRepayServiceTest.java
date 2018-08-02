package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.luxury.LuxuryStageRepayService;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class LuxuryStageRepayServiceTest {

    @InjectMocks
    private LuxuryStageRepayService luxuryStageRepayService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.luxuryStageRepayService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.luxuryStageRepayService, this.redisWrapperClient);
    }

    @Test
    public void shouldRepayFailedWhenInvalidParameters() {
        long luxuryOrderId = 1;
        int period = 1;
        String mobile = "13900000000";
        BaseDto<PayFormDataDto> dto = this.luxuryStageRepayService.repay(luxuryOrderId, period, mobile, 0);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款金额必须大于零"));

        when(accountMapper.findByMobile(mobile)).thenReturn(null);
        dto = this.luxuryStageRepayService.repay(luxuryOrderId, period, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));

        when(accountMapper.findByMobile(mobile)).thenReturn(new AccountModel());
        when(redisWrapperClient.exists(MessageFormat.format("luxury:stage:repay:expired:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period)))).thenReturn(true);

        dto = this.luxuryStageRepayService.repay(luxuryOrderId, period, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款交易进行中, 请30分钟后查看"));
    }

    @Test
    public void shouldGenerateRepayFormSuccess() throws Exception {
        long luxuryOrderId = 1;
        int period = 2;
        String mobile = "13900000000";
        int amount = 10;
        ArgumentCaptor<String> expiredKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> expiredValueCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> expiredStatusCaptor = ArgumentCaptor.forClass(String.class);

        ArgumentCaptor<String> setKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> setValueCaptor = ArgumentCaptor.forClass(String.class);

        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        BaseDto<PayFormDataDto> dto = new BaseDto<>(payFormDataDto);
        payFormDataDto.setStatus(true);
        when(this.payAsyncClient.generateFormData(eq(LuxuryStageRepayProjectTransferMapper.class), any(ProjectTransferRequestModel.class))).thenReturn(dto);
        when(this.redisWrapperClient.exists(anyString())).thenReturn(false);

        assertThat(this.luxuryStageRepayService.repay(luxuryOrderId, period, mobile, amount), is(dto));

        verify(this.redisWrapperClient, times(2))
                .setex(expiredKeyCaptor.capture(), expiredValueCaptor.capture(), expiredStatusCaptor.capture());

        verify(this.redisWrapperClient, times(1))
                .set(setKeyCaptor.capture(), setValueCaptor.capture());


        verify(this.payAsyncClient, times(1))
                .generateFormData(eq(LuxuryStageRepayProjectTransferMapper.class), requestModelCaptor.capture());

        assertThat(expiredKeyCaptor.getAllValues().get(0), is(MessageFormat.format("luxury:stage:repay:expired:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period))));
        assertThat(expiredValueCaptor.getAllValues().get(0), is(30 * 60));
        assertThat(expiredStatusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(expiredKeyCaptor.getAllValues().get(1), is(MessageFormat.format("luxury:stage:repay:info:{0}", String.valueOf(luxuryOrderId))));
        assertThat(expiredValueCaptor.getAllValues().get(1), is(24 * 60 * 60));
        assertThat(expiredStatusCaptor.getAllValues().get(1), is(MessageFormat.format("{0}|{1}|{2}|{3}",
                mobile, String.valueOf(luxuryOrderId), String.valueOf(period), String.valueOf(amount))));

        assertThat(setKeyCaptor.getValue(), is(MessageFormat.format("luxury:stage:repay:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period))));
        assertThat(setValueCaptor.getValue(), is(SyncRequestStatus.SENT.name()));


        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(luxuryOrderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertTrue(dto.getData().getStatus());
    }

    @Test
    public void shouldGenerateRepayFormFail() throws PayException {
        long luxuryOrderId = 1;
        int period = 2;
        String mobile = "13900000000";
        int amount = 888;
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(this.payAsyncClient.generateFormData(eq(LuxuryStageRepayProjectTransferMapper.class), any(ProjectTransferRequestModel.class))).thenThrow(new PayException("error"));
        when(this.redisWrapperClient.exists(anyString())).thenReturn(false);

        BaseDto<PayFormDataDto> actual = this.luxuryStageRepayService.repay(luxuryOrderId, period, mobile, amount);
        verify(this.redisWrapperClient, times(0)).setex(anyString(), anyInt(), anyString());
        verify(this.redisWrapperClient, times(0)).set(anyString(), anyString());
        verify(this.payAsyncClient, times(1))
                .generateFormData(eq(LuxuryStageRepayProjectTransferMapper.class), requestModelCaptor.capture());

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(luxuryOrderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertFalse(actual.getData().getStatus());
        assertThat(actual.getData().getMessage(), is("生成交易数据失败"));
    }

    @Test
    public void shouldRepayCallbackSuccess() {
        long luxuryOrderId = 1;
        int period = 2;
        String mobile = "13900000000";
        long amount = 888;
        ArgumentCaptor<MessageQueue> queueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        BaseCallbackRequestModel callbackRequestModel = new BaseCallbackRequestModel();
        callbackRequestModel.setOrderId(String.valueOf(luxuryOrderId) + "X");
        callbackRequestModel.setRetCode("0000");
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        UserModel userModel = new UserModel();
        userModel.setLoginName(accountModel.getLoginName());
        when(this.userMapper.findByMobile(mobile)).thenReturn(userModel);
        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class),
                anyString(), eq(LuxuryStageRepayProjectTransferNotifyMapper.class), eq(ProjectTransferNotifyRequestModel.class)))
                .thenReturn(callbackRequestModel);

        when(this.redisWrapperClient.get(MessageFormat.format("luxury:stage:repay:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period)))).thenReturn(SyncRequestStatus.SENT.name());
        when(this.redisWrapperClient.get(MessageFormat.format("luxury:stage:repay:info:{0}", String.valueOf(luxuryOrderId))))
                .thenReturn(MessageFormat.format("{0}|{1}|{2}|{3}",
                        mobile, String.valueOf(luxuryOrderId), String.valueOf(period), String.valueOf(amount)));

        doNothing().when(this.mqWrapperClient).sendMessage(any(), any());

        this.luxuryStageRepayService.repayCallback(Maps.newHashMap(), null);

        verify(this.redisWrapperClient, times(1))
                .set(MessageFormat.format("luxury:stage:repay:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period)), SyncRequestStatus.SUCCESS.name());

        verify(this.mqWrapperClient, times(3))
                .sendMessage(queueCaptor.capture(), messageCaptor.capture());

        assertThat(queueCaptor.getAllValues().get(0), is(MessageQueue.LuxuryStageRepayQueue));
        assertThat(((Map) messageCaptor.getAllValues().get(0)).get("order_id"), is(luxuryOrderId));
        assertThat(((Map) messageCaptor.getAllValues().get(0)).get("period"), is(period));
        assertThat(((Map) messageCaptor.getAllValues().get(0)).get("success"), is(true));

        assertThat(queueCaptor.getAllValues().get(1), is(MessageQueue.AmountTransfer));
        assertThat(((AmountTransferMessage) messageCaptor.getAllValues().get(1)).getTransferType(), is(TransferType.TRANSFER_OUT_BALANCE));
        assertThat(((AmountTransferMessage) messageCaptor.getAllValues().get(1)).getLoginName(), is(userModel.getLoginName()));
        assertThat(((AmountTransferMessage) messageCaptor.getAllValues().get(1)).getOrderId(), is(luxuryOrderId));
        assertThat(((AmountTransferMessage) messageCaptor.getAllValues().get(1)).getBusinessType(), is(UserBillBusinessType.LUXURY_STAGE_REPAY));

        assertThat(queueCaptor.getAllValues().get(2), is(MessageQueue.CreditLoanBill));
//        assertThat(((CreditLoanBillModel) messageCaptor.getAllValues().get(2)).getOrderId(), is(luxuryOrderId));
//        assertThat(((CreditLoanBillModel) messageCaptor.getAllValues().get(2)).getAmount(), is(amount));
//        assertThat(((CreditLoanBillModel) messageCaptor.getAllValues().get(2)).getBusinessType(), is(CreditLoanBillBusinessType.CREDIT_LOAN_REPAY));
//        assertThat(((CreditLoanBillModel) messageCaptor.getAllValues().get(2)).getOperationType(), is(CreditLoanBillOperationType.IN));
//        assertThat(((CreditLoanBillModel) messageCaptor.getAllValues().get(2)).getMobile(), is(mobile));
    }

    @Test
    public void shouldLoanRepayCallbackFail() {
        long luxuryOrderId = 1;
        int period = 2;
        String mobile = "13900000000";
        long amount = 888;
        ArgumentCaptor<MessageQueue> queueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        BaseCallbackRequestModel callbackRequestModel = new BaseCallbackRequestModel();
        callbackRequestModel.setOrderId(String.valueOf(luxuryOrderId) + "X");
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        UserModel userModel = new UserModel();
        userModel.setLoginName(accountModel.getLoginName());
        when(this.userMapper.findByMobile(mobile)).thenReturn(userModel);
        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class),
                anyString(), eq(LuxuryStageRepayProjectTransferNotifyMapper.class), eq(ProjectTransferNotifyRequestModel.class)))
                .thenReturn(callbackRequestModel);

        when(this.redisWrapperClient.get(MessageFormat.format("luxury:stage:repay:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period)))).thenReturn(SyncRequestStatus.SENT.name());
        when(this.redisWrapperClient.get(MessageFormat.format("luxury:stage:repay:info:{0}", String.valueOf(luxuryOrderId))))
                .thenReturn(MessageFormat.format("{0}|{1}|{2}|{3}",
                        mobile, String.valueOf(luxuryOrderId), String.valueOf(period), String.valueOf(amount)));

        doNothing().when(this.mqWrapperClient).sendMessage(any(), any());

        this.luxuryStageRepayService.repayCallback(Maps.newHashMap(), null);

        verify(this.mqWrapperClient, times(1))
                .sendMessage(queueCaptor.capture(), messageCaptor.capture());

        assertThat(queueCaptor.getAllValues().get(0), is(MessageQueue.LuxuryStageRepayQueue));
        assertThat(((Map) messageCaptor.getAllValues().get(0)).get("order_id"), is(luxuryOrderId));
        assertThat(((Map) messageCaptor.getAllValues().get(0)).get("period"), is(period));
        assertThat(((Map) messageCaptor.getAllValues().get(0)).get("success"), is(false));
    }
}
