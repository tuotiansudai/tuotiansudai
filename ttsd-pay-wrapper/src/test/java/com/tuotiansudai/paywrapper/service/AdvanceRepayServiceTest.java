package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
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

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AdvanceRepayServiceTest {

    private MockWebServer mockPayServer;

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

    @Autowired
    private RepayGeneratorService repayGeneratorService;

    @Resource(name = "advanceRepayServiceImpl")
    private RepayService advanceRepayService;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Before
    public void setUp() throws Exception {
        this.mockPayServer = new MockWebServer();
        this.mockPayServer.start();

        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.injectInto(payAsyncClient);
        MockPayGateWrapper.setUrl(this.mockPayServer.getUrl("/").toString());
    }

    @After
    public void clean() throws Exception {
        this.mockPayServer.shutdown();
    }

    @Test
    public void shouldAdvanceRepayFirstPeriodWhenLoanTypeIsType1() throws Exception {
        DateTime today = new DateTime();
        UserModel fakeLoaner = this.getFakeUser("loaner");
        UserModel fakeInvestor = this.getFakeUser("investor");
        userMapper.create(fakeLoaner);
        userMapper.create(fakeInvestor);
        AccountModel fakeLoanerAccount = getFakeAccount(fakeLoaner);
        AccountModel fakeInvestorAccount = getFakeAccount(fakeInvestor);
        accountMapper.create(fakeLoanerAccount);
        accountMapper.create(fakeInvestorAccount);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, fakeLoaner.getLoginName(), today.toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel = getFakeInvestModel(fakeNormalLoan.getId(), loanAmount, fakeInvestor.getLoginName(), today.minusDays(10).toDate());
        investMapper.create(fakeInvestModel);
        repayGeneratorService.generateRepay(fakeNormalLoan.getId());

        BaseDto<PayFormDataDto> dto = advanceRepayService.repay(fakeNormalLoan.getId());

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getFields().get("amount"), is("10036"));

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());

        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(loanRepayModels.get(0).getActualInterest(), is(36L));
        assertThat(new DateTime(loanRepayModels.get(0).getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(today.withTimeAtStartOfDay().getMillis()));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(2).getStatus(), is(RepayStatus.REPAYING));
    }

    @Test
    public void shouldAdvanceRepayFirstPeriodWhenLoanTypeIsType3() throws Exception {
        DateTime today = new DateTime();
        UserModel fakeLoaner = this.getFakeUser("loaner");
        UserModel fakeInvestor = this.getFakeUser("investor");
        userMapper.create(fakeLoaner);
        userMapper.create(fakeInvestor);
        AccountModel fakeLoanerAccount = getFakeAccount(fakeLoaner);
        AccountModel fakeInvestorAccount = getFakeAccount(fakeInvestor);
        accountMapper.create(fakeLoanerAccount);
        accountMapper.create(fakeInvestorAccount);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, fakeLoaner.getLoginName(), today.toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel = getFakeInvestModel(fakeNormalLoan.getId(), loanAmount, fakeInvestor.getLoginName(), today.minusDays(10).toDate());
        investMapper.create(fakeInvestModel);
        repayGeneratorService.generateRepay(fakeNormalLoan.getId());

        BaseDto<PayFormDataDto> dto = advanceRepayService.repay(fakeNormalLoan.getId());

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getFields().get("amount"), is("10003"));

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());

        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(loanRepayModels.get(0).getActualInterest(), is(3L));
        assertThat(new DateTime(loanRepayModels.get(0).getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(today.withTimeAtStartOfDay().getMillis()));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(2).getStatus(), is(RepayStatus.REPAYING));
    }

    @Test
    public void shouldAdvancedRepayLastPeriod() throws Exception {
        DateTime today = new DateTime();
        UserModel fakeLoaner = this.getFakeUser("loaner");
        UserModel fakeInvestor = this.getFakeUser("investor");
        userMapper.create(fakeLoaner);
        userMapper.create(fakeInvestor);
        AccountModel fakeLoanerAccount = getFakeAccount(fakeLoaner);
        AccountModel fakeInvestorAccount = getFakeAccount(fakeInvestor);
        accountMapper.create(fakeLoanerAccount);
        accountMapper.create(fakeInvestorAccount);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.09, 0.03, 0.1, fakeLoaner.getLoginName(), today.minusDays(30).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel = getFakeInvestModel(fakeNormalLoan.getId(), loanAmount, fakeInvestor.getLoginName(), today.minusDays(30).toDate());
        investMapper.create(fakeInvestModel);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 0, 0, today.minusDays(10).toDate(), today.minusDays(20).toDate(), RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, loanAmount, today.toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        BaseDto<PayFormDataDto> dto = advanceRepayService.repay(fakeNormalLoan.getId());

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getFields().get("amount"), is("10065"));

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());

        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(loanRepayModels.get(1).getActualInterest(), is(65L));
        assertThat(new DateTime(loanRepayModels.get(1).getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(today.withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void shouldAdvanceRepayMiddlePeriod() throws Exception {
        DateTime today = new DateTime();
        UserModel fakeLoaner = this.getFakeUser("loaner");
        UserModel fakeInvestor = this.getFakeUser("investor");
        userMapper.create(fakeLoaner);
        userMapper.create(fakeInvestor);
        AccountModel fakeLoanerAccount = getFakeAccount(fakeLoaner);
        AccountModel fakeInvestorAccount = getFakeAccount(fakeInvestor);
        accountMapper.create(fakeLoanerAccount);
        accountMapper.create(fakeInvestorAccount);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, fakeLoaner.getLoginName(), today.minusDays(30).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel = getFakeInvestModel(fakeNormalLoan.getId(), 10000, fakeInvestor.getLoginName(), today.minusDays(30).toDate());
        investMapper.create(fakeInvestModel);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 0, 0, today.minusDays(10).toDate(), today.minusDays(20).toDate(), RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.plusDays(1).toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, loanAmount, today.plusDays(10).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3));

        BaseDto<PayFormDataDto> dto = advanceRepayService.repay(fakeNormalLoan.getId());

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getFields().get("amount"), is("10065"));

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());

        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(loanRepayModels.get(1).getActualInterest(), is(65L));
        assertThat(new DateTime(loanRepayModels.get(1).getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(today.withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void shouldAdvancedRepayWhenCurrentPeriodIsComplete() throws Exception {
        DateTime today = new DateTime();
        UserModel fakeLoaner = this.getFakeUser("loaner");
        UserModel fakeInvestor = this.getFakeUser("investor");
        userMapper.create(fakeLoaner);
        userMapper.create(fakeInvestor);
        AccountModel fakeLoanerAccount = getFakeAccount(fakeLoaner);
        AccountModel fakeInvestorAccount = getFakeAccount(fakeInvestor);
        accountMapper.create(fakeLoanerAccount);
        accountMapper.create(fakeInvestorAccount);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.09, 0.03, 0.1, fakeLoaner.getLoginName(), today.minusDays(30).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel = getFakeInvestModel(fakeNormalLoan.getId(), loanAmount, fakeInvestor.getLoginName(), today.minusDays(30).toDate());
        investMapper.create(fakeInvestModel);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 0, 0, today.plusDays(10).toDate(), today.minusDays(10).toDate(), RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, loanAmount, today.plusDays(20).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        BaseDto<PayFormDataDto> dto = advanceRepayService.repay(fakeNormalLoan.getId());

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getFields().get("amount"), is("10032"));

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());

        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(loanRepayModels.get(1).getActualInterest(), is(32L));
        assertThat(new DateTime(loanRepayModels.get(1).getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(today.withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void shouldAdvancedRepayWhenCurrentPeriodIsCompleteAndSameDay() throws Exception {
        DateTime today = new DateTime();
        UserModel fakeLoaner = this.getFakeUser("loaner");
        UserModel fakeInvestor = this.getFakeUser("investor");
        userMapper.create(fakeLoaner);
        userMapper.create(fakeInvestor);
        AccountModel fakeLoanerAccount = getFakeAccount(fakeLoaner);
        AccountModel fakeInvestorAccount = getFakeAccount(fakeInvestor);
        accountMapper.create(fakeLoanerAccount);
        accountMapper.create(fakeInvestorAccount);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.09, 0.03, 0.1, fakeLoaner.getLoginName(), today.minusDays(30).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel = getFakeInvestModel(fakeNormalLoan.getId(), loanAmount, fakeInvestor.getLoginName(), today.minusDays(30).toDate());
        investMapper.create(fakeInvestModel);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 0, 0, today.plusDays(10).toDate(), today.withTimeAtStartOfDay().toDate(), RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, loanAmount, today.plusDays(10).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        BaseDto<PayFormDataDto> dto = advanceRepayService.repay(fakeNormalLoan.getId());

        assertTrue(dto.getData().getStatus());
        assertThat(dto.getData().getFields().get("amount"), is(String.valueOf(loanAmount)));

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());

        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(loanRepayModels.get(1).getActualInterest(), is(0L));
        assertThat(new DateTime(loanRepayModels.get(1).getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(today.withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void shouldAdvanceRepayCallbackFirstPeriodWhenLoanTypeIsType1() throws Exception {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);
        AccountModel loanerAccount = getFakeAccount(loaner);
        AccountModel investAccount1 = getFakeAccount(investor1);
        AccountModel investAccount2 = getFakeAccount(investor2);
        accountMapper.create(loanerAccount);
        accountMapper.create(investAccount1);
        accountMapper.create(investAccount2);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, loaner.getLoginName(), today.toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(10).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(5).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);
        repayGeneratorService.generateRepay(fakeNormalLoan.getId());
        advanceRepayService.repay(fakeNormalLoan.getId());

        this.generateMockResponse(10);

        long currentLoanRepayId = loanRepayMapper.findByLoanIdAndPeriod(fakeNormalLoan.getId(), 1).getId();

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(currentLoanRepayId), "");
        boolean isSuccess = advanceRepayService.postRepayCallback(currentLoanRepayId);

        assertTrue(isSuccess);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getPeriod(), is(2));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(2).getPeriod(), is(3));
        assertThat(loanRepayModels.get(2).getStatus(), is(RepayStatus.COMPLETE));

        List<InvestRepayModel> investRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        List<InvestRepayModel> investRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());

        assertThat(investRepayModels1.get(0).getPeriod(), is(1));
        assertThat(investRepayModels1.get(0).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(0).getActualInterest(), is(3L));
        assertThat(investRepayModels1.get(0).getActualFee(), is(0L));
        assertThat(investRepayModels1.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(0).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(1).getPeriod(), is(2));
        assertThat(investRepayModels1.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(1).getActualInterest(), is(0L));
        assertThat(investRepayModels1.get(1).getActualFee(), is(0L));
        assertThat(investRepayModels1.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(2).getPeriod(), is(3));
        assertThat(investRepayModels1.get(2).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(2).getActualInterest(), is(0L));
        assertThat(investRepayModels1.get(2).getActualFee(), is(0L));
        assertThat(investRepayModels1.get(2).getStatus(), is(RepayStatus.COMPLETE));

        assertThat(investRepayModels2.get(0).getPeriod(), is(1));
        assertThat(investRepayModels2.get(0).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(0).getActualInterest(), is(17L));
        assertThat(investRepayModels2.get(0).getActualFee(), is(1L));
        assertThat(investRepayModels2.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(1).getPeriod(), is(2));
        assertThat(investRepayModels2.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(1).getActualInterest(), is(0L));
        assertThat(investRepayModels2.get(1).getActualFee(), is(0L));
        assertThat(investRepayModels2.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(2).getPeriod(), is(3));
        assertThat(investRepayModels2.get(2).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(2).getActualInterest(), is(0L));
        assertThat(investRepayModels2.get(2).getActualFee(), is(0L));
        assertThat(investRepayModels2.get(2).getStatus(), is(RepayStatus.COMPLETE));


        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepayModels.get(0).getActualInterest() + fakeNormalLoan.getLoanAmount()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills1 = userBillMapper.findByLoginName(investor1.getLoginName());
        assertThat(investorUserBills1.get(0).getAmount(), is(investRepayModels1.get(0).getActualInterest() + fakeInvestModel1.getAmount()));
        assertThat(investorUserBills1.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills1.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(1).getAmount(), is(investRepayModels1.get(0).getActualFee()));
        assertThat(investorUserBills1.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills2 = userBillMapper.findByLoginName(investor2.getLoginName());
        assertThat(investorUserBills2.get(0).getAmount(), is(investRepayModels2.get(0).getActualInterest() + fakeInvestModel2.getAmount()));
        assertThat(investorUserBills2.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills2.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills2.get(1).getAmount(), is(investRepayModels2.get(0).getActualFee()));
        assertThat(investorUserBills2.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills2.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(investRepayModels1.get(0).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(investRepayModels1.get(0).getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(investRepayModels2.get(0).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(investRepayModels2.get(0).getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(loanRepayModels.get(0).getId(), SystemBillBusinessType.LOAN_REMAINING_INTEREST);
        assertThat(systemBillModel3.getAmount(), is(1L));
        assertThat(systemBillModel3.getOperationType(), is(SystemBillOperationType.IN));

        assertThat(loanMapper.findById(fakeNormalLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));
    }

    @Test
    public void shouldAdvanceRepayCallbackFirstPeriodWhenLoanTypeIsType3() throws Exception {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);
        AccountModel loanerAccount = getFakeAccount(loaner);
        AccountModel investAccount1 = getFakeAccount(investor1);
        AccountModel investAccount2 = getFakeAccount(investor2);
        accountMapper.create(loanerAccount);
        accountMapper.create(investAccount1);
        accountMapper.create(investAccount2);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.5, loaner.getLoginName(), today.minusDays(1).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(10).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(5).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);
        repayGeneratorService.generateRepay(fakeNormalLoan.getId());
        advanceRepayService.repay(fakeNormalLoan.getId());

        this.generateMockResponse(10);

        long currentLoanRepayId = loanRepayMapper.findByLoanIdAndPeriod(fakeNormalLoan.getId(), 1).getId();
        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(currentLoanRepayId), "");
        boolean isSuccess = advanceRepayService.postRepayCallback(currentLoanRepayId);

        assertTrue(isSuccess);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getPeriod(), is(2));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(2).getPeriod(), is(3));
        assertThat(loanRepayModels.get(2).getStatus(), is(RepayStatus.COMPLETE));

        List<InvestRepayModel> investRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        List<InvestRepayModel> investRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());

        assertThat(investRepayModels1.get(0).getPeriod(), is(1));
        assertThat(investRepayModels1.get(0).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(0).getActualInterest(), is(0L));
        assertThat(investRepayModels1.get(0).getActualFee(), is(0L));
        assertThat(investRepayModels1.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(2).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(2).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));

        assertThat(investRepayModels2.get(0).getPeriod(), is(1));
        assertThat(investRepayModels2.get(0).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(0).getActualInterest(), is(5L));
        assertThat(investRepayModels2.get(0).getActualFee(), is(2L));
        assertThat(investRepayModels1.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(2).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(2).getActualRepayDate().getTime(), is(loanRepayModels.get(0).getActualRepayDate().getTime()));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepayModels.get(0).getActualInterest() + fakeNormalLoan.getLoanAmount()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills1 = userBillMapper.findByLoginName(investor1.getLoginName());
        assertThat(investorUserBills1.get(0).getAmount(), is(investRepayModels1.get(0).getActualInterest() + fakeInvestModel1.getAmount()));
        assertThat(investorUserBills1.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills1.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(1).getAmount(), is(investRepayModels1.get(0).getActualFee()));
        assertThat(investorUserBills1.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills2 = userBillMapper.findByLoginName(investor2.getLoginName());
        assertThat(investorUserBills2.get(0).getAmount(), is(investRepayModels2.get(0).getActualInterest() + fakeInvestModel2.getAmount()));
        assertThat(investorUserBills2.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills2.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills2.get(1).getAmount(), is(investRepayModels2.get(0).getActualFee()));
        assertThat(investorUserBills2.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills2.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(investRepayModels1.get(0).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(investRepayModels1.get(0).getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(investRepayModels2.get(0).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(investRepayModels2.get(0).getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        assertThat(loanMapper.findById(fakeNormalLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));
    }

    @Test
    public void shouldAdvanceRepayCallbackLastPeriod() throws Exception {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);
        AccountModel loanerAccount = getFakeAccount(loaner);
        AccountModel investAccount1 = getFakeAccount(investor1);
        AccountModel investAccount2 = getFakeAccount(investor2);
        accountMapper.create(loanerAccount);
        accountMapper.create(investAccount1);
        accountMapper.create(investAccount2);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(30).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(10).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(5).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.minusDays(10).toDate(), today.minusDays(20).toDate(), RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, loanAmount, today.plusDays(10).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        InvestRepayModel fakeInvest1RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), fakeLoanRepayModel1.getActualRepayDate(), RepayStatus.COMPLETE);
        InvestRepayModel fakeInvest1RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 2, fakeInvestModel1.getAmount(), fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), fakeLoanRepayModel1.getActualRepayDate(), RepayStatus.COMPLETE);
        InvestRepayModel fakeInvest2RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 2, fakeInvestModel2.getAmount(), fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(fakeInvest1RepayModel1, fakeInvest1RepayModel2, fakeInvest2RepayModel1, fakeInvest2RepayModel2));

        advanceRepayService.repay(fakeNormalLoan.getId());

        this.generateMockResponse(10);

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(fakeLoanRepayModel2.getId()), "");
        boolean isSuccess = advanceRepayService.postRepayCallback(fakeLoanRepayModel2.getId());

        assertTrue(isSuccess);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        List<InvestRepayModel> investRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        List<InvestRepayModel> investRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());

        assertThat(investRepayModels1.get(1).getPeriod(), is(2));
        assertThat(investRepayModels1.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(1).getActualInterest(), is(6L));
        assertThat(investRepayModels1.get(1).getActualFee(), is(0L));
        assertThat(investRepayModels1.get(1).getStatus(), is(RepayStatus.COMPLETE));

        assertThat(investRepayModels2.get(1).getPeriod(), is(2));
        assertThat(investRepayModels2.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(1).getActualInterest(), is(59L));
        assertThat(investRepayModels2.get(1).getActualFee(), is(5L));
        assertThat(investRepayModels2.get(1).getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepayModels.get(1).getActualInterest() + loanRepayModels.get(1).getCorpus()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills1 = userBillMapper.findByLoginName(investor1.getLoginName());
        assertThat(investorUserBills1.get(0).getAmount(), is(investRepayModels1.get(1).getActualInterest() + investRepayModels1.get(1).getCorpus()));
        assertThat(investorUserBills1.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills1.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(1).getAmount(), is(investRepayModels1.get(1).getActualFee()));
        assertThat(investorUserBills1.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills2 = userBillMapper.findByLoginName(investor2.getLoginName());
        assertThat(investorUserBills2.get(0).getAmount(), is(investRepayModels2.get(1).getActualInterest() + investRepayModels2.get(1).getCorpus()));
        assertThat(investorUserBills2.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills2.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills2.get(1).getAmount(), is(investRepayModels2.get(1).getActualFee()));
        assertThat(investorUserBills2.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills2.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(investRepayModels1.get(1).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(investRepayModels1.get(1).getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(investRepayModels2.get(1).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(investRepayModels2.get(1).getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        assertThat(loanMapper.findById(fakeNormalLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));
    }

    @Test
    public void shouldOverdueAdvanceRepayCallbackLastPeriod() throws Exception {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);
        AccountModel loanerAccount = getFakeAccount(loaner);
        AccountModel investAccount1 = getFakeAccount(investor1);
        AccountModel investAccount2 = getFakeAccount(investor2);
        accountMapper.create(loanerAccount);
        accountMapper.create(investAccount1);
        accountMapper.create(investAccount2);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(10).toDate());
        fakeNormalLoan.setStatus(LoanStatus.OVERDUE);
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(20).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(10).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.minusDays(5).toDate(), null, RepayStatus.OVERDUE);
        fakeLoanRepayModel1.setDefaultInterest(3L);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, loanAmount, today.plusDays(10).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        InvestRepayModel fakeInvest1RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), fakeLoanRepayModel1.getActualRepayDate(), RepayStatus.OVERDUE);
        fakeInvest1RepayModel1.setDefaultInterest(1L);
        InvestRepayModel fakeInvest1RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 2, fakeInvestModel1.getAmount(), fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), fakeLoanRepayModel1.getActualRepayDate(), RepayStatus.OVERDUE);
        fakeInvest2RepayModel1.setDefaultInterest(1L);
        InvestRepayModel fakeInvest2RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 2, fakeInvestModel2.getAmount(), fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(fakeInvest1RepayModel1, fakeInvest1RepayModel2, fakeInvest2RepayModel1, fakeInvest2RepayModel2));

        advanceRepayService.repay(fakeNormalLoan.getId());

        this.generateMockResponse(10);

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(fakeLoanRepayModel2.getId()), "");
        boolean isSuccess = advanceRepayService.postRepayCallback(fakeLoanRepayModel2.getId());

        assertTrue(isSuccess);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(loanRepayModels.get(0).getActualInterest(), is(0L));
        assertThat(loanRepayModels.get(0).getDefaultInterest(), is(3L));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getPeriod(), is(2));
        assertThat(loanRepayModels.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(loanRepayModels.get(1).getActualInterest(), is(39L));
        assertThat(loanRepayModels.get(1).getDefaultInterest(), is(0L));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.COMPLETE));

        List<InvestRepayModel> investRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        List<InvestRepayModel> investRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());

        assertThat(investRepayModels1.get(0).getPeriod(), is(1));
        assertThat(investRepayModels1.get(0).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(0).getActualInterest(), is(0L));
        assertThat(investRepayModels1.get(0).getActualFee(), is(0L));
        assertThat(investRepayModels1.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(1).getPeriod(), is(2));
        assertThat(investRepayModels1.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(1).getActualInterest(), is(6L));
        assertThat(investRepayModels1.get(1).getActualFee(), is(0L));
        assertThat(investRepayModels1.get(1).getStatus(), is(RepayStatus.COMPLETE));

        assertThat(investRepayModels2.get(0).getPeriod(), is(1));
        assertThat(investRepayModels2.get(0).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(0).getActualInterest(), is(0L));
        assertThat(investRepayModels2.get(0).getActualFee(), is(0L));
        assertThat(investRepayModels2.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(1).getPeriod(), is(2));
        assertThat(investRepayModels2.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(1).getActualInterest(), is(32L));
        assertThat(investRepayModels2.get(1).getActualFee(), is(3L));
        assertThat(investRepayModels2.get(1).getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepayModels.get(1).getActualInterest() + loanRepayModels.get(0).getDefaultInterest() +loanRepayModels.get(1).getCorpus()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.OVERDUE_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills1 = userBillMapper.findByLoginName(investor1.getLoginName());
        assertThat(investorUserBills1.get(0).getAmount(), is(investRepayModels1.get(1).getActualInterest() + investRepayModels1.get(0).getDefaultInterest() + investRepayModels1.get(1).getCorpus()));
        assertThat(investorUserBills1.get(0).getBusinessType(), is(UserBillBusinessType.OVERDUE_REPAY));
        assertThat(investorUserBills1.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(1).getAmount(), is(investRepayModels1.get(1).getActualFee()));
        assertThat(investorUserBills1.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills2 = userBillMapper.findByLoginName(investor2.getLoginName());
        assertThat(investorUserBills2.get(0).getAmount(), is(investRepayModels2.get(1).getActualInterest() + investRepayModels2.get(0).getDefaultInterest() + investRepayModels2.get(1).getCorpus()));
        assertThat(investorUserBills2.get(0).getBusinessType(), is(UserBillBusinessType.OVERDUE_REPAY));
        assertThat(investorUserBills2.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills2.get(1).getAmount(), is(investRepayModels2.get(1).getActualFee()));
        assertThat(investorUserBills2.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills2.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(investRepayModels1.get(1).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(investRepayModels1.get(1).getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));
        assertThat(systemBillModel1.getBusinessType(), is(SystemBillBusinessType.INVEST_FEE));

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(investRepayModels2.get(1).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(investRepayModels2.get(1).getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));
        assertThat(systemBillModel2.getBusinessType(), is(SystemBillBusinessType.INVEST_FEE));

        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(loanRepayModels.get(1).getId(), SystemBillBusinessType.LOAN_REMAINING_INTEREST);
        assertThat(systemBillModel3.getAmount(), is(2L));
        assertThat(systemBillModel3.getOperationType(), is(SystemBillOperationType.IN));

        assertThat(loanMapper.findById(fakeNormalLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));
    }

    @Test
    public void shouldAdvanceRepayCallbackMiddlePeriod() throws Exception {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);
        AccountModel loanerAccount = getFakeAccount(loaner);
        AccountModel investAccount1 = getFakeAccount(investor1);
        AccountModel investAccount2 = getFakeAccount(investor2);
        accountMapper.create(loanerAccount);
        accountMapper.create(investAccount1);
        accountMapper.create(investAccount2);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(20).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(20).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(20).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.minusDays(5).toDate(), today.minusDays(10).toDate(), RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, 0, today.plusDays(10).toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 3, loanAmount, today.plusDays(20).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3));

        InvestRepayModel fakeInvest1RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), fakeLoanRepayModel1.getActualRepayDate(), RepayStatus.COMPLETE);
        InvestRepayModel fakeInvest1RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest1RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 3, fakeInvestModel1.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), fakeLoanRepayModel1.getActualRepayDate(), RepayStatus.COMPLETE);
        InvestRepayModel fakeInvest2RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 3, fakeInvestModel2.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(fakeInvest1RepayModel1, fakeInvest1RepayModel2, fakeInvest1RepayModel3, fakeInvest2RepayModel1, fakeInvest2RepayModel2, fakeInvest2RepayModel3));

        advanceRepayService.repay(fakeNormalLoan.getId());

        this.generateMockResponse(10);

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(fakeLoanRepayModel2.getId()), "");
        boolean isSuccess = advanceRepayService.postRepayCallback(fakeLoanRepayModel2.getId());

        assertTrue(isSuccess);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getPeriod(), is(2));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(2).getPeriod(), is(3));
        assertThat(loanRepayModels.get(2).getStatus(), is(RepayStatus.COMPLETE));

        List<InvestRepayModel> investRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        List<InvestRepayModel> investRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());

        assertThat(investRepayModels1.get(1).getPeriod(), is(2));
        assertThat(investRepayModels1.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(1).getActualInterest(), is(3L));
        assertThat(investRepayModels1.get(1).getActualFee(), is(0L));
        assertThat(investRepayModels1.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(2).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(2).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels1.get(2).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels1.get(2).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));

        assertThat(investRepayModels2.get(1).getPeriod(), is(2));
        assertThat(investRepayModels2.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(1).getActualInterest(), is(29L));
        assertThat(investRepayModels2.get(1).getActualFee(), is(2L));
        assertThat(investRepayModels2.get(1).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(2).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(2).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels2.get(2).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModels2.get(2).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepayModels.get(1).getActualInterest() + fakeNormalLoan.getLoanAmount()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills1 = userBillMapper.findByLoginName(investor1.getLoginName());
        assertThat(investorUserBills1.get(0).getAmount(), is(investRepayModels1.get(1).getActualInterest() + fakeInvestModel1.getAmount()));
        assertThat(investorUserBills1.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills1.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(1).getAmount(), is(investRepayModels1.get(1).getActualFee()));
        assertThat(investorUserBills1.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills2 = userBillMapper.findByLoginName(investor2.getLoginName());
        assertThat(investorUserBills2.get(0).getAmount(), is(investRepayModels2.get(1).getActualInterest() + fakeInvestModel2.getAmount()));
        assertThat(investorUserBills2.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills2.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills2.get(1).getAmount(), is(investRepayModels2.get(1).getActualFee()));
        assertThat(investorUserBills2.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills2.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(investRepayModels1.get(1).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(investRepayModels1.get(1).getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));

        SystemBillModel systemBillModel2 = systemBillMapper.findByOrderId(investRepayModels2.get(1).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel2.getAmount(), is(investRepayModels2.get(1).getActualFee()));
        assertThat(systemBillModel2.getOperationType(), is(SystemBillOperationType.IN));

        assertThat(loanMapper.findById(fakeNormalLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));
    }

    @Test
    public void shouldAdvanceRepayCallbackWhenCurrentRepayIsComplete() throws Exception {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor = this.getFakeUser("investor");
        userMapper.create(loaner);
        userMapper.create(investor);
        AccountModel loanerAccount = getFakeAccount(loaner);
        AccountModel investAccount1 = getFakeAccount(investor);
        accountMapper.create(loanerAccount);
        accountMapper.create(investAccount1);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(30).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel = getFakeInvestModel(fakeNormalLoan.getId(), loanAmount, investor.getLoginName(), today.minusDays(10).toDate());
        investMapper.create(fakeInvestModel);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.plusDays(10).toDate(), today.minusDays(10).toDate(), RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, loanAmount, today.plusDays(20).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        InvestRepayModel fakeInvestRepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), fakeLoanRepayModel1.getActualRepayDate(), RepayStatus.COMPLETE);
        InvestRepayModel fakeInvestRepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel.getId(), 2, fakeInvestModel.getAmount(), fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(fakeInvestRepayModel1, fakeInvestRepayModel2));

        advanceRepayService.repay(fakeNormalLoan.getId());

        CouponModel coupon = this.getFakeCoupon(loanAmount, loaner.getLoginName());
        couponMapper.create(coupon);
        UserCouponModel userCouponModel = new UserCouponModel(investor.getLoginName(), coupon.getId(), new Date(), new Date());
        userCouponMapper.create(userCouponModel);
        userCouponModel.setLoanId(fakeNormalLoan.getId());
        userCouponModel.setUsedTime(today.minusDays(10).toDate());
        userCouponModel.setInvestId(fakeInvestModel.getId());
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userCouponModel);

        this.generateMockResponse(10);

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(fakeLoanRepayModel2.getId()), "");
        boolean isSuccess = advanceRepayService.postRepayCallback(fakeLoanRepayModel2.getId());

        assertTrue(isSuccess);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getPeriod(), is(2));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.COMPLETE));

        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel.getId());
        assertThat(investRepayModels.get(1).getPeriod(), is(2));
        assertThat(investRepayModels.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels.get(1).getActualInterest(), is(32L));
        assertThat(investRepayModels.get(1).getActualFee(), is(3L));
        assertThat(investRepayModels.get(1).getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepayModels.get(1).getActualInterest() + fakeNormalLoan.getLoanAmount()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills1 = userBillMapper.findByLoginName(investor.getLoginName());
        assertThat(investorUserBills1.get(0).getAmount(), is(investRepayModels.get(1).getActualInterest() + fakeInvestModel.getAmount()));
        assertThat(investorUserBills1.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills1.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(1).getAmount(), is(investRepayModels.get(1).getActualFee()));
        assertThat(investorUserBills1.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(investorUserBills1.get(2).getAmount(), is(32L));
        assertThat(investorUserBills1.get(2).getBusinessType(), is(UserBillBusinessType.INVEST_COUPON));
        assertThat(investorUserBills1.get(2).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(3).getAmount(), is(3L));
        assertThat(investorUserBills1.get(3).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(3).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(investRepayModels.get(1).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(investRepayModels.get(1).getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));

        assertThat(loanMapper.findById(fakeNormalLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));

        UserCouponModel actualUserCouponModel = userCouponMapper.findById(userCouponModel.getId());
        assertThat(actualUserCouponModel.getActualInterest(), is(32L));
        assertThat(actualUserCouponModel.getActualFee(), is(3L));
        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(userCouponModel.getId(), SystemBillBusinessType.COUPON);
        assertThat(systemBillModel3.getAmount(), is(29L));
    }

    @Test
    public void shouldAdvanceRepayCallbackWhenCurrentRepayIsCompleteAndSameDay() throws Exception {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor = this.getFakeUser("investor");
        userMapper.create(loaner);
        userMapper.create(investor);
        AccountModel loanerAccount = getFakeAccount(loaner);
        AccountModel investAccount1 = getFakeAccount(investor);
        accountMapper.create(loanerAccount);
        accountMapper.create(investAccount1);
        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 2, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(30).toDate());
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel = getFakeInvestModel(fakeNormalLoan.getId(), loanAmount, investor.getLoginName(), today.minusDays(10).toDate());
        investMapper.create(fakeInvestModel);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.plusDays(10).toDate(), today.withTimeAtStartOfDay().toDate(), RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, loanAmount, today.plusDays(20).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        InvestRepayModel fakeInvestRepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), fakeLoanRepayModel1.getActualRepayDate(), RepayStatus.COMPLETE);
        InvestRepayModel fakeInvestRepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel.getId(), 2, fakeInvestModel.getAmount(), fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(fakeInvestRepayModel1, fakeInvestRepayModel2));

        advanceRepayService.repay(fakeNormalLoan.getId());

        CouponModel coupon = this.getFakeCoupon(loanAmount, loaner.getLoginName());
        couponMapper.create(coupon);
        UserCouponModel userCouponModel = new UserCouponModel(investor.getLoginName(), coupon.getId(), new Date(), new Date());
        userCouponMapper.create(userCouponModel);
        userCouponModel.setLoanId(fakeNormalLoan.getId());
        userCouponModel.setUsedTime(today.minusDays(10).toDate());
        userCouponModel.setInvestId(fakeInvestModel.getId());
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userCouponModel);

        this.generateMockResponse(10);

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(fakeLoanRepayModel2.getId()), "");
        boolean isSuccess = advanceRepayService.postRepayCallback(fakeLoanRepayModel2.getId());

        assertTrue(isSuccess);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModels.get(1).getPeriod(), is(2));
        assertThat(loanRepayModels.get(1).getActualInterest(), is(0L));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.COMPLETE));

        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel.getId());
        assertThat(investRepayModels.get(1).getPeriod(), is(2));
        assertThat(investRepayModels.get(1).getActualRepayDate().getTime(), is(loanRepayModels.get(1).getActualRepayDate().getTime()));
        assertThat(investRepayModels.get(1).getActualInterest(), is(0L));
        assertThat(investRepayModels.get(1).getActualFee(), is(0L));
        assertThat(investRepayModels.get(1).getStatus(), is(RepayStatus.COMPLETE));

        List<UserBillModel> loanerUserBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(loanerUserBills.get(0).getAmount(), is(loanRepayModels.get(1).getActualInterest() + fakeNormalLoan.getLoanAmount()));
        assertThat(loanerUserBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(loanerUserBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        List<UserBillModel> investorUserBills1 = userBillMapper.findByLoginName(investor.getLoginName());
        assertThat(investorUserBills1.get(0).getAmount(), is(investRepayModels.get(1).getActualInterest() + fakeInvestModel.getAmount()));
        assertThat(investorUserBills1.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(investorUserBills1.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(1).getAmount(), is(investRepayModels.get(1).getActualFee()));
        assertThat(investorUserBills1.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(investorUserBills1.get(2).getAmount(), is(0L));
        assertThat(investorUserBills1.get(2).getBusinessType(), is(UserBillBusinessType.INVEST_COUPON));
        assertThat(investorUserBills1.get(2).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(investorUserBills1.get(3).getAmount(), is(0L));
        assertThat(investorUserBills1.get(3).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));
        assertThat(investorUserBills1.get(3).getOperationType(), is(UserBillOperationType.TO_BALANCE));

        SystemBillModel systemBillModel1 = systemBillMapper.findByOrderId(investRepayModels.get(1).getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBillModel1.getAmount(), is(investRepayModels.get(1).getActualFee()));
        assertThat(systemBillModel1.getOperationType(), is(SystemBillOperationType.IN));

        assertThat(loanMapper.findById(fakeNormalLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));

        UserCouponModel actualUserCouponModel = userCouponMapper.findById(userCouponModel.getId());
        assertThat(actualUserCouponModel.getActualInterest(), is(0L));
        assertThat(actualUserCouponModel.getActualFee(), is(0L));
        SystemBillModel systemBillModel3 = systemBillMapper.findByOrderId(userCouponModel.getId(), SystemBillBusinessType.COUPON);
        assertThat(systemBillModel3.getAmount(), is(0L));
    }

    private UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setMobile(loginName);
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private AccountModel getFakeAccount(UserModel userModel) {
        AccountModel accountModel = new AccountModel(userModel.getLoginName(), userModel.getLoginName(), "ID", "payUserId", "payAccountId", new Date());
        accountModel.setBalance(1000000);
        return accountModel;
    }

    private LoanModel getFakeNormalLoan(LoanType loanType, long amount, int periods, double baseRate, double activityRate, double investFeeRate, String loginName, Date recheckTime) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanAmount(amount);
        fakeLoanModel.setLoanerLoginName(loginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(loginName);
        fakeLoanModel.setType(loanType);
        fakeLoanModel.setPeriods(periods);
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setBaseRate(baseRate);
        fakeLoanModel.setActivityRate(activityRate);
        fakeLoanModel.setInvestFeeRate(investFeeRate);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(recheckTime);
        return fakeLoanModel;
    }

    private InvestModel getFakeInvestModel(long loanId, long amount, String loginName, Date investTime) {
        InvestModel model = new InvestModel();
        model.setId(idGenerator.generate());
        model.setAmount(amount);
        model.setLoanId(loanId);
        model.setLoginName(loginName);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setCreatedTime(investTime);
        return model;
    }

    private LoanRepayModel getFakeLoanRepayModel(long loanId, int period, long corpus, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        LoanRepayModel fakeLoanRepay = new LoanRepayModel(idGenerator.generate(), loanId, period, 0, expectedRepayDate, repayStatus);
        fakeLoanRepay.setActualRepayDate(actualRepayDate);
        fakeLoanRepay.setCorpus(corpus);
        return fakeLoanRepay;
    }

    private InvestRepayModel getFakeInvestRepayModel(long investId, int period, long corpus, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        InvestRepayModel fakeInvestRepay = new InvestRepayModel(idGenerator.generate(), investId, period, 0, 0, expectedRepayDate, repayStatus);
        fakeInvestRepay.setActualRepayDate(actualRepayDate);
        fakeInvestRepay.setCorpus(corpus);
        return fakeInvestRepay;
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

    private void generateMockResponse(int times) {
        for (int index = 0; index < times; index++){
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

    private CouponModel getFakeCoupon(long amount, String loginName) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(amount);
        couponModel.setTotalCount(1L);
        couponModel.setActive(true);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType.JYF));
        return couponModel;
    }
}