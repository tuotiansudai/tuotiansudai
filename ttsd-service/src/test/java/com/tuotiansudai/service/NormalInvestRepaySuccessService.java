package com.tuotiansudai.service;


import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.BankUserBillBusinessType;
import com.tuotiansudai.fudian.message.BankLoanCallbackMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.BankSystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class NormalInvestRepaySuccessService {

    @InjectMocks
    private InvestRepaySuccessService investRepaySuccessService;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SuppressWarnings({ "unchecked"})
    public void investRepaySuccess(){

        ArgumentCaptor<InvestRepayModel> investRepayModelCaptor = ArgumentCaptor.forClass(InvestRepayModel.class);
        ArgumentCaptor<List<AmountTransferMessage>> amountTransferMessageCaptor = ArgumentCaptor.forClass((Class)List.class);
        ArgumentCaptor<BankSystemBillMessage> systemBillMessageCaptor = ArgumentCaptor.forClass(BankSystemBillMessage.class);

        when(investMapper.findById(anyLong())).thenReturn(mockInvestModel());
        when(investRepayMapper.findById(anyLong())).thenReturn(mockInvestRepayModel(RepayStatus.REPAYING));
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        when(loanRepayMapper.findByLoanIdAndPeriod(anyLong(), anyInt())).thenReturn(mockLoanRepayModel());
        doNothing().when(investRepayMapper).update(any(InvestRepayModel.class));

        investRepaySuccessService.processNormalInvestRepaySuccess(mockBankLoanCallbackMessage());

        verify(investMapper, times(1)).findById(anyLong());
        verify(investRepayMapper, times(1)).findById(anyLong());
        verify(loanMapper, times(1)).findById(anyLong());
        verify(loanRepayMapper, times(1)).findByLoanIdAndPeriod(anyLong(), anyInt());
        verify(investRepayMapper, times(1)).update(investRepayModelCaptor.capture());
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.AmountTransfer), amountTransferMessageCaptor.capture());
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.BankSystemBill), systemBillMessageCaptor.capture());

        assertThat(investRepayModelCaptor.getValue().getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModelCaptor.getValue().getId(), is(1L));
        assertThat(investRepayModelCaptor.getValue().getBankOrderNo(), is("111111"));
        assertThat(investRepayModelCaptor.getValue().getRepayAmount(), is(1009L));
        assertThat(systemBillMessageCaptor.getValue().getAmount(), is(1L));
        assertThat(amountTransferMessageCaptor.getValue().size(), is(2));
        assertThat(amountTransferMessageCaptor.getValue().get(0).getAmount(), is(1010L));
        assertThat(amountTransferMessageCaptor.getValue().get(0).getBusinessType(), is(BankUserBillBusinessType.NORMAL_REPAY));
        assertThat(amountTransferMessageCaptor.getValue().get(1).getAmount(), is(1L));
        assertThat(amountTransferMessageCaptor.getValue().get(1).getBusinessType(), is(BankUserBillBusinessType.INVEST_FEE));
    }

    @Test
    public void investRepayFail(){
        when(investMapper.findById(anyLong())).thenReturn(mockInvestModel());
        when(investRepayMapper.findById(anyLong())).thenReturn(mockInvestRepayModel(RepayStatus.COMPLETE));
        investRepaySuccessService.processNormalInvestRepaySuccess(mockBankLoanCallbackMessage());
        verify(investMapper, times(1)).findById(anyLong());
        verify(investRepayMapper, times(1)).findById(anyLong());
        verify(loanMapper, times(0)).findById(anyLong());
    }

    private BankLoanCallbackMessage mockBankLoanCallbackMessage() {
        return new BankLoanCallbackMessage(1, 1, 1000, 10, 0, 1, "111111", "20180810", true);
    }

    private InvestModel mockInvestModel(){
        InvestModel investModel = new InvestModel(1, 1, null, "loginName", 1000, 0, false, new Date(), Source.WEB, null);
        investModel.setBankOrderNo("investOrderNo");
        investModel.setBankOrderDate("investOrderDate");
        investModel.setTradingTime(new Date());
        investModel.setStatus(InvestStatus.SUCCESS);
        return investModel;
    }

    private InvestRepayModel mockInvestRepayModel(RepayStatus status){
        return new InvestRepayModel(1, 1, 1, 1000, 10, 1, DateTime.now().plusDays(1).toDate(), status);
    }

    private LoanRepayModel mockLoanRepayModel(){
        LoanRepayModel loanRepayModel = new LoanRepayModel(1, 1, 1, 1000, 10, new Date(), RepayStatus.COMPLETE);
        loanRepayModel.setActualInterest(1010);
        loanRepayModel.setActualRepayDate(new Date());
        return loanRepayModel;
    }

    private LoanModel mockLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("agentLoginName");
        loanModel.setBaseRate(16.00);
        loanModel.setId(1);
        loanModel.setName("房车抵押");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(1);
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setLoanerLoginName("loanerLoginName");
        loanModel.setLoanerUserName("借款人");
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setLoanTxNo("loanTxNo");
        return loanModel;
    }
}
