package com.tuotiansudai.mq.consumer.loan.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.message.BankLoanInvestMessage;
import com.tuotiansudai.mq.consumer.loan.InvestSuccessCheckLoanFullMessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class InvestSuccessCheckLoanFullMessageConsumerTest {

    @InjectMocks
    private InvestSuccessCheckLoanFullMessageConsumer investSuccessCheckLoanFullMessageConsumer;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestMapper investMapper;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkFullSuccess(){
        Gson gson = new GsonBuilder().create();
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        when(investMapper.findById(anyLong())).thenReturn(mockInvestModel(2, 500));
        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(Lists.newArrayList(mockInvestModel(1, 500), mockInvestModel(2, 500)));
        investSuccessCheckLoanFullMessageConsumer.consume(gson.toJson(mockBankLoanInvestMessage()));
        verify(loanMapper, times(1)).updateRaisingCompleteTime(anyLong());
    }

    @Test
    public void checkFullFail(){
        Gson gson = new GsonBuilder().create();
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        when(investMapper.findById(anyLong())).thenReturn(mockInvestModel(2, 500));
        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(Lists.newArrayList(mockInvestModel(2, 500)));
        investSuccessCheckLoanFullMessageConsumer.consume(gson.toJson(mockBankLoanInvestMessage()));
        verify(loanMapper, times(0)).updateRaisingCompleteTime(anyLong());
    }

    private BankLoanInvestMessage mockBankLoanInvestMessage(){
        return new BankLoanInvestMessage(1L, "loanName", 2L, 500L, "loginName", "mobile", "bankUserName", "bankAccountNo", "bankOrderNo", "bankOrderDate");
    }

    private LoanModel mockLoanModel(){
        LoanModel loanModel = new LoanModel();
        loanModel.setId(1);
        loanModel.setLoanAmount(1000);
        return loanModel;
    }

    private InvestModel mockInvestModel(long id, long amount){
        InvestModel investModel = new InvestModel();
        investModel.setId(id);
        investModel.setAmount(amount);
        investModel.setStatus(InvestStatus.SUCCESS);
        return investModel;
    }
}
