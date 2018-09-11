package com.tuotiansudai.transfer;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.transfer.service.impl.TransferServiceImpl;
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
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class InvestTransferServiceTest {

    @InjectMocks
    private TransferServiceImpl transferService;

    @Mock
    private BankWrapperClient bankWrapperClient;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private UserMembershipService userMembershipService;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = this.transferService.getClass().getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(this.transferService, this.bankWrapperClient);

        when(userMapper.findByLoginName(anyString())).thenReturn(mockUserModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(mockBankAccountModel());
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());
        when(transferApplicationMapper.findById(anyLong())).thenReturn(mockTransferApplicationModel());
        when(investMapper.findById(anyLong())).thenReturn(mockInvestModel());
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0D);
    }

    @Test
    public void investServiceSuccess() throws InvestException {

        ArgumentCaptor<InvestModel> investModelCaptor = ArgumentCaptor.forClass(InvestModel.class);
        transferService.transferPurchase(mockInvestDto());

        verify(investMapper, times(1)).create(investModelCaptor.capture());
        assertThat(investModelCaptor.getValue().getAmount(), is(1000L));
        assertThat(investModelCaptor.getValue().getLoginName(), is("transferee"));
        assertThat(investModelCaptor.getValue().getTransferInvestId(), is(1L));
    }

    @Test
    public void investServiceTransferApplicationStatusFail() {
        TransferApplicationModel model = mockTransferApplicationModel();
        model.setStatus(TransferStatus.SUCCESS);
        when(transferApplicationMapper.findById(anyLong())).thenReturn(model);

        String error = null;
        try {
            transferService.transferPurchase(mockInvestDto());
        } catch (InvestException e) {
            error = e.getType().name();
        }
        assertThat(error, is("ILLEGAL_LOAN_STATUS"));
    }

    @Test
    public void investServiceTransferApplicationLoginNameFail() {
        TransferApplicationModel model = mockTransferApplicationModel();
        model.setLoginName("transferee");
        when(transferApplicationMapper.findById(anyLong())).thenReturn(model);

        String error = null;
        try {
            transferService.transferPurchase(mockInvestDto());
        } catch (InvestException e) {
            error = e.getType().name();
        }
        assertThat(error, is("INVESTOR_IS_LOANER"));
    }

    @Test
    public void investServiceAmountFail() {
        TransferApplicationModel model = mockTransferApplicationModel();
        model.setTransferAmount(2000);
        when(transferApplicationMapper.findById(anyLong())).thenReturn(model);
        String error = null;
        try {
            transferService.transferPurchase(mockInvestDto());
        } catch (InvestException e) {
            error = e.getType().name();
        }
        assertThat(error, is("NOT_ENOUGH_BALANCE"));
    }

    private InvestDto mockInvestDto() {
        InvestDto investDto = new InvestDto();
        investDto.setLoanId("1");
        investDto.setTransferApplicationId("1");
        investDto.setLoginName("transferee");
        return investDto;
    }

    private BankAccountModel mockBankAccountModel() {
        BankAccountModel bankAccountModel = new BankAccountModel("loginName", "bankUserName", "bankAccountNo", "1111111", "20180810", "22222222222");
        bankAccountModel.setBalance(1000);
        return bankAccountModel;
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
        loanModel.setMaxInvestAmount(10000l);
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

    private TransferApplicationModel mockTransferApplicationModel() {
        InvestModel investModel = mockInvestModel();
        TransferApplicationModel model = new TransferApplicationModel(investModel, "ZR20180810-001", 1, 1000, 10, new Date(), 1, Source.WEB);
        model.setId(1);
        return model;
    }

    private InvestModel mockInvestModel() {
        InvestModel investModel = new InvestModel();
        investModel.setLoanId(1);
        investModel.setId(1);
        investModel.setLoginName("transferer");
        investModel.setAmount(1000);
        investModel.setTransferStatus(TransferStatus.TRANSFERRING);
        investModel.setStatus(InvestStatus.SUCCESS);
        return investModel;
    }

    private UserModel mockUserModel() {
        UserModel userModel = new UserModel();
        userModel.setLoginName("transferee");
        userModel.setMobile("11111111111");
        return userModel;
    }
}
