package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.luxury.LuxuryStageRepayService;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRepayNoPwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayNoPwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
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
import org.springframework.http.HttpStatus;
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
public class LuxuryStageNoPwdRepayServiceTest {

    @InjectMocks
    private LuxuryStageRepayService luxuryStageRepayService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PaySyncClient paySyncClient;

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
    public void shouldNoPwdRepayFailedWhenInvalidParameters() {
        long luxuryOrderId = 1;
        int period = 1;
        String mobile = "13900000000";
        BaseDto<PayDataDto> dto = this.luxuryStageRepayService.noPasswordRepay(luxuryOrderId, period, mobile, 0, true);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款金额必须大于零"));

        when(accountMapper.findByMobile(mobile)).thenReturn(null);
        dto = this.luxuryStageRepayService.noPasswordRepay(luxuryOrderId, period, mobile, 1, true);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));

        AccountModel accountModel = new AccountModel();
        accountModel.setNoPasswordInvest(false);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        dto = this.luxuryStageRepayService.noPasswordRepay(luxuryOrderId, period, mobile, 1, false);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通免密支付功能"));

        accountModel.setAutoInvest(false);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        dto = this.luxuryStageRepayService.noPasswordRepay(luxuryOrderId, period, mobile, 1, true);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未签署免密协议"));

        accountModel.setNoPasswordInvest(true);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(redisWrapperClient.exists(MessageFormat.format("luxury:stage:repay:expired:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period)))).thenReturn(true);
        dto = this.luxuryStageRepayService.noPasswordRepay(luxuryOrderId, period, mobile, 1, false);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款交易进行中, 请30分钟后查看"));
    }

    @Test
    public void LuxuryStageNoPwdRepayNoPwdSuccess() throws Exception {
        long luxuryOrderId = 1;
        int period = 2;
        String mobile = "13900000000";
        int amount = 10;
        ArgumentCaptor<String> setexKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> setexValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> setKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> setValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        responseModel.setRetCode("0000");

        when(this.redisWrapperClient.exists(anyString())).thenReturn(false);
        when(this.paySyncClient.send(eq(LuxuryStageRepayNoPwdMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenReturn(responseModel);

        BaseDto<PayDataDto> dto = this.luxuryStageRepayService.noPasswordRepay(luxuryOrderId, period , mobile, amount, false);

        verify(this.redisWrapperClient, times(1))
                .setex(setexKeyCaptor.capture(), anyInt(), setexValueCaptor.capture());

        verify(this.redisWrapperClient, times(1))
                .set(setKeyCaptor.capture(), setValueCaptor.capture());

        assertThat(setexKeyCaptor.getValue(), is(MessageFormat.format("luxury:stage:repay:info:{0}", String.valueOf(luxuryOrderId))));
        assertThat(setexValueCaptor.getValue(), is(MessageFormat.format("{0}|{1}|{2}|{3}",
                mobile, String.valueOf(luxuryOrderId), String.valueOf(period), String.valueOf(amount))));

        assertThat(setKeyCaptor.getValue(), is(MessageFormat.format("luxury:stage:repay:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period))));
        assertThat(setValueCaptor.getValue(), is(SyncRequestStatus.SENT.name()));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(luxuryOrderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is("0000"));
        assertNotNull(dto.getData().getExtraValues());
    }

    @Test
    public void LuxuryStageNoPwdRepayNoPwdFail() throws Exception {
        long luxuryOrderId = 1;
        int period = 2;
        String mobile = "13900000000";
        int amount = 10;
        ArgumentCaptor<String> setexKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> setexValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> setKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> setValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        responseModel.setRetCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));

        when(this.redisWrapperClient.exists(anyString())).thenReturn(false);
        when(this.paySyncClient.send(eq(LuxuryStageRepayNoPwdMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenReturn(responseModel);

        BaseDto<PayDataDto> dto = this.luxuryStageRepayService.noPasswordRepay(luxuryOrderId, period , mobile, amount, false);

        verify(this.redisWrapperClient, times(1))
                .setex(setexKeyCaptor.capture(), anyInt(), setexValueCaptor.capture());

        verify(this.redisWrapperClient, times(1))
                .set(setKeyCaptor.capture(), setValueCaptor.capture());

        assertThat(setexKeyCaptor.getValue(), is(MessageFormat.format("luxury:stage:repay:info:{0}", String.valueOf(luxuryOrderId))));
        assertThat(setexValueCaptor.getValue(), is(MessageFormat.format("{0}|{1}|{2}|{3}",
                mobile, String.valueOf(luxuryOrderId), String.valueOf(period), String.valueOf(amount))));

        assertThat(setKeyCaptor.getValue(), is(MessageFormat.format("luxury:stage:repay:{0}:{1}", String.valueOf(luxuryOrderId), String.valueOf(period))));
        assertThat(setValueCaptor.getValue(), is(SyncRequestStatus.SENT.name()));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(luxuryOrderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertNotNull(dto.getData().getExtraValues());
    }
}
