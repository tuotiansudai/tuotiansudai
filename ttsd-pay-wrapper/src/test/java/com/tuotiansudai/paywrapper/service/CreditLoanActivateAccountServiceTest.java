package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.credit.CreditLoanActivateAccountService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanActivateAccountMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanNopwdActivateAccountMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class CreditLoanActivateAccountServiceTest {

    @InjectMocks
    private CreditLoanActivateAccountService creditLoanActivateAccountService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.creditLoanActivateAccountService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.creditLoanActivateAccountService, this.redisWrapperClient);
    }

    @Test
    public void shouldActivateAccountFailedFailedWhenInvalidParameters() {
        String mobile = "13999999999";
        when(accountMapper.findByMobile(mobile)).thenReturn(null);
        BaseDto<PayFormDataDto> dto = this.creditLoanActivateAccountService.passwordActivateAccount(mobile);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));

        when(accountMapper.findByMobile(mobile)).thenReturn(new AccountModel());
        when(redisWrapperClient.exists(MessageFormat.format("credit:loan:activate:account:concurrency:{0}", mobile))).thenReturn(true);
        dto = this.creditLoanActivateAccountService.passwordActivateAccount(mobile);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("正在激活账户, 请30分钟后查看"));

        when(accountMapper.findByMobile(mobile)).thenReturn(new AccountModel());
        when(redisWrapperClient.exists(MessageFormat.format("credit:loan:activate:account:concurrency:{0}", mobile))).thenReturn(false);
        when(redisWrapperClient.get(MessageFormat.format("credit:loan:activate:account:{0}", mobile))).thenReturn(SyncRequestStatus.SUCCESS.name());
        dto = this.creditLoanActivateAccountService.passwordActivateAccount(mobile);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("您已经激活过账户"));
    }


    @Test
    public void shouldGeneratePasswordActivateAccountSuccess() throws Exception {
        String mobile = "13999999999";
        int amount = 1;
        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> expiredCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);

        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        BaseDto<PayFormDataDto> dto = new BaseDto<>(payFormDataDto);
        payFormDataDto.setStatus(true);
        when(this.payAsyncClient.generateFormData(eq(CreditLoanActivateAccountMapper.class), any(ProjectTransferRequestModel.class))).thenReturn(dto);
        when(this.redisWrapperClient.exists(anyString())).thenReturn(false);

        assertThat(this.creditLoanActivateAccountService.passwordActivateAccount(mobile), is(dto));
        verify(this.redisWrapperClient, times(1))
                .setex(redisKeyCaptor.capture(), expiredCaptor.capture(), statusCaptor.capture());

        verify(this.payAsyncClient,times(1))
                .generateFormData(eq(CreditLoanActivateAccountMapper.class), requestModelCaptor.capture());

        assertThat(redisKeyCaptor.getValue(), is(MessageFormat.format("credit:loan:activate:account:concurrency:{0}", mobile)));
        assertThat(expiredCaptor.getValue(), is(30 * 60));
        assertThat(statusCaptor.getValue(), is(SyncRequestStatus.SENT.name()));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(mobile + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertTrue(dto.getData().getStatus());
    }

    @Test
    public void shouldGeneratePasswordActivateAccountFail() throws Exception {
        String mobile = "13999999999";
        int amount = 1;
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(this.payAsyncClient.generateFormData(eq(CreditLoanActivateAccountMapper.class), any(ProjectTransferRequestModel.class))).thenThrow(new PayException("error"));
        when(this.redisWrapperClient.exists(anyString())).thenReturn(false);

        BaseDto<PayFormDataDto> actual = this.creditLoanActivateAccountService.passwordActivateAccount(mobile);
        verify(this.redisWrapperClient, times(0)).setex(anyString(), anyInt(), anyString());
        verify(this.redisWrapperClient, times(0)).set(anyString(), anyString());
        verify(this.payAsyncClient, times(1))
                .generateFormData(eq(CreditLoanActivateAccountMapper.class), requestModelCaptor.capture());

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(mobile + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertFalse(actual.getData().getStatus());
        assertThat(actual.getData().getMessage(), is("发送激活账户数据失败"));
    }


    @Test
    public void shouldNoPasswordActivateAccountFailedWhenInvalidParameters() {
        String mobile = "13999999999";

        when(accountMapper.findByMobile(mobile)).thenReturn(null);
        BaseDto<PayDataDto> dto = this.creditLoanActivateAccountService.noPasswordActivateAccount(mobile);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));

        AccountModel accountModel = new AccountModel();
        accountModel.setNoPasswordInvest(false);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        dto = this.creditLoanActivateAccountService.noPasswordActivateAccount(mobile);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通免密支付功能"));

        accountModel.setNoPasswordInvest(true);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(redisWrapperClient.get(MessageFormat.format("credit:loan:activate:account:{0}", mobile))).thenReturn(SyncRequestStatus.SUCCESS.name());
        dto = this.creditLoanActivateAccountService.noPasswordActivateAccount(mobile);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("您已经激活过账户"));
    }

    @Test
    public void shouldNoPasswordActivateAccountSuccess() throws Exception {
        String mobile = "13999999999";
        int amount = 1;

        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> dto = new BaseDto<>(payDataDto);
        payDataDto.setStatus(true);

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        responseModel.setRetCode("0000");

        when(this.paySyncClient.send(eq(CreditLoanNopwdActivateAccountMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenReturn(responseModel);
        when(this.redisWrapperClient.get(anyString())).thenReturn("");
        when(this.redisWrapperClient.set(redisKeyCaptor.capture(), statusCaptor.capture())).thenReturn("");

        dto = this.creditLoanActivateAccountService.noPasswordActivateAccount(mobile);

        assertThat(redisKeyCaptor.getValue(), is(MessageFormat.format("credit:loan:activate:account:{0}", mobile)));
        assertThat(statusCaptor.getValue(), is(SyncRequestStatus.SENT.name()));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(mobile + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is("0000"));
    }

    @Test
    public void shouldNoPasswordActivateAccountFail() throws Exception {
        String mobile = "13999999999";
        int amount = 1;
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);

        when(this.redisWrapperClient.get(anyString())).thenReturn("");
        when(this.paySyncClient.send(eq(CreditLoanNopwdActivateAccountMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenThrow(new PayException("激活账户失败"));

        BaseDto<PayDataDto> dataDtoBaseDto = this.creditLoanActivateAccountService.noPasswordActivateAccount(mobile);

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(mobile + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertThat(dataDtoBaseDto.getData().getMessage(), is("激活账户失败"));
        assertFalse(dataDtoBaseDto.getData().getStatus());
    }


}
