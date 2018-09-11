package com.tuotiansudai.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.fudian.message.BankLoanRepayMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class NormalLoanRepaySuccessService {

    @InjectMocks
    private LoanRepaySuccessService loanRepaySuccessService;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loanRepaySuccess(){

        ArgumentCaptor<LoanRepayModel> loanRepayModelCaptor = ArgumentCaptor.forClass(LoanRepayModel.class);

        when(loanRepayMapper.findById(anyLong())).thenReturn(mockLoanRepayModel(1,1, RepayStatus.WAIT_PAY));
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        doNothing().when(loanRepayMapper).update(any(LoanRepayModel.class));
        when(loanRepayMapper.findLastLoanRepay(anyLong())).thenReturn(mockLoanRepayModel(3,3, RepayStatus.WAIT_PAY));
        doNothing().when(loanMapper).updateStatus(anyLong(), any());

        loanRepaySuccessService.processNormalLoanRepaySuccess(mockBankLoanRepayMessage());

        verify(loanRepayMapper,times(1)).findById(anyLong());
        verify(loanMapper,times(1)).findById(anyLong());
        verify(loanRepayMapper,times(1)).update(loanRepayModelCaptor.capture());
        verify(loanMapper, times(0)).updateStatus(anyLong(), any());
        verify(loanRepayMapper,times(1)).findLastLoanRepay(anyLong());
        verify(mqWrapperClient,times(1)).sendMessage(eq(MessageQueue.AmountTransfer), anyListOf(AmountTransferMessage.class));

        assertThat(loanRepayModelCaptor.getValue().getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModelCaptor.getValue().getBankOrderNo(), is("111111"));
    }

    @Test
    public void lastLoanRepaySuccess(){

        ArgumentCaptor<LoanRepayModel> loanRepayModelCaptor = ArgumentCaptor.forClass(LoanRepayModel.class);
        ArgumentCaptor<LoanStatus> loanStatusCaptor = ArgumentCaptor.forClass(LoanStatus.class);

        when(loanRepayMapper.findById(anyLong())).thenReturn(mockLoanRepayModel(3,3, RepayStatus.WAIT_PAY));
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        doNothing().when(loanRepayMapper).update(any(LoanRepayModel.class));
        when(loanRepayMapper.findLastLoanRepay(anyLong())).thenReturn(mockLoanRepayModel(3,3, RepayStatus.WAIT_PAY));
        doNothing().when(loanMapper).updateStatus(anyLong(), any());

        loanRepaySuccessService.processNormalLoanRepaySuccess(mockBankLoanRepayMessage());

        verify(loanRepayMapper,times(1)).findById(anyLong());
        verify(loanMapper,times(1)).findById(anyLong());
        verify(loanRepayMapper,times(1)).update(loanRepayModelCaptor.capture());
        verify(loanMapper, times(1)).updateStatus(anyLong(), loanStatusCaptor.capture());
        verify(loanRepayMapper,times(1)).findLastLoanRepay(anyLong());
        verify(mqWrapperClient,times(1)).sendMessage(eq(MessageQueue.AmountTransfer), anyListOf(AmountTransferMessage.class));

        assertThat(loanRepayModelCaptor.getValue().getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModelCaptor.getValue().getBankOrderNo(), is("111111"));
        assertThat(loanStatusCaptor.getValue(), is(LoanStatus.COMPLETE));
    }

    @Test
    public void loanRepayFail(){
        when(loanRepayMapper.findById(anyLong())).thenReturn(mockLoanRepayModel(1,1, RepayStatus.COMPLETE));
        loanRepaySuccessService.processNormalLoanRepaySuccess(mockBankLoanRepayMessage());
        verify(loanRepayMapper,times(1)).findById(anyLong());
        verify(loanMapper,times(0)).findById(anyLong());
    }

    private BankLoanRepayMessage mockBankLoanRepayMessage(){
        return new BankLoanRepayMessage(1, 1, 1000, 10, true, "agentLoginName", "00000000000", "111111", "20180810");
    }

    private LoanRepayModel mockLoanRepayModel(long id, int period, RepayStatus status){
        return new LoanRepayModel(id, 1, period, 1000, 10, new Date(), status);
    }

    private LoanModel mockLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("agentLoginName");
        loanModel.setBaseRate(16.00);
        loanModel.setId(1);
        loanModel.setName("房车抵押");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(3);
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
