package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.BankUserBillBusinessType;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.BankSystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class LoanFullServiceTest {

    @InjectMocks
    private LoanFullService loanFullService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private BankWrapperClient bankWrapperClient;

    @Mock
    private MQWrapperClient mqWrapperClient;

    private static final long loanId = 1;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = this.loanFullService.getClass().getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(this.loanFullService, this.bankWrapperClient);
    }

    @Test
    public void loanFullSuccess(){

        ArgumentCaptor<Long> loanIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<LoanModel> loanModelCaptor = ArgumentCaptor.forClass(LoanModel.class);
        ArgumentCaptor<List<AmountTransferMessage>> amountTransferMessageListCaptor = ArgumentCaptor.forClass((Class) List.class);
        ArgumentCaptor<BankSystemBillMessage> bankSystemBillMessageCaptor = ArgumentCaptor.forClass(BankSystemBillMessage.class);

        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        doNothing().when(investMapper).cleanWaitingInvest(anyLong());
        doNothing().when(loanMapper).update(any(LoanModel.class));

        loanFullService.processLoanFull(mockBankLoanFullMessage());

        verify(investMapper, times(1)).cleanWaitingInvest(loanIdCaptor.capture());
        verify(loanMapper, times(1)).update(loanModelCaptor.capture());
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.AmountTransfer), amountTransferMessageListCaptor.capture());
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.BankSystemBill), bankSystemBillMessageCaptor.capture());

        assertThat(loanIdCaptor.getValue(), is(loanId));
        assertThat(loanModelCaptor.getValue().getStatus(), is(LoanStatus.REPAYING));
        assertThat(loanModelCaptor.getValue().getRecheckLoginName(), is("checkerLoginName"));
        assertThat(amountTransferMessageListCaptor.getValue().get(0).getAmount(), is(10000L));
        assertThat(amountTransferMessageListCaptor.getValue().get(0).getBusinessType(), is(BankUserBillBusinessType.LOAN_SUCCESS));
        assertThat(amountTransferMessageListCaptor.getValue().get(1).getAmount(), is(10L));
        assertThat(amountTransferMessageListCaptor.getValue().get(1).getBusinessType(), is(BankUserBillBusinessType.LOAN_FEE));
        assertThat(bankSystemBillMessageCaptor.getValue().getAmount(), is(10L));
    }

    private LoanModel mockLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("agentLoginName");
        loanModel.setBaseRate(16.00);
        loanModel.setId(loanId);
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
        loanModel.setStatus(LoanStatus.RECHECK);
        loanModel.setLoanerLoginName("loanerLoginName");
        loanModel.setLoanerUserName("借款人");
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setLoanTxNo("loanTxNo");
        loanModel.setLoanFee(10);
        return loanModel;
    }

    private BankLoanFullMessage mockBankLoanFullMessage(){
        return new BankLoanFullMessage(loanId, "loanTxNo", "checkerLoginName", "111111", "20180810", "2018-08-10 10:00:00");
    }
}
