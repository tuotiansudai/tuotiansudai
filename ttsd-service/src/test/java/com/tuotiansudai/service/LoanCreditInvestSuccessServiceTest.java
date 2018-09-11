package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.BankUserBillBusinessType;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.fudian.message.BankLoanCreditInvestMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.BankSystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class LoanCreditInvestSuccessServiceTest {

    @InjectMocks
    private LoanCreditInvestSuccessService loanCreditInvestSuccessService;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Mock
    private UserMapper userMapper;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void investSuccess(){

        ArgumentCaptor<TransferApplicationModel> updateTransferApplicationModelCaptor = ArgumentCaptor.forClass(TransferApplicationModel.class);
        ArgumentCaptor<Long> updateTransfererIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<InvestModel> updateTransfereeInvestModelCaptor = ArgumentCaptor.forClass(InvestModel.class);
        ArgumentCaptor<InvestRepayModel> updateTransfererRepayModelCaptor = ArgumentCaptor.forClass(InvestRepayModel.class);
        ArgumentCaptor<List<InvestRepayModel>> createTransfereeRepayModelCaptor = ArgumentCaptor.forClass((Class) List.class);
        ArgumentCaptor<List<AmountTransferMessage>> amountTransferMessageCaptor = ArgumentCaptor.forClass((Class) List.class);
        ArgumentCaptor<BankSystemBillMessage> bankSystemBillMessageCaptor = ArgumentCaptor.forClass(BankSystemBillMessage.class);

        when(transferApplicationMapper.findById(anyLong())).thenReturn(mockTransferApplicationModel());

        when(investMapper.findById(eq(1L))).thenReturn(mockInvestModel(1, "transferer", TransferStatus.TRANSFERRING, InvestStatus.SUCCESS));
        when(investMapper.findById(eq(2L))).thenReturn(mockInvestModel(2, "transferee", TransferStatus.TRANSFERABLE, InvestStatus.WAIT_PAY));
        when(investRepayMapper.findByInvestIdAndPeriodAsc(eq(1L))).thenReturn(mockTransfererInvestRepay());
        when(userMapper.findByLoginName(eq("transferer"))).thenReturn(mockUserModel());

        loanCreditInvestSuccessService.processLoanCreditInvestSuccess(mockBankLoanCreditInvestMessage());

        verify(transferApplicationMapper, times(1)).update(updateTransferApplicationModelCaptor.capture());
        verify(investMapper, times(1)).updateTransferStatus(updateTransfererIdCaptor.capture(), eq(TransferStatus.SUCCESS));
        verify(investMapper, times(1)).update(updateTransfereeInvestModelCaptor.capture());
        verify(investRepayMapper, times(3)).update(updateTransfererRepayModelCaptor.capture());
        verify(investRepayMapper, times(1)).create(createTransfereeRepayModelCaptor.capture());
        verify(mqWrapperClient, times(2)).sendMessage(eq(MessageQueue.AmountTransfer), amountTransferMessageCaptor.capture());
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.BankSystemBill), bankSystemBillMessageCaptor.capture());

        assertThat(updateTransferApplicationModelCaptor.getValue().getId(), is(1L));
        assertThat(updateTransferApplicationModelCaptor.getValue().getName(), is("ZR20180810-001"));
        assertThat(updateTransferApplicationModelCaptor.getValue().getStatus(), is(TransferStatus.SUCCESS));

        assertThat(updateTransfererIdCaptor.getValue(), is(1L));
        assertThat(updateTransfereeInvestModelCaptor.getValue().getId(), is(2L));
        assertThat(updateTransfereeInvestModelCaptor.getValue().getBankOrderNo(), is("111111"));

        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(0).getId(), is(1L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(1).getId(), is(2L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(2).getId(), is(3L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(0).getInvestId(), is(1L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(1).getInvestId(), is(1L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(2).getInvestId(), is(1L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(0).getExpectedInterest(), is(0L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(1).getExpectedInterest(), is(0L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(2).getExpectedInterest(), is(0L));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(updateTransfererRepayModelCaptor.getAllValues().get(2).getStatus(), is(RepayStatus.COMPLETE));

        assertThat(createTransfereeRepayModelCaptor.getValue().get(0).getInvestId(), is(2L));
        assertThat(createTransfereeRepayModelCaptor.getValue().get(1).getInvestId(), is(2L));
        assertThat(createTransfereeRepayModelCaptor.getValue().get(2).getInvestId(), is(2L));
        assertThat(createTransfereeRepayModelCaptor.getValue().get(0).getCorpus(), is(0L));
        assertThat(createTransfereeRepayModelCaptor.getValue().get(1).getCorpus(), is(0L));
        assertThat(createTransfereeRepayModelCaptor.getValue().get(2).getCorpus(), is(1000L));
        assertThat(createTransfereeRepayModelCaptor.getValue().get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(createTransfereeRepayModelCaptor.getValue().get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(createTransfereeRepayModelCaptor.getValue().get(2).getStatus(), is(RepayStatus.REPAYING));

        assertThat(amountTransferMessageCaptor.getAllValues().get(0).get(0).getLoginName(), is("transferee"));
        assertThat(amountTransferMessageCaptor.getAllValues().get(0).get(0).getAmount(), is(1000L));
        assertThat(amountTransferMessageCaptor.getAllValues().get(0).get(0).getBusinessType(), is(BankUserBillBusinessType.INVEST_TRANSFER_IN));

        assertThat(amountTransferMessageCaptor.getAllValues().get(1).get(0).getLoginName(), is("transferer"));
        assertThat(amountTransferMessageCaptor.getAllValues().get(1).get(0).getAmount(), is(1000L));
        assertThat(amountTransferMessageCaptor.getAllValues().get(1).get(0).getBusinessType(), is(BankUserBillBusinessType.INVEST_TRANSFER_OUT));
        assertThat(amountTransferMessageCaptor.getAllValues().get(1).get(1).getLoginName(), is("transferer"));
        assertThat(amountTransferMessageCaptor.getAllValues().get(1).get(1).getAmount(), is(10L));
        assertThat(amountTransferMessageCaptor.getAllValues().get(1).get(1).getBusinessType(), is(BankUserBillBusinessType.TRANSFER_FEE));

        assertThat(bankSystemBillMessageCaptor.getValue().getBusinessType(), is(SystemBillBusinessType.TRANSFER_FEE));
        assertThat(bankSystemBillMessageCaptor.getValue().getAmount(), is(10L));
    }

    @Test
    public void transferApplicationNullInvestFail(){
        when(transferApplicationMapper.findById(anyLong())).thenReturn(null);
        loanCreditInvestSuccessService.processLoanCreditInvestSuccess(mockBankLoanCreditInvestMessage());
        verify(transferApplicationMapper, times(1)).findById(anyLong());
        verify(investMapper, times(0)).findById(anyLong());
    }

    @Test
    public void transfererInvestNullInvestFail(){
        when(transferApplicationMapper.findById(anyLong())).thenReturn(mockTransferApplicationModel());
        when(investMapper.findById(anyLong())).thenReturn(null);
        loanCreditInvestSuccessService.processLoanCreditInvestSuccess(mockBankLoanCreditInvestMessage());
        verify(transferApplicationMapper, times(1)).findById(anyLong());
        verify(investMapper, times(1)).findById(anyLong());
        verify(transferApplicationMapper, times(0)).update(any());
    }

    private BankLoanCreditInvestMessage mockBankLoanCreditInvestMessage(){
        return new BankLoanCreditInvestMessage(1,2,1000, "transferee", "22222222222", "bankUserName", "bankAccountNo", "111111", "20180810");
    }

    private TransferApplicationModel mockTransferApplicationModel(){
        InvestModel investModel = mockInvestModel(1, "transferer", TransferStatus.TRANSFERRING, InvestStatus.SUCCESS);
        TransferApplicationModel model = new TransferApplicationModel(investModel, "ZR20180810-001", 1, 1000, 10, new Date(), 1, Source.WEB);
        model.setId(1);
        return model;
    }

    private InvestModel mockInvestModel(long id, String loginName, TransferStatus status, InvestStatus investStatus){
        InvestModel investModel = new InvestModel();
        investModel.setLoanId(1);
        investModel.setId(id);
        investModel.setLoginName(loginName);
        investModel.setAmount(1000);
        investModel.setTransferStatus(status);
        investModel.setStatus(investStatus);
        return investModel;
    }

    private List<InvestRepayModel> mockTransfererInvestRepay(){
        InvestRepayModel investRepayModel1 = new InvestRepayModel(1, 1, 1, 0, 10, 10, new Date(), RepayStatus.REPAYING);
        InvestRepayModel investRepayModel2 = new InvestRepayModel(2, 1, 2, 0, 10, 10, DateTime.now().plusMonths(1).toDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepayModel3 = new InvestRepayModel(3, 1, 3, 1000, 10, 10, DateTime.now().plusMonths(2).toDate(), RepayStatus.REPAYING);
        return Lists.newArrayList(investRepayModel1, investRepayModel2, investRepayModel3);
    }

    private UserModel mockUserModel(){
        UserModel userModel = new UserModel();
        userModel.setLoginName("transferer");
        userModel.setMobile("11111111111");
        return userModel;
    }
}
