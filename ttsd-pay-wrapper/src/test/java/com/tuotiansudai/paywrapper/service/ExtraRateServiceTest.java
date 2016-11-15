package com.tuotiansudai.paywrapper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.repository.mapper.ExtraRateNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ExtraRateNotifyRequestModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExtraRateServiceTest{

    @Autowired
    private ExtraRateService extraRateService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Autowired
    protected PaySyncClient paySyncClient;

    @Autowired
    private ExtraRateNotifyRequestMapper extraRateNotifyRequestMapper;

    private MockWebServer mockServer;

    private MockWebServer mockUmPayService() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                "<html>\n" +
                "  <head>\n" +
                "\t<META NAME=\"MobilePayPlatform\" CONTENT=\"ret_code=0000&ret_msg=成功&sign=HvmQ2sW1pHii6FtvjfhXU/q2kvkb8hWYbCes2WV1jL/XCA38va1Il6eIlirj3/PrIPOexKpjhnYNQymUWRb2Jdwd6DfQ249dxNffENlgMSr3B4S5r/nZ9F+qeh27SyekDUfWfha84vElgIzLyBE/Rl/ISdLGth/9u9GWA5Wd2nM=\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "  </body>\n" +
                "</html>");
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        return mockWebServer;
    }

    @Before
    public void setup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockServer = mockUmPayService();
        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.setUrl(this.mockServer.getUrl("/").toString());
    }

    @After
    public void clean() throws Exception {
        this.mockServer.shutdown();
    }

    @Test
    public void shouldNormalRepayOk() throws PayException{
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 2, fakeLoan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));
        UserModel userModel = this.createFakeUser("investor", 1000000, 0);
        InvestModel investModel = this.createFakeInvest(fakeLoan.getId(), null, 1000000, userModel.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        this.createFakeInvestExtraRate(fakeLoan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());
        ExtraRateNotifyRequestModel extraRateNotifyRequestModel = this.getFakeExtraRateNotifyRequestModel(investModel.getId());
        extraRateNotifyRequestMapper.create(extraRateNotifyRequestModel);

        extraRateService.normalRepay(loanRepay2.getId());

        extraRateService.asyncExtraRateInvestCallback();

        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investModel.getId());

        long actualInterest = investExtraRateModel.getExpectedInterest();
        assertThat(investExtraRateModel.getExpectedInterest(), is(actualInterest));

        long actualFee = investExtraRateModel.getExpectedFee();
        assertThat(investExtraRateModel.getExpectedFee(), is(actualFee));
        assertThat(investExtraRateModel.getRepayAmount(), is(actualInterest - actualFee));

        List<UserBillModel> userBills = userBillMapper.findByLoginName(investModel.getLoginName());

        assertThat(userBills.get(0).getAmount(), is(actualInterest - actualFee));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.EXTRA_RATE));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
    }

    @Test
    public void shouldNormalRepayIsTransferFail() {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 2, fakeLoan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));
        UserModel userModel = this.createFakeUser("investor", 1000000, 0);
        InvestModel investModel = this.createFakeInvest(fakeLoan.getId(), null, 1000000, userModel.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);
        InvestExtraRateModel investExtraRateModel = this.createFakeInvestExtraRate(fakeLoan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());
        investExtraRateModel.setTransfer(true);
        investExtraRateMapper.update(investExtraRateModel);

        extraRateService.normalRepay(loanRepay2.getId());

        InvestExtraRateModel investExtraRateModel1 = investExtraRateMapper.findByInvestId(investModel.getId());

        assertThat(investExtraRateModel1.getActualFee(), is(0l));
        assertThat(investExtraRateModel1.getActualInterest(), is(0l));
        assertThat(investExtraRateModel1.getRepayAmount(), is(0l));
        assertTrue(investExtraRateModel1.getActualRepayDate() == null);

        List<UserBillModel> userBills = userBillMapper.findByLoginName(investModel.getLoginName());
        assertTrue(CollectionUtils.isEmpty(userBills));
    }

    @Test
    public void shouldNormalRepayIsLastFail() {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.REPAYING);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 2, fakeLoan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));
        UserModel userModel = this.createFakeUser("investor", 1000000, 0);
        InvestModel investModel = this.createFakeInvest(fakeLoan.getId(), null, 1000000, userModel.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);
        this.createFakeInvestExtraRate(fakeLoan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());

        extraRateService.normalRepay(loanRepay1.getId());

        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investModel.getId());

        assertThat(investExtraRateModel.getActualFee(), is(0l));
        assertThat(investExtraRateModel.getActualInterest(), is(0l));
        assertThat(investExtraRateModel.getRepayAmount(), is(0l));
        assertTrue(investExtraRateModel.getActualRepayDate() == null);

        List<UserBillModel> userBills = userBillMapper.findByLoginName(investModel.getLoginName());
        assertTrue(CollectionUtils.isEmpty(userBills));
    }

    @Test
    public void shouldAdvanceRepayOk() {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 2, fakeLoan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));
        UserModel userModel = this.createFakeUser("investor", 1000000, 0);
        InvestModel investModel = this.createFakeInvest(fakeLoan.getId(), null, 1000000, userModel.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        this.createFakeInvestExtraRate(fakeLoan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());

        ExtraRateNotifyRequestModel extraRateNotifyRequestModel = this.getFakeExtraRateNotifyRequestModel(investModel.getId());
        extraRateNotifyRequestMapper.create(extraRateNotifyRequestModel);

        extraRateService.advanceRepay(loanRepay2.getId());

        extraRateService.asyncExtraRateInvestCallback();

        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investModel.getId());

        long actualInterest = InterestCalculator.calculateExtraLoanRateInterest(fakeLoan, investExtraRateModel.getExtraRate(), investModel, new Date());
        assertThat(investExtraRateModel.getActualInterest(), is(actualInterest));

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(investModel.getLoginName());
        long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(membershipModel.getFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        assertThat(investExtraRateModel.getActualFee(), is(actualFee));
        assertThat(investExtraRateModel.getRepayAmount(), is(actualInterest - actualFee));

        List<UserBillModel> userBills = userBillMapper.findByLoginName(investModel.getLoginName());

        assertThat(userBills.get(0).getAmount(), is(actualInterest - actualFee));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.EXTRA_RATE));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
    }

    @Test
    public void shouldAdvanceRepayIsTransferFail() {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), fakeLoan.getId(), 2, fakeLoan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));
        UserModel userModel = this.createFakeUser("investor", 1000000, 0);
        InvestModel investModel = this.createFakeInvest(fakeLoan.getId(), null, 1000000, userModel.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);
        InvestExtraRateModel investExtraRateModel = this.createFakeInvestExtraRate(fakeLoan.getId(), investModel.getId(), investModel.getAmount(), investModel.getLoginName());
        investExtraRateModel.setTransfer(true);
        investExtraRateMapper.update(investExtraRateModel);

        extraRateService.advanceRepay(loanRepay2.getId());

        InvestExtraRateModel investExtraRateModel1 = investExtraRateMapper.findByInvestId(investModel.getId());

        assertThat(investExtraRateModel1.getActualFee(), is(0l));
        assertThat(investExtraRateModel1.getActualInterest(), is(0l));
        assertThat(investExtraRateModel1.getRepayAmount(), is(0l));
        assertTrue(investExtraRateModel1.getActualRepayDate() == null);

        List<UserBillModel> userBills = userBillMapper.findByLoginName(investModel.getLoginName());
        assertTrue(CollectionUtils.isEmpty(userBills));
    }

    @Test
    public void shouldTransferPurchaseOk() {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);
        InvestModel fakeTransfeeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), 1000000, transferee.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        this.createFakeInvestExtraRate(fakeLoan.getId(), fakeTransferInvest.getId(), fakeTransferInvest.getAmount(), fakeTransferInvest.getLoginName());
        extraRateService.transferPurchase(fakeTransfeeInvest.getId());

        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(fakeTransferInvest.getId());
        assertTrue(investExtraRateModel.isTransfer());
    }

    private LoanRepayModel getFakeLoanRepayModel(long loanRepayId, long loanId, int period, long corpus, long expectedInterest, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        LoanRepayModel fakeLoanRepay = new LoanRepayModel(loanRepayId, loanId, period, corpus, expectedInterest, expectedRepayDate, repayStatus);
        fakeLoanRepay.setActualRepayDate(actualRepayDate);
        return fakeLoanRepay;
    }

    private InvestExtraRateModel createFakeInvestExtraRate(long loanId, long investId, long amount, String loginName) {
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
        investExtraRateMapper.create(investExtraRateModel);
        return investExtraRateModel;
    }

    private InvestModel createFakeInvest(long loanId, Long transferInvestId, long amount, String loginName, Date investTime, InvestStatus investStatus, TransferStatus transferStatus) {
        InvestModel fakeInvestModel = new InvestModel(idGenerator.generate(), loanId, transferInvestId, amount, loginName, investTime, Source.WEB, null, 0.1);
        fakeInvestModel.setStatus(investStatus);
        fakeInvestModel.setTransferStatus(transferStatus);
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        fakeInvestModel.setInvestFeeRate(membershipModel.getFee());
        investMapper.create(fakeInvestModel);
        return fakeInvestModel;
    }

    private UserModel createFakeUser(String loginName, long balance, long freeze) {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(loginName);
        fakeUserModel.setPassword("password");
        fakeUserModel.setMobile(loginName);
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.create(accountModel);
        MembershipModel membershipModel = membershipMapper.findByLevel(0);
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUserModel.getLoginName(), membershipModel.getId(), new DateTime().plusDays(1).toDate(), UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel);
        return fakeUserModel;
    }

    private LoanModel createFakeLoan(LoanType loanType, long amount, int periods, double baseRate, Date recheckTime) {
        UserModel loaner = this.createFakeUser("loaner", 0, 0);
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
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
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    private ExtraRateNotifyRequestModel getFakeExtraRateNotifyRequestModel(Long orderId){
        ExtraRateNotifyRequestModel model = new ExtraRateNotifyRequestModel();
        model.setSign("sign");
        model.setSignType("RSA");
        model.setMerId("mer_id");
        model.setVersion("1.0");
        model.setTradeNo("trade_no");
        model.setOrderId(String.valueOf(orderId));
        model.setStatus(NotifyProcessStatus.NOT_DONE.toString());
        model.setMerDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setService("");
        model.setRetCode("0000");
        model.setRequestData(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setRequestData("mer_date=20161101&mer_id=7099088&order_id="+orderId+"&ret_code=0000&sign_type=RSA&version=1.0&sign=JoP0KGZ1j6hXsovsqFMGfTNwqFXGQFbSMmGp+EfK4vzJtgwAjmESgusrND+KcWPZl+BI1aMiGX6Z6sySa31Xi9+OuTjRfMcWSSnAAcX1PBJdhhEci40XHUw8LRnN3WDwrswu4Zg71kaSrdNT/nGYBaszsvjjwWlhPxslz48cRvc=");
        return model;
    }
}