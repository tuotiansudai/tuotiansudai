package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.impl.RepayServiceImpl;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

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
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId())).thenReturn(getLoanRepayModels());


    }

    private LoanModel getLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setId(1l);
        loanModel.setRecheckTime(new DateTime().minusMonths(2).toDate());
        return loanModel;
    }

    private List<LoanRepayModel> getLoanRepayModels() {
        long loanId = getLoanModel().getId();
        List<LoanRepayModel> loanRepayModelList = new ArrayList<LoanRepayModel>();

        LoanRepayModel l1 = new LoanRepayModel();
        LoanRepayModel l2 = new LoanRepayModel();
        LoanRepayModel l3 = new LoanRepayModel();
        l1.setStatus(RepayStatus.COMPLETE);
        l1.setActualRepayDate(new DateTime().minusMonths(1).toDate());
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
        List<BankLoanRepayInvestDataView> bankLoanRepayInvestDataViewList = new ArrayList<>();
        BankLoanRepayInvestDataView data1 = new BankLoanRepayInvestDataView();
        BankLoanRepayInvestDataView data2 = new BankLoanRepayInvestDataView();
        BankLoanRepayInvestDataView data3 = new BankLoanRepayInvestDataView();

        bankLoanRepayInvestDataViewList.add(data1);
        bankLoanRepayInvestDataViewList.add(data2);
        bankLoanRepayInvestDataViewList.add(data3);
        return bankLoanRepayInvestDataViewList;
    }
}
