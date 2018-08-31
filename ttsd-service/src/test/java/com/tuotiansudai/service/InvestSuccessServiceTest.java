package com.tuotiansudai.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.fudian.message.BankLoanInvestMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/30.
 */
@ActiveProfiles("test")
public class InvestSuccessServiceTest {
    @InjectMocks
    private InvestSuccessService investSuccessService;
    @Mock
    private InvestMapper investMapper;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processInvestSuccess() {
        InvestModel investModel = new InvestModel();
        investModel.setStatus(InvestStatus.WAIT_PAY);
        BankLoanInvestMessage bankLoanInvestMessage = new BankLoanInvestMessage(1l, "loanName", 2l, 2l, "loginName", "mobile", "bankUserName", "bankAccountNo", "bankOrderNo", "bankOrderDate");
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        investSuccessService.processInvestSuccess(bankLoanInvestMessage);
        //
        verify(investMapper, times(1)).findById(anyLong());
        verify(investMapper, times(1)).update(any(InvestModel.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.AmountTransfer), any(List.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.Invest_CompletePointTask), any(BankLoanInvestMessage.class));
        //私有方法的断言
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.EventMessage), any(EventMessage.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.PushMessage), any(PushMessage.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.WeChatMessageNotify), any(WeChatMessageNotify.class));
    }

    @Test()
    public void processInvestSuccessFalse() {
        investSuccessService.processInvestSuccess(new BankLoanInvestMessage());
        verify(investMapper, times(0)).update(any(InvestModel.class));
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.AmountTransfer), any(List.class));
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.Invest_CompletePointTask), any(BankLoanInvestMessage.class));
        //私有方法的断言
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.EventMessage), any(EventMessage.class));
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.PushMessage), any(PushMessage.class));
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.WeChatMessageNotify), any(WeChatMessageNotify.class));
    }

}
