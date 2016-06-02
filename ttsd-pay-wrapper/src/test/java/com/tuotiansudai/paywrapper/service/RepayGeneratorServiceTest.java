package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
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
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class RepayGeneratorServiceTest {

    @Autowired
    private RepayGeneratorService repayGeneratorService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Test
    public void shouldCreateRepayWhenLoanType1OnePeriodActivityRate() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        userMapper.create(fakeUser);
        long loanAmount = 10000;
        DateTime recheckTime = new DateTime().withDate(2015, 2, 1).withTimeAtStartOfDay();
        double baseRate = 0.09;
        double activityRate = 0.03;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 1, baseRate, activityRate, 0.1, fakeUser.getLoginName(), recheckTime.toDate());
        loanMapper.create(fakeNormalLoan);
        DateTime investTime1 = recheckTime.minusDays(10);
        DateTime investTime2 = recheckTime.minusDays(5);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, fakeUser.getLoginName(), investTime1.toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 2000, fakeUser.getLoginName(), investTime2.toDate());
        InvestModel fakeInvestModel3 = getFakeInvestModel(fakeNormalLoan.getId(), 7000, fakeUser.getLoginName(), recheckTime.toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);
        investMapper.create(fakeInvestModel3);

        repayGeneratorService.generateRepay(fakeNormalLoan.getId());

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.size(), is(1));
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getCorpus(), is(10000L));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(0).getExpectedInterest(), is(105L));
        assertThat(loanRepayModels.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        assertThat(fakeInvestRepayModels1.size(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getCorpus(), is(1000L));
        assertThat(fakeInvestRepayModels1.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedInterest(), is(13L));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedFee(), is(1L));
        assertThat(fakeInvestRepayModels1.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());
        assertThat(fakeInvestRepayModels2.size(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getCorpus(), is(2000L));
        assertThat(fakeInvestRepayModels2.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedInterest(), is(23L));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedFee(), is(2L));
        assertThat(fakeInvestRepayModels2.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels3 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel3.getId());
        assertThat(fakeInvestRepayModels3.size(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getCorpus(), is(7000L));
        assertThat(fakeInvestRepayModels3.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels3.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
    }

    @Test
    public void shouldCreateRepayWhenLoanType1ThreePeriods() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        userMapper.create(fakeUser);
        long loanAmount = 10000;
        DateTime recheckTime = new DateTime().withDate(2015, 2, 1).withTimeAtStartOfDay();
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.12, 0.0, 0.1, fakeUser.getLoginName(), recheckTime.toDate());
        loanMapper.create(fakeNormalLoan);
        DateTime investTime1 = recheckTime.minusDays(10);
        DateTime investTime2 = recheckTime.minusDays(5);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, fakeUser.getLoginName(), investTime1.toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 2000, fakeUser.getLoginName(), investTime2.toDate());
        InvestModel fakeInvestModel3 = getFakeInvestModel(fakeNormalLoan.getId(), 7000, fakeUser.getLoginName(), recheckTime.toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);
        investMapper.create(fakeInvestModel3);

        repayGeneratorService.generateRepay(fakeNormalLoan.getId());

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.size(), is(3));
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getCorpus(), is(0L));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(0).getExpectedInterest(), is(105L));
        assertThat(loanRepayModels.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
        assertThat(loanRepayModels.get(1).getPeriod(), is(2));
        assertThat(loanRepayModels.get(1).getCorpus(), is(0L));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(1).getExpectedInterest(), is(98L));
        assertThat(loanRepayModels.get(1).getRepayDate(), is(new DateTime().withDate(2015, 4, 1).withTime(23, 59, 59, 0).toDate()));
        assertThat(loanRepayModels.get(2).getPeriod(), is(3));
        assertThat(loanRepayModels.get(2).getCorpus(), is(10000L));
        assertThat(loanRepayModels.get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(2).getExpectedInterest(), is(98L));
        assertThat(loanRepayModels.get(2).getRepayDate(), is(new DateTime().withDate(2015, 5, 1).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        assertThat(fakeInvestRepayModels1.size(), is(3));
        assertThat(fakeInvestRepayModels1.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels1.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedInterest(), is(13L));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedFee(), is(1L));
        assertThat(fakeInvestRepayModels1.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels1.get(1).getPeriod(), is(2));
        assertThat(fakeInvestRepayModels1.get(1).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels1.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(1).getExpectedInterest(), is(9L));
        assertThat(fakeInvestRepayModels1.get(1).getExpectedFee(), is(0L));
        assertThat(fakeInvestRepayModels1.get(1).getRepayDate(), is(new DateTime().withDate(2015, 4, 1).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels1.get(2).getPeriod(), is(3));
        assertThat(fakeInvestRepayModels1.get(2).getCorpus(), is(1000L));
        assertThat(fakeInvestRepayModels1.get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(2).getExpectedInterest(), is(9L));
        assertThat(fakeInvestRepayModels1.get(2).getExpectedFee(), is(0L));
        assertThat(fakeInvestRepayModels1.get(2).getRepayDate(), is(new DateTime().withDate(2015, 5, 1).withTime(23, 59, 59, 0).toDate()));


        List<InvestRepayModel> fakeInvestRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());
        assertThat(fakeInvestRepayModels2.size(), is(3));
        assertThat(fakeInvestRepayModels2.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels2.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedInterest(), is(23L));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedFee(), is(2L));
        assertThat(fakeInvestRepayModels2.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels2.get(1).getPeriod(), is(2));
        assertThat(fakeInvestRepayModels2.get(1).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels2.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(1).getExpectedInterest(), is(19L));
        assertThat(fakeInvestRepayModels2.get(1).getExpectedFee(), is(1L));
        assertThat(fakeInvestRepayModels2.get(1).getRepayDate(), is(new DateTime().withDate(2015, 4, 1).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels2.get(2).getPeriod(), is(3));
        assertThat(fakeInvestRepayModels2.get(2).getCorpus(), is(2000L));
        assertThat(fakeInvestRepayModels2.get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(2).getExpectedInterest(), is(19L));
        assertThat(fakeInvestRepayModels2.get(2).getExpectedFee(), is(1L));
        assertThat(fakeInvestRepayModels2.get(2).getRepayDate(), is(new DateTime().withDate(2015, 5, 1).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels3 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel3.getId());
        assertThat(fakeInvestRepayModels3.size(), is(3));
        assertThat(fakeInvestRepayModels3.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels3.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels3.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels3.get(1).getPeriod(), is(2));
        assertThat(fakeInvestRepayModels3.get(1).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels3.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(1).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels3.get(1).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels3.get(1).getRepayDate(), is(new DateTime().withDate(2015, 4, 1).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels3.get(2).getPeriod(), is(3));
        assertThat(fakeInvestRepayModels3.get(2).getCorpus(), is(7000L));
        assertThat(fakeInvestRepayModels3.get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(2).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels3.get(2).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels3.get(2).getRepayDate(), is(new DateTime().withDate(2015, 5, 1).withTime(23, 59, 59, 0).toDate()));
    }

    @Test
    public void shouldCreateRepayWhenLoanType3OnePeriodActivityRate() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        userMapper.create(fakeUser);
        long loanAmount = 10000;
        DateTime recheckTime = new DateTime().withDate(2015, 2, 1).withTimeAtStartOfDay();
        double baseRate = 0.09;
        double activityRate = 0.03;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, loanAmount, 1, baseRate, activityRate, 0.1, fakeUser.getLoginName(), recheckTime.toDate());
        loanMapper.create(fakeNormalLoan);
        DateTime investTime1 = recheckTime.minusDays(10);
        DateTime investTime2 = recheckTime.minusDays(5);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, fakeUser.getLoginName(), investTime1.toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 2000, fakeUser.getLoginName(), investTime2.toDate());
        InvestModel fakeInvestModel3 = getFakeInvestModel(fakeNormalLoan.getId(), 7000, fakeUser.getLoginName(), recheckTime.toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);
        investMapper.create(fakeInvestModel3);

        repayGeneratorService.generateRepay(fakeNormalLoan.getId());

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.size(), is(1));
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getCorpus(), is(10000L));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(0).getExpectedInterest(), is(98L));
        assertThat(loanRepayModels.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        assertThat(fakeInvestRepayModels1.size(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getCorpus(), is(1000L));
        assertThat(fakeInvestRepayModels1.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedInterest(), is(9L));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedFee(), is(0L));
        assertThat(fakeInvestRepayModels1.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());
        assertThat(fakeInvestRepayModels2.size(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getCorpus(), is(2000L));
        assertThat(fakeInvestRepayModels2.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedInterest(), is(19L));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedFee(), is(1L));
        assertThat(fakeInvestRepayModels2.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels3 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel3.getId());
        assertThat(fakeInvestRepayModels3.size(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getCorpus(), is(7000L));
        assertThat(fakeInvestRepayModels3.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels3.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
    }

    @Test
    public void shouldCreateRepayWhenLoanType3ThreePeriods() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        userMapper.create(fakeUser);
        long loanAmount = 10000;
        DateTime recheckTime = new DateTime().withDate(2015, 2, 1).withTimeAtStartOfDay();
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, loanAmount, 3, 0.12, 0.0, 0.1, fakeUser.getLoginName(), recheckTime.toDate());
        loanMapper.create(fakeNormalLoan);
        DateTime investTime1 = recheckTime.minusDays(10);
        DateTime investTime2 = recheckTime.minusDays(5);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, fakeUser.getLoginName(), investTime1.toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 2000, fakeUser.getLoginName(), investTime2.toDate());
        InvestModel fakeInvestModel3 = getFakeInvestModel(fakeNormalLoan.getId(), 7000, fakeUser.getLoginName(), recheckTime.toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);
        investMapper.create(fakeInvestModel3);

        repayGeneratorService.generateRepay(fakeNormalLoan.getId());

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.size(), is(3));
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getCorpus(), is(0L));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(0).getExpectedInterest(), is(98L));
        assertThat(loanRepayModels.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
        assertThat(loanRepayModels.get(1).getPeriod(), is(2));
        assertThat(loanRepayModels.get(1).getCorpus(), is(0L));
        assertThat(loanRepayModels.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(1).getExpectedInterest(), is(98L));
        assertThat(loanRepayModels.get(1).getRepayDate(), is(new DateTime().withDate(2015, 4, 1).withTime(23, 59, 59, 0).toDate()));
        assertThat(loanRepayModels.get(2).getPeriod(), is(3));
        assertThat(loanRepayModels.get(2).getCorpus(), is(10000L));
        assertThat(loanRepayModels.get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(2).getExpectedInterest(), is(98L));
        assertThat(loanRepayModels.get(2).getRepayDate(), is(new DateTime().withDate(2015, 5, 1).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        assertThat(fakeInvestRepayModels1.size(), is(3));
        assertThat(fakeInvestRepayModels1.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels1.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedInterest(), is(9L));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedFee(), is(0L));
        assertThat(fakeInvestRepayModels1.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels1.get(1).getPeriod(), is(2));
        assertThat(fakeInvestRepayModels1.get(1).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels1.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(1).getExpectedInterest(), is(9L));
        assertThat(fakeInvestRepayModels1.get(1).getExpectedFee(), is(0L));
        assertThat(fakeInvestRepayModels1.get(1).getRepayDate(), is(new DateTime().withDate(2015, 4, 1).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels1.get(2).getPeriod(), is(3));
        assertThat(fakeInvestRepayModels1.get(2).getCorpus(), is(1000L));
        assertThat(fakeInvestRepayModels1.get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(2).getExpectedInterest(), is(9L));
        assertThat(fakeInvestRepayModels1.get(2).getExpectedFee(), is(0L));
        assertThat(fakeInvestRepayModels1.get(2).getRepayDate(), is(new DateTime().withDate(2015, 5, 1).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());
        assertThat(fakeInvestRepayModels2.size(), is(3));
        assertThat(fakeInvestRepayModels2.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels2.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedInterest(), is(19L));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedFee(), is(1L));
        assertThat(fakeInvestRepayModels2.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels2.get(1).getPeriod(), is(2));
        assertThat(fakeInvestRepayModels2.get(1).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels2.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(1).getExpectedInterest(), is(19L));
        assertThat(fakeInvestRepayModels2.get(1).getExpectedFee(), is(1L));
        assertThat(fakeInvestRepayModels2.get(1).getRepayDate(), is(new DateTime().withDate(2015, 4, 1).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels2.get(2).getPeriod(), is(3));
        assertThat(fakeInvestRepayModels2.get(2).getCorpus(), is(2000L));
        assertThat(fakeInvestRepayModels2.get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(2).getExpectedInterest(), is(19L));
        assertThat(fakeInvestRepayModels2.get(2).getExpectedFee(), is(1L));
        assertThat(fakeInvestRepayModels2.get(2).getRepayDate(), is(new DateTime().withDate(2015, 5, 1).withTime(23, 59, 59, 0).toDate()));

        List<InvestRepayModel> fakeInvestRepayModels3 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel3.getId());
        assertThat(fakeInvestRepayModels3.size(), is(3));
        assertThat(fakeInvestRepayModels3.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels3.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels3.get(0).getRepayDate(), is(new DateTime().withDate(2015, 3, 2).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels3.get(1).getPeriod(), is(2));
        assertThat(fakeInvestRepayModels3.get(1).getCorpus(), is(0L));
        assertThat(fakeInvestRepayModels3.get(1).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(1).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels3.get(1).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels3.get(1).getRepayDate(), is(new DateTime().withDate(2015, 4, 1).withTime(23, 59, 59, 0).toDate()));
        assertThat(fakeInvestRepayModels3.get(2).getPeriod(), is(3));
        assertThat(fakeInvestRepayModels3.get(2).getCorpus(), is(7000L));
        assertThat(fakeInvestRepayModels3.get(2).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(2).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels3.get(2).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels3.get(2).getRepayDate(), is(new DateTime().withDate(2015, 5, 1).withTime(23, 59, 59, 0).toDate()));
    }

    @Test
    public void shouldCreateRepayWhenLoanType5ActivityRate() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        userMapper.create(fakeUser);
        long loanAmount = 10000;
        DateTime recheckTime = new DateTime().withDate(2015, 2, 1).withTimeAtStartOfDay();
        int duration = 100;
        Date expectedRepayDay = recheckTime.plusDays(duration).minusSeconds(1).toDate();
        double baseRate = 0.09;
        double activityRate = 0.03;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, loanAmount, 1, baseRate, activityRate, 0.1, fakeUser.getLoginName(), recheckTime.toDate());
        fakeNormalLoan.setDuration(100);
        loanMapper.create(fakeNormalLoan);
        DateTime investTime1 = recheckTime.minusDays(10);
        DateTime investTime2 = recheckTime.minusDays(5);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, fakeUser.getLoginName(), investTime1.toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 2000, fakeUser.getLoginName(), investTime2.toDate());
        InvestModel fakeInvestModel3 = getFakeInvestModel(fakeNormalLoan.getId(), 7000, fakeUser.getLoginName(), recheckTime.toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);
        investMapper.create(fakeInvestModel3);

        repayGeneratorService.generateRepay(fakeNormalLoan.getId());

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.size(), is(1));
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getCorpus(), is(10000L));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(0).getExpectedInterest(), is(335L));
        assertThat(loanRepayModels.get(0).getRepayDate(), is(expectedRepayDay));

        List<InvestRepayModel> fakeInvestRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        assertThat(fakeInvestRepayModels1.size(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getCorpus(), is(1000L));
        assertThat(fakeInvestRepayModels1.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedInterest(), is(36L));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedFee(), is(3L));
        assertThat(fakeInvestRepayModels1.get(0).getRepayDate(), is(expectedRepayDay));

        List<InvestRepayModel> fakeInvestRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());
        assertThat(fakeInvestRepayModels2.size(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getCorpus(), is(2000L));
        assertThat(fakeInvestRepayModels2.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedInterest(), is(69L));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels2.get(0).getRepayDate(), is(expectedRepayDay));

        List<InvestRepayModel> fakeInvestRepayModels3 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel3.getId());
        assertThat(fakeInvestRepayModels3.size(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getCorpus(), is(7000L));
        assertThat(fakeInvestRepayModels3.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedInterest(), is(230L));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedFee(), is(23L));
        assertThat(fakeInvestRepayModels3.get(0).getRepayDate(), is(expectedRepayDay));
    }

    @Test
    public void shouldCreateRepayWhenLoanType4ActivityRate() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        userMapper.create(fakeUser);
        long loanAmount = 10000;
        DateTime recheckTime = new DateTime().withDate(2015, 2, 1).withTimeAtStartOfDay();
        int duration = 100;
        Date expectedRepayDay = recheckTime.plusDays(duration).withTimeAtStartOfDay().minusSeconds(1).toDate();
        double baseRate = 0.09;
        double activityRate = 0.03;
        LoanModel fakeNormalLoan = this.getFakeNormalLoan(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY, loanAmount, 1, baseRate, activityRate, 0.1, fakeUser.getLoginName(), recheckTime.toDate());
        fakeNormalLoan.setDuration(duration);
        loanMapper.create(fakeNormalLoan);
        DateTime investTime1 = recheckTime.minusDays(10);
        DateTime investTime2 = recheckTime.minusDays(5);
        InvestModel fakeInvestModel1 = getFakeInvestModel(fakeNormalLoan.getId(), 1000, fakeUser.getLoginName(), investTime1.toDate());
        InvestModel fakeInvestModel2 = getFakeInvestModel(fakeNormalLoan.getId(), 2000, fakeUser.getLoginName(), investTime2.toDate());
        InvestModel fakeInvestModel3 = getFakeInvestModel(fakeNormalLoan.getId(), 7000, fakeUser.getLoginName(), recheckTime.toDate());
        investMapper.create(fakeInvestModel1);
        investMapper.create(fakeInvestModel2);
        investMapper.create(fakeInvestModel3);

        repayGeneratorService.generateRepay(fakeNormalLoan.getId());

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(fakeNormalLoan.getId());
        assertThat(loanRepayModels.size(), is(1));
        assertThat(loanRepayModels.get(0).getPeriod(), is(1));
        assertThat(loanRepayModels.get(0).getCorpus(), is(10000L));
        assertThat(loanRepayModels.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(loanRepayModels.get(0).getExpectedInterest(), is(328L));
        assertThat(loanRepayModels.get(0).getRepayDate(), is(expectedRepayDay));

        List<InvestRepayModel> fakeInvestRepayModels1 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel1.getId());
        assertThat(fakeInvestRepayModels1.size(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels1.get(0).getCorpus(), is(1000L));
        assertThat(fakeInvestRepayModels1.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedInterest(), is(32L));
        assertThat(fakeInvestRepayModels1.get(0).getExpectedFee(), is(3L));
        assertThat(fakeInvestRepayModels1.get(0).getRepayDate(), is(expectedRepayDay));

        List<InvestRepayModel> fakeInvestRepayModels2 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel2.getId());
        assertThat(fakeInvestRepayModels2.size(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels2.get(0).getCorpus(), is(2000L));
        assertThat(fakeInvestRepayModels2.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedInterest(), is(65L));
        assertThat(fakeInvestRepayModels2.get(0).getExpectedFee(), is(6L));
        assertThat(fakeInvestRepayModels2.get(0).getRepayDate(), is(expectedRepayDay));

        List<InvestRepayModel> fakeInvestRepayModels3 = investRepayMapper.findByInvestIdAndPeriodAsc(fakeInvestModel3.getId());
        assertThat(fakeInvestRepayModels3.size(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getPeriod(), is(1));
        assertThat(fakeInvestRepayModels3.get(0).getCorpus(), is(7000L));
        assertThat(fakeInvestRepayModels3.get(0).getStatus(), is(RepayStatus.REPAYING));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedInterest(), is(230L));
        assertThat(fakeInvestRepayModels3.get(0).getExpectedFee(), is(23L));
        assertThat(fakeInvestRepayModels3.get(0).getRepayDate(), is(expectedRepayDay));
    }

    private UserModel getFakeUser() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("loginName");
        userModelTest.setPassword("password");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private LoanModel getFakeNormalLoan(LoanType loanType,  long amount, int periods, double baseRate, double activityRate, double investFeeRate, String loginName, Date recheckTime) {
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
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, amount, loginName, investTime, Source.WEB, null);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
    }
}
