package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.credit.CreditLoanActivateAccountService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanActivateAccountMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
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
import java.text.MessageFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CreditLoanActivateAccountServiceTest {

    @InjectMocks
    private CreditLoanActivateAccountService creditLoanActivateAccountService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

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
    public void shouldActivateAccountFailedWhenMobileIsNotExists() {
        String mobile = "13999999999";
        when(accountMapper.findByMobile(mobile)).thenReturn(null);
        BaseDto<PayFormDataDto> dto = this.creditLoanActivateAccountService.passwordActivateAccount(mobile);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));
    }

    @Test
    public void shouldActivateAccountFailedWhenAccountIsAcitving() {
        String mobile = "13999999999";
        when(accountMapper.findByMobile(mobile)).thenReturn(new AccountModel());

        when(redisWrapperClient.exists(MessageFormat.format("credit:loan:activate:account:concurrency:{0}",mobile))).thenReturn(true);
        BaseDto<PayFormDataDto> dto = this.creditLoanActivateAccountService.passwordActivateAccount(mobile);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("正在激活账户, 请30分钟后查看"));
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
        verify(this.payAsyncClient,times(1))
                .generateFormData(eq(CreditLoanActivateAccountMapper.class), requestModelCaptor.capture());

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(mobile + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertFalse(actual.getData().getStatus());
        assertThat(actual.getData().getMessage(), is("发送激活账户数据失败"));
    }
}
