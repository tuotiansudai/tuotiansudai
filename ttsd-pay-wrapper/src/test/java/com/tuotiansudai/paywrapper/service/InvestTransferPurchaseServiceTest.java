package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class InvestTransferPurchaseServiceTest {

    private MockWebServer mockPayServer;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestTransferPurchaseService investTransferPurchaseService;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Before
    public void setUp() throws Exception {
        this.mockPayServer = new MockWebServer();
        this.mockPayServer.start();

        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.injectInto(payAsyncClient);
        MockPayGateWrapper.setUrl(this.mockPayServer.getUrl("/").toString());

        this.generateMockResponse(10);
    }

    @After
    public void clean() throws Exception {
        this.mockPayServer.shutdown();
    }

    @Test
    public void shouldPurchaseNoPassword() throws Exception {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 1, 900000, 100);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 10000, 1000, new DateTime().withDate(2016, 3, 31).toDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 10000, 1000, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        InvestDto investDto = new InvestDto();
        investDto.setLoginName(transferee.getLoginName());
        investDto.setTransferApplicationId(String.valueOf(fakeTransferApplication.getId()));
        investDto.setSource(Source.WEB);

        UserMembershipModel userMembershipModel = new UserMembershipModel(investDto.getLoginName(), 1, new DateTime(2200, 1, 1, 1, 1).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel.setCreatedTime(new DateTime().plusDays(-1).toDate());
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(transferee.getLoginName());

        investTransferPurchaseService.noPasswordPurchase(investDto);

        InvestModel investModel = investMapper.findPaginationByLoginName(transferee.getLoginName(), 0, 1).get(0);

        investTransferPurchaseService.postPurchase(investModel.getId());
        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(), Lists.newArrayList(TransferStatus.SUCCESS));
        TransferApplicationModel transferApplicationModel = transferApplicationModels.get(0);
        InvestModel actualInvest = investMapper.findById(transferApplicationModel.getInvestId());
        assertThat(actualInvest.getStatus(), is(InvestStatus.SUCCESS));

        verifyAmountTransferMessage(transferrer, transferee, fakeTransferApplication);

        InvestModel actualTransferInvest = investMapper.findById(fakeTransferInvest.getId());
        assertThat(actualTransferInvest.getTransferStatus(), is(TransferStatus.SUCCESS));
        List<TransferApplicationModel> actualTransferApplications = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(), Lists.newArrayList(TransferStatus.SUCCESS));
        TransferApplicationModel actualTransferApplication = actualTransferApplications.get(0);
        assertNotNull(actualTransferApplication);
        assertThat(actualTransferApplication.getInvestId(), is(transferApplicationModel.getInvestId()));

        List<InvestRepayModel> actualTransferrerInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeTransferInvest.getId());
        assertThat(actualTransferrerInvestRepays.size(), is(2));
        assertThat(actualTransferrerInvestRepays.get(0).getPeriod(), is(1));
        assertTrue(actualTransferrerInvestRepays.get(0).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedInterest(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedFee(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getPeriod(), is(2));
        assertTrue(actualTransferrerInvestRepays.get(1).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedInterest(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedFee(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getCorpus(), is(0L));

        List<InvestRepayModel> actualTransfereeInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getInvestId());
        assertThat(actualTransfereeInvestRepays.size(), is(2));
        assertThat(actualTransfereeInvestRepays.get(0).getPeriod(), is(1));
        assertFalse(actualTransfereeInvestRepays.get(0).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedInterest(), is(fakeTransferInvestRepay1.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedFee(), is(new BigDecimal(actualTransfereeInvestRepays.get(0).getExpectedInterest()).multiply(new BigDecimal(membershipModel.getFee())).longValue()));
        assertThat(actualTransfereeInvestRepays.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransfereeInvestRepays.get(1).getPeriod(), is(2));
        assertFalse(actualTransfereeInvestRepays.get(1).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedInterest(), is(fakeTransferInvestRepay2.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedFee(), is(new BigDecimal(actualTransfereeInvestRepays.get(1).getExpectedInterest()).multiply(new BigDecimal(membershipModel.getFee())).longValue()));
        assertThat(actualTransfereeInvestRepays.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(1).getCorpus(), is(fakeTransferInvestRepay2.getCorpus()));

        verifySystemBillMessage(actualTransferApplication);
    }

    @Test
    public void shouldSuccessOnFirstPeriodAndLoanIsOnInterestStartLoanOut() throws Exception {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        Date transferTime = new DateTime().withDate(2016, 3, 11).toDate();
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 1, 900000, 100);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), 1000000, transferee.getLoginName(), transferTime, InvestStatus.WAIT_PAY, TransferStatus.TRANSFERABLE);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 10000, 1000, new DateTime().withDate(2016, 3, 31).toDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 10000, 1000, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeInvest.getLoginName(), 1, new DateTime(2200, 1, 1, 1, 1).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel.setCreatedTime(new DateTime().plusDays(-1).toDate());
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(transferee.getLoginName());

        investTransferPurchaseService.postPurchase(fakeInvest.getId());

        InvestModel actualInvest = investMapper.findById(fakeInvest.getId());
        assertThat(actualInvest.getStatus(), is(InvestStatus.SUCCESS));

        verifyAmountTransferMessage(transferrer, transferee, fakeTransferApplication);

        InvestModel actualTransferInvest = investMapper.findById(fakeTransferInvest.getId());
        assertThat(actualTransferInvest.getTransferStatus(), is(TransferStatus.SUCCESS));
        List<TransferApplicationModel> actualTransferApplications = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(), Lists.newArrayList(TransferStatus.SUCCESS));
        TransferApplicationModel actualTransferApplication = actualTransferApplications.get(0);
        assertNotNull(actualTransferApplication);
        assertThat(actualTransferApplication.getInvestId(), is(fakeInvest.getId()));

        List<InvestRepayModel> actualTransferrerInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeTransferInvest.getId());
        assertThat(actualTransferrerInvestRepays.size(), is(2));
        assertThat(actualTransferrerInvestRepays.get(0).getPeriod(), is(1));
        assertTrue(actualTransferrerInvestRepays.get(0).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedInterest(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedFee(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getPeriod(), is(2));
        assertTrue(actualTransferrerInvestRepays.get(1).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedInterest(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedFee(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getCorpus(), is(0L));

        List<InvestRepayModel> actualTransfereeInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvest.getId());
        assertThat(actualTransfereeInvestRepays.size(), is(2));
        assertThat(actualTransfereeInvestRepays.get(0).getPeriod(), is(1));
        assertFalse(actualTransfereeInvestRepays.get(0).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedInterest(), is(fakeTransferInvestRepay1.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedFee(), is(new BigDecimal(actualTransfereeInvestRepays.get(0).getExpectedInterest()).multiply(new BigDecimal(membershipModel.getFee())).longValue()));
        assertThat(actualTransfereeInvestRepays.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransfereeInvestRepays.get(1).getPeriod(), is(2));
        assertFalse(actualTransfereeInvestRepays.get(1).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedInterest(), is(fakeTransferInvestRepay2.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedFee(), is(new BigDecimal(actualTransfereeInvestRepays.get(1).getExpectedInterest()).multiply(new BigDecimal(membershipModel.getFee())).longValue()));
        assertThat(actualTransfereeInvestRepays.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(1).getCorpus(), is(fakeTransferInvestRepay2.getCorpus()));
        verifySystemBillMessage(actualTransferApplication);
    }

    @Test
    public void shouldSuccessOnFirstPeriodAndLoanIsOnInterestStartAtInvest() throws Exception {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        Date transferTime = new DateTime().withDate(2016, 3, 11).toDate();
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), new DateTime().withDate(2016, 2, 20).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 1, 900000, 100);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), 1000000, transferee.getLoginName(), transferTime, InvestStatus.WAIT_PAY, TransferStatus.TRANSFERABLE);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 13479, 1347, new DateTime().withDate(2016, 3, 31).toDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 9836, 983, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeInvest.getLoginName(), 1, new DateTime(2200, 1, 1, 1, 1).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel.setCreatedTime(new DateTime().plusDays(-1).toDate());
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(transferee.getLoginName());

        investTransferPurchaseService.postPurchase(fakeInvest.getId());

        InvestModel actualInvest = investMapper.findById(fakeInvest.getId());
        assertThat(actualInvest.getStatus(), is(InvestStatus.SUCCESS));

        verifyAmountTransferMessage(transferrer, transferee, fakeTransferApplication);

        InvestModel actualTransferInvest = investMapper.findById(fakeTransferInvest.getId());
        assertThat(actualTransferInvest.getTransferStatus(), is(TransferStatus.SUCCESS));
        List<TransferApplicationModel> actualTransferApplications = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(), Lists.newArrayList(TransferStatus.SUCCESS));
        TransferApplicationModel actualTransferApplication = actualTransferApplications.get(0);
        assertNotNull(actualTransferApplication);
        assertThat(actualTransferApplication.getInvestId(), is(fakeInvest.getId()));

        List<InvestRepayModel> actualTransferrerInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeTransferInvest.getId());
        assertThat(actualTransferrerInvestRepays.size(), is(2));
        assertThat(actualTransferrerInvestRepays.get(0).getPeriod(), is(1));
        assertTrue(actualTransferrerInvestRepays.get(0).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedInterest(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedFee(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getPeriod(), is(2));
        assertTrue(actualTransferrerInvestRepays.get(1).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedInterest(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedFee(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getCorpus(), is(0L));

        List<InvestRepayModel> actualTransfereeInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvest.getId());
        assertThat(actualTransfereeInvestRepays.size(), is(2));
        assertThat(actualTransfereeInvestRepays.get(0).getPeriod(), is(1));
        assertFalse(actualTransfereeInvestRepays.get(0).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedInterest(), is(fakeTransferInvestRepay1.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedFee(), is(fakeTransferInvestRepay1.getExpectedFee()));
        assertThat(actualTransfereeInvestRepays.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransfereeInvestRepays.get(1).getPeriod(), is(2));
        assertFalse(actualTransfereeInvestRepays.get(1).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedInterest(), is(fakeTransferInvestRepay2.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedFee(), is(new BigDecimal(actualTransfereeInvestRepays.get(1).getExpectedInterest()).multiply(new BigDecimal(membershipModel.getFee())).longValue()));
        assertThat(actualTransfereeInvestRepays.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(1).getCorpus(), is(fakeTransferInvestRepay2.getCorpus()));

        verifySystemBillMessage(actualTransferApplication);
    }

    @Test
    public void shouldSuccessOnLastPeriod() throws Exception {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        Date transferTime = new DateTime().withDate(2016, 4, 11).toDate();
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), new DateTime().withDate(2016, 2, 20).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 2, 900000, 100);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), 1000000, transferee.getLoginName(), transferTime, InvestStatus.WAIT_PAY, TransferStatus.TRANSFERABLE);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 13479, 1347, new DateTime().withDate(2016, 3, 31).toDate(), new DateTime().withDate(2016, 3, 31).toDate(), RepayStatus.COMPLETE);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 9836, 983, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        investTransferPurchaseService.postPurchase(fakeInvest.getId());

        InvestModel actualInvest = investMapper.findById(fakeInvest.getId());
        assertThat(actualInvest.getStatus(), is(InvestStatus.SUCCESS));

        verifyAmountTransferMessage(transferrer, transferee, fakeTransferApplication);

        InvestModel actualTransferInvest = investMapper.findById(fakeTransferInvest.getId());
        assertThat(actualTransferInvest.getTransferStatus(), is(TransferStatus.SUCCESS));
        List<TransferApplicationModel> actualTransferApplications = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(), Lists.newArrayList(TransferStatus.SUCCESS));
        TransferApplicationModel actualTransferApplication = actualTransferApplications.get(0);
        assertNotNull(actualTransferApplication);
        assertThat(actualTransferApplication.getInvestId(), is(fakeInvest.getId()));

        List<InvestRepayModel> actualTransferrerInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeTransferInvest.getId());
        assertThat(actualTransferrerInvestRepays.size(), is(2));
        assertThat(actualTransferrerInvestRepays.get(0).getPeriod(), is(1));
        assertFalse(actualTransferrerInvestRepays.get(0).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedInterest(), is(fakeTransferInvestRepay1.getExpectedInterest()));
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedFee(), is(fakeTransferInvestRepay1.getExpectedFee()));
        assertThat(actualTransferrerInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getPeriod(), is(2));
        assertTrue(actualTransferrerInvestRepays.get(1).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedInterest(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedFee(), is(0L));
        assertThat(actualTransferrerInvestRepays.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getCorpus(), is(0L));

        List<InvestRepayModel> actualTransfereeInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvest.getId());
        assertThat(actualTransfereeInvestRepays.size(), is(1));
        assertThat(actualTransfereeInvestRepays.get(0).getPeriod(), is(2));
        assertFalse(actualTransfereeInvestRepays.get(0).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedInterest(), is(fakeTransferInvestRepay2.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedFee(), is(fakeTransferInvestRepay2.getExpectedFee()));
        assertThat(actualTransfereeInvestRepays.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(0).getCorpus(), is(fakeTransferInvestRepay2.getCorpus()));
        verifySystemBillMessage(actualTransferApplication);
    }

    private void verifyAmountTransferMessage(UserModel transferrer, UserModel transferee, TransferApplicationModel fakeTransferApplication) throws IOException {

        String transferMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
        AmountTransferMessage transferMessage = JsonConverter.readValue(transferMessageBody, AmountTransferMessage.class);
        assertThat(transferMessage.getLoginName(), CoreMatchers.is(transferrer.getLoginName()));
        assertThat(transferMessage.getAmount(), CoreMatchers.is(fakeTransferApplication.getTransferAmount()));
        assertThat(transferMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_TRANSFER_OUT));
        assertThat(transferMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_IN_BALANCE));

        AmountTransferMessage transferFeeMessage = transferMessage.getNext();
        assertThat(transferFeeMessage.getLoginName(), CoreMatchers.is(transferrer.getLoginName()));
        assertThat(transferFeeMessage.getAmount(), CoreMatchers.is(fakeTransferApplication.getTransferFee()));
        assertThat(transferFeeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.TRANSFER_FEE));
        assertThat(transferFeeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));

        String transfereeMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
        AmountTransferMessage transfereeMessage = JsonConverter.readValue(transfereeMessageBody, AmountTransferMessage.class);
        assertThat(transfereeMessage.getLoginName(), CoreMatchers.is(transferee.getLoginName()));
        assertThat(transfereeMessage.getAmount(), CoreMatchers.is(fakeTransferApplication.getTransferAmount()));
        assertThat(transfereeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_TRANSFER_IN));
        assertThat(transfereeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));
    }

    private void verifySystemBillMessage(TransferApplicationModel actualTransferApplication) throws IOException {
        String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.SystemBill.getQueueName()));
        SystemBillMessage message = JsonConverter.readValue(messageBody, SystemBillMessage.class);
        assertThat(message.getAmount(), CoreMatchers.is(actualTransferApplication.getTransferFee()));
        assertThat(message.getBusinessType(), CoreMatchers.is(SystemBillBusinessType.TRANSFER_FEE));
    }

    @Test
    public void shouldSuccessClearCouponRepayOnPostPurchase() throws Exception {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        Date transferTime = new DateTime().withDate(2016, 3, 11).toDate();
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        this.createFakeTransferApplication(fakeTransferInvest, 1, 900000, 100);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), 1000000, transferee.getLoginName(), transferTime, InvestStatus.WAIT_PAY, TransferStatus.TRANSFERABLE);
        this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 10000, 1000, new DateTime().withDate(2016, 3, 31).toDate(), null, RepayStatus.REPAYING);
        this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 10000, 1000, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeInvest.getLoginName(), 1, new DateTime(2200, 1, 1, 1, 1).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel.setCreatedTime(new DateTime().plusDays(-1).toDate());
        userMembershipMapper.create(userMembershipModel);
        CouponModel fakeInterestCoupon = this.createFakeInterestCoupon(1, "transferrer");
        UserCouponModel fakeUserCoupon = this.createFakeUserCoupon(transferee.getLoginName(), fakeInterestCoupon.getId(), fakeLoan.getId(), fakeTransferInvest.getId());
        couponRepayMapper.create(new CouponRepayModel(transferee.getLoginName(), fakeInterestCoupon.getId(), fakeUserCoupon.getId(), fakeTransferInvest.getId(), 100, 10, 1, new DateTime().withDate(2016, 1, 1).toDate()));

        investTransferPurchaseService.postPurchase(fakeInvest.getId());

        List<CouponRepayModel> couponRepayModels = couponRepayMapper.findByUserCouponByInvestId(fakeTransferInvest.getId());
        assertTrue(couponRepayModels.get(0).getActualFee() == 0);
        assertTrue(couponRepayModels.get(0).getActualInterest() == 0);
        assertTrue(couponRepayModels.get(0).isTransferred());
        assertTrue(couponRepayModels.get(0).getStatus().equals(RepayStatus.COMPLETE));

    }

    public UserCouponModel createFakeUserCoupon(String loginName, long couponId, long loanId, long investId) {
        UserCouponModel userCouponModel = new UserCouponModel(loginName, couponId, new Date(), new Date());
        userCouponModel.setLoanId(loanId);
        userCouponModel.setUsedTime(new Date());
        userCouponModel.setInvestId(investId);
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.create(userCouponModel);
        return userCouponModel;
    }

    public CouponModel createFakeInterestCoupon(double rate, String loginName) {
        CouponModel couponModel = new CouponModel();
        couponModel.setRate(rate);
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setStartTime(new Date());
        couponModel.setEndTime(new DateTime().plusDays(1).toDate());
        couponModel.setDeadline(30);
        couponModel.setActivatedBy(loginName);
        couponModel.setCreatedBy(loginName);
        couponModel.setTotalCount(10L);
        couponModel.setUsedCount(0L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.INTEREST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360));
        couponModel.setCouponSource("couponSource");
        couponMapper.create(couponModel);
        return couponModel;
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
        userMembershipModel.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel);
        return fakeUserModel;
    }

    private LoanModel createFakeLoan(LoanType loanType, long amount, int periods, double baseRate, Date recheckTime) {
        UserModel loaner = this.createFakeUser("loaner", 0, 0);
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
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    private InvestModel createFakeInvest(long loanId, Long transferInvestId, long amount, String loginName, Date investTime, InvestStatus investStatus, TransferStatus transferStatus) {
        InvestModel fakeInvestModel = new InvestModel(IdGenerator.generate(), loanId, transferInvestId, amount, loginName, investTime, Source.WEB, null, 0.1);
        fakeInvestModel.setStatus(investStatus);
        fakeInvestModel.setTransferStatus(transferStatus);
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        fakeInvestModel.setInvestFeeRate(membershipModel.getFee());
        investMapper.create(fakeInvestModel);
        return fakeInvestModel;
    }

    private InvestRepayModel createFakeInvestRepay(long investId, int period, long corpus, long expectedInterest, long expectedFee, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        InvestRepayModel fakeInvestRepayModel = new InvestRepayModel(IdGenerator.generate(), investId, period, corpus, expectedInterest, expectedFee, expectedRepayDate, repayStatus);
        fakeInvestRepayModel.setActualRepayDate(actualRepayDate);
        investRepayMapper.create(Lists.newArrayList(fakeInvestRepayModel));
        return fakeInvestRepayModel;
    }

    private TransferApplicationModel createFakeTransferApplication(InvestModel investModel, int period, long transferAmount, long transferFee) {
        TransferApplicationModel fakeTransferApplication = new TransferApplicationModel(investModel, "name", period, transferAmount, transferFee, new DateTime().plusDays(1).toDate(), 3, Source.WEB);
        transferApplicationMapper.create(fakeTransferApplication);
        return fakeTransferApplication;
    }

    private void generateMockResponse(int times) {
        for (int index = 0; index < times; index++) {
            MockResponse mockResponse = new MockResponse();
            mockResponse.setBody("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <META NAME=\"MobilePayPlatform\" CONTENT=\"mer_id=7099088&ret_code=0000&ret_msg=成功&sign_type=RSA&version=1.0&sign=rqxyL+LrtzdGba4k4rFd1cs232Kcc4aQaUHTQlfZ0y9ayowzpxMwnbrbKyVHPGRxVz/UzLdo6uhNjPmGHND8F/yT0TDXkF1K8KW5AEjCzOwq39dWhEpLon62a1K4fchubLrpdeAx45X1YqpqL0s6uug/jb4SeWAYPi0ktnlHFVE=\">\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "  </body>\n" +
                    "</html>");
            mockResponse.setResponseCode(200);
            this.mockPayServer.enqueue(mockResponse);
        }
    }
}
