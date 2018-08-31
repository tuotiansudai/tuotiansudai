package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.fudian.dto.BankLoanRepayInvestDto;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.impl.RepayServiceImpl;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by qduljs2011 on 2018/8/31.
 */
public class AdvanceLoanRepayServiceTest {
    @InjectMocks
    private RepayServiceImpl repayService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private BankWrapperClient bankWrapperClient;

    @Test
    public void advancedRepaySuccess() {
        LoanModel loanModel = getLoanModel();
        ArgumentCaptor<List<BankLoanRepayInvestDto>> bankLoanRepayInvestDto=ArgumentCaptor.forClass((Class)List.class);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId())).thenReturn(getLoanRepayModels());
        when(investRepayMapper.queryBankInvestRepayData(anyLong(),anyInt())).thenReturn(getBanLoanRepayInvestList());
        List<BankLoanRepayInvestDto> bankLoanRepayInvests=null;

    }

    private LoanModel getLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setId(1l);
        loanModel.setRecheckTime(new DateTime().minusMonths(2).toDate());
        loanModel.setBaseRate(0.2);
        loanModel.setActivityRate(0);
        loanModel.setType(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY);
        return loanModel;
    }

    private List<LoanRepayModel> getLoanRepayModels() {
        long loanId = getLoanModel().getId();
        List<LoanRepayModel> loanRepayModelList = new ArrayList<LoanRepayModel>();

        LoanRepayModel l1 = new LoanRepayModel();
        LoanRepayModel l2 = new LoanRepayModel();
        LoanRepayModel l3 = new LoanRepayModel();
        l1.setStatus(RepayStatus.COMPLETE);
        l1.setActualRepayDate(new DateTime().minusDays(30).toDate());
        l1.setPeriod(1);
        l1.setLoanId(loanId);
        l2.setStatus(RepayStatus.REPAYING);
        l2.setPeriod(2);
        l2.setLoanId(loanId);
        l3.setStatus(RepayStatus.REPAYING);
        l3.setPeriod(3);
        l3.setLoanId(loanId);


        loanRepayModelList.add(l1);
        loanRepayModelList.add(l2);
        loanRepayModelList.add(l3);
        return loanRepayModelList;
    }

    private List<BankLoanRepayInvestDataView> getBanLoanRepayInvestList() {
        LoanModel loanModel = getLoanModel();
        int sumDayOfYear = new DateTime(loanModel.getRecheckTime()).dayOfYear().getMaximumValue();

        List<BankLoanRepayInvestDataView> bankLoanRepayInvestDataViewList = new ArrayList<>();
        BankLoanRepayInvestDataView data1 = new BankLoanRepayInvestDataView();
        data1.setInvestAmount(sumDayOfYear * 100000l);
        data1.setTradingTime(loanModel.getRecheckTime());
        BankLoanRepayInvestDataView data2 = new BankLoanRepayInvestDataView();
        data2.setInvestAmount(sumDayOfYear * 100000l);
        data2.setTradingTime(loanModel.getRecheckTime());
        BankLoanRepayInvestDataView data3 = new BankLoanRepayInvestDataView();
        data3.setInvestAmount(sumDayOfYear * 100000l);
        data3.setTradingTime(loanModel.getRecheckTime());

        bankLoanRepayInvestDataViewList.add(data1);
        bankLoanRepayInvestDataViewList.add(data2);
        bankLoanRepayInvestDataViewList.add(data3);
        return bankLoanRepayInvestDataViewList;
    }
}
