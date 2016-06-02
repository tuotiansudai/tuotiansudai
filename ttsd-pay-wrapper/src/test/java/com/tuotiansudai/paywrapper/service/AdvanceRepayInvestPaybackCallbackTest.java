package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AdvanceRepayInvestPaybackCallbackTest extends RepayBaseTest {

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
    private AdvanceRepayService advanceRepayService;

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

        DateTime recheckTime = new DateTime().minusDays(5);
        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), recheckTime.toDate());
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.COMPLETE);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(idGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay1.setActualInterest(investRepay1.getExpectedInterest());
        investRepay1.setActualFee(investRepay1.getExpectedFee());
        investRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 100, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        advanceRepayService.investPaybackCallback(this.getFakeCallbackParamsMap(investRepay1.getId()), "");

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());

        List<UserBillModel> userBills = userBillMapper.findByLoginName(investor.getLoginName());
        assertThat(userBills.size(), is(2));
        assertThat(userBills.get(0).getAmount(), is(invest.getAmount() + actualInvestRepay1.getActualInterest()));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));

        assertThat(userBills.get(1).getAmount(), is(actualInvestRepay1.getActualFee()));
        assertThat(userBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(userBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));

        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualInvestRepay1.getActualRepayDate().getTime(), is(loanRepay1.getActualRepayDate().getTime()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualInvestRepay2.getActualRepayDate().getTime(), is(loanRepay2.getActualRepayDate().getTime()));
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
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).minusDays(30).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(5).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay2.setActualInterest(loanRepay2ExpectedInterest);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(idGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setActualInterest(investRepay1.getExpectedInterest());
        investRepay1.setActualFee(investRepay1.getExpectedFee());
        investRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay2.setActualInterest(investRepay2.getExpectedInterest());
        investRepay2.setActualFee(investRepay2.getExpectedFee());
        investRepay2.setActualRepayDate(loanRepay2.getActualRepayDate());
        investRepay2.setCorpus(invest.getAmount());

        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        advanceRepayService.investPaybackCallback(this.getFakeCallbackParamsMap(investRepay2.getId()), "");

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());

        List<UserBillModel> userBills = userBillMapper.findByLoginName(investor.getLoginName());
        assertThat(userBills.size(), is(2));
        assertThat(userBills.get(0).getAmount(), is(invest.getAmount() + actualInvestRepay2.getActualInterest()));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TI_BALANCE));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));

        assertThat(userBills.get(1).getAmount(), is(actualInvestRepay2.getActualFee()));
        assertThat(userBills.get(1).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(userBills.get(1).getBusinessType(), is(UserBillBusinessType.INVEST_FEE));

        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualInvestRepay1.getActualRepayDate().getTime(), is(loanRepay1.getActualRepayDate().getTime()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualInvestRepay2.getActualRepayDate().getTime(), is(loanRepay2.getActualRepayDate().getTime()));
    }
}
