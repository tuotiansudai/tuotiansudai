package com.tuotiansudai.paywrapper.service;


import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.credit.CreditLoanRepayService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRepayNoPwdMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class CreditLoanNoPasswordRepayServiceTest {

    @InjectMocks
    private CreditLoanRepayService creditLoanRepayService;

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
        Field redisWrapperClientField = this.creditLoanRepayService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.creditLoanRepayService, this.redisWrapperClient);
    }

    @Test
    public void shouldLoanNoPwdRepayFailedWhenInvalidParameters() {
        int orderId = 1;
        String mobile = "13900000000";
        BaseDto<PayDataDto> dto = this.creditLoanRepayService.noPasswordRepay(orderId, mobile, 0, false);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款金额必须大于零"));
        when(userMapper.lockByLoginName(anyString())).thenReturn(new UserModel());

        when(accountMapper.findByMobile(mobile)).thenReturn(null);
        dto = this.creditLoanRepayService.noPasswordRepay(orderId, mobile, 1, false);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));

        AccountModel accountModel = new AccountModel();
        accountModel.setNoPasswordInvest(false);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        dto = this.creditLoanRepayService.noPasswordRepay(orderId, mobile, 1, false);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通免密支付功能"));

        accountModel.setAutoInvest(false);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        dto = this.creditLoanRepayService.noPasswordRepay(orderId, mobile, 1, true);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未签署免密协议"));

        accountModel.setNoPasswordInvest(true);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(redisWrapperClient.exists(MessageFormat.format("credit:loan:password:repay:expired:{0}", String.valueOf(orderId)))).thenReturn(true);
        dto = this.creditLoanRepayService.noPasswordRepay(orderId, mobile, 1, false);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款交易进行中, 请30分钟后查看"));
    }

    @Test
    public void creditLoanRepayNoPwdSuccess() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        int amount = 888;
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        when(userMapper.lockByLoginName(anyString())).thenReturn(new UserModel());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        responseModel.setRetCode("0000");

        when(this.paySyncClient.send(eq(CreditLoanRepayNoPwdMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenReturn(responseModel);
        when(this.redisWrapperClient.exists(MessageFormat.format("credit:loan:password:repay:expired:{0}", String.valueOf(orderId)))).thenReturn(false);
        when(this.redisWrapperClient.exists(MessageFormat.format("credit:loan:repay:{0}", String.valueOf(orderId)))).thenReturn(false);

        BaseDto<PayDataDto> dto = this.creditLoanRepayService.noPasswordRepay(orderId, mobile, amount, false);

        verify(this.redisWrapperClient, times(2))
                .set(redisKeyCaptor.capture(), statusCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is(MessageFormat.format("credit:loan:repay:{0}", String.valueOf(orderId))));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(redisKeyCaptor.getAllValues().get(1), is(MessageFormat.format("credit:loan:repay:info:{0}", String.valueOf(orderId))));
        assertThat(statusCaptor.getAllValues().get(1), is(MessageFormat.format("{0}|{1}", mobile, String.valueOf(amount))));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(orderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is("0000"));
        assertNotNull(dto.getData().getExtraValues());
    }

    @Test
    public void creditLoanRepayNoPwdFail() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        int amount = 888;
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        responseModel.setRetCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        when(userMapper.lockByLoginName(anyString())).thenReturn(new UserModel());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);

        when(this.paySyncClient.send(eq(CreditLoanRepayNoPwdMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenReturn(responseModel);
        when(this.redisWrapperClient.exists(MessageFormat.format("credit:loan:password:repay:expired:{0}", String.valueOf(orderId)))).thenReturn(false);
        when(this.redisWrapperClient.exists(MessageFormat.format("credit:loan:repay:{0}", String.valueOf(orderId)))).thenReturn(false);

        BaseDto<PayDataDto> dto = this.creditLoanRepayService.noPasswordRepay(orderId, mobile, amount, false);

        verify(this.redisWrapperClient, times(2))
                .set(redisKeyCaptor.capture(), statusCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is(MessageFormat.format("credit:loan:repay:{0}", String.valueOf(orderId))));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(redisKeyCaptor.getAllValues().get(1), is(MessageFormat.format("credit:loan:repay:info:{0}", String.valueOf(orderId))));
        assertThat(statusCaptor.getAllValues().get(1), is(MessageFormat.format("{0}|{1}", mobile, String.valueOf(amount))));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(orderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertNotNull(dto.getData().getExtraValues());
    }

    @Test
    public void creditLoanRepayNoPwdException() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        int amount = 888;
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        when(userMapper.lockByLoginName(anyString())).thenReturn(new UserModel());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);

        when(this.paySyncClient.send(eq(CreditLoanRepayNoPwdMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenThrow(new PayException("error"));
        when(this.redisWrapperClient.exists(MessageFormat.format("credit:loan:password:repay:expired:{0}", String.valueOf(orderId)))).thenReturn(false);
        when(this.redisWrapperClient.exists(MessageFormat.format("credit:loan:repay:{0}", String.valueOf(orderId)))).thenReturn(false);

        BaseDto<PayDataDto> dto = this.creditLoanRepayService.noPasswordRepay(orderId, mobile, amount, false);

        verify(this.redisWrapperClient, times(2))
                .set(redisKeyCaptor.capture(), statusCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is(MessageFormat.format("credit:loan:repay:{0}", String.valueOf(orderId))));
        assertThat(statusCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));

        assertThat(redisKeyCaptor.getAllValues().get(1), is(MessageFormat.format("credit:loan:repay:info:{0}", String.valueOf(orderId))));
        assertThat(statusCaptor.getAllValues().get(1), is(MessageFormat.format("{0}|{1}", mobile, String.valueOf(amount))));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(orderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
    }
}
