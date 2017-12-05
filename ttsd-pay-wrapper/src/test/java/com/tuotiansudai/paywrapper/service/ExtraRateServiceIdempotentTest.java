package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.extrarate.service.InvestRateService;
import com.tuotiansudai.paywrapper.extrarate.service.impl.ExtraRateServiceImpl;
import com.tuotiansudai.paywrapper.repository.mapper.ExtraRateTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.map.HashedMap;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class ExtraRateServiceIdempotentTest {
    @InjectMocks
    private ExtraRateServiceImpl extraRateService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private InvestExtraRateMapper investExtraRateMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private InvestRateService investRateService;

    private final static String REPAY_REDIS_KEY_TEMPLATE = "EXTRA_RATE_REPAY:{0}";

    private final Map<String, Integer> userMembershipLevelMap = new HashedMap<>();

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        userMembershipLevelMap.clear();

        Field redisWrapperClientField = this.extraRateService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.extraRateService, this.redisWrapperClient);
    }

    @After
    public void clean() throws Exception {
    }

    @Test
    public void shouldNormalRepayIsIdempotentOkFirstCall() throws PayException {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        UserModel loaner = getFakeUser("loaner");
        LoanModel loan = getFakeLoan(loaner, LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        LoanRepayModel loanRepay1 = getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, 1000, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(1000);
        LoanRepayModel loanRepay2 = getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), 1000, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        UserModel investor = getFakeUser("investor");
        AccountModel investorAccount = getFakeAccount(investor.getLoginName(), 0, 0);
        userMembershipLevelMap.put(investor.getLoginName(), 0);
        InvestModel investModel = getFakeInvest(loan.getId(), null, 1000000, investor.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        InvestExtraRateModel investExtraRateModel = getFakeInvestExtraRate(loan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepay2.getId()));

        when(loanRepayMapper.findById(loanRepay1.getId())).thenReturn(loanRepay1);
        when(loanRepayMapper.findById(loanRepay2.getId())).thenReturn(loanRepay2);
        when(loanRepayMapper.findLastLoanRepay(loan.getId())).thenReturn(loanRepay2);
        when(accountMapper.findByLoginName(investor.getLoginName())).thenReturn(investorAccount);
        when(investMapper.findById(investModel.getId())).thenReturn(investModel);
        when(investExtraRateMapper.findByLoanId(loan.getId())).thenReturn(Lists.newArrayList(investExtraRateModel));
        when(redisWrapperClient.hget(redisKey, String.valueOf(investExtraRateModel.getId()))).thenReturn(null);
        when(paySyncClient.send(eq(ExtraRateTransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class))).thenReturn(responseModel);

        extraRateService.normalRepay(loanRepay2.getId());

        verify(paySyncClient, times(1)).send(eq(ExtraRateTransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class));
        ArgumentCaptor<String> syncRequestStatusArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(2)).hset(anyString(), anyString(), syncRequestStatusArgumentCaptor.capture());
        syncRequestStatusArgumentCaptor.getAllValues();
        assertThat(syncRequestStatusArgumentCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));
        assertThat(syncRequestStatusArgumentCaptor.getAllValues().get(1), is(SyncRequestStatus.SUCCESS.name()));
    }

    @Test
    public void shouldNormalRepayIsIdempotentOkSecondCall() throws PayException {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        UserModel loaner = getFakeUser("loaner");
        LoanModel loan = getFakeLoan(loaner, LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        LoanRepayModel loanRepay1 = getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, 1000, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(1000);
        LoanRepayModel loanRepay2 = getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), 1000, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        UserModel investor = getFakeUser("investor");
        AccountModel investorAccount = getFakeAccount(investor.getLoginName(), 0, 0);
        userMembershipLevelMap.put(investor.getLoginName(), 0);
        InvestModel investModel = getFakeInvest(loan.getId(), null, 1000000, investor.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        InvestExtraRateModel investExtraRateModel = getFakeInvestExtraRate(loan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepay2.getId()));

        when(loanRepayMapper.findById(loanRepay1.getId())).thenReturn(loanRepay1);
        when(loanRepayMapper.findById(loanRepay2.getId())).thenReturn(loanRepay2);
        when(loanRepayMapper.findLastLoanRepay(loan.getId())).thenReturn(loanRepay2);
        when(accountMapper.findByLoginName(investor.getLoginName())).thenReturn(investorAccount);
        when(investMapper.findById(investModel.getId())).thenReturn(investModel);
        when(investExtraRateMapper.findByLoanId(loan.getId())).thenReturn(Lists.newArrayList(investExtraRateModel));
        doNothing().when(investExtraRateMapper).update(any(InvestExtraRateModel.class));
        when(redisWrapperClient.hget(redisKey, String.valueOf(investExtraRateModel.getId()))).thenReturn(SyncRequestStatus.SUCCESS.name());
        when(paySyncClient.send(eq(ExtraRateTransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class))).thenReturn(responseModel);

        extraRateService.normalRepay(loanRepay2.getId());

        verify(paySyncClient, never()).send(eq(ExtraRateTransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class));
        ArgumentCaptor<String> syncRequestStatusArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, never()).hset(anyString(), anyString(), syncRequestStatusArgumentCaptor.capture());
    }

    @Test
    public void shouldAdvanceRepayIsIdempotentOkFirstCall() throws PayException {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        UserModel loaner = getFakeUser("loaner");
        LoanModel loan = getFakeLoan(loaner, LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        LoanRepayModel loanRepay1 = getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, 1000, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(1000);
        LoanRepayModel loanRepay2 = getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), 1000, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        UserModel investor = getFakeUser("investor");
        AccountModel investorAccount = getFakeAccount(investor.getLoginName(), 0, 0);
        userMembershipLevelMap.put(investor.getLoginName(), 0);
        InvestModel investModel = getFakeInvest(loan.getId(), null, 1000000, investor.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        InvestExtraRateModel investExtraRateModel = getFakeInvestExtraRate(loan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepay2.getId()));

        when(loanMapper.findById(loan.getId())).thenReturn(loan);
        when(loanRepayMapper.findById(loanRepay1.getId())).thenReturn(loanRepay1);
        when(loanRepayMapper.findById(loanRepay2.getId())).thenReturn(loanRepay2);
        when(loanRepayMapper.findLastLoanRepay(loan.getId())).thenReturn(loanRepay2);
        when(accountMapper.findByLoginName(investor.getLoginName())).thenReturn(investorAccount);
        when(investMapper.findById(investModel.getId())).thenReturn(investModel);
        when(investExtraRateMapper.findByLoanId(loan.getId())).thenReturn(Lists.newArrayList(investExtraRateModel));
        doNothing().when(investExtraRateMapper).update(any(InvestExtraRateModel.class));
        when(redisWrapperClient.hget(redisKey, String.valueOf(investExtraRateModel.getId()))).thenReturn(null);
        when(paySyncClient.send(eq(ExtraRateTransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class))).thenReturn(responseModel);

        extraRateService.advanceRepay(loanRepay2.getId());

        verify(paySyncClient, times(1)).send(eq(ExtraRateTransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class));
        ArgumentCaptor<String> syncRequestStatusArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(2)).hset(anyString(), anyString(), syncRequestStatusArgumentCaptor.capture());
        syncRequestStatusArgumentCaptor.getAllValues();
        assertThat(syncRequestStatusArgumentCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));
        assertThat(syncRequestStatusArgumentCaptor.getAllValues().get(1), is(SyncRequestStatus.SUCCESS.name()));
    }

    @Test
    public void shouldAdvanceRepayIsIdempotentOkSecondCall() throws PayException {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        UserModel loaner = getFakeUser("loaner");
        LoanModel loan = getFakeLoan(loaner, LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        LoanRepayModel loanRepay1 = getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, 1000, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(1000);
        LoanRepayModel loanRepay2 = getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), 1000, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        UserModel investor = getFakeUser("investor");
        AccountModel investorAccount = getFakeAccount(investor.getLoginName(), 0, 0);
        userMembershipLevelMap.put(investor.getLoginName(), 0);
        InvestModel investModel = getFakeInvest(loan.getId(), null, 1000000, investor.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        InvestExtraRateModel investExtraRateModel = getFakeInvestExtraRate(loan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepay2.getId()));

        when(loanMapper.findById(loan.getId())).thenReturn(loan);
        when(loanRepayMapper.findById(loanRepay1.getId())).thenReturn(loanRepay1);
        when(loanRepayMapper.findById(loanRepay2.getId())).thenReturn(loanRepay2);
        when(loanRepayMapper.findLastLoanRepay(loan.getId())).thenReturn(loanRepay2);
        when(accountMapper.findByLoginName(investor.getLoginName())).thenReturn(investorAccount);
        when(investMapper.findById(investModel.getId())).thenReturn(investModel);
        when(investExtraRateMapper.findByLoanId(loan.getId())).thenReturn(Lists.newArrayList(investExtraRateModel));
        when(redisWrapperClient.hget(redisKey, String.valueOf(investExtraRateModel.getId()))).thenReturn(SyncRequestStatus.SUCCESS.name());
        when(paySyncClient.send(eq(ExtraRateTransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class))).thenReturn(responseModel);

        extraRateService.advanceRepay(loanRepay2.getId());

        verify(paySyncClient, never()).send(eq(ExtraRateTransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class));
        ArgumentCaptor<String> syncRequestStatusArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, never()).hset(anyString(), anyString(), syncRequestStatusArgumentCaptor.capture());
    }

    private UserModel getFakeUser(String loginName) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("password");
        userModel.setMobile(loginName);
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));

        return userModel;
    }

    private AccountModel getFakeAccount(String loginName, long balance, long freeze) {
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        return accountModel;
    }

    private MembershipModel getMembershipModelByLevel(int level) {
        List<MembershipModel> membershipModels = Lists.newArrayList(
                new MembershipModel(1, 0, 0, 0.1),
                new MembershipModel(2, 1, 5000, 0.1),
                new MembershipModel(3, 2, 50000, 0.09),
                new MembershipModel(4, 3, 300000, 0.08),
                new MembershipModel(5, 4, 1500000, 0.08),
                new MembershipModel(6, 5, 5000000, 0.07));
        return membershipModels.stream().filter(a -> a.getLevel() == level).findFirst().orElse(null);
    }

    private LoanModel getFakeLoan(UserModel loaner, LoanType loanType, long amount, int periods, double baseRate, Date recheckTime) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanAmount(amount);
        fakeLoanModel.setLoanerLoginName(loaner.getLoginName());
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("id");
        fakeLoanModel.setAgentLoginName(loaner.getLoginName());
        fakeLoanModel.setType(loanType);
        fakeLoanModel.setPeriods(periods);
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setBaseRate(baseRate);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(recheckTime);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);

        return fakeLoanModel;
    }

    private InvestModel getFakeInvest(long loanId, Long transferInvestId, long amount, String loginName, Date investTime, InvestStatus investStatus, TransferStatus transferStatus) {
        InvestModel fakeInvestModel = new InvestModel(IdGenerator.generate(), loanId, transferInvestId, amount, loginName, investTime, Source.WEB, null, 0.1);
        fakeInvestModel.setStatus(investStatus);
        fakeInvestModel.setTransferStatus(transferStatus);
        MembershipModel membershipModel = getMembershipModelByLevel(userMembershipLevelMap.get(loginName));
        fakeInvestModel.setInvestFeeRate(membershipModel.getFee());

        return fakeInvestModel;
    }

    private LoanRepayModel getFakeLoanRepayModel(long loanRepayId, long loanId, int period, long corpus, long expectedInterest, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        LoanRepayModel fakeLoanRepay = new LoanRepayModel(loanRepayId, loanId, period, corpus, expectedInterest, expectedRepayDate, repayStatus);
        fakeLoanRepay.setActualRepayDate(actualRepayDate);
        return fakeLoanRepay;
    }

    private InvestExtraRateModel getFakeInvestExtraRate(long loanId, long investId, long amount, String loginName) {
        InvestExtraRateModel investExtraRateModel = new InvestExtraRateModel();
        investExtraRateModel.setLoanId(loanId);
        investExtraRateModel.setInvestId(investId);
        investExtraRateModel.setAmount(amount);
        investExtraRateModel.setLoginName(loginName);
        investExtraRateModel.setExtraRate(0.2);
        investExtraRateModel.setExpectedInterest(100);
        investExtraRateModel.setExpectedFee(5);
        investExtraRateModel.setRepayDate(new DateTime().plusDays(20).toDate());
        investExtraRateModel.setCreatedTime(new Date());

        return investExtraRateModel;
    }
}
