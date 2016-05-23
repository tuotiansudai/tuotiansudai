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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AdvanceRepayInvestFeeCallbackTest extends RepayBaseTest {

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
    private SystemBillMapper systemBillMapper;

    @Autowired
    private AdvanceRepayService advanceRepayService;

    @Test
    public void shouldCallbackInvestFee() throws Exception {
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
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().plusDays(5).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.COMPLETE);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(idGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay1.setActualInterest(loanRepay1ExpectedInterest);
        investRepay1.setActualFee(investRepay1.getActualFee());

        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 100, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        advanceRepayService.investFeeCallback(this.getFakeCallbackParamsMap(loanRepay1.getId()), "");

        SystemBillModel systemBill = systemBillMapper.findByOrderId(loanRepay1.getId(), SystemBillBusinessType.INVEST_FEE);
        assertThat(systemBill.getAmount(), is(loanRepay1.getActualInterest() - investRepay1.getActualInterest() + investRepay1.getActualFee()));
        assertThat(systemBill.getOperationType(), is(SystemBillOperationType.IN));
        assertThat(systemBill.getBusinessType(), is(SystemBillBusinessType.INVEST_FEE));
    }
}
