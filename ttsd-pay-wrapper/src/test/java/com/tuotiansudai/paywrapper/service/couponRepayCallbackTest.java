package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.job.CouponRepayNotifyCallbackJob;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.loanout.impl.CouponRepayServiceImpl;
import com.tuotiansudai.paywrapper.repository.mapper.CouponRepayNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.CouponRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration
@Transactional
public class couponRepayCallbackTest {

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
    private CouponRepayNotifyRequestMapper couponRepayNotifyRequestMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private UserBillMapper userBillMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldOnlyOneRecordsNormalCouponRepayCallBack(){

        UserModel userModel = mockUser("testCouponRepay", "13900880000", "12311@abc");
        LoanRepayModel loanRepay = this.getFakeLoanRepayModel(idGenerator.generate(), idGenerator.generate(), 1, 0, 100000, new DateTime().withMillisOfSecond(0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.REPAYING);
        LoanModel loanModel = fakeLoanModel(userModel.getLoginName());
        CouponModel couponModel = mockCoupon(userModel.getLoginName(), 200000l);
        AccountModel accountModel = mockAccountModel(userModel.getLoginName());
        InvestModel investModel = mockInvest(idGenerator.generate(), userModel.getLoginName(), 50000l);
        UserCouponModel userCouponModel = mockUserCoupon(userModel.getLoginName(), couponModel.getId(), loanModel.getId(), investModel.getId());
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        CouponRepayModel couponRepayModel1 = this.getFakeCouponRepayModel(userModel.getLoginName(), couponModel.getId(), userCouponModel.getId(), investModel.getId(), 1, 45, 5, new Date(), new Date());
        CouponRepayModel couponRepayModel2 = this.getFakeCouponRepayModel(userModel.getLoginName(), couponModel.getId(), userCouponModel.getId(), investModel.getId(), 2, 45, 5, null, new DateTime().plusDays(30).toDate());
        CouponRepayModel couponRepayModel3 = this.getFakeCouponRepayModel(userModel.getLoginName(), couponModel.getId(), userCouponModel.getId(), investModel.getId(), 3, 45, 5, null, new DateTime().plusDays(30).toDate());

        List<CouponRepayModel> couponRepayModelList = Lists.newArrayList(couponRepayModel1, couponRepayModel2, couponRepayModel3);

        CouponRepayNotifyRequestModel couponRepayNotifyRequestModel = this.mockCouponRepayNotifyRequestModel(couponRepayModel1.getId(), NotifyProcessStatus.NOT_DONE);
        List<CouponRepayNotifyRequestModel> couponRepayNotifyRequestModelList = Lists.newArrayList(couponRepayNotifyRequestModel);

        when(couponRepayMapper.findById(anyLong())).thenReturn(couponRepayModel1);
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findById(anyLong())).thenReturn(loanRepay);
        when(loanRepayMapper.findByLoanIdAndPeriod(anyLong(),anyInt())).thenReturn(loanRepay);
        when(userCouponMapper.findById(anyLong())).thenReturn(userCouponModel);
        when(couponMapper.findById(anyLong())).thenReturn(couponModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(couponRepayMapper.findByUserCouponByInvestId(anyLong())).thenReturn(couponRepayModelList);
        doNothing().when(userCouponMapper).update(any(UserCouponModel.class));
        doNothing().when(systemBillService).transferOut(anyLong(), anyLong(), any(SystemBillBusinessType.class), anyString());
        when(couponRepayNotifyRequestMapper.getTodoList(anyInt())).thenReturn(couponRepayNotifyRequestModelList);

        couponRepayService.asyncCouponRepayCallback();

        verify(redisWrapperClient, times(1)).decr(CouponRepayNotifyCallbackJob.COUPON_REPAY_JOB_TRIGGER_KEY);
        verify(couponRepayNotifyRequestMapper, times(1)).updateStatus(couponRepayNotifyRequestModel.getId(), NotifyProcessStatus.DONE);
        assertEquals(RepayStatus.COMPLETE, couponRepayModel1.getStatus());
        assertEquals(RepayStatus.REPAYING, couponRepayModel2.getStatus());
        assertEquals(RepayStatus.REPAYING, couponRepayModel3.getStatus());

    }

    @Test
    public void shouldOnlyOneRecordsAdvanceCouponRepayCallBack(){

        UserModel userModel = mockUser("testCouponRepay", "13900880000", "12311@abc");
        LoanRepayModel loanRepay = this.getFakeLoanRepayModel(idGenerator.generate(), idGenerator.generate(), 1, 0, 100000, new DateTime().withMillisOfSecond(0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.REPAYING);
        LoanModel loanModel = fakeLoanModel(userModel.getLoginName());
        CouponModel couponModel = mockCoupon(userModel.getLoginName(), 200000l);
        AccountModel accountModel = mockAccountModel(userModel.getLoginName());
        InvestModel investModel = mockInvest(idGenerator.generate(), userModel.getLoginName(), 50000l);
        UserCouponModel userCouponModel = mockUserCoupon(userModel.getLoginName(), couponModel.getId(), loanModel.getId(), investModel.getId());
        TransferResponseModel responseModel = new TransferResponseModel();
        responseModel.setRetCode("0000");

        CouponRepayModel couponRepayModel1 = this.getFakeCouponRepayModel(userModel.getLoginName(), couponModel.getId(), userCouponModel.getId(), investModel.getId(), 1, 45, 5, new DateTime().minusDays(10).toDate(), new DateTime().minusDays(8).toDate());
        CouponRepayModel couponRepayModel2 = this.getFakeCouponRepayModel(userModel.getLoginName(), couponModel.getId(), userCouponModel.getId(), investModel.getId(), 2, 45, 5, new DateTime().minusDays(5).toDate(), new DateTime().minusDays(1).toDate());
        CouponRepayModel couponRepayModel3 = this.getFakeCouponRepayModel(userModel.getLoginName(), couponModel.getId(), userCouponModel.getId(), investModel.getId(), 3, 45, 5, new DateTime().plusDays(15).toDate(), new DateTime().plusDays(15).toDate());

        List<CouponRepayModel> couponRepayModelList = Lists.newArrayList(couponRepayModel1, couponRepayModel2, couponRepayModel3);

        CouponRepayNotifyRequestModel couponRepayNotifyRequestModel = this.mockCouponRepayNotifyRequestModel(couponRepayModel1.getId(), NotifyProcessStatus.NOT_DONE);
        List<CouponRepayNotifyRequestModel> couponRepayNotifyRequestModelList = Lists.newArrayList(couponRepayNotifyRequestModel);

        when(couponRepayMapper.findById(anyLong())).thenReturn(couponRepayModel1);
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findById(anyLong())).thenReturn(loanRepay);
        when(loanRepayMapper.findByLoanIdAndPeriod(anyLong(),anyInt())).thenReturn(loanRepay);
        when(userCouponMapper.findById(anyLong())).thenReturn(userCouponModel);
        when(couponMapper.findById(anyLong())).thenReturn(couponModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        doNothing().when(userCouponMapper).update(any(UserCouponModel.class));
        when(couponRepayMapper.findByUserCouponByInvestId(anyLong())).thenReturn(couponRepayModelList);
        doNothing().when(systemBillService).transferOut(anyLong(), anyLong(), any(SystemBillBusinessType.class), anyString());
        when(couponRepayNotifyRequestMapper.getTodoList(anyInt())).thenReturn(couponRepayNotifyRequestModelList);

        couponRepayService.asyncCouponRepayCallback();

        verify(redisWrapperClient, times(1)).decr(CouponRepayNotifyCallbackJob.COUPON_REPAY_JOB_TRIGGER_KEY);
        verify(couponRepayNotifyRequestMapper, times(1)).updateStatus(couponRepayNotifyRequestModel.getId(), NotifyProcessStatus.DONE);
        assertEquals(RepayStatus.COMPLETE, couponRepayModel1.getStatus());
        assertEquals(RepayStatus.COMPLETE, couponRepayModel2.getStatus());
        assertEquals(RepayStatus.COMPLETE, couponRepayModel3.getStatus());

    }

    @Test
    public void shouldNoAnyRecords(){

        List<CouponRepayNotifyRequestModel> couponRepayNotifyRequestModelList = Lists.newArrayList();

        when(couponRepayNotifyRequestMapper.getTodoList(anyInt())).thenReturn(couponRepayNotifyRequestModelList);
        couponRepayService.asyncCouponRepayCallback();
        verify(redisWrapperClient, times(0)).decr(CouponRepayNotifyCallbackJob.COUPON_REPAY_JOB_TRIGGER_KEY);
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
        AccountModel model = new AccountModel(loginName, "payUserId", "payAccountId" , new Date());
        return model;
    }

    private CouponRepayNotifyRequestModel mockCouponRepayNotifyRequestModel(long orderId, NotifyProcessStatus notifyProcessStatus){
        CouponRepayNotifyRequestModel couponRepayNotifyRequestModel = new CouponRepayNotifyRequestModel();
        couponRepayNotifyRequestModel.setId(idGenerator.generate());
        couponRepayNotifyRequestModel.setSign("sign");
        couponRepayNotifyRequestModel.setSignType("RSA");
        couponRepayNotifyRequestModel.setMerId("mer_id");
        couponRepayNotifyRequestModel.setVersion("1.0");
        couponRepayNotifyRequestModel.setTradeNo("trade_no");
        couponRepayNotifyRequestModel.setOrderId(String.valueOf(orderId));
        couponRepayNotifyRequestModel.setStatus(notifyProcessStatus.toString());
        couponRepayNotifyRequestModel.setMerDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        couponRepayNotifyRequestModel.setService("");
        couponRepayNotifyRequestModel.setRetCode("0000");
        couponRepayNotifyRequestModel.setRequestData(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        couponRepayNotifyRequestModel.setRequestData("mer_date=20161101&mer_id=7099088&order_id="+orderId+"&ret_code=0000&sign_type=RSA&version=1.0&sign=JoP0KGZ1j6hXsovsqFMGfTNwqFXGQFbSMmGp+EfK4vzJtgwAjmESgusrND+KcWPZl+BI1aMiGX6Z6sySa31Xi9+OuTjRfMcWSSnAAcX1PBJdhhEci40XHUw8LRnN3WDwrswu4Zg71kaSrdNT/nGYBaszsvjjwWlhPxslz48cRvc=");
        return couponRepayNotifyRequestModel;
    }

    private CouponRepayModel getFakeCouponRepayModel(String loginName, long couponId, long userCouponId, long investId, int periods, long actualInterest, long actualFee, Date actualRepayDate, Date repayDate){
        CouponRepayModel couponRepayModel = new CouponRepayModel();
        couponRepayModel.setId(idGenerator.generate());
        couponRepayModel.setLoginName(loginName);
        couponRepayModel.setCouponId(couponId);
        couponRepayModel.setUserCouponId(userCouponId);
        couponRepayModel.setInvestId(investId);
        couponRepayModel.setPeriod(periods);
        couponRepayModel.setActualInterest(actualInterest);
        couponRepayModel.setActualFee(actualFee);
        couponRepayModel.setStatus(RepayStatus.REPAYING);
        couponRepayModel.setActualRepayDate(actualRepayDate);
        couponRepayModel.setRepayDate(repayDate);
        return couponRepayModel;
    }

}

