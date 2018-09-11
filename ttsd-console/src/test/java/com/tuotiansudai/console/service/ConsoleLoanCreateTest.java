package com.tuotiansudai.console.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanCreateBaseRequestDto;
import com.tuotiansudai.dto.LoanCreateRequestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankLoanCancelMessage;
import com.tuotiansudai.fudian.message.BankLoanCreateMessage;
import com.tuotiansudai.job.DelayMessageDeliveryJob;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.quartz.TriggeredJobBuilder;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.Scheduler;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/9/3.
 */
@ActiveProfiles("test")
public class ConsoleLoanCreateTest {
    @InjectMocks
    private ConsoleLoanCreateService consoleLoanCreateService;
    @Mock
    private LoanTitleMapper loanTitleMapper;

    @Mock
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private LoanerDetailsMapper loanerDetailsMapper;

    @Mock
    private LoanDetailsMapper loanDetailsMapper;

    @Mock
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Mock
    private ExtraLoanRateRuleMapper extraLoanRateRuleMapper;

    @Mock
    private LoanerEnterpriseDetailsMapper loanerEnterpriseDetailsMapper;

    @Mock
    private LoanerEnterpriseInfoMapper loanerEnterpriseInfoMapper;

    @Mock
    private PledgeHouseMapper pledgeHouseMapper;

    @Mock
    private PledgeVehicleMapper pledgeVehicleMapper;

    @Mock
    private PledgeEnterpriseMapper pledgeEnterpriseMapper;

    @Mock
    private JobManager jobManager;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private BankWrapperClient bankWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = consoleLoanCreateService.getClass().getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(consoleLoanCreateService, this.bankWrapperClient);
    }

    @Test
    public void openLoanSuccess() {
        LoanCreateRequestDto loanCreateRequestDto = new LoanCreateRequestDto();
        LoanCreateBaseRequestDto loan = new LoanCreateBaseRequestDto();
        loan.setId(1l);
        loanCreateRequestDto.setLoan(loan);
        LoanModel loanModel = mockLoanModel();
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);

        Scheduler scheduler = mock(Scheduler.class);
        TriggeredJobBuilder triggeredJobBuilder = new TriggeredJobBuilder(DelayMessageDeliveryJob.class, scheduler);

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(mockBankAccountModel());
        when(jobManager.newJob(any(JobType.class), any())).thenReturn(triggeredJobBuilder);
        BankLoanCreateMessage bankLoanCreateMessage = new BankLoanCreateMessage();
        bankLoanCreateMessage.setStatus(true);
        when(bankWrapperClient.createLoan(anyString(), anyString(), anyString(), anyLong(), anyString())).thenReturn(bankLoanCreateMessage);
        BaseDto<PayDataDto> baseDto = consoleLoanCreateService.openLoan(loanCreateRequestDto, "ip");
        verify(loanMapper, times(1)).update(any(LoanModel.class));
        verify(bankWrapperClient, times(1)).createLoan(anyString(), anyString(), anyString(), anyLong(), anyString());
        assertNotNull(baseDto);
    }

    @Test
    public void openLoanFalseStatus() {
        LoanCreateRequestDto loanCreateRequestDto = new LoanCreateRequestDto();
        LoanCreateBaseRequestDto loan = new LoanCreateBaseRequestDto();
        loan.setId(1l);
        loanCreateRequestDto.setLoan(loan);
        LoanModel loanModel = new LoanModel();

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(new BankAccountModel());
        BankLoanCreateMessage bankLoanCreateMessage = new BankLoanCreateMessage();
        bankLoanCreateMessage.setStatus(true);
        when(bankWrapperClient.createLoan(anyString(), anyString(), anyString(), anyLong(), anyString())).thenReturn(bankLoanCreateMessage);
        BaseDto<PayDataDto> baseDto = consoleLoanCreateService.openLoan(loanCreateRequestDto, "ip");
        verify(loanMapper, times(0)).update(any(LoanModel.class));
        verify(bankWrapperClient, times(0)).createLoan(anyString(), anyString(), anyString(), anyLong(), anyString());
        assertNotNull(baseDto);
    }

    @Test
    public void openLoanFalseReturnStatus() {
        LoanCreateRequestDto loanCreateRequestDto = new LoanCreateRequestDto();
        LoanCreateBaseRequestDto loan = new LoanCreateBaseRequestDto();
        loan.setId(1l);
        loanCreateRequestDto.setLoan(loan);
        LoanModel loanModel = mockLoanModel();
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(mockBankAccountModel());
        BankLoanCreateMessage bankLoanCreateMessage = new BankLoanCreateMessage();
        bankLoanCreateMessage.setStatus(false);
        when(bankWrapperClient.createLoan(anyString(), anyString(), anyString(), anyLong(), anyString())).thenReturn(bankLoanCreateMessage);
        BaseDto<PayDataDto> baseDto = consoleLoanCreateService.openLoan(loanCreateRequestDto, "ip");
        verify(loanMapper, times(0)).update(any(LoanModel.class));
        verify(bankWrapperClient, times(1)).createLoan(anyString(), anyString(), anyString(), anyLong(), anyString());
        assertNotNull(baseDto);
    }

    @Test
    public void cancelLoanSuccess() {
        LoanCreateRequestDto loanCreateRequestDto = new LoanCreateRequestDto();
        LoanCreateBaseRequestDto loan = new LoanCreateBaseRequestDto();
        loan.setId(1l);
        loanCreateRequestDto.setLoan(loan);

        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        when(investMapper.findWaitingInvestCountAfter(anyLong(), any(Date.class))).thenReturn(0);
        BankLoanCancelMessage bankLoanCancelMessage = new BankLoanCancelMessage();
        bankLoanCancelMessage.setStatus(true);
        when(bankWrapperClient.cancelLoan(anyLong(), anyString(), anyString(), anyString())).thenReturn(bankLoanCancelMessage);
        BaseDto<PayDataDto> baseDto = consoleLoanCreateService.cancelLoan(loanCreateRequestDto);
        verify(investMapper, times(1)).cleanWaitingInvest(anyLong());
        verify(bankWrapperClient, times(1)).cancelLoan(anyLong(), anyString(), anyString(), anyString());
        assertNotNull(baseDto);
    }
    @Test
    public void cancelLoanFalseLoanNull() {
        LoanCreateRequestDto loanCreateRequestDto = new LoanCreateRequestDto();
        LoanCreateBaseRequestDto loan = new LoanCreateBaseRequestDto();
        loan.setId(1l);
        loanCreateRequestDto.setLoan(loan);

        when(investMapper.findWaitingInvestCountAfter(anyLong(), any(Date.class))).thenReturn(0);
        BankLoanCancelMessage bankLoanCancelMessage = new BankLoanCancelMessage();
        bankLoanCancelMessage.setStatus(true);
        when(bankWrapperClient.cancelLoan(anyLong(), anyString(), anyString(), anyString())).thenReturn(bankLoanCancelMessage);
        BaseDto<PayDataDto> baseDto = consoleLoanCreateService.cancelLoan(loanCreateRequestDto);
        verify(investMapper, times(0)).cleanWaitingInvest(anyLong());
        verify(bankWrapperClient, times(0)).cancelLoan(anyLong(), anyString(), anyString(), anyString());
        assertNotNull(baseDto);
    }

    @Test
    public void cancelLoanFalsevalid() {
        LoanCreateRequestDto loanCreateRequestDto = new LoanCreateRequestDto();
        LoanCreateBaseRequestDto loan = new LoanCreateBaseRequestDto();
        loan.setId(1l);
        loanCreateRequestDto.setLoan(loan);
        LoanModel loanModel = new LoanModel();
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.findWaitingInvestCountAfter(anyLong(), any(Date.class))).thenReturn(1);
        BankLoanCancelMessage bankLoanCancelMessage = new BankLoanCancelMessage();
        bankLoanCancelMessage.setStatus(true);
        when(bankWrapperClient.cancelLoan(anyLong(), anyString(), anyString(), anyString())).thenReturn(bankLoanCancelMessage);
        BaseDto<PayDataDto> baseDto = consoleLoanCreateService.cancelLoan(loanCreateRequestDto);
        verify(investMapper, times(0)).cleanWaitingInvest(anyLong());
        verify(bankWrapperClient, times(0)).cancelLoan(anyLong(), anyString(), anyString(), anyString());
        assertNotNull(baseDto);
    }

    private LoanModel mockLoanModel(){
        LoanModel loanModel = new LoanModel();
        loanModel.setName("loanName");
        loanModel.setAgentLoginName("agentLoanName");
        loanModel.setLoanAmount(1000L);
        loanModel.setLoanTxNo("loanTxNo");
        loanModel.setBankOrderNo("111111");
        loanModel.setBankOrderDate("20180810");
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setFundraisingEndTime(new Date());
        return loanModel;
    }

    private BankAccountModel mockBankAccountModel(){
        BankAccountModel bankAccountModel = new BankAccountModel();
        bankAccountModel.setLoginName("agentLoanName");
        bankAccountModel.setBankUserName("bankUserName");
        bankAccountModel.setBankAccountNo("bankAccountNo");
        return bankAccountModel;
    }
}
