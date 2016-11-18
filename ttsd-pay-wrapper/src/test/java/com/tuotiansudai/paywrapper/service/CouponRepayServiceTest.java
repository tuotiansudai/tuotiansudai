package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.coupon.service.impl.CouponRepayServiceImpl;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration
@Transactional
public class CouponRepayServiceTest {

    @InjectMocks
    private CouponRepayServiceImpl couponRepayService;

    @Mock
    private CouponRepayMapper couponRepayMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private UserCouponMapper userCouponMapper;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private AmountTransfer amountTransfer;

    @Mock
    private SystemBillService systemBillService;

    @Mock
    private UserBillMapper userBillMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    private final static String REPAY_REDIS_KEY_TEMPLATE = "COUPON_REPAY:{0}";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNormalRepayCouponRepayIsSuccess() throws PayException, AmountTransferException {
        UserModel userModel = mockUser("testCouponRepay", "13900880000", "12311@abc");
        LoanModel loanModel = fakeLoanModel(userModel.getLoginName());
        LoanRepayModel loanRepay = this.getFakeLoanRepayModel(idGenerator.generate(), loanModel.getId(), 1, 0, 100000, new DateTime().withMillisOfSecond(0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.REPAYING);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(loanRepay);
        CouponModel couponModel = mockCoupon(userModel.getLoginName(), 200000l);
        AccountModel accountModel = mockAccountModel(userModel.getLoginName());
        InvestModel investModel = mockInvest(idGenerator.generate(), userModel.getLoginName(), 50000l);
        UserCouponModel userCouponModel = mockUserCoupon(userModel.getLoginName(), couponModel.getId(), loanModel.getId(), investModel.getId());
        List<UserCouponModel> userCouponModels = Lists.newArrayList(userCouponModel);
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepay.getId()));

        CouponRepayModel couponRepayModel = new CouponRepayModel();
        couponRepayModel.setLoginName(userModel.getLoginName());
        couponRepayModel.setCouponId(couponModel.getId());
        couponRepayModel.setUserCouponId(userCouponModel.getId());
        couponRepayModel.setPeriod(loanRepay.getPeriod());

        when(loanRepayMapper.findById(loanRepay.getId())).thenReturn(loanRepay);
        when(loanMapper.findById(loanRepay.getLoanId())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanRepay.getLoanId())).thenReturn(loanRepayModels);
        when(userCouponMapper.findByLoanId(loanRepay.getLoanId(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON, CouponType.BIRTHDAY_COUPON))).thenReturn(userCouponModels);
        when(transferApplicationMapper.findByTransferInvestId(anyLong(), any(List.class))).thenReturn(null);
        when(couponMapper.findById(couponModel.getId())).thenReturn(couponModel);
        when(investMapper.findById(investModel.getId())).thenReturn(investModel);
        when(accountMapper.findByLoginName(accountModel.getLoginName())).thenReturn(accountModel);
        when(couponRepayMapper.findByUserCouponIdAndPeriod(userCouponModel.getId(), couponRepayModel.getPeriod())).thenReturn(couponRepayModel);
        when(paySyncClient.send(eq(TransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class))).thenReturn(responseModel);
        when(redisWrapperClient.hget(redisKey, String.valueOf(couponRepayModel.getId()))).thenReturn(null);
        doNothing().when(userCouponMapper).update(any(UserCouponModel.class));

        couponRepayService.repay(loanRepay.getId(), false);

        ArgumentCaptor<UserCouponModel> userCouponModelArgumentCaptor = ArgumentCaptor.forClass(UserCouponModel.class);
        verify(userCouponMapper, times(1)).update(userCouponModelArgumentCaptor.capture());
        assertEquals("4", String.valueOf(userCouponModelArgumentCaptor.getValue().getActualFee()));
        assertEquals("45", String.valueOf(userCouponModelArgumentCaptor.getValue().getActualInterest()));
        verify(paySyncClient, times(1)).send(eq(TransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class));
        ArgumentCaptor<String> syncRequestStatusArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(2)).hset(anyString(), anyString(), syncRequestStatusArgumentCaptor.capture());
        syncRequestStatusArgumentCaptor.getAllValues();
        assertThat(syncRequestStatusArgumentCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));
        assertThat(syncRequestStatusArgumentCaptor.getAllValues().get(1), is(SyncRequestStatus.SUCCESS.name()));
    }

    @Test
    public void shouldNormalRepayCouponRepayIsIdempotentSuccess() throws PayException, AmountTransferException {
        UserModel userModel = mockUser("testCouponRepay", "13900880000", "12311@abc");
        LoanModel loanModel = fakeLoanModel(userModel.getLoginName());
        LoanRepayModel loanRepay = this.getFakeLoanRepayModel(idGenerator.generate(), loanModel.getId(), 1, 0, 100000, new DateTime().withMillisOfSecond(0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.REPAYING);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(loanRepay);
        CouponModel couponModel = mockCoupon(userModel.getLoginName(), 200000l);
        AccountModel accountModel = mockAccountModel(userModel.getLoginName());
        InvestModel investModel = mockInvest(idGenerator.generate(), userModel.getLoginName(), 50000l);
        UserCouponModel userCouponModel = mockUserCoupon(userModel.getLoginName(), couponModel.getId(), loanModel.getId(), investModel.getId());
        List<UserCouponModel> userCouponModels = Lists.newArrayList(userCouponModel);
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepay.getId()));

        CouponRepayModel couponRepayModel = new CouponRepayModel();
        couponRepayModel.setLoginName(userModel.getLoginName());
        couponRepayModel.setCouponId(couponModel.getId());
        couponRepayModel.setUserCouponId(userCouponModel.getId());
        couponRepayModel.setPeriod(loanRepay.getPeriod());

        when(loanRepayMapper.findById(loanRepay.getId())).thenReturn(loanRepay);
        when(loanMapper.findById(loanRepay.getLoanId())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanRepay.getLoanId())).thenReturn(loanRepayModels);
        when(userCouponMapper.findByLoanId(loanRepay.getLoanId(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON, CouponType.BIRTHDAY_COUPON))).thenReturn(userCouponModels);
        when(transferApplicationMapper.findByTransferInvestId(anyLong(), any(List.class))).thenReturn(null);
        when(couponMapper.findById(couponModel.getId())).thenReturn(couponModel);
        when(investMapper.findById(investModel.getId())).thenReturn(investModel);
        when(accountMapper.findByLoginName(accountModel.getLoginName())).thenReturn(accountModel);
        when(couponRepayMapper.findByUserCouponIdAndPeriod(userCouponModel.getId(), couponRepayModel.getPeriod())).thenReturn(couponRepayModel);
        when(paySyncClient.send(eq(TransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class))).thenReturn(responseModel);
        when(redisWrapperClient.hget(redisKey, String.valueOf(couponRepayModel.getId()))).thenReturn(SyncRequestStatus.SUCCESS.name());
        doNothing().when(userCouponMapper).update(any(UserCouponModel.class));

        couponRepayService.repay(loanRepay.getId(), false);
        verify(paySyncClient, never()).send(eq(TransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class));
        ArgumentCaptor<String> syncRequestStatusArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, never()).hset(anyString(), anyString(), syncRequestStatusArgumentCaptor.capture());
    }

    @Test
    public void shouldAdvanceRepayCouponRepayIsSuccess() throws PayException, AmountTransferException {
        UserModel userModel = mockUser("testCouponRepay", "13900880000", "12311@abc");
        LoanModel loanModel = fakeLoanModel(userModel.getLoginName());
        LoanRepayModel currentLoanRepay = this.getFakeLoanRepayModel(idGenerator.generate(), loanModel.getId(), 1, 0, 100000, new DateTime().minusDays(10).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(currentLoanRepay);
        CouponModel couponModel = mockCoupon(userModel.getLoginName(), 200000l);
        AccountModel accountModel = mockAccountModel(userModel.getLoginName());
        InvestModel investModel = mockInvest(idGenerator.generate(), userModel.getLoginName(), 50000l);
        UserCouponModel userCouponModel = mockUserCoupon(userModel.getLoginName(), couponModel.getId(), loanModel.getId(), investModel.getId());
        List<UserCouponModel> userCouponModels = Lists.newArrayList(userCouponModel);
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        CouponRepayModel couponRepayModel = new CouponRepayModel();
        couponRepayModel.setLoginName(userModel.getLoginName());
        couponRepayModel.setCouponId(couponModel.getId());
        couponRepayModel.setUserCouponId(userCouponModel.getId());

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(currentLoanRepay.getId()));

        when(loanRepayMapper.findById(currentLoanRepay.getId())).thenReturn(currentLoanRepay);
        when(loanMapper.findById(currentLoanRepay.getLoanId())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(currentLoanRepay.getLoanId())).thenReturn(loanRepayModels);
        when(userCouponMapper.findByLoanId(currentLoanRepay.getLoanId(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON, CouponType.BIRTHDAY_COUPON))).thenReturn(userCouponModels);
        when(transferApplicationMapper.findByTransferInvestId(anyLong(), any(List.class))).thenReturn(null);
        when(couponMapper.findById(anyLong())).thenReturn(couponModel);
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(couponRepayMapper.findByUserCouponIdAndPeriod(anyLong(), anyLong())).thenReturn(couponRepayModel);
        when(paySyncClient.send(eq(TransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class))).thenReturn(responseModel);
        when(redisWrapperClient.hget(redisKey, String.valueOf(couponRepayModel.getId()))).thenReturn(null);
        doNothing().when(userCouponMapper).update(any(UserCouponModel.class));
        doNothing().when(systemBillService).transferOut(anyLong(), anyLong(), any(SystemBillBusinessType.class), anyString());
        couponRepayService.repay(currentLoanRepay.getId(), true);

        ArgumentCaptor<UserCouponModel> userCouponModelArgumentCaptor = ArgumentCaptor.forClass(UserCouponModel.class);
        verify(userCouponMapper, times(1)).update(userCouponModelArgumentCaptor.capture());
        assertEquals("4", String.valueOf(userCouponModelArgumentCaptor.getValue().getActualFee()));
        assertEquals("45", String.valueOf(userCouponModelArgumentCaptor.getValue().getActualInterest()));
        verify(paySyncClient, times(1)).send(eq(TransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class));
        ArgumentCaptor<String> syncRequestStatusArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(2)).hset(anyString(), anyString(), syncRequestStatusArgumentCaptor.capture());
        syncRequestStatusArgumentCaptor.getAllValues();
        assertThat(syncRequestStatusArgumentCaptor.getAllValues().get(0), is(SyncRequestStatus.SENT.name()));
        assertThat(syncRequestStatusArgumentCaptor.getAllValues().get(1), is(SyncRequestStatus.SUCCESS.name()));
    }

    @Test
    public void shouldAdvanceRepayCouponRepayIsIdempotentSuccess() throws PayException, AmountTransferException {
        UserModel userModel = mockUser("testCouponRepay", "13900880000", "12311@abc");
        LoanModel loanModel = fakeLoanModel(userModel.getLoginName());
        LoanRepayModel currentLoanRepay = this.getFakeLoanRepayModel(idGenerator.generate(), loanModel.getId(), 1, 0, 100000, new DateTime().minusDays(10).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(currentLoanRepay);
        CouponModel couponModel = mockCoupon(userModel.getLoginName(), 200000l);
        AccountModel accountModel = mockAccountModel(userModel.getLoginName());
        InvestModel investModel = mockInvest(idGenerator.generate(), userModel.getLoginName(), 50000l);
        UserCouponModel userCouponModel = mockUserCoupon(userModel.getLoginName(), couponModel.getId(), loanModel.getId(), investModel.getId());
        List<UserCouponModel> userCouponModels = Lists.newArrayList(userCouponModel);
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        CouponRepayModel couponRepayModel = new CouponRepayModel();
        couponRepayModel.setLoginName(userModel.getLoginName());
        couponRepayModel.setCouponId(couponModel.getId());
        couponRepayModel.setUserCouponId(userCouponModel.getId());

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(currentLoanRepay.getId()));

        when(loanRepayMapper.findById(currentLoanRepay.getId())).thenReturn(currentLoanRepay);
        when(loanMapper.findById(currentLoanRepay.getLoanId())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(currentLoanRepay.getLoanId())).thenReturn(loanRepayModels);
        when(userCouponMapper.findByLoanId(currentLoanRepay.getLoanId(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON, CouponType.BIRTHDAY_COUPON))).thenReturn(userCouponModels);
        when(transferApplicationMapper.findByTransferInvestId(anyLong(), any(List.class))).thenReturn(null);
        when(couponMapper.findById(anyLong())).thenReturn(couponModel);
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(couponRepayMapper.findByUserCouponIdAndPeriod(anyLong(), anyLong())).thenReturn(couponRepayModel);
        when(paySyncClient.send(eq(TransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class))).thenReturn(responseModel);
        when(redisWrapperClient.hget(redisKey, String.valueOf(couponRepayModel.getId()))).thenReturn(SyncRequestStatus.SUCCESS.name());
        doNothing().when(userCouponMapper).update(any(UserCouponModel.class));

        couponRepayService.repay(currentLoanRepay.getId(), false);
        verify(paySyncClient, never()).send(eq(TransferMapper.class), any(TransferRequestModel.class), eq(TransferResponseModel.class));
        ArgumentCaptor<String> syncRequestStatusArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, never()).hset(anyString(), anyString(), syncRequestStatusArgumentCaptor.capture());
    }

    @Test
    public void shouldGenerateCouponPayment() {
        UserModel testGenerateUser = mockUser("testGenerateUser", "18911239999", "18911239999@163.com");
        LoanModel loanModel = fakeLoanModel(testGenerateUser.getLoginName());
        CouponModel couponModel = mockCoupon(testGenerateUser.getLoginName(), 200000l);
        InvestModel investModel = mockInvest(idGenerator.generate(), testGenerateUser.getLoginName(), 50000l);
        UserCouponModel userCouponModel = mockUserCoupon(testGenerateUser.getLoginName(), couponModel.getId(), loanModel.getId(), investModel.getId());

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(Lists.newArrayList(investModel));
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(anyLong(), anyListOf(CouponType.class))).thenReturn(Lists.newArrayList(userCouponModel));
        when(couponMapper.findById(anyLong())).thenReturn(couponModel);
        couponRepayService.generateCouponRepay(loanModel.getId());

        doNothing().when(couponRepayMapper).create(any(CouponRepayModel.class));
        ArgumentCaptor<CouponRepayModel> argumentCaptor = ArgumentCaptor.forClass(CouponRepayModel.class);
        verify(couponRepayMapper, times(3)).create(argumentCaptor.capture());
        CouponRepayModel value = argumentCaptor.getValue();

        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);
        Date repayDate = lastRepayDate.plusDays(InterestCalculator.DAYS_OF_MONTH).toDate();
        assertEquals("123", String.valueOf(value.getExpectedInterest()));
        assertEquals("12", String.valueOf(value.getExpectedFee()));

        when(couponRepayMapper.findByUserCouponIdAndPeriod(anyLong(),anyLong())).thenReturn(new CouponRepayModel());
        couponRepayService.generateCouponRepay(loanModel.getId());
        verify(couponRepayMapper, times(3)).create(argumentCaptor.capture());
    }


    protected LoanRepayModel getFakeLoanRepayModel(long loanRepayId, long loanId, int period, long corpus, long expectedInterest, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        LoanRepayModel fakeLoanRepay = new LoanRepayModel(loanRepayId, loanId, period, corpus, expectedInterest, expectedRepayDate, repayStatus);
        fakeLoanRepay.setActualRepayDate(actualRepayDate);
        return fakeLoanRepay;
    }

    private LoanModel fakeLoanModel(String loginName) {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("loginName");
        loanModel.setBaseRate(16.00);
        long id = idGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
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
        loanModel.setLoanAmount(100000L);
        loanModel.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000L);
        loanModel.setMinInvestAmount(1);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setLoanerLoginName(loginName);
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setDuration(ProductType._90.getDuration());
        loanModel.setRecheckTime(new DateTime().minusDays(10).withTimeAtStartOfDay().toDate());
        return loanModel;
    }

    private CouponModel mockCoupon(String loginName, long amount) {
        CouponModel couponModel = new CouponModel();
        couponModel.setId(idGenerator.generate());
        couponModel.setAmount(amount);
        couponModel.setTotalCount(1L);
        couponModel.setActive(true);
        couponModel.setRate(0.03);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setCouponType(CouponType.INTEREST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        return couponModel;
    }

    private UserCouponModel mockUserCoupon(String loginName, long couponId, long loanId, long investId) {
        UserCouponModel userCouponModel = new UserCouponModel(loginName, couponId, new Date(), new Date());
        userCouponModel.setId(idGenerator.generate());
        userCouponModel.setLoanId(loanId);
        userCouponModel.setUsedTime(new DateTime().minusDays(10).toDate());
        userCouponModel.setInvestId(investId);
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        return userCouponModel;
    }

    private UserModel mockUser(String loginName, String mobile, String email) {
        UserModel um = new UserModel();
        um.setId(idGenerator.generate());
        um.setLoginName(loginName);
        um.setMobile(mobile);
        um.setEmail(email);
        um.setStatus(UserStatus.ACTIVE);
        um.setPassword(loginName);
        um.setSalt(loginName);
        return um;
    }

    private InvestModel mockInvest(long loanId, String loginName, long amount) {
        InvestModel im = new InvestModel(idGenerator.generate(), loanId, null, amount, loginName, new Date(), Source.WEB, null, 0.1);
        im.setStatus(InvestStatus.SUCCESS);
        return im;
    }

    private AccountModel mockAccountModel(String loginName) {
        AccountModel model = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        return model;
    }
}
