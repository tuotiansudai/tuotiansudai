package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
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
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AdvanceRepayCallbackTest extends RepayBaseTest {

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
    private TransferApplicationMapper transferApplicationMapper;

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
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, recheckTime.plusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setActualInterest(23L);
        loanRepay1.setRepayAmount(loan.getLoanAmount() + loanRepay1.getActualInterest());
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(60).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(idGenerator.generate(), loan.getId(), null, loan.getLoanAmount(), investor.getLoginName(), Source.WEB, null);
        invest.setCreatedTime(recheckTime.minusDays(1).toDate());
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId()), "");

        List<UserBillModel> userBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(userBills.size(), is(1));
        assertThat(userBills.get(0).getAmount(), is(loanRepay2.getCorpus() + loanRepay1.getActualInterest()));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));

        LoanRepayModel actualLoanRepay1 = loanRepayMapper.findById(loanRepay1.getId());
        assertThat(actualLoanRepay1.getStatus(), is(RepayStatus.COMPLETE));
        LoanRepayModel actualLoanRepay2 = loanRepayMapper.findById(loanRepay2.getId());
        assertThat(actualLoanRepay2.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualLoanRepay2.getActualRepayDate().getTime(), is(loanRepay1.getActualRepayDate().getTime()));

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        assertThat(actualInvestRepay1.getActualInterest(), is(23L));
        assertThat(actualInvestRepay1.getActualFee(), is(2L));
        assertThat(actualInvestRepay1.getActualRepayDate(), is(loanRepay1.getActualRepayDate()));
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.WAIT_PAY));
    }

    @Test
    public void shouldCallbackFirstPeriodWhenLoanIsRepayingTransfer() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);

        UserModel transferee = this.getFakeUser("transferee");
        AccountModel transfereeAccount = this.getFakeAccount(transferee);
        userMapper.create(transferee);
        accountMapper.create(transfereeAccount);

        DateTime recheckTime = new DateTime().minusDays(5);
        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, recheckTime.plusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setActualInterest(23L);
        loanRepay1.setRepayAmount(loan.getLoanAmount() + loanRepay1.getActualInterest());
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(60).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(idGenerator.generate(), loan.getId(), null, loan.getLoanAmount(), investor.getLoginName(), Source.WEB, null);
        invest.setCreatedTime(recheckTime.minusDays(1).toDate());
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);

        InvestModel investTransferee = new InvestModel(idGenerator.generate(), loan.getId(), null, loan.getLoanAmount(), investor.getLoginName(), Source.WEB, null);
        investTransferee.setCreatedTime(recheckTime.minusDays(1).toDate());
        investTransferee.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investTransferee);

        Date dateLine = new DateTime().plusDays(5).toDate();
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(invest,"name",2,9800,200,dateLine,5);
        transferApplicationModel.setInvestId(investTransferee.getId());
        transferApplicationMapper.create(transferApplicationModel);


        InvestRepayModel investRepay1 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepay2 = new InvestRepayModel(idGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        InvestRepayModel investRepayTransferee1 = new InvestRepayModel(idGenerator.generate(), investTransferee.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepayTransferee2 = new InvestRepayModel(idGenerator.generate(), investTransferee.getId(), 2, investTransferee.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepayTransferee1, investRepayTransferee2));

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId()), "");

        List<UserBillModel> userBills = userBillMapper.findByLoginName(loaner.getLoginName());
        assertThat(userBills.size(), is(1));
        assertThat(userBills.get(0).getAmount(), is(loanRepay2.getCorpus() + loanRepay1.getActualInterest()));
        assertThat(userBills.get(0).getOperationType(), is(UserBillOperationType.TO_BALANCE));
        assertThat(userBills.get(0).getBusinessType(), is(UserBillBusinessType.ADVANCE_REPAY));

        LoanRepayModel actualLoanRepay1 = loanRepayMapper.findById(loanRepay1.getId());
        assertThat(actualLoanRepay1.getStatus(), is(RepayStatus.COMPLETE));
        LoanRepayModel actualLoanRepay2 = loanRepayMapper.findById(loanRepay2.getId());
        assertThat(actualLoanRepay2.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualLoanRepay2.getActualRepayDate().getTime(), is(loanRepay1.getActualRepayDate().getTime()));

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepayTransferee1.getId());
        InvestModel actualInvest1 = investMapper.findById(investRepayTransferee1.getInvestId());
        assertThat(actualInvestRepay1.getActualInterest(), is(23L));
        assertThat(actualInvestRepay1.getActualFee(), is(2L));
        assertNotNull(loanRepay1.getActualRepayDate());
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.WAIT_PAY));

        assertThat(actualInvest1.getLoginName(),is(actualInvest1.getLoginName()));
    }
}
