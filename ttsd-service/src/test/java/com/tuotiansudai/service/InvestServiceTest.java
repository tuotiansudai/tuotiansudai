package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.impl.InvestServiceImpl;
import com.tuotiansudai.transfer.service.InvestTransferService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/9/3.
 */
@ActiveProfiles("test")
public class InvestServiceTest {
    @InjectMocks
    private InvestServiceImpl investService;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private UserCouponMapper userCouponMapper;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private InvestExtraRateMapper investExtraRateMapper;

    @Mock
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private LoanDetailsMapper loanDetailsMapper;

    @Mock
    private UserCouponService userCouponService;

    @Mock
    private UserOpLogService userOpLogService;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Mock
    private CouponRepayMapper couponRepayMapper;

    @Mock
    private CouponService couponService;

    @Mock
    private InvestTransferService investTransferService;

    @Mock
    private UserMembershipEvaluator userMembershipEvaluator;

    @Mock
    private UserMembershipService userMembershipService;

    @Mock
    private BankWrapperClient bankWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = investService.getClass().getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(investService, this.bankWrapperClient);
        //
        List<String> list = new ArrayList<>();
        list.add("test");
        Field field = investService.getClass().getDeclaredField("showRandomLoginNameList");
        field.setAccessible(true);
        field.set(investService, list);
        //
        Field newBilField = investService.getClass().getDeclaredField("NEWBIE_INVEST_LIMIT");
        newBilField.setAccessible(true);
        modifiersField.setInt(newBilField, newBilField.getModifiers() & ~Modifier.FINAL);
        newBilField.set(investService, 1);
    }

    @Test
    public void investSuccess() {
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(loanMapper.findById(anyLong())).thenReturn(getLoanModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(mockUserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        boolean hasError = false;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasError = false;
        }
        verify(investMapper, times(1)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(1)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(bankAsyncMessage);
        assertEquals(false, hasError);
    }

    @Test
    public void fastInvestSuccess() {
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(loanMapper.findById(anyLong())).thenReturn(getLoanModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(mockUserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.fastInvest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankReturnCallbackMessage());
        boolean hasError = false;
        BankReturnCallbackMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.noPasswordInvest(getInvestDto());
        } catch (InvestException e) {
            hasError = false;
        }
        verify(investMapper, times(1)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(1)).fastInvest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(bankAsyncMessage);
        assertEquals(false, hasError);
    }


    @Test
    public void investFalseNoLoan() {
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.NOT_ENOUGH_BALANCE, hasException.getType());
    }

    @Test
    public void investFalseEqualLoan() {
        LoanModel loanModel = getLoanModel();
        InvestDto investDto = getInvestDto();
        investDto.setLoginName("equal");
        loanModel.setAgentLoginName("equal");
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(investDto);
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.INVESTOR_IS_LOANER, hasException.getType());
    }

    @Test
    public void investFalseNoBalance() {
        BankAccountModel bankAccountModel = getBankAccountModel();
        bankAccountModel.setBalance(0l);
        when(loanMapper.findById(anyLong())).thenReturn(getLoanModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(bankAccountModel);
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.NOT_ENOUGH_BALANCE, hasException.getType());
    }

    @Test
    public void investFalseLoanStatus() {
        LoanModel loanModel = getLoanModel();
        loanModel.setStatus(LoanStatus.CANCEL);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.ILLEGAL_LOAN_STATUS, hasException.getType());
    }

    @Test
    public void investFalseNewbie() {
        LoanModel loanModel = getLoanModel();
        loanModel.setActivityType(ActivityType.NEWBIE);
        when(investMapper.countSuccessNewbieInvestByLoginName(anyString())).thenReturn(2000);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.OUT_OF_NOVICE_INVEST_LIMIT, hasException.getType());
    }

    @Test
    public void investFalseMinAmount() {
        LoanModel loanModel = getLoanModel();
        loanModel.setMinInvestAmount(100000000l);
        when(investMapper.countSuccessNewbieInvestByLoginName(anyString())).thenReturn(2000);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.LESS_THAN_MIN_INVEST_AMOUNT, hasException.getType());
    }

    @Test
    public void investFalseIncreasingAmount() {
        LoanModel loanModel = getLoanModel();
        loanModel.setInvestIncreasingAmount(333l);
        when(investMapper.countSuccessNewbieInvestByLoginName(anyString())).thenReturn(2000);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.ILLEGAL_INVEST_AMOUNT, hasException.getType());
    }

    @Test
    public void investFalseLoanIsFull() {
        when(investMapper.countSuccessNewbieInvestByLoginName(anyString())).thenReturn(2000);
        when(loanMapper.findById(anyLong())).thenReturn(getLoanModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(99999999999l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.LOAN_IS_FULL, hasException.getType());
    }

    @Test
    public void investFalseLoanIsOut() {
        when(investMapper.countSuccessNewbieInvestByLoginName(anyString())).thenReturn(2000);
        when(loanMapper.findById(anyLong())).thenReturn(getLoanModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(990009l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.EXCEED_MONEY_NEED_RAISED, hasException.getType());
    }

    @Test
    public void investFalseUserInvestAmout() {
        when(loanMapper.findById(anyLong())).thenReturn(getLoanModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(999999999l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasException = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(getInvestDto());
        } catch (InvestException e) {
            hasException = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasException);
        assertEquals(InvestExceptionType.MORE_THAN_MAX_INVEST_AMOUNT, hasException.getType());
    }

    @Test
    public void investFalseNotUserCoupon() {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setDisableCoupon(true);
        InvestDto investDto = getInvestDto();
        List<Long> list = new ArrayList<>();
        list.add(1l);
        investDto.setUserCouponIds(list);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(loanDetailsModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(getBankAccountModel());
        when(loanMapper.findById(anyLong())).thenReturn(getLoanModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(0l);
        when(investMapper.sumSuccessInvestAmountByLoginName(anyLong(), anyString(), anyBoolean())).thenReturn(0l);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(bankWrapperClient.invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString())).thenReturn(new BankAsyncMessage());
        InvestException hasError = null;
        BankAsyncMessage bankAsyncMessage = null;
        try {
            bankAsyncMessage = investService.invest(investDto);
        } catch (InvestException e) {
            hasError = e;
        }
        verify(investMapper, times(0)).create(any(InvestModel.class));
        verify(bankWrapperClient, times(0)).invest(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(), anyString());
        assertNotNull(hasError);
        assertEquals(InvestExceptionType.NOT_USE_COUPON, hasError.getType());
    }

    private BankAccountModel getBankAccountModel() {
        BankAccountModel bankAccountModel = new BankAccountModel();
        bankAccountModel.setBalance(10000000l);
        bankAccountModel.setAutoInvest(true);
        bankAccountModel.setBankAccountNo("bankAccountNo");
        bankAccountModel.setBankUserName("bankUserName");
        bankAccountModel.setLoginName("loginName");
        return bankAccountModel;
    }

    private LoanModel getLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("agent");
        loanModel.setMinInvestAmount(100l);
        loanModel.setInvestIncreasingAmount(100l);
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setMaxInvestAmount(100000000l);
        loanModel.setLoanAmount(1000000l);
        loanModel.setLoanTxNo("loanTxNo");
        loanModel.setName("loanName");
        return loanModel;
    }

    private InvestDto getInvestDto() {
        InvestDto investDto = new InvestDto();
        investDto.setLoanId("1");
        investDto.setLoginName("investor");
        investDto.setAmount("10000");
        return investDto;
    }

    private UserModel mockUserModel(){
        UserModel userModel = new UserModel();
        userModel.setLoginName("loginName");
        userModel.setMobile("11111111111");
        return userModel;
    }
}
