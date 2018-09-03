package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class GenerateRepayServiceTest {

    @InjectMocks
    private GenerateRepayService generateRepayService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    private static final long loanId = 1;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void generateRepaySuccess(){

        ArgumentCaptor<List<LoanRepayModel>> loanRepayModelCaptor = ArgumentCaptor.forClass((Class) List.class);
        ArgumentCaptor<List<InvestRepayModel>> investRepayModelCaptor = ArgumentCaptor.forClass((Class) List.class);

        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(mockInvestModelList());
        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(null);
        when(loanRepayMapper.findByLoanIdAndPeriod(anyLong(), anyInt())).thenReturn(null);

        generateRepayService.generateRepay(mockBankLoanFullMessage());

        verify(loanRepayMapper, times(1)).create(loanRepayModelCaptor.capture());
        verify(investRepayMapper, times(1)).create(investRepayModelCaptor.capture());

        assertThat(loanRepayModelCaptor.getValue().size(), is(3));
        assertThat(loanRepayModelCaptor.getValue().get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModelCaptor.getValue().get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModelCaptor.getValue().get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModelCaptor.getValue().get(2).getCorpus(), is(2000L));

        assertThat(investRepayModelCaptor.getValue().size(), is(6));
        assertThat(investRepayModelCaptor.getValue().get(0).getInvestId(), is(1L));
        assertThat(investRepayModelCaptor.getValue().get(1).getInvestId(), is(2L));
        assertThat(investRepayModelCaptor.getValue().get(2).getInvestId(), is(1L));
        assertThat(investRepayModelCaptor.getValue().get(3).getInvestId(), is(2L));
        assertThat(investRepayModelCaptor.getValue().get(4).getInvestId(), is(1L));
        assertThat(investRepayModelCaptor.getValue().get(4).getCorpus(), is(1000L));
        assertThat(investRepayModelCaptor.getValue().get(5).getInvestId(), is(2L));
        assertThat(investRepayModelCaptor.getValue().get(5).getCorpus(), is(1000L));
    }

    private BankLoanFullMessage mockBankLoanFullMessage(){
        return new BankLoanFullMessage(loanId, "loanTxNo", "checkerLoginName", "111111", "20180810", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
    }

    private LoanModel mockLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("agentLoginName");
        loanModel.setBaseRate(16.00);
        loanModel.setId(loanId);
        loanModel.setName("房车抵押");
        loanModel.setActivityRate(0);
        loanModel.setPeriods(3);
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(DateTime.now().minusDays(1).toDate());
        loanModel.setFundraisingStartTime(DateTime.now().plusDays(1).toDate());
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(2000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(2000);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(DateTime.now().minusDays(1).toDate());
        loanModel.setStatus(LoanStatus.RECHECK);
        loanModel.setLoanerLoginName("loanerLoginName");
        loanModel.setLoanerUserName("借款人");
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setLoanTxNo("loanTxNo");
        loanModel.setLoanFee(10);
        loanModel.setDeadline(DateTime.now().plusMonths(3).minusDays(3).toDate());
        loanModel.setRecheckTime(DateTime.now().toDate());
        loanModel.setRecheckLoginName("checkerLoginName");
        return loanModel;
    }

    private List<InvestModel> mockInvestModelList(){
        InvestModel investModel1 = new InvestModel(1, loanId, null, "invest1", 1000, 0, false, new Date(), Source.WEB, null);
        investModel1.setStatus(InvestStatus.SUCCESS);
        investModel1.setBankOrderNo("111111");
        investModel1.setBankOrderDate("20180810");
        InvestModel investModel2 = new InvestModel(2, loanId, null, "invest2", 1000, 0, false, new Date(), Source.WEB, null);
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel2.setBankOrderNo("222222");
        investModel2.setBankOrderDate("20180810");
        return Lists.newArrayList(investModel1, investModel2);
    }
}
