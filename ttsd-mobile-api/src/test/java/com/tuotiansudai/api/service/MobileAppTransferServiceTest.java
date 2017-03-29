package com.tuotiansudai.api.service;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppTransferServiceImpl;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppTransferServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppTransferServiceImpl mobileAppTransferServiceImpl;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Mock
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Mock
    private TransferService transferService;

    @Mock
    private MobileAppChannelService channelService;

    @Mock
    private AccountService accountService;

    @Mock
    private RandomUtils randomUtils;

    @Mock
    private PageValidUtils pageValidUtils;

    @Test
    public void shouldGetTransferTransferee() {
        long transferApplicationId = idGenerator.generate();
        long investId = idGenerator.generate();
        TransferTransfereeRequestDto transferTransfereeRequestDto = new TransferTransfereeRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        transferTransfereeRequestDto.setBaseParam(baseParam);
        transferTransfereeRequestDto.setPageSize(10);
        transferTransfereeRequestDto.setIndex(1);
        transferTransfereeRequestDto.setTransferApplicationId(transferApplicationId);

        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setId(transferApplicationId);
        transferApplicationModel.setInvestId(investId);
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setTransferAmount(1000);
        transferApplicationModel.setTransferTime(new Date());
        when(transferApplicationMapper.findById(anyLong())).thenReturn(transferApplicationModel);

        InvestModel investModel = new InvestModel();
        investModel.setId(investId);
        investModel.setLoginName("testTransferee");
        when(investMapper.findById(anyLong())).thenReturn(investModel);

        when(randomUtils.encryptMobile(anyString(), anyString(), anyLong())).thenReturn("test***");
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);

        BaseResponseDto<TransferTransfereeResponseDataDto> baseResponseDto =  mobileAppTransferServiceImpl.getTransferee(transferTransfereeRequestDto);
        assertThat(baseResponseDto.getData().getTransferee().get(0).getTransferAmount(), is("10.00"));
        assertThat(baseResponseDto.getData().getTransferee().get(0).getTransfereeLoginName(), is("test***"));
    }

    @Test
    public void shouldTransferNoPasswordPurchaseSuccess() throws Exception {
        TransferPurchaseRequestDto transferPurchaseRequestDto = new TransferPurchaseRequestDto();
        transferPurchaseRequestDto.setBaseParam(BaseParamTest.getInstance());
        transferPurchaseRequestDto.setTransferApplicationId(String.valueOf(idGenerator.generate()));

        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setTransferAmount(100000);
        transferApplicationModel.setLoanId(idGenerator.generate());
        when(transferApplicationMapper.findById(anyLong())).thenReturn(transferApplicationModel);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);

        AccountModel accountModel = new AccountModel();
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(true);
        when(accountService.findByLoginName(anyString())).thenReturn(accountModel);

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setSuccess(true);
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("order_id", "order_id").build()));
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
        when(transferService.noPasswordTransferPurchase(any(InvestDto.class))).thenReturn(baseDto);

        BaseResponseDto<InvestNoPassResponseDataDto> baseResponseDto = mobileAppTransferServiceImpl.transferNoPasswordPurchase(transferPurchaseRequestDto);
        assertTrue(baseResponseDto.isSuccess());
        assertThat(baseResponseDto.getCode(), is("0000"));
    }

    @Test
    public void shouldTransferNoPasswordPurchaseFailedInvestorNotOpen() throws Exception {
        TransferPurchaseRequestDto transferPurchaseRequestDto = new TransferPurchaseRequestDto();
        transferPurchaseRequestDto.setBaseParam(BaseParamTest.getInstance());
        transferPurchaseRequestDto.setTransferApplicationId(String.valueOf(idGenerator.generate()));

        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setTransferAmount(100000);
        transferApplicationModel.setLoanId(idGenerator.generate());
        when(transferApplicationMapper.findById(anyLong())).thenReturn(transferApplicationModel);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);

        AccountModel accountModel = new AccountModel();
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(false);
        when(accountService.findByLoginName(anyString())).thenReturn(accountModel);

        BaseDto<PayDataDto> baseDto = new BaseDto<>(new PayDataDto());
        baseDto.setSuccess(false);
        when(transferService.noPasswordTransferPurchase(any(InvestDto.class))).thenReturn(baseDto);

        InvestModel successInvest = new InvestModel();
        successInvest.setId(1);
        when(investMapper.findLatestSuccessInvest(anyString())).thenReturn(successInvest);

        BaseResponseDto<InvestNoPassResponseDataDto> baseResponseDto = mobileAppTransferServiceImpl.transferNoPasswordPurchase(transferPurchaseRequestDto);
        assertFalse(baseResponseDto.isSuccess());
    }

    @Test
    public void shouldPurchaseSuccess() throws Exception{
        TransferPurchaseRequestDto transferPurchaseRequestDto = new TransferPurchaseRequestDto();
        transferPurchaseRequestDto.setBaseParam(BaseParamTest.getInstance());
        transferPurchaseRequestDto.setTransferApplicationId(String.valueOf(idGenerator.generate()));
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(true);
        payFormDataDto.setUrl("url");
        BaseDto<PayFormDataDto> successResponseDto = new BaseDto<>();
        successResponseDto.setSuccess(true);
        successResponseDto.setData(payFormDataDto);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setTransferAmount(100000);
        transferApplicationModel.setLoanId(idGenerator.generate());
        when(transferApplicationMapper.findById(anyLong())).thenReturn(transferApplicationModel);
        when(transferService.transferPurchase(any(InvestDto.class))).thenReturn(successResponseDto);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);

        BaseResponseDto<InvestResponseDataDto> responseDto = mobileAppTransferServiceImpl.transferPurchase(transferPurchaseRequestDto);
        assertTrue(responseDto.isSuccess());
        assertThat(responseDto.getData().getUrl(), is("url"));
    }

}
