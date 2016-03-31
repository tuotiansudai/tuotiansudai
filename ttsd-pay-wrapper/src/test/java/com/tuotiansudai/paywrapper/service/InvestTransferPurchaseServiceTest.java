package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
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
    private IdGenerator idGenerator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Autowired
    private SystemBillMapper systemBillMapper;

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
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 1, 900000, true, 100);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 10000, 10, new DateTime().withDate(2016, 3, 31).toDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 10000, 10, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        InvestDto investDto = new InvestDto();
        investDto.setLoginName(transferee.getLoginName());
        investDto.setTransferInvestId(String.valueOf(fakeTransferInvest.getId()));
        investDto.setSource(Source.WEB);
        investTransferPurchaseService.noPasswordPurchase(investDto);

        InvestModel investModel = investMapper.findByLoginName(transferee.getLoginName(), 0, 1).get(0);

        investTransferPurchaseService.postPurchase(investModel.getId());
        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(), Lists.newArrayList(TransferStatus.SUCCESS));
        TransferApplicationModel transferApplicationModel = transferApplicationModels.get(0);
        InvestModel actualInvest = investMapper.findById(transferApplicationModel.getInvestId());
        assertThat(actualInvest.getStatus(), is(InvestStatus.SUCCESS));
        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(1));
        assertThat(transfereeUserBills.get(0).getAmount(), is(fakeTransferApplication.getTransferAmount()));
        assertThat(transfereeUserBills.get(0).getLoginName(), is(transferee.getLoginName()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.INVEST_TRANSFER_IN));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        InvestModel actualTransferInvest = investMapper.findById(fakeTransferInvest.getId());
        assertThat(actualTransferInvest.getTransferStatus(), is(TransferStatus.SUCCESS));
        List<TransferApplicationModel> actualTransferApplications = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(),Lists.newArrayList(TransferStatus.SUCCESS));
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
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedFee(), is(fakeTransferInvestRepay1.getExpectedFee()));
        assertThat(actualTransfereeInvestRepays.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransfereeInvestRepays.get(1).getPeriod(), is(2));
        assertFalse(actualTransfereeInvestRepays.get(1).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedInterest(), is(fakeTransferInvestRepay2.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedFee(), is(fakeTransferInvestRepay2.getExpectedFee()));
        assertThat(actualTransfereeInvestRepays.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(1).getCorpus(), is(fakeTransferInvestRepay2.getCorpus()));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(2));
        assertThat(transferrerUserBills.get(0).getAmount(), is(actualTransferApplication.getTransferAmount()));
        assertThat(transferrerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.INVEST_TRANSFER_OUT));
        assertThat(transferrerUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transferrerUserBills.get(1).getAmount(), is(actualTransferApplication.getTransferFee()));
        assertThat(transferrerUserBills.get(1).getBusinessType(), is(UserBillBusinessType.TRANSFER_FEE));
        assertThat(transferrerUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel = systemBillMapper.findByOrderId(actualTransferApplication.getId(), SystemBillBusinessType.TRANSFER_FEE);
        assertThat(systemBillModel.getAmount(), is(actualTransferApplication.getTransferFee()));
    }

    @Test
    public void shouldSuccessWhenTransferInterestOnFirstPeriodAndLoanIsOnInterestStartLoanOut() throws Exception {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        Date transferTime = new DateTime().withDate(2016, 3, 11).toDate();
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 1, 900000, true, 100);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), 1000000, transferee.getLoginName(), transferTime, InvestStatus.WAIT_PAY, TransferStatus.TRANSFERABLE);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 10000, 10, new DateTime().withDate(2016, 3, 31).toDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 10000, 10, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        investTransferPurchaseService.postPurchase(fakeInvest.getId());

        InvestModel actualInvest = investMapper.findById(fakeInvest.getId());
        assertThat(actualInvest.getStatus(), is(InvestStatus.SUCCESS));
        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(1));
        assertThat(transfereeUserBills.get(0).getAmount(), is(fakeTransferApplication.getTransferAmount()));
        assertThat(transfereeUserBills.get(0).getLoginName(), is(transferee.getLoginName()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.INVEST_TRANSFER_IN));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        InvestModel actualTransferInvest = investMapper.findById(fakeTransferInvest.getId());
        assertThat(actualTransferInvest.getTransferStatus(), is(TransferStatus.SUCCESS));
        List<TransferApplicationModel> actualTransferApplications = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(),Lists.newArrayList(TransferStatus.SUCCESS));
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
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedFee(), is(fakeTransferInvestRepay2.getExpectedFee()));
        assertThat(actualTransfereeInvestRepays.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(1).getCorpus(), is(fakeTransferInvestRepay2.getCorpus()));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(2));
        assertThat(transferrerUserBills.get(0).getAmount(), is(actualTransferApplication.getTransferAmount()));
        assertThat(transferrerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.INVEST_TRANSFER_OUT));
        assertThat(transferrerUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transferrerUserBills.get(1).getAmount(), is(actualTransferApplication.getTransferFee()));
        assertThat(transferrerUserBills.get(1).getBusinessType(), is(UserBillBusinessType.TRANSFER_FEE));
        assertThat(transferrerUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel = systemBillMapper.findByOrderId(actualTransferApplication.getId(), SystemBillBusinessType.TRANSFER_FEE);
        assertThat(systemBillModel.getAmount(), is(actualTransferApplication.getTransferFee()));
    }

    @Test
    public void shouldSuccessWhenNotTransferInterestOnFirstPeriodAndLoanIsOnInterestStartAtInvest() throws Exception {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        Date transferTime = new DateTime().withDate(2016, 3, 11).toDate();
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), new DateTime().withDate(2016, 2, 20).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 1, 900000, false, 100);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), 1000000, transferee.getLoginName(), transferTime, InvestStatus.WAIT_PAY, TransferStatus.TRANSFERABLE);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 13479, 1347, new DateTime().withDate(2016, 3, 31).toDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 9836, 10, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        investTransferPurchaseService.postPurchase(fakeInvest.getId());

        InvestModel actualInvest = investMapper.findById(fakeInvest.getId());
        assertThat(actualInvest.getStatus(), is(InvestStatus.SUCCESS));
        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(1));
        assertThat(transfereeUserBills.get(0).getAmount(), is(fakeTransferApplication.getTransferAmount()));
        assertThat(transfereeUserBills.get(0).getLoginName(), is(transferee.getLoginName()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.INVEST_TRANSFER_IN));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        InvestModel actualTransferInvest = investMapper.findById(fakeTransferInvest.getId());
        assertThat(actualTransferInvest.getTransferStatus(), is(TransferStatus.SUCCESS));
        List<TransferApplicationModel> actualTransferApplications = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(),Lists.newArrayList(TransferStatus.SUCCESS));
        TransferApplicationModel actualTransferApplication = actualTransferApplications.get(0);
        assertNotNull(actualTransferApplication);
        assertThat(actualTransferApplication.getInvestId(), is(fakeInvest.getId()));

        List<InvestRepayModel> actualTransferrerInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeTransferInvest.getId());
        assertThat(actualTransferrerInvestRepays.size(), is(2));
        assertThat(actualTransferrerInvestRepays.get(0).getPeriod(), is(1));
        assertTrue(actualTransferrerInvestRepays.get(0).isTransferred());
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedInterest(), is(6557L));
        assertThat(actualTransferrerInvestRepays.get(0).getExpectedFee(), is(655L));
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
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedInterest(), is(6885L));
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedFee(), is(688L));
        assertThat(actualTransfereeInvestRepays.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(0).getCorpus(), is(0L));
        assertThat(actualTransfereeInvestRepays.get(1).getPeriod(), is(2));
        assertFalse(actualTransfereeInvestRepays.get(1).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedInterest(), is(fakeTransferInvestRepay2.getExpectedInterest()));
        assertThat(actualTransfereeInvestRepays.get(1).getExpectedFee(), is(fakeTransferInvestRepay2.getExpectedFee()));
        assertThat(actualTransfereeInvestRepays.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(1).getCorpus(), is(fakeTransferInvestRepay2.getCorpus()));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(2));
        assertThat(transferrerUserBills.get(0).getAmount(), is(actualTransferApplication.getTransferAmount()));
        assertThat(transferrerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.INVEST_TRANSFER_OUT));
        assertThat(transferrerUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transferrerUserBills.get(1).getAmount(), is(actualTransferApplication.getTransferFee()));
        assertThat(transferrerUserBills.get(1).getBusinessType(), is(UserBillBusinessType.TRANSFER_FEE));
        assertThat(transferrerUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel = systemBillMapper.findByOrderId(actualTransferApplication.getId(), SystemBillBusinessType.TRANSFER_FEE);
        assertThat(systemBillModel.getAmount(), is(actualTransferApplication.getTransferFee()));
    }

    @Test
    public void shouldSuccessWhenNotTransferInterestOnLastPeriod() throws Exception {
        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        Date transferTime = new DateTime().withDate(2016, 4, 11).toDate();
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 1000000, 0);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), new DateTime().withDate(2016, 2, 20).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 2, 900000, false, 100);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), 1000000, transferee.getLoginName(), transferTime, InvestStatus.WAIT_PAY, TransferStatus.TRANSFERABLE);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 13479, 1347, new DateTime().withDate(2016, 3, 31).toDate(), new DateTime().withDate(2016, 3, 31).toDate(), RepayStatus.COMPLETE);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 9836, 983, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        investTransferPurchaseService.postPurchase(fakeInvest.getId());

        InvestModel actualInvest = investMapper.findById(fakeInvest.getId());
        assertThat(actualInvest.getStatus(), is(InvestStatus.SUCCESS));
        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(1));
        assertThat(transfereeUserBills.get(0).getAmount(), is(fakeTransferApplication.getTransferAmount()));
        assertThat(transfereeUserBills.get(0).getLoginName(), is(transferee.getLoginName()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.INVEST_TRANSFER_IN));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        InvestModel actualTransferInvest = investMapper.findById(fakeTransferInvest.getId());
        assertThat(actualTransferInvest.getTransferStatus(), is(TransferStatus.SUCCESS));
        List<TransferApplicationModel> actualTransferApplications = transferApplicationMapper.findByTransferInvestId(fakeTransferInvest.getId(),Lists.newArrayList(TransferStatus.SUCCESS));
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
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedInterest(), is(3278L));
        assertThat(actualTransferrerInvestRepays.get(1).getExpectedFee(), is(327L));
        assertThat(actualTransferrerInvestRepays.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualTransferrerInvestRepays.get(1).getCorpus(), is(0L));

        List<InvestRepayModel> actualTransfereeInvestRepays = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvest.getId());
        assertThat(actualTransfereeInvestRepays.size(), is(1));
        assertThat(actualTransfereeInvestRepays.get(0).getPeriod(), is(2));
        assertFalse(actualTransfereeInvestRepays.get(0).isTransferred());
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedInterest(), is(6557L));
        assertThat(actualTransfereeInvestRepays.get(0).getExpectedFee(), is(655L));
        assertThat(actualTransfereeInvestRepays.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(actualTransfereeInvestRepays.get(0).getCorpus(), is(fakeTransferInvestRepay2.getCorpus()));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(2));
        assertThat(transferrerUserBills.get(0).getAmount(), is(actualTransferApplication.getTransferAmount()));
        assertThat(transferrerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.INVEST_TRANSFER_OUT));
        assertThat(transferrerUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transferrerUserBills.get(1).getAmount(), is(actualTransferApplication.getTransferFee()));
        assertThat(transferrerUserBills.get(1).getBusinessType(), is(UserBillBusinessType.TRANSFER_FEE));
        assertThat(transferrerUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel = systemBillMapper.findByOrderId(actualTransferApplication.getId(), SystemBillBusinessType.TRANSFER_FEE);
        assertThat(systemBillModel.getAmount(), is(actualTransferApplication.getTransferFee()));
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
        AccountModel accountModel = new AccountModel(loginName, loginName, "id", "payUserId", "payAccountId", new Date());
        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.create(accountModel);
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
        fakeLoanModel.setInvestFeeRate(0.1);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(recheckTime);
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    private InvestModel createFakeInvest(long loanId, Long transferInvestId, long amount, String loginName, Date investTime, InvestStatus investStatus, TransferStatus transferStatus) {
        InvestModel fakeInvestModel = new InvestModel(idGenerator.generate(), loanId, transferInvestId, amount, loginName, Source.WEB, null);
        fakeInvestModel.setStatus(investStatus);
        fakeInvestModel.setTransferStatus(transferStatus);
        fakeInvestModel.setCreatedTime(investTime);
        investMapper.create(fakeInvestModel);
        return fakeInvestModel;
    }

    private InvestRepayModel createFakeInvestRepay(long investId, int period, long corpus, long expectedInterest, long expectedFee, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        InvestRepayModel fakeInvestRepayModel = new InvestRepayModel(idGenerator.generate(), investId, period, corpus, expectedInterest, expectedFee, expectedRepayDate, repayStatus);
        fakeInvestRepayModel.setActualRepayDate(actualRepayDate);
        investRepayMapper.create(Lists.newArrayList(fakeInvestRepayModel));
        return fakeInvestRepayModel;
    }

    private TransferApplicationModel createFakeTransferApplication(InvestModel investModel, int period, long transferAmount, boolean transferInterest, long transferFee) {
        TransferApplicationModel fakeTransferApplication = new TransferApplicationModel(investModel, "name", period, transferAmount, transferInterest, transferFee, new DateTime().plusDays(1).toDate(),0);
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
