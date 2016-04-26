package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class DefaultInterestTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayService loanRepayService;

    @Value("${pay.overdue.fee}")
    private double overdueFee;

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

    @Test
    @Transactional
    public void shouldDefaultInterestPeriods3Delay2() {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);

        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(10).toDate());

        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(20).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(20).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.minusDays(5).minusSeconds(30).toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, 0, today.plusDays(10).minusSeconds(30).toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 3, loanAmount, today.plusDays(20).minusSeconds(30).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3));

        InvestRepayModel fakeInvest1RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest1RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest1RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 3, fakeInvestModel1.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 3, fakeInvestModel2.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(fakeInvest1RepayModel1, fakeInvest1RepayModel2, fakeInvest1RepayModel3, fakeInvest2RepayModel1, fakeInvest2RepayModel2, fakeInvest2RepayModel3));

        loanRepayService.calculateDefaultInterest();

        LoanModel loanModel = loanMapper.findById(fakeNormalLoan.getId());
        assertThat(loanModel.getStatus(), is(LoanStatus.OVERDUE));

        LoanRepayModel loanRepayModel1 = loanRepayMapper.findById(fakeLoanRepayModel1.getId());
        assertThat(loanRepayModel1.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(loanRepayModel1.getDefaultInterest(), is(12L));

        LoanRepayModel loanRepayModel2 = loanRepayMapper.findById(fakeLoanRepayModel2.getId());
        assertThat(loanRepayModel2.getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModel2.getDefaultInterest(), is(0L));

        LoanRepayModel loanRepayModel3 = loanRepayMapper.findById(fakeLoanRepayModel3.getId());
        assertThat(loanRepayModel3.getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModel3.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel11 = investRepayMapper.findById(fakeInvest1RepayModel1.getId());
        assertThat(investRepayModel11.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel11.getDefaultInterest(), is(1L));

        InvestRepayModel investRepayModel12 = investRepayMapper.findById(fakeInvest1RepayModel2.getId());
        assertThat(investRepayModel12.getStatus(), is(RepayStatus.REPAYING));
        assertThat(investRepayModel12.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel13 = investRepayMapper.findById(fakeInvest1RepayModel3.getId());
        assertThat(investRepayModel13.getStatus(), is(RepayStatus.REPAYING));
        assertThat(investRepayModel13.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel21 = investRepayMapper.findById(fakeInvest2RepayModel1.getId());
        assertThat(investRepayModel21.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel21.getDefaultInterest(), is(10L));

        InvestRepayModel investRepayModel22 = investRepayMapper.findById(fakeInvest2RepayModel2.getId());
        assertThat(investRepayModel22.getStatus(), is(RepayStatus.REPAYING));
        assertThat(investRepayModel22.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel23 = investRepayMapper.findById(fakeInvest2RepayModel3.getId());
        assertThat(investRepayModel23.getStatus(), is(RepayStatus.REPAYING));
        assertThat(investRepayModel23.getDefaultInterest(), is(0L));
    }

    @Test
    @Transactional
    public void shouldDefaultInterestPeriods3Delay3() {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);

        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(10).toDate());
        fakeNormalLoan.setStatus(LoanStatus.OVERDUE);
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(20).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(20).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.minusDays(35).minusSeconds(30).toDate(), null, RepayStatus.OVERDUE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, 0, today.minusDays(5).minusSeconds(30).toDate(), null, RepayStatus.OVERDUE);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 3, loanAmount, today.plusDays(20).minusSeconds(30).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3));

        InvestRepayModel fakeInvest1RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest1RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest1RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 3, fakeInvestModel1.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest2RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest2RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 3, fakeInvestModel2.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(fakeInvest1RepayModel1, fakeInvest1RepayModel2, fakeInvest1RepayModel3, fakeInvest2RepayModel1, fakeInvest2RepayModel2, fakeInvest2RepayModel3));

        loanRepayService.calculateDefaultInterest();

        LoanModel loanModel = loanMapper.findById(fakeNormalLoan.getId());
        assertThat(loanModel.getStatus(), is(LoanStatus.OVERDUE));

        LoanRepayModel loanRepayModel1 = loanRepayMapper.findById(fakeLoanRepayModel1.getId());
        assertThat(loanRepayModel1.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(loanRepayModel1.getDefaultInterest(), is(72L));

        LoanRepayModel loanRepayModel2 = loanRepayMapper.findById(fakeLoanRepayModel2.getId());
        assertThat(loanRepayModel2.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(loanRepayModel2.getDefaultInterest(), is(0L));

        LoanRepayModel loanRepayModel3 = loanRepayMapper.findById(fakeLoanRepayModel3.getId());
        assertThat(loanRepayModel3.getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModel3.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel11 = investRepayMapper.findById(fakeInvest1RepayModel1.getId());
        assertThat(investRepayModel11.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel11.getDefaultInterest(), is(7L));

        InvestRepayModel investRepayModel12 = investRepayMapper.findById(fakeInvest1RepayModel2.getId());
        assertThat(investRepayModel12.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel12.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel13 = investRepayMapper.findById(fakeInvest1RepayModel3.getId());
        assertThat(investRepayModel13.getStatus(), is(RepayStatus.REPAYING));
        assertThat(investRepayModel13.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel21 = investRepayMapper.findById(fakeInvest2RepayModel1.getId());
        assertThat(investRepayModel21.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel21.getDefaultInterest(), is(64L));

        InvestRepayModel investRepayModel22 = investRepayMapper.findById(fakeInvest2RepayModel2.getId());
        assertThat(investRepayModel22.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel22.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel23 = investRepayMapper.findById(fakeInvest2RepayModel3.getId());
        assertThat(investRepayModel23.getStatus(), is(RepayStatus.REPAYING));
        assertThat(investRepayModel23.getDefaultInterest(), is(0L));
    }

    @Test
    @Transactional
    public void shouldDefaultInterestPeriods3DelayAll() {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);

        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(10).toDate());
        fakeNormalLoan.setStatus(LoanStatus.OVERDUE);
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(20).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(20).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.minusDays(65).minusSeconds(30).toDate(), null, RepayStatus.OVERDUE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, 0, today.minusDays(35).minusSeconds(30).toDate(), null, RepayStatus.OVERDUE);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 3, loanAmount, today.minusDays(5).toDate(), null, RepayStatus.OVERDUE);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3));

        InvestRepayModel fakeInvest1RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest1RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest1RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 3, fakeInvestModel1.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest2RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest2RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest2RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 3, fakeInvestModel2.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.OVERDUE);
        investRepayMapper.create(Lists.newArrayList(fakeInvest1RepayModel1, fakeInvest1RepayModel2, fakeInvest1RepayModel3, fakeInvest2RepayModel1, fakeInvest2RepayModel2, fakeInvest2RepayModel3));

        loanRepayService.calculateDefaultInterest();

        LoanModel loanModel = loanMapper.findById(fakeNormalLoan.getId());
        assertThat(loanModel.getStatus(), is(LoanStatus.OVERDUE));

        LoanRepayModel loanRepayModel1 = loanRepayMapper.findById(fakeLoanRepayModel1.getId());
        assertThat(loanRepayModel1.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(loanRepayModel1.getDefaultInterest(), is(132L));

        LoanRepayModel loanRepayModel2 = loanRepayMapper.findById(fakeLoanRepayModel2.getId());
        assertThat(loanRepayModel2.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(loanRepayModel2.getDefaultInterest(), is(0L));

        LoanRepayModel loanRepayModel3 = loanRepayMapper.findById(fakeLoanRepayModel3.getId());
        assertThat(loanRepayModel3.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(loanRepayModel3.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel11 = investRepayMapper.findById(fakeInvest1RepayModel1.getId());
        assertThat(investRepayModel11.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel11.getDefaultInterest(), is(13L));

        InvestRepayModel investRepayModel12 = investRepayMapper.findById(fakeInvest1RepayModel2.getId());
        assertThat(investRepayModel12.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel12.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel13 = investRepayMapper.findById(fakeInvest1RepayModel3.getId());
        assertThat(investRepayModel13.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel13.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel21 = investRepayMapper.findById(fakeInvest2RepayModel1.getId());
        assertThat(investRepayModel21.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel21.getDefaultInterest(), is(118L));

        InvestRepayModel investRepayModel22 = investRepayMapper.findById(fakeInvest2RepayModel2.getId());
        assertThat(investRepayModel22.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel22.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel23 = investRepayMapper.findById(fakeInvest2RepayModel3.getId());
        assertThat(investRepayModel23.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel23.getDefaultInterest(), is(0L));
    }

    @Test
    @Transactional
    public void shouldDefaultInterestPeriods3DelayAllAndRepayFirst() {
        DateTime today = new DateTime();
        UserModel loaner = this.getFakeUser("loaner");
        UserModel investor1 = this.getFakeUser("investor1");
        UserModel investor2 = this.getFakeUser("investor2");
        userMapper.create(loaner);
        userMapper.create(investor1);
        userMapper.create(investor2);

        long loanAmount = 10000;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.09, 0.03, 0.1, loaner.getLoginName(), today.minusDays(10).toDate());
        fakeNormalLoan.setStatus(LoanStatus.OVERDUE);
        loanMapper.create(fakeNormalLoan);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, investor1.getLoginName(), today.minusDays(20).toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 9000, investor2.getLoginName(), today.minusDays(20).toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);

        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 1, 0, today.minusDays(45).minusSeconds(30).toDate(), null, RepayStatus.COMPLETE);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 2, 0, today.minusDays(15).minusSeconds(30).toDate(), null, RepayStatus.OVERDUE);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeNormalLoan.getId(), 3, loanAmount, today.plusDays(15).minusSeconds(30).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3));

        InvestRepayModel fakeInvest1RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), null, RepayStatus.COMPLETE);
        InvestRepayModel fakeInvest1RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest1RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel1.getId(), 3, fakeInvestModel1.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeInvest2RepayModel1 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 1, 0, fakeLoanRepayModel1.getRepayDate(), null, RepayStatus.COMPLETE);
        InvestRepayModel fakeInvest2RepayModel2 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 2, 0, fakeLoanRepayModel2.getRepayDate(), null, RepayStatus.OVERDUE);
        InvestRepayModel fakeInvest2RepayModel3 = this.getFakeInvestRepayModel(fakeInvestModel2.getId(), 3, fakeInvestModel2.getAmount(), fakeLoanRepayModel3.getRepayDate(), null, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(fakeInvest1RepayModel1, fakeInvest1RepayModel2, fakeInvest1RepayModel3, fakeInvest2RepayModel1, fakeInvest2RepayModel2, fakeInvest2RepayModel3));

        loanRepayService.calculateDefaultInterest();

        LoanModel loanModel = loanMapper.findById(fakeNormalLoan.getId());
        assertThat(loanModel.getStatus(), is(LoanStatus.OVERDUE));

        LoanRepayModel loanRepayModel1 = loanRepayMapper.findById(fakeLoanRepayModel1.getId());
        assertThat(loanRepayModel1.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(loanRepayModel1.getDefaultInterest(), is(0L));

        LoanRepayModel loanRepayModel2 = loanRepayMapper.findById(fakeLoanRepayModel2.getId());
        assertThat(loanRepayModel2.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(loanRepayModel2.getDefaultInterest(), is(32L));

        LoanRepayModel loanRepayModel3 = loanRepayMapper.findById(fakeLoanRepayModel3.getId());
        assertThat(loanRepayModel3.getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModel3.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel11 = investRepayMapper.findById(fakeInvest1RepayModel1.getId());
        assertThat(investRepayModel11.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModel11.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel12 = investRepayMapper.findById(fakeInvest1RepayModel2.getId());
        assertThat(investRepayModel12.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel12.getDefaultInterest(), is(3L));

        InvestRepayModel investRepayModel13 = investRepayMapper.findById(fakeInvest1RepayModel3.getId());
        assertThat(investRepayModel13.getStatus(), is(RepayStatus.REPAYING));
        assertThat(investRepayModel13.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel21 = investRepayMapper.findById(fakeInvest2RepayModel1.getId());
        assertThat(investRepayModel21.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(investRepayModel21.getDefaultInterest(), is(0L));

        InvestRepayModel investRepayModel22 = investRepayMapper.findById(fakeInvest2RepayModel2.getId());
        assertThat(investRepayModel22.getStatus(), is(RepayStatus.OVERDUE));
        assertThat(investRepayModel22.getDefaultInterest(), is(28L));

        InvestRepayModel investRepayModel23 = investRepayMapper.findById(fakeInvest2RepayModel3.getId());
        assertThat(investRepayModel23.getStatus(), is(RepayStatus.REPAYING));
        assertThat(investRepayModel23.getDefaultInterest(), is(0L));
    }
}
