package com.tuotiansudai.paywrapper.service;


import com.tuotiansudai.client.SmsWrapperClient;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CreditLoanRepayNoPwdServiceMockTest {

    @InjectMocks
    private CreditLoanRepayService creditLoanRepayNoPwdService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.creditLoanRepayNoPwdService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.creditLoanRepayNoPwdService, this.redisWrapperClient);
    }

    @Test
    public void shouldLoanNoPwdRepayFailedWhenInvalidParameters() {
        int orderId = 1;
        String mobile = "13900000000";
        BaseDto<PayDataDto> dto = this.creditLoanRepayNoPwdService.noPwdRepay(orderId, mobile, 0);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款金额必须大于零"));

        when(accountMapper.findByMobile(mobile)).thenReturn(null);
        dto = this.creditLoanRepayNoPwdService.noPwdRepay(orderId, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));

        AccountModel accountModel = new AccountModel();
        accountModel.setNoPasswordInvest(false);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        dto = this.creditLoanRepayNoPwdService.noPwdRepay(orderId, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通免密支付功能"));

        accountModel.setNoPasswordInvest(true);
        when(accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        when(redisWrapperClient.get(MessageFormat.format("credit:loan:repay:{0}", String.valueOf(orderId)))).thenReturn(SyncRequestStatus.SUCCESS.name());
        dto = this.creditLoanRepayNoPwdService.noPwdRepay(orderId, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("您已还款成功"));
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
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> dto = new BaseDto<>(payDataDto);
        payDataDto.setStatus(true);

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        responseModel.setRetCode("0000");

        when(this.paySyncClient.send(eq(CreditLoanRepayNoPwdMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenReturn(responseModel);
        when(this.redisWrapperClient.get(anyString())).thenReturn("");
        when(this.redisWrapperClient.set(redisKeyCaptor.capture(), statusCaptor.capture())).thenReturn("");

        dto = this.creditLoanRepayNoPwdService.noPwdRepay(orderId, mobile, amount);

        assertThat(redisKeyCaptor.getValue(), is(MessageFormat.format("credit:loan:repay:{0}", String.valueOf(orderId))));
        assertThat(statusCaptor.getValue(), is(SyncRequestStatus.SENT.name()));

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(orderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getCode(), is("0000"));
    }

    @Test
    public void creditLoanRepayNoPwdFail() throws Exception {
        int orderId = 1;
        String mobile = "13900000000";
        int amount = 888;
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);
        when(this.accountMapper.findByMobile(mobile)).thenReturn(accountModel);

        when(this.redisWrapperClient.get(anyString())).thenReturn("");
        when(this.paySyncClient.send(eq(CreditLoanRepayNoPwdMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenThrow(new PayException("error"));

        BaseDto<PayDataDto> dataDtoBaseDto = this.creditLoanRepayNoPwdService.noPwdRepay(orderId, mobile, amount);

        assertTrue(requestModelCaptor.getValue().getOrderId().startsWith(String.valueOf(orderId) + "X"));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(amount)));

        assertThat(dataDtoBaseDto.getData().getMessage(), is("error"));
        assertFalse(dataDtoBaseDto.getData().getStatus());
    }

}
