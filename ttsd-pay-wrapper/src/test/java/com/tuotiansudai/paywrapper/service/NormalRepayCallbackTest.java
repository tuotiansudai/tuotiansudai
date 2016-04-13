package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class NormalRepayCallbackTest extends RepayBaseTest {

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
    private NormalRepayService normalRepayService;

    @Test
    public void shouldCallbackFirstPeriodWhenLoanIsRepaying() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);

        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(loan.getId(), 10000, investor.getLoginName(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId()), "");

        List<UserBillModel> userBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(userBills.size(), is(1));
        assertThat(userBills.get(0).getAmount(), is(loanRepay1.getActualInterest()));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        assertThat(actualInvestRepay1.getActualInterest(), is(investRepay1.getExpectedInterest()));
        assertThat(actualInvestRepay1.getActualFee(), is(investRepay1.getExpectedFee()));
        assertThat(actualInvestRepay1.getActualRepayDate(), is(loanRepay1.getActualRepayDate()));
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.WAIT_PAY));
    }

    @Test
    public void shouldCallbackLastPeriodWhenLoanIsRepaying() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);

        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().minusDays(30).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay2.setActualInterest(loanRepay2ExpectedInterest);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(loan.getId(), 10000, investor.getLoginName(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setActualInterest(loanRepay1ExpectedInterest);
        investRepay1.setActualFee(100);
        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId()), "");

        List<UserBillModel> userBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(userBills.size(), is(1));
        assertThat(userBills.get(0).getAmount(), is(loanRepay2.getCorpus() + loanRepay2ExpectedInterest));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.NORMAL_REPAY));

        assertThat(loanRepayMapper.findById(loanRepay2.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());
        assertThat(actualInvestRepay2.getActualInterest(), is(investRepay2.getExpectedInterest()));
        assertThat(actualInvestRepay2.getActualFee(), is(investRepay2.getExpectedFee()));
        assertThat(actualInvestRepay2.getActualRepayDate(), is(loanRepay2.getActualRepayDate()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.WAIT_PAY));
    }

    @Test
    public void shouldCallbackOverdueWhenOnePeriodIsOverdue() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);

        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.OVERDUE);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1OverdueInterest = 20;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest + loanRepay1OverdueInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(loan.getId(), 10000, investor.getLoginName(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.OVERDUE);
        investRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId()), "");

        List<UserBillModel> userBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(userBills.size(), is(1));
        assertThat(userBills.get(0).getAmount(), is(loanRepay1ExpectedInterest + loanRepay1OverdueInterest));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.OVERDUE_REPAY));

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        assertThat(actualInvestRepay1.getActualInterest(), is(investRepay1.getExpectedInterest() + investRepay1.getDefaultInterest()));
        assertThat(actualInvestRepay1.getActualFee(), is(investRepay1.getExpectedFee()));
        assertThat(actualInvestRepay1.getActualRepayDate(), is(loanRepay1.getActualRepayDate()));
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.WAIT_PAY));
    }

    @Test
    public void shouldCallbackOverdueWhenTwoPeriodsAreOverdue() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);

        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.OVERDUE);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1OverdueInterest = 20;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.OVERDUE);
        loanRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay2.setActualInterest(loanRepay1ExpectedInterest + loanRepay2ExpectedInterest + loanRepay1OverdueInterest);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(loan.getId(), 10000, investor.getLoginName(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.OVERDUE);
        investRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.OVERDUE);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId()), "");

        List<UserBillModel> userBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(userBills.size(), is(1));
        assertThat(userBills.get(0).getAmount(), is(loanRepay2.getCorpus() + loanRepay1ExpectedInterest + loanRepay2ExpectedInterest + loanRepay1OverdueInterest));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.OVERDUE_REPAY));

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));
        assertNotNull(loanRepayMapper.findById(loanRepay1.getId()).getActualRepayDate());
        assertThat(loanRepayMapper.findById(loanRepay2.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());
        assertThat(actualInvestRepay2.getActualInterest(), is(investRepay1.getExpectedInterest() + investRepay1.getDefaultInterest() + investRepay2.getExpectedInterest()));
        assertThat(actualInvestRepay2.getActualFee(), is(investRepay1.getExpectedFee() + investRepay2.getExpectedFee()));
        assertThat(actualInvestRepay2.getActualRepayDate(), is(loanRepay2.getActualRepayDate()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.WAIT_PAY));
    }
}
