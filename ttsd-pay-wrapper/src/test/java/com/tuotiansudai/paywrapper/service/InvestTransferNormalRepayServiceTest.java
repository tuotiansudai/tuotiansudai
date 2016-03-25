package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestTransferNormalRepayServiceTest {

    private MockWebServer mockPayServer;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

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
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Autowired
    private SystemBillMapper systemBillMapper;

    @Resource(name = "normalRepayServiceImpl")
    private RepayService normalRepayService;

    public InvestTransferNormalRepayServiceTest() {
    }

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
    public void shouldRepayFirstPeriod() throws Exception {
        DateTime recheckTime = new DateTime().minusDays(30);
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 0, 0);
        long loanAmount = 10000;
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.12, recheckTime.toDate());
        DateTime transferrerInvestTime = recheckTime.minusDays(10);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, loanAmount, transferrer.getLoginName(), transferrerInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);

        DateTime repayDate1 = new DateTime();
        DateTime repayDate2 = repayDate1.plusDays(30);
        LoanRepayModel loanRepay1 = this.createFakeLoanRepay(fakeLoan.getId(), 1, 0,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(fakeTransferInvest), transferrerInvestTime, repayDate1), repayDate1.toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel loanRepay2 = this.createFakeLoanRepay(fakeLoan.getId(), 2, loanAmount,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(fakeTransferInvest), repayDate1, repayDate2), repayDate2.toDate(), null, RepayStatus.REPAYING);

        DateTime transfereeInvestTime = recheckTime.plusDays(10);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, null, 1, 9000, true, 100, transfereeInvestTime.toDate(), TransferStatus.SUCCESS);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), loanAmount, transferee.getLoginName(), transfereeInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);

        BaseDto<PayFormDataDto> dto = normalRepayService.repay(fakeLoan.getId());

        assertTrue(dto.getData().getStatus());
        long expectedInterest = InterestCalculator.calculateInterest(fakeLoan, loanAmount * Days.daysBetween(transferrerInvestTime.minusDays(1), repayDate1).getDays() + 1);
        assertThat(dto.getData().getFields().get("amount"), is(String.valueOf(expectedInterest)));
    }

    @Test
    public void shouldRepayLastPeriod() throws Exception {
        DateTime recheckTime = new DateTime().minusDays(60);
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 0, 0);
        long loanAmount = 10000;
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.12, recheckTime.toDate());
        DateTime transferrerInvestTime = recheckTime.minusDays(70);
        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, loanAmount, transferrer.getLoginName(), transferrerInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);

        DateTime repayDate2 = new DateTime();
        DateTime repayDate1 = repayDate2.minusDays(30);
        LoanRepayModel loanRepay1 = this.createFakeLoanRepay(fakeLoan.getId(), 1, 0,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(fakeTransferInvest), transferrerInvestTime, repayDate1), repayDate1.toDate(), repayDate1.withTimeAtStartOfDay().toDate(), RepayStatus.COMPLETE);
        LoanRepayModel loanRepay2 = this.createFakeLoanRepay(fakeLoan.getId(), 2, loanAmount,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(fakeTransferInvest), repayDate1, repayDate2), repayDate2.toDate(), null, RepayStatus.REPAYING);

        DateTime transfereeInvestTime = repayDate1.plusDays(10);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, null, 1, 9000, true, 100, transfereeInvestTime.toDate(), TransferStatus.SUCCESS);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), fakeTransferInvest.getId(), loanAmount, transferee.getLoginName(), transfereeInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);

        BaseDto<PayFormDataDto> dto = normalRepayService.repay(fakeLoan.getId());

        assertTrue(dto.getData().getStatus());
        long expectedInterest = InterestCalculator.calculateInterest(fakeLoan, loanAmount * Days.daysBetween(repayDate1, repayDate2).getDays());
        assertThat(dto.getData().getFields().get("amount"), is(String.valueOf(loanAmount + expectedInterest)));
    }

    @Test
    public void shouldRepayCallbackFirstPeriodWhenTransferInterest() throws Exception {
        DateTime recheckTime = new DateTime().minusDays(30);
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 0, 0);
        long loanAmount = 10000L;
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.12, recheckTime.toDate());
        DateTime transferrerInvestTime = recheckTime.minusDays(10);
        InvestModel transferrerInvest = this.createFakeInvest(fakeLoan.getId(), null, loanAmount, transferrer.getLoginName(), transferrerInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);

        DateTime repayDate1 = new DateTime();
        DateTime repayDate2 = repayDate1.plusDays(30);
        LoanRepayModel loanRepay1 = this.createFakeLoanRepay(fakeLoan.getId(), 1, 0,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), recheckTime.minusDays(1), repayDate1), repayDate1.toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel loanRepay2 = this.createFakeLoanRepay(fakeLoan.getId(), 2, loanAmount,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), repayDate1, repayDate2), repayDate2.toDate(), null, RepayStatus.REPAYING);

        InvestRepayModel transferrerInvestRepay1 = this.createFakeInvestRepay(transferrerInvest.getId(), 1, 0, 0, 0, repayDate1.toDate(), null, RepayStatus.COMPLETE, true);
        InvestRepayModel transferrerInvestRepay2 = this.createFakeInvestRepay(transferrerInvest.getId(), 2, 0, 0, 0, repayDate2.toDate(), null, RepayStatus.COMPLETE, true);

        DateTime transfereeInvestTime = recheckTime.plusDays(10);
        InvestModel transfereeInvest = this.createFakeInvest(fakeLoan.getId(), transferrerInvest.getId(), loanAmount, transferee.getLoginName(), transfereeInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(transferrerInvest, transfereeInvest.getId(), 1, 9000, true, 100, transfereeInvestTime.toDate(), TransferStatus.SUCCESS);
        InvestRepayModel transfereeInvestRepay1 = this.createFakeInvestRepay(transfereeInvest.getId(), 1, 0,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, transfereeInvestTime, repayDate1), 0, repayDate1.toDate(), null, RepayStatus.REPAYING, false);
        InvestRepayModel transfereeInvestRepay2 = this.createFakeInvestRepay(transfereeInvest.getId(), 2, loanAmount,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, repayDate1, repayDate2), 0, repayDate2.toDate(), null, RepayStatus.REPAYING, false);

        normalRepayService.repay(fakeLoan.getId());
        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId()), "");
        boolean isSuccess = normalRepayService.postRepayCallback(loanRepay1.getId());

        assertTrue(isSuccess);

        assertThat(loanMapper.findById(fakeLoan.getId()).getStatus(), is(LoanStatus.REPAYING));

        LoanRepayModel loanRepay1Model = loanRepayMapper.findByLoanIdAndPeriod(fakeLoan.getId(), 1);
        assertThat(loanRepay1Model.getActualInterest(), is(134L));
        assertThat(loanRepay1Model.getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel transferrerInvestRepay1Model = investRepayMapper.findByInvestIdAndPeriod(transferrerInvest.getId(), 1);
        InvestRepayModel transfereeInvestRepay1Model = investRepayMapper.findByInvestIdAndPeriod(transfereeInvest.getId(), 1);

        assertThat(transferrerInvestRepay1Model.getPeriod(), is(1));
        assertNull(transferrerInvestRepay1Model.getActualRepayDate());
        assertThat(transferrerInvestRepay1Model.getActualInterest(), is(0L));
        assertThat(transferrerInvestRepay1Model.getActualFee(), is(0L));
        assertThat(transferrerInvestRepay1Model.getStatus(), is(RepayStatus.COMPLETE));

        assertThat(transfereeInvestRepay1Model.getPeriod(), is(1));
        assertThat(transfereeInvestRepay1Model.getActualRepayDate().getTime(), is(loanRepay1Model.getActualRepayDate().getTime()));
        assertThat(transfereeInvestRepay1Model.getActualInterest(), is(134L));
        assertThat(transfereeInvestRepay1Model.getActualFee(), is(13L));
        assertThat(transfereeInvestRepay1Model.getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(fakeLoan.getAgentLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepay1Model.getActualInterest()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(0));

        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(2));
        assertThat(transfereeUserBills.get(0).getAmount(), is(transfereeInvestRepay1Model.getActualInterest()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transfereeUserBills.get(1).getAmount(), is(transfereeInvestRepay1Model.getActualFee()));
        assertThat(transfereeUserBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(transfereeUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(transferrerInvestRepay1Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertNull(systemBillModel1);

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(transfereeInvestRepay1Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(transfereeInvestRepay1Model.getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(loanRepay1.getId(), SystemBillBusinessType.LOAN_REMAINING_INTEREST);
        assertNull(systemBillModel3);
    }

    @Test
    public void shouldRepayCallbackFirstPeriodWhenNotTransferInterest() throws Exception {
        DateTime recheckTime = new DateTime().minusDays(30);
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 0, 0);
        long loanAmount = 10000;
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.12, recheckTime.toDate());
        DateTime transferrerInvestTime = recheckTime.minusDays(10);
        InvestModel transferrerInvest = this.createFakeInvest(fakeLoan.getId(), null, loanAmount, transferrer.getLoginName(), transferrerInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);

        DateTime repayDate1 = new DateTime();
        DateTime repayDate2 = repayDate1.plusDays(30);
        LoanRepayModel loanRepay1 = this.createFakeLoanRepay(fakeLoan.getId(), 1, 0,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), recheckTime.minusDays(1), repayDate1), repayDate1.toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel loanRepay2 = this.createFakeLoanRepay(fakeLoan.getId(), 2, loanAmount,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), repayDate1, repayDate2), repayDate2.toDate(), null, RepayStatus.REPAYING);

        InvestRepayModel transferrerInvestRepay1 = this.createFakeInvestRepay(transferrerInvest.getId(), 1, 0, 0, 0, repayDate1.toDate(), null, RepayStatus.COMPLETE, true);
        InvestRepayModel transferrerInvestRepay2 = this.createFakeInvestRepay(transferrerInvest.getId(), 2, 0, 0, 0, repayDate2.toDate(), null, RepayStatus.COMPLETE, true);

        DateTime transfereeInvestTime = recheckTime.plusDays(10);
        InvestModel transfereeInvest = this.createFakeInvest(fakeLoan.getId(), transferrerInvest.getId(), loanAmount, transferee.getLoginName(), transfereeInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        InvestRepayModel transfereeInvestRepay1 = this.createFakeInvestRepay(transfereeInvest.getId(), 1, 0,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, transfereeInvestTime, repayDate1), 0, repayDate1.toDate(), null, RepayStatus.REPAYING, false);
        InvestRepayModel transfereeInvestRepay2 = this.createFakeInvestRepay(transfereeInvest.getId(), 2, loanAmount,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, repayDate1, repayDate2), 0, repayDate2.toDate(), null, RepayStatus.REPAYING, false);

        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(transferrerInvest, transfereeInvest.getId(), 1, 9000, false, 100, transfereeInvestTime.toDate(), TransferStatus.SUCCESS);

        normalRepayService.repay(fakeLoan.getId());
        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId()), "");
        boolean isSuccess = normalRepayService.postRepayCallback(loanRepay1.getId());

        assertTrue(isSuccess);

        assertThat(loanMapper.findById(fakeLoan.getId()).getStatus(), is(LoanStatus.REPAYING));

        LoanRepayModel loanRepay1Model = loanRepayMapper.findByLoanIdAndPeriod(fakeLoan.getId(), 1);
        assertThat(loanRepay1Model.getActualInterest(), is(134L));
        assertThat(loanRepay1Model.getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel transferrerInvestRepay1Model = investRepayMapper.findByInvestIdAndPeriod(transferrerInvest.getId(), 1);
        InvestRepayModel transfereeInvestRepay1Model = investRepayMapper.findByInvestIdAndPeriod(transfereeInvest.getId(), 1);

        assertThat(transferrerInvestRepay1Model.getPeriod(), is(1));
        assertThat(transferrerInvestRepay1Model.getActualRepayDate().getTime(), is(loanRepay1Model.getActualRepayDate().getTime()));
        assertThat(transferrerInvestRepay1Model.getActualInterest(), is(65L));
        assertThat(transferrerInvestRepay1Model.getActualFee(), is(6L));
        assertThat(transferrerInvestRepay1Model.getStatus(), is(RepayStatus.COMPLETE));

        assertThat(transfereeInvestRepay1Model.getPeriod(), is(1));
        assertThat(transfereeInvestRepay1Model.getActualRepayDate().getTime(), is(loanRepay1Model.getActualRepayDate().getTime()));
        assertThat(transfereeInvestRepay1Model.getActualInterest(), is(68L));
        assertThat(transfereeInvestRepay1Model.getActualFee(), is(6L));
        assertThat(transfereeInvestRepay1Model.getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(fakeLoan.getAgentLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepay1Model.getActualInterest()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(2));
        assertThat(transferrerUserBills.get(0).getAmount(), is(transferrerInvestRepay1Model.getActualInterest()));
        assertThat(transferrerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(transferrerUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transferrerUserBills.get(1).getAmount(), is(transferrerInvestRepay1Model.getActualFee()));
        assertThat(transferrerUserBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(transferrerUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(2));
        assertThat(transfereeUserBills.get(0).getAmount(), is(transfereeInvestRepay1Model.getActualInterest()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transfereeUserBills.get(1).getAmount(), is(transfereeInvestRepay1Model.getActualFee()));
        assertThat(transfereeUserBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(transfereeUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(transferrerInvestRepay1Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(transferrerInvestRepay1Model.getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(transfereeInvestRepay1Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(transfereeInvestRepay1Model.getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(loanRepay1.getId(), SystemBillBusinessType.LOAN_REMAINING_INTEREST);
        assertThat(systemBillModel3.getAmount(), is(1L));
        assertThat(systemBillModel3.getOperationType(), is(SystemBillOperationType.IN));
    }

    @Test
    public void shouldRepayCallbackLastPeriodWhenTransferInterest() {
        DateTime recheckTime = new DateTime().minusDays(60);
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 0, 0);
        long loanAmount = 10000L;
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.12, recheckTime.toDate());
        DateTime transferrerInvestTime = recheckTime.minusDays(10);
        InvestModel transferrerInvest = this.createFakeInvest(fakeLoan.getId(), null, loanAmount, transferrer.getLoginName(), transferrerInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);

        DateTime repayDate2 = new DateTime();
        DateTime repayDate1 = repayDate2.minusDays(30);
        LoanRepayModel loanRepay1 = this.createFakeLoanRepay(fakeLoan.getId(), 1, 0,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), recheckTime.minusDays(1), repayDate1), repayDate1.toDate(), repayDate1.withTimeAtStartOfDay().toDate(), RepayStatus.COMPLETE);
        LoanRepayModel loanRepay2 = this.createFakeLoanRepay(fakeLoan.getId(), 2, loanAmount,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), repayDate1, repayDate2), repayDate2.toDate(), null, RepayStatus.REPAYING);

        InvestRepayModel transferrerInvestRepay1 = this.createFakeInvestRepay(transferrerInvest.getId(), 1, 0,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, transferrerInvestTime.minusDays(1), repayDate1), 0, repayDate1.toDate(), repayDate1.toDate(), RepayStatus.COMPLETE, false);
        InvestRepayModel transferrerInvestRepay2 = this.createFakeInvestRepay(transferrerInvest.getId(), 2, 0, 0, 0, repayDate2.toDate(), null, RepayStatus.COMPLETE, true);

        DateTime transfereeInvestTime = repayDate2.minusDays(10);
        InvestModel transfereeInvest = this.createFakeInvest(fakeLoan.getId(), transferrerInvest.getId(), loanAmount, transferee.getLoginName(), transfereeInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(transferrerInvest, transfereeInvest.getId(), 2, 9000, true, 100, transfereeInvestTime.toDate(), TransferStatus.SUCCESS);
        InvestRepayModel transfereeInvestRepay2 = this.createFakeInvestRepay(transfereeInvest.getId(), 2, loanAmount,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, repayDate1, repayDate2), 0, repayDate2.toDate(), null, RepayStatus.REPAYING, false);

        normalRepayService.repay(fakeLoan.getId());
        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId()), "");
        boolean isSuccess = normalRepayService.postRepayCallback(loanRepay2.getId());

        assertTrue(isSuccess);

        assertThat(loanMapper.findById(fakeLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));

        LoanRepayModel loanRepay2Model = loanRepayMapper.findByLoanIdAndPeriod(fakeLoan.getId(), 2);
        assertThat(loanRepay2Model.getActualInterest(), is(98L));
        assertThat(loanRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel transferrerInvestRepay2Model = investRepayMapper.findByInvestIdAndPeriod(transferrerInvest.getId(), 2);
        InvestRepayModel transfereeInvestRepay2Model = investRepayMapper.findByInvestIdAndPeriod(transfereeInvest.getId(), 2);

        assertThat(transferrerInvestRepay2Model.getPeriod(), is(2));
        assertNull(transferrerInvestRepay2Model.getActualRepayDate());
        assertThat(transferrerInvestRepay2Model.getActualInterest(), is(0L));
        assertThat(transferrerInvestRepay2Model.getActualFee(), is(0L));
        assertThat(transferrerInvestRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        assertThat(transfereeInvestRepay2Model.getPeriod(), is(2));
        assertThat(transfereeInvestRepay2Model.getActualRepayDate().getTime(), is(loanRepay2Model.getActualRepayDate().getTime()));
        assertThat(transfereeInvestRepay2Model.getActualInterest(), is(98L));
        assertThat(transfereeInvestRepay2Model.getActualFee(), is(9L));
        assertThat(transfereeInvestRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(fakeLoan.getAgentLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepay2Model.getCorpus() + loanRepay2Model.getActualInterest()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(0));

        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(2));
        assertThat(transfereeUserBills.get(0).getAmount(), is(transfereeInvestRepay2Model.getCorpus() + transfereeInvestRepay2Model.getActualInterest()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transfereeUserBills.get(1).getAmount(), is(transfereeInvestRepay2Model.getActualFee()));
        assertThat(transfereeUserBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(transfereeUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(transferrerInvestRepay2Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertNull(systemBillModel1);

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(transfereeInvestRepay2Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(transfereeInvestRepay2Model.getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(loanRepay2.getId(), SystemBillBusinessType.LOAN_REMAINING_INTEREST);
        assertNull(systemBillModel3);
    }

    @Test
    public void shouldRepayCallbackLastPeriodWhenNotTransferInterest() {
        DateTime recheckTime = new DateTime().minusDays(60);
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 0, 0);
        long loanAmount = 10000L;
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.12, recheckTime.toDate());
        DateTime transferrerInvestTime = recheckTime.minusDays(10);
        InvestModel transferrerInvest = this.createFakeInvest(fakeLoan.getId(), null, loanAmount, transferrer.getLoginName(), transferrerInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);

        DateTime repayDate2 = new DateTime();
        DateTime repayDate1 = repayDate2.minusDays(30);
        LoanRepayModel loanRepay1 = this.createFakeLoanRepay(fakeLoan.getId(), 1, 0,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), recheckTime.minusDays(1), repayDate1), repayDate1.toDate(), repayDate1.withTimeAtStartOfDay().toDate(), RepayStatus.COMPLETE);
        LoanRepayModel loanRepay2 = this.createFakeLoanRepay(fakeLoan.getId(), 2, loanAmount,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), repayDate1, repayDate2), repayDate2.toDate(), null, RepayStatus.REPAYING);

        InvestRepayModel transferrerInvestRepay1 = this.createFakeInvestRepay(transferrerInvest.getId(), 1, 0,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, transferrerInvestTime.minusDays(1), repayDate1), 0, repayDate1.toDate(), repayDate1.toDate(), RepayStatus.COMPLETE, false);
        InvestRepayModel transferrerInvestRepay2 = this.createFakeInvestRepay(transferrerInvest.getId(), 2, 0, 0, 0, repayDate2.toDate(), null, RepayStatus.COMPLETE, true);

        DateTime transfereeInvestTime = repayDate2.minusDays(15);
        InvestModel transfereeInvest = this.createFakeInvest(fakeLoan.getId(), transferrerInvest.getId(), loanAmount, transferee.getLoginName(), transfereeInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(transferrerInvest, transfereeInvest.getId(), 2, 9000, false, 100, transfereeInvestTime.toDate(), TransferStatus.SUCCESS);
        InvestRepayModel transfereeInvestRepay2 = this.createFakeInvestRepay(transfereeInvest.getId(), 2, loanAmount,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, transfereeInvestTime.minusDays(1), repayDate2), 0, repayDate2.toDate(), null, RepayStatus.REPAYING, false);

        normalRepayService.repay(fakeLoan.getId());
        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId()), "");
        boolean isSuccess = normalRepayService.postRepayCallback(loanRepay2.getId());

        assertTrue(isSuccess);

        assertThat(loanMapper.findById(fakeLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));

        LoanRepayModel loanRepay2Model = loanRepayMapper.findByLoanIdAndPeriod(fakeLoan.getId(), 2);
        assertThat(loanRepay2Model.getActualInterest(), is(98L));
        assertThat(loanRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel transferrerInvestRepay2Model = investRepayMapper.findByInvestIdAndPeriod(transferrerInvest.getId(), 2);
        InvestRepayModel transfereeInvestRepay2Model = investRepayMapper.findByInvestIdAndPeriod(transfereeInvest.getId(), 2);

        assertThat(transferrerInvestRepay2Model.getPeriod(), is(2));
        assertThat(transferrerInvestRepay2Model.getActualRepayDate().getTime(), is(loanRepay2Model.getActualRepayDate().getTime()));
        assertThat(transferrerInvestRepay2Model.getActualInterest(), is(45L));
        assertThat(transferrerInvestRepay2Model.getActualFee(), is(4L));
        assertThat(transferrerInvestRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        assertThat(transfereeInvestRepay2Model.getPeriod(), is(2));
        assertThat(transfereeInvestRepay2Model.getActualRepayDate().getTime(), is(loanRepay2Model.getActualRepayDate().getTime()));
        assertThat(transfereeInvestRepay2Model.getActualInterest(), is(52L));
        assertThat(transfereeInvestRepay2Model.getActualFee(), is(5L));
        assertThat(transfereeInvestRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(fakeLoan.getAgentLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepay2Model.getCorpus() + loanRepay2Model.getActualInterest()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(2));
        assertThat(transferrerUserBills.get(0).getAmount(), is(transferrerInvestRepay2Model.getCorpus() + transferrerInvestRepay2Model.getActualInterest()));
        assertThat(transferrerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(transferrerUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transferrerUserBills.get(1).getAmount(), is(transferrerInvestRepay2Model.getActualFee()));
        assertThat(transferrerUserBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(transferrerUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(2));
        assertThat(transfereeUserBills.get(0).getAmount(), is(transfereeInvestRepay2Model.getCorpus() + transfereeInvestRepay2Model.getActualInterest()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transfereeUserBills.get(1).getAmount(), is(transfereeInvestRepay2Model.getActualFee()));
        assertThat(transfereeUserBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(transfereeUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(transferrerInvestRepay2Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(transferrerInvestRepay2Model.getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(transfereeInvestRepay2Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(transfereeInvestRepay2Model.getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(loanRepay2.getId(), SystemBillBusinessType.LOAN_REMAINING_INTEREST);
        assertThat(systemBillModel3.getAmount(), is(1L));
        assertThat(systemBillModel3.getOperationType(), is(SystemBillOperationType.IN));
    }

    @Test
    public void shouldRepayCallbackWhenCurrentPeriodHasNoTransfer() {
        DateTime recheckTime = new DateTime().minusDays(60);
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);
        UserModel transferee = this.createFakeUser("transferee", 0, 0);
        long loanAmount = 10000L;
        LoanModel fakeLoan = this.createFakeLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.12, recheckTime.toDate());
        DateTime transferrerInvestTime = recheckTime.minusDays(10);
        InvestModel transferrerInvest = this.createFakeInvest(fakeLoan.getId(), null, loanAmount, transferrer.getLoginName(), transferrerInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.SUCCESS);

        DateTime repayDate2 = new DateTime();
        DateTime repayDate1 = repayDate2.minusDays(30);
        LoanRepayModel loanRepay1 = this.createFakeLoanRepay(fakeLoan.getId(), 1, 0,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), recheckTime.minusDays(1), repayDate1), repayDate1.toDate(), repayDate1.withTimeAtStartOfDay().toDate(), RepayStatus.COMPLETE);
        LoanRepayModel loanRepay2 = this.createFakeLoanRepay(fakeLoan.getId(), 2, loanAmount,
                InterestCalculator.calculateLoanRepayInterest(fakeLoan, Lists.newArrayList(transferrerInvest), repayDate1, repayDate2), repayDate2.toDate(), null, RepayStatus.REPAYING);

        InvestRepayModel transferrerInvestRepay1 = this.createFakeInvestRepay(transferrerInvest.getId(), 1, 0, 0, 0, repayDate1.toDate(), null, RepayStatus.COMPLETE, true);
        InvestRepayModel transferrerInvestRepay2 = this.createFakeInvestRepay(transferrerInvest.getId(), 2, 0, 0, 0, repayDate2.toDate(), null, RepayStatus.COMPLETE, true);

        DateTime transfereeInvestTime = recheckTime.plusDays(10);
        InvestModel transfereeInvest = this.createFakeInvest(fakeLoan.getId(), transferrerInvest.getId(), loanAmount, transferee.getLoginName(), transfereeInvestTime.toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(transferrerInvest, transfereeInvest.getId(), 1, 9000, true, 100, transfereeInvestTime.toDate(), TransferStatus.SUCCESS);
        InvestRepayModel transfereeInvestRepay1 = this.createFakeInvestRepay(transfereeInvest.getId(), 1, loanAmount,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, recheckTime.minusDays(1), repayDate1), 0, repayDate1.toDate(), repayDate1.withTimeAtStartOfDay().toDate(), RepayStatus.COMPLETE, false);
        InvestRepayModel transfereeInvestRepay2 = this.createFakeInvestRepay(transfereeInvest.getId(), 2, loanAmount,
                InterestCalculator.calculateInvestRepayInterest(fakeLoan, transferrerInvest, repayDate1, repayDate2), 0, repayDate2.toDate(), null, RepayStatus.REPAYING, false);

        normalRepayService.repay(fakeLoan.getId());
        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId()), "");
        boolean isSuccess = normalRepayService.postRepayCallback(loanRepay2.getId());

        assertTrue(isSuccess);

        assertThat(loanMapper.findById(fakeLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));

        LoanRepayModel loanRepay2Model = loanRepayMapper.findByLoanIdAndPeriod(fakeLoan.getId(), 2);
        assertThat(loanRepay2Model.getActualInterest(), is(98L));
        assertThat(loanRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel transferrerInvestRepay2Model = investRepayMapper.findByInvestIdAndPeriod(transferrerInvest.getId(), 2);
        InvestRepayModel transfereeInvestRepay2Model = investRepayMapper.findByInvestIdAndPeriod(transfereeInvest.getId(), 2);

        assertThat(transferrerInvestRepay2Model.getPeriod(), is(2));
        assertNull(transferrerInvestRepay2Model.getActualRepayDate());
        assertThat(transferrerInvestRepay2Model.getActualInterest(), is(0L));
        assertThat(transferrerInvestRepay2Model.getActualFee(), is(0L));
        assertThat(transferrerInvestRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        assertThat(transfereeInvestRepay2Model.getPeriod(), is(2));
        assertThat(transfereeInvestRepay2Model.getActualRepayDate().getTime(), is(loanRepay2Model.getActualRepayDate().getTime()));
        assertThat(transfereeInvestRepay2Model.getActualInterest(), is(98L));
        assertThat(transfereeInvestRepay2Model.getActualFee(), is(9L));
        assertThat(transfereeInvestRepay2Model.getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(fakeLoan.getAgentLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepay2Model.getCorpus() + loanRepay2Model.getActualInterest()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> transferrerUserBills = userBillMapper.findByLoginName(transferrer.getLoginName());
        assertThat(transferrerUserBills.size(), is(0));

        List<UserBillModel> transfereeUserBills = userBillMapper.findByLoginName(transferee.getLoginName());
        assertThat(transfereeUserBills.size(), is(2));
        assertThat(transfereeUserBills.get(0).getAmount(), is(transfereeInvestRepay2Model.getCorpus() + transfereeInvestRepay2Model.getActualInterest()));
        assertThat(transfereeUserBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));
        assertThat(transfereeUserBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(transfereeUserBills.get(1).getAmount(), is(transfereeInvestRepay2Model.getActualFee()));
        assertThat(transfereeUserBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(transfereeUserBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(transferrerInvestRepay2Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertNull(systemBillModel1);

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(transfereeInvestRepay2Model.getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(transfereeInvestRepay2Model.getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(loanRepay2.getId(), SystemBillBusinessType.LOAN_REMAINING_INTEREST);
        assertNull(systemBillModel3);
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
        UserModel loaner = this.createFakeUser("loaner", 1000000, 0);
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanAmount(amount);
        fakeLoanModel.setLoanerLoginName(loaner.getLoginName());
        fakeLoanModel.setLoanerUserName("loanerUserName");
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
        InvestModel fakeInvestModel = new InvestModel();
        fakeInvestModel.setId(idGenerator.generate());
        fakeInvestModel.setLoanId(loanId);
        fakeInvestModel.setTransferInvestId(transferInvestId);
        fakeInvestModel.setAmount(amount);
        fakeInvestModel.setLoginName(loginName);
        fakeInvestModel.setSource(Source.WEB);
        fakeInvestModel.setStatus(investStatus);
        fakeInvestModel.setCreatedTime(investTime);
        fakeInvestModel.setTransferStatus(transferStatus);
        investMapper.create(fakeInvestModel);
        return fakeInvestModel;
    }

    private InvestRepayModel createFakeInvestRepay(long investId, int period, long corpus, long expectedInterest, long expectedFee, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus, boolean isTransferred) {
        InvestRepayModel fakeInvestRepayModel = new InvestRepayModel(idGenerator.generate(), investId, period, corpus, expectedInterest, expectedFee, expectedRepayDate, repayStatus);
        fakeInvestRepayModel.setActualRepayDate(actualRepayDate);
        fakeInvestRepayModel.setTransferred(isTransferred);
        investRepayMapper.create(Lists.newArrayList(fakeInvestRepayModel));
        return fakeInvestRepayModel;
    }

    private LoanRepayModel createFakeLoanRepay(long loanId, int period, long corpus, long expectedInterest, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        LoanRepayModel fakeLoanRepay = new LoanRepayModel(idGenerator.generate(), loanId, period, corpus, expectedInterest, expectedRepayDate, repayStatus);
        fakeLoanRepay.setActualRepayDate(actualRepayDate);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepay));
        return fakeLoanRepay;
    }

    private TransferApplicationModel createFakeTransferApplication(InvestModel transferInvestModel, Long investId, int period, long transferAmount, boolean transferInterest, long transferFee, Date transferTime, TransferStatus transferStatus) {
        TransferApplicationModel fakeTransferApplication = new TransferApplicationModel(transferInvestModel, "name", period, transferAmount, transferInterest, transferFee, new DateTime().plusDays(1).toDate());
        fakeTransferApplication.setInvestId(investId);
        fakeTransferApplication.setTransferTime(transferTime);
        fakeTransferApplication.setStatus(transferStatus);
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

    private Map<String, String> getFakeCallbackParamsMap(long orderId) {
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("service", "project_transfer_notify")
                .put("sign_type", "RSA")
                .put("sign", "sign")
                .put("mer_id", "mer_id")
                .put("version", "1.0")
                .put("trade_no", "trade_no")
                .put("order_id", String.valueOf(orderId))
                .put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .put("ret_code", "0000")
                .build());
    }
}
