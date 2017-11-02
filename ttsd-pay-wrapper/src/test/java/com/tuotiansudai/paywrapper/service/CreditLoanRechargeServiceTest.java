package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.credit.CreditLoanRechargeService;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanNopwdRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanPwdRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRechargeNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanRechargeMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CreditLoanRechargeModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.util.AmountConverter;
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
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyMapOf;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class CreditLoanRechargeServiceTest {

    @InjectMocks
    private CreditLoanRechargeService creditLoanRechargeService;

    @Mock
    private AccountMapper accountMapper;
    @Mock
    private CreditLoanRechargeMapper creditLoanRechargeMapper;
    @Mock
    private PaySyncClient paySyncClient;
    @Mock
    private PayAsyncClient payAsyncClient;
    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field creditLoanAgentField = this.creditLoanRechargeService.getClass().getDeclaredField("creditLoanAgent");
        creditLoanAgentField.setAccessible(true);
        creditLoanAgentField.set(this.creditLoanRechargeService, "00000000000");

    }

    @Test
    public void shouldRechargeFailedWhenAccountError() throws Exception {
        CreditLoanRechargeDto dto = new CreditLoanRechargeDto();
        dto.setMobile("mobile");
        dto.setOperatorLoginName("admin");
        dto.setAmount("100");

        BaseDto<PayDataDto> baseDtoNoPwd = this.creditLoanRechargeService.creditLoanRechargeNoPwd(dto);
        assertThat(baseDtoNoPwd.getData().getMessage(), is("该资金来源账户不是信用贷代理人"));

        BaseDto<PayFormDataDto> baseDtoPwd = creditLoanRechargeService.creditLoanRecharge(dto);
        assertThat(baseDtoNoPwd.getData().getMessage(), is("该资金来源账户不是信用贷代理人"));

    }

    @Test
    public void shouldRechargeNoPasswordSuccess() throws Exception {
        ArgumentCaptor<ProjectTransferNopwdRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferNopwdRequestModel.class);

        AccountModel accountModel = new AccountModel("creditLoan", "payUserId", "payAccountId", new Date());
        when(accountMapper.findByMobile(anyString())).thenReturn(accountModel);

        CreditLoanRechargeDto dto = new CreditLoanRechargeDto();
        dto.setMobile("00000000000");
        dto.setOperatorLoginName("admin");
        dto.setAmount("100");

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        responseModel.setRetCode("0000");

        when(this.paySyncClient.send(eq(CreditLoanNopwdRechargeMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class))).thenReturn(responseModel);
        BaseDto<PayDataDto> baseDto = this.creditLoanRechargeService.creditLoanRechargeNoPwd(dto);

        verify(this.paySyncClient, times(1)).send(eq(CreditLoanNopwdRechargeMapper.class), requestModelCaptor.capture(), eq(ProjectTransferNopwdResponseModel.class));

        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(AmountConverter.convertStringToCent(dto.getAmount()))));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertNotNull(requestModelCaptor.getValue().getOrderId());
        assertTrue(baseDto.getData().getStatus());
        assertThat(baseDto.getData().getCode(), is("0000"));
    }

    @Test
    public void shouldRechargePwdSuccess() throws Exception {
        ArgumentCaptor<ProjectTransferRequestModel> requestModelCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);

        AccountModel accountModel = new AccountModel("creditLoan", "payUserId", "payAccountId", new Date());
        when(accountMapper.findByMobile(anyString())).thenReturn(accountModel);

        CreditLoanRechargeDto dto = new CreditLoanRechargeDto();
        dto.setMobile("00000000000");
        dto.setOperatorLoginName("admin");
        dto.setAmount("100");

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(true);
        baseDto.setData(payFormDataDto);

        when(this.payAsyncClient.generateFormData(eq(CreditLoanPwdRechargeMapper.class), any(ProjectTransferRequestModel.class))).thenReturn(baseDto);
        BaseDto<PayFormDataDto> reqBaseDto = this.creditLoanRechargeService.creditLoanRecharge(dto);

        verify(this.payAsyncClient, times(1)).generateFormData(eq(CreditLoanPwdRechargeMapper.class), requestModelCaptor.capture());

        assertThat(requestModelCaptor.getValue().getAmount(), is(String.valueOf(AmountConverter.convertStringToCent(dto.getAmount()))));
        assertThat(requestModelCaptor.getValue().getUserId(), is(accountModel.getPayUserId()));
        assertNotNull(requestModelCaptor.getValue().getOrderId());
        assertTrue(reqBaseDto.getData().getStatus());
    }

    @Test
    public void shouldCreditLoanCallBackSuccess() throws Exception {

        long orderId = 1l;
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<RechargeStatus> statusCaptor = ArgumentCaptor.forClass(RechargeStatus.class);

        BaseCallbackRequestModel callbackRequestModel = new BaseCallbackRequestModel();
        callbackRequestModel.setOrderId(String.valueOf(orderId));
        callbackRequestModel.setRetCode("0000");
        callbackRequestModel.setResponseData("success");

        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class), anyString(), eq(CreditLoanRechargeNotifyMapper.class), eq(TransferNotifyRequestModel.class))).thenReturn(callbackRequestModel);

        CreditLoanRechargeModel creditLoanRechargeModel = new CreditLoanRechargeModel();
        creditLoanRechargeModel.setId(orderId);
        creditLoanRechargeModel.setAccountName("accountName");
        creditLoanRechargeModel.setAmount(100);
        creditLoanRechargeModel.setStatus(RechargeStatus.WAIT_PAY);

        when(creditLoanRechargeMapper.findById(orderId)).thenReturn(creditLoanRechargeModel);
        doNothing().when(mqWrapperClient).sendMessage(any(MessageQueue.class), any(Object.class));

        String message = this.creditLoanRechargeService.creditLoanRechargeCallback(Maps.newHashMap(), null);
        this.creditLoanRechargeMapper.updateCreditLoanRechargeStatus(idCaptor.capture(), statusCaptor.capture());

        verify(this.creditLoanRechargeMapper, times(2)).updateCreditLoanRechargeStatus(idCaptor.capture(), statusCaptor.capture());
        verify(this.mqWrapperClient, times(2)).sendMessage(any(MessageQueue.class), any(Object.class));

        assertThat(message, is("success"));
        assertThat(idCaptor.getAllValues().get(0), is(orderId));
        assertThat(statusCaptor.getAllValues().get(0), is(RechargeStatus.SUCCESS));
    }

    @Test
    public void shouldCreditLoanRechargeFailed() throws Exception {
        long orderId = 1l;
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<RechargeStatus> statusCaptor = ArgumentCaptor.forClass(RechargeStatus.class);

        BaseCallbackRequestModel callbackRequestModel = new BaseCallbackRequestModel();
        callbackRequestModel.setOrderId(String.valueOf(orderId));
        callbackRequestModel.setRetCode("code");

        CreditLoanRechargeModel creditLoanRechargeModel = new CreditLoanRechargeModel();
        creditLoanRechargeModel.setId(orderId);
        creditLoanRechargeModel.setAccountName("accountName");
        creditLoanRechargeModel.setAmount(100);

        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class), anyString(), eq(CreditLoanRechargeNotifyMapper.class), eq(TransferNotifyRequestModel.class))).thenReturn(null);
        String message = this.creditLoanRechargeService.creditLoanRechargeCallback(Maps.newHashMap(), null);
        assertNull(message);

        when(this.payAsyncClient.parseCallbackRequest(anyMapOf(String.class, String.class), anyString(), eq(CreditLoanRechargeNotifyMapper.class), eq(TransferNotifyRequestModel.class))).thenReturn(callbackRequestModel);
        when(creditLoanRechargeMapper.findById(orderId)).thenReturn(null);
        String message1 = this.creditLoanRechargeService.creditLoanRechargeCallback(Maps.newHashMap(), null);
        assertNull(message1);

        creditLoanRechargeModel.setStatus(RechargeStatus.FAIL);
        when(creditLoanRechargeMapper.findById(orderId)).thenReturn(creditLoanRechargeModel);
        String message2 = this.creditLoanRechargeService.creditLoanRechargeCallback(Maps.newHashMap(), null);
        assertNull(message2);

        creditLoanRechargeModel.setStatus(RechargeStatus.WAIT_PAY);
        callbackRequestModel.setResponseData("response");
        String message3 = this.creditLoanRechargeService.creditLoanRechargeCallback(Maps.newHashMap(), null);
        verify(this.creditLoanRechargeMapper, times(1)).updateCreditLoanRechargeStatus(idCaptor.capture(), statusCaptor.capture());

        assertThat(message3, is("response"));
        assertThat(idCaptor.getAllValues().get(0), is(orderId));
        assertThat(statusCaptor.getAllValues().get(0), is(RechargeStatus.FAIL));

    }
}
