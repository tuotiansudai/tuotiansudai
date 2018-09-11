package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.impl.RepayServiceImpl;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThat;

@ActiveProfiles("test")
public class NormalLoanRepayServiceTest {

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

    private static final long loanId = 1;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = this.repayService.getClass().getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(this.repayService, this.bankWrapperClient);
    }

    @Test
    public void repayServiceSuccess() {

        ArgumentCaptor<BankLoanRepayDto> bankLoanRepayDtoCaptor = ArgumentCaptor.forClass(BankLoanRepayDto.class);
        ArgumentCaptor<LoanRepayModel> loanRepayModelCaptor = ArgumentCaptor.forClass(LoanRepayModel.class);

        when(loanRepayMapper.findEnabledLoanRepayByLoanId(anyLong())).thenReturn(mockLoanRepayModel(RepayStatus.REPAYING));
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(mockUserModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(mockBankAccountModel());
        when(investRepayMapper.queryBankInvestRepayData(anyLong(), anyInt())).thenReturn(Lists.newArrayList(mockBankLoanRepayInvestDataView()));

        BankAsyncMessage message = new BankAsyncMessage();
        message.setStatus(true);
        when(bankWrapperClient.loanRepay(any(BankLoanRepayDto.class))).thenReturn(message);
        doNothing().when(loanRepayMapper).update(any(LoanRepayModel.class));

        RepayDto repayDto = new RepayDto();
        repayDto.setLoanId(loanId);
        repayDto.setIsAdvanced(false);
        repayService.normalRepay(repayDto);

        verify(loanRepayMapper, times(1)).findEnabledLoanRepayByLoanId(anyLong());
        verify(loanMapper, times(1)).findById(anyLong());
        verify(userMapper, times(1)).findByLoginName(anyString());
        verify(bankAccountMapper, times(1)).findByLoginNameAndRole(anyString(), eq(Role.LOANER));
        verify(investRepayMapper, times(1)).queryBankInvestRepayData(anyLong(), anyInt());
        verify(bankWrapperClient, times(1)).loanRepay(bankLoanRepayDtoCaptor.capture());
        verify(loanRepayMapper, times(1)).update(loanRepayModelCaptor.capture());

        assertThat(bankLoanRepayDtoCaptor.getValue().getLoanId(), is(1L));
        assertThat(bankLoanRepayDtoCaptor.getValue().getLoanTxNo(), is("loanTxNo"));
        assertThat(bankLoanRepayDtoCaptor.getValue().getLoanRepayId(), is(1L));
        assertThat(bankLoanRepayDtoCaptor.getValue().getCapital(), is(1000L));
        assertThat(bankLoanRepayDtoCaptor.getValue().getBankAccountNo(), is("agentBankAccountNo"));
        assertThat(bankLoanRepayDtoCaptor.getValue().getBankLoanRepayInvests().size(), is(1));
        assertThat(bankLoanRepayDtoCaptor.getValue().getBankLoanRepayInvests().get(0).getInvestId(), is(1L));
        assertThat(bankLoanRepayDtoCaptor.getValue().getBankLoanRepayInvests().get(0).getCapital(), is(1000L));
        assertThat(bankLoanRepayDtoCaptor.getValue().getBankLoanRepayInvests().get(0).getBankAccountNo(), is("bankAccountNo"));
        assertThat(bankLoanRepayDtoCaptor.getValue().getBankLoanRepayInvests().get(0).getInvestOrderNo(), is("investBankOrderNo"));
        assertThat(loanRepayModelCaptor.getValue().getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(loanRepayModelCaptor.getValue().getRepayAmount(), is(1010L));
    }

    @Test
    public void repayServiceFail(){
        when(loanRepayMapper.findEnabledLoanRepayByLoanId(anyLong())).thenReturn(mockLoanRepayModel(RepayStatus.WAIT_PAY));
        RepayDto repayDto = new RepayDto();
        repayDto.setLoanId(loanId);
        repayDto.setIsAdvanced(false);
        BankAsyncMessage message = repayService.normalRepay(repayDto);
        assertFalse(message.isStatus());
        assertThat(message.getMessage(), is("该标的今天没有待还款，或还款等待支付，请半小时后重试"));
    }

    private LoanModel mockLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("agentLoginName");
        loanModel.setBaseRate(16.00);
        loanModel.setId(loanId);
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

    private LoanRepayModel mockLoanRepayModel(RepayStatus status) {
        return new LoanRepayModel(1, loanId, 1, 1000, 10, new Date(), status);
    }

    private UserModel mockUserModel() {
        UserModel userModel = new UserModel();
        userModel.setMobile("00000000000");
        userModel.setLoginName("agentLoginName");
        return userModel;
    }

    private BankAccountModel mockBankAccountModel() {
        return new BankAccountModel("agentLoginName", "agentBankUserName", "agentBankAccountNo", "1111111", "20180810", "22222222222");
    }

    private BankLoanRepayInvestDataView mockBankLoanRepayInvestDataView() {
        BankLoanRepayInvestDataView view = new BankLoanRepayInvestDataView();
        view.setInvestId(1);
        view.setInvestRepayId(1);
        view.setBankUserName("bankUserName");
        view.setBankAccountNo("bankAccountNo");
        view.setLoginName("loginName");
        view.setMobile("11111111111");
        view.setLoanTxNo("loanTxNo");
        view.setCorpus(1000);
        view.setExpectedInterest(10);
        view.setExpectedFee(1);
        view.setInvestBankOrderNo("investBankOrderNo");
        view.setInvestBankOrderDate("investBankOrderDate");
        return view;
    }
}
