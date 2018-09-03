package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.dto.BankLoanRepayInvestDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.impl.RepayServiceImpl;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

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

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 放款后计算利息
     */
    @Test
    public void advancedRepaySuccess() {
        LoanModel loanModel = getLoanModel();
        ArgumentCaptor<BankLoanRepayDto> bankLoanRepayDto = ArgumentCaptor.forClass(BankLoanRepayDto.class);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId())).thenReturn(getLoanRepayModels());
        when(investRepayMapper.queryBankInvestRepayData(anyLong(), anyInt())).thenReturn(getBanLoanRepayInvestList());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(new BankAccountModel());
        BankAsyncMessage bankAsyncMessage = new BankAsyncMessage();
        bankAsyncMessage.setStatus(true);
        when(bankWrapperClient.loanRepay(any(BankLoanRepayDto.class))).thenReturn(bankAsyncMessage);
        //
        BankAsyncMessage returnMsg = repayService.advancedRepay(new RepayDto());
        //
        verify(bankWrapperClient, times(1)).loanRepay(bankLoanRepayDto.capture());
        verify(loanRepayMapper, times(1)).update(any(LoanRepayModel.class));
        assertNotNull(returnMsg);
        assertEquals(true, returnMsg.isStatus());
        for (BankLoanRepayInvestDto bankLoanRepayInvestDto1 : bankLoanRepayDto.getValue().getBankLoanRepayInvests()) {
            assertEquals(600000l, bankLoanRepayInvestDto1.getInterest());
            assertEquals(120000l, bankLoanRepayInvestDto1.getInterestFee());
        }
    }

    /**
     * 即投即生息
     */
    @Test
    public void advancedRepaySuccessInvestNow() {
        LoanModel loanModel = getLoanModel();
        loanModel.setRecheckTime(new DateTime().minusDays(59).toDate());
        loanModel.setType(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY);
        List<LoanRepayModel> loanRepayModelList = getLoanRepayModels();
        loanRepayModelList.get(0).setStatus(RepayStatus.REPAYING);
        ArgumentCaptor<BankLoanRepayDto> bankLoanRepayDto = ArgumentCaptor.forClass(BankLoanRepayDto.class);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);

        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId())).thenReturn(loanRepayModelList);
        when(investRepayMapper.queryBankInvestRepayData(anyLong(), anyInt())).thenReturn(getBanLoanRepayInvestList());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(new BankAccountModel());
        BankAsyncMessage bankAsyncMessage = new BankAsyncMessage();
        bankAsyncMessage.setStatus(true);
        when(bankWrapperClient.loanRepay(any(BankLoanRepayDto.class))).thenReturn(bankAsyncMessage);
        //
        BankAsyncMessage returnMsg = repayService.advancedRepay(new RepayDto());
        //
        verify(bankWrapperClient, times(1)).loanRepay(bankLoanRepayDto.capture());
        verify(loanRepayMapper, times(1)).update(any(LoanRepayModel.class));
        assertNotNull(returnMsg);
        assertEquals(true, returnMsg.isStatus());
        for (BankLoanRepayInvestDto bankLoanRepayInvestDto1 : bankLoanRepayDto.getValue().getBankLoanRepayInvests()) {
            assertEquals(600000l, bankLoanRepayInvestDto1.getInterest());
            assertEquals(120000l, bankLoanRepayInvestDto1.getInterestFee());
        }
    }

    @Test
    public void advancedRepayFalseRepayStatus() {
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.CANCEL);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        BankAsyncMessage returnMsg = repayService.advancedRepay(new RepayDto());
        verify(loanRepayMapper, times(0)).findByLoanIdOrderByPeriodAsc(anyLong());
        verify(bankWrapperClient, times(0)).loanRepay(any(BankLoanRepayDto.class));
        assertNotNull(returnMsg);
        assertEquals(false, returnMsg.isStatus());
    }

    @Test
    public void advancedRepayFalseNotSupportAdvance() {
        LoanModel loanModel = getLoanModel();
        List<LoanRepayModel> loanRepayModelList = getLoanRepayModels();
        loanRepayModelList.stream().forEach(item -> {
            item.setStatus(RepayStatus.COMPLETE);
        });
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId())).thenReturn(loanRepayModelList);
        BankAsyncMessage returnMsg = repayService.advancedRepay(new RepayDto());
        verify(loanRepayMapper, times(1)).findByLoanIdOrderByPeriodAsc(anyLong());
        verify(bankWrapperClient, times(0)).loanRepay(any(BankLoanRepayDto.class));
        assertNotNull(returnMsg);
        assertEquals(false, returnMsg.isStatus());
    }

    @Test
    public void advancedRepayFalseReturnMsg() {
        LoanModel loanModel = getLoanModel();
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId())).thenReturn(getLoanRepayModels());
        when(investRepayMapper.queryBankInvestRepayData(anyLong(), anyInt())).thenReturn(getBanLoanRepayInvestList());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(new BankAccountModel());
        BankAsyncMessage bankAsyncMessage = new BankAsyncMessage();
        bankAsyncMessage.setStatus(false);
        when(bankWrapperClient.loanRepay(any(BankLoanRepayDto.class))).thenReturn(bankAsyncMessage);
        BankAsyncMessage returnMsg = repayService.advancedRepay(new RepayDto());
        //
        verify(bankWrapperClient, times(1)).loanRepay(any(BankLoanRepayDto.class));
        verify(loanRepayMapper, times(0)).update(any(LoanRepayModel.class));
        assertNotNull(returnMsg);
        assertEquals(false, returnMsg.isStatus());
    }


    private LoanModel getLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setId(1l);
        loanModel.setRecheckTime(new DateTime().minusMonths(2).toDate());
        loanModel.setBaseRate(0.2);
        loanModel.setActivityRate(0);
        loanModel.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
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
        data1.setTradingTime(new DateTime().minusDays(29).toDate());
        data1.setInvestFeeRate(0.2);
        BankLoanRepayInvestDataView data2 = new BankLoanRepayInvestDataView();
        data2.setInvestAmount(sumDayOfYear * 100000l);
        data2.setTradingTime(new DateTime().minusDays(29).toDate());
        data2.setInvestFeeRate(0.2);
        BankLoanRepayInvestDataView data3 = new BankLoanRepayInvestDataView();
        data3.setInvestAmount(sumDayOfYear * 100000l);
        data3.setTradingTime(new DateTime().minusDays(29).toDate());
        data3.setInvestFeeRate(0.2);

        bankLoanRepayInvestDataViewList.add(data1);
        bankLoanRepayInvestDataViewList.add(data2);
        bankLoanRepayInvestDataViewList.add(data3);
        return bankLoanRepayInvestDataViewList;
    }
}
