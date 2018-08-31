package com.tuotiansudai.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.fudian.message.BankLoanCancelMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/31.
 */
@ActiveProfiles("test")
public class LoanCancelServiceTest {
    @InjectMocks
    private LoanCancelService loanCancelService;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private InvestMapper investMapper;
    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void cancelSuccess() {
        LoanModel loanModel = new LoanModel();
        List<InvestModel> investModelList = getSuccessInvest();
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investModelList);

        loanCancelService.cancel(new BankLoanCancelMessage(1l, "bankOrderNo", "bankOrderDate"));

        verify(loanMapper, times(1)).updateStatus(anyLong(), eq(LoanStatus.CANCEL));
        verify(investMapper, times(investModelList.size())).update(any(InvestModel.class));
        verify(mqWrapperClient, times(investModelList.size())).sendMessage(eq(MessageQueue.AmountTransfer), any(List.class));
    }

    @Test
    public void cancelFail() {
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.CANCEL);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        loanCancelService.cancel(new BankLoanCancelMessage(1l, "bankOrderNo", "bankOrderDate"));
        verify(loanMapper, times(0)).updateStatus(anyLong(), eq(LoanStatus.CANCEL));
        verify(investMapper, times(0)).findSuccessInvestsByLoanId(anyLong());
    }

    private List<InvestModel> getSuccessInvest() {
        List<InvestModel> list = new ArrayList<>();
        InvestModel i1 = new InvestModel();
        InvestModel i2 = new InvestModel();
        list.add(i1);
        list.add(i2);
        return list;
    }
}
