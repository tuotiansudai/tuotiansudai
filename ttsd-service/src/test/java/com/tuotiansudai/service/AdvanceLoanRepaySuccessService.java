package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.fudian.message.BankLoanRepayMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class AdvanceLoanRepaySuccessService {

    @InjectMocks
    private LoanRepaySuccessService loanRepaySuccessService;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loanRepaySuccess(){

        ArgumentCaptor<LoanRepayModel> loanRepayModelCaptor = ArgumentCaptor.forClass(LoanRepayModel.class);

        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        when(loanRepayMapper.findById(anyLong())).thenReturn(mockLoanRepayModel(1,1, RepayStatus.WAIT_PAY));
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(anyLong())).thenReturn(Lists.newArrayList(mockLoanRepayModel(1,1, RepayStatus.WAIT_PAY), mockLoanRepayModel(2,2, RepayStatus.REPAYING), mockLoanRepayModel(3,3, RepayStatus.REPAYING)));
        doNothing().when(transferApplicationMapper).update(any(TransferApplicationModel.class));
        when(userMapper.findByLoginName(anyString())).thenReturn(mockUserModel());

        loanRepaySuccessService.processAdvancedLoanRepaySuccess(mockBankLoanRepayMessage());

        verify(loanRepayMapper,times(1)).findById(anyLong());
        verify(loanRepayMapper,times(1)).findByLoanIdOrderByPeriodAsc(anyLong());
        verify(loanRepayMapper,times(3)).update(loanRepayModelCaptor.capture());
        verify(loanMapper, times(1)).updateStatus(anyLong(), eq(LoanStatus.COMPLETE));
        verify(mqWrapperClient,times(1)).sendMessage(eq(MessageQueue.AmountTransfer), anyListOf(AmountTransferMessage.class));

        assertThat(loanRepayModelCaptor.getAllValues().size(), is(3));
        assertThat(loanRepayModelCaptor.getAllValues().get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModelCaptor.getAllValues().get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModelCaptor.getAllValues().get(2).getStatus(), is(RepayStatus.COMPLETE));
    }

    @Test
    public void loanRepayFail(){
        when(loanRepayMapper.findById(anyLong())).thenReturn(mockLoanRepayModel(1,1, RepayStatus.COMPLETE));
        loanRepaySuccessService.processAdvancedLoanRepaySuccess(mockBankLoanRepayMessage());
        verify(loanRepayMapper,times(0)).findByLoanIdOrderByPeriodAsc(anyLong());
    }

    private BankLoanRepayMessage mockBankLoanRepayMessage(){
        return new BankLoanRepayMessage(1, 1, 1000, 10, false, "agentLoginName", "00000000000", "111111", "20180810");
    }

    private LoanRepayModel mockLoanRepayModel(long id, int period, RepayStatus status){
        return new LoanRepayModel(id, 1, period, period == 3 ? 100 : 0, 10, new Date(), status);
    }

    private List<TransferApplicationModel> mockTransferApplicationModel(){
        TransferApplicationModel model = new TransferApplicationModel();
        model.setLoanId(1);
        model.setStatus(TransferStatus.TRANSFERRING);
        return Lists.newArrayList(model);
    }

    private UserModel mockUserModel(){
        UserModel userModel = new UserModel();
        userModel.setLoginName("loginName");
        userModel.setMobile("11111111111");
        return userModel;
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
