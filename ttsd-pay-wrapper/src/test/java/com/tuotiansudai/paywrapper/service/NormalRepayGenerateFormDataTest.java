package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
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
public class NormalRepayGenerateFormDataTest extends RepayBaseTest {

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
    private NormalRepayService normalRepayService;

    @Test
    public void shouldGenerateFirstPeriodFormDataWhenLoanIsRepaying() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        BaseDto<PayFormDataDto> formData = normalRepayService.generateRepayFormData(loan.getId());

        assertThat(Long.parseLong(formData.getData().getFields().get("amount")), is(loanRepay1.getExpectedInterest()));
        assertThat(Long.parseLong(formData.getData().getFields().get("order_id").split("X")[0]), is(loanRepay1.getId()));

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepay1.getId());
        assertThat(enabledLoanRepay.getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(enabledLoanRepay.getActualInterest(), is(loanRepay1.getExpectedInterest()));
        assertThat(enabledLoanRepay.getRepayAmount(), is(loanRepay1.getExpectedInterest()));
        assertThat(new DateTime(enabledLoanRepay.getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(new DateTime().withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void shouldGenerateLastPeriodFormDataWhenLoanIsRepaying() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().minusDays(30).toDate(), RepayStatus.COMPLETE);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        BaseDto<PayFormDataDto> formData = normalRepayService.generateRepayFormData(loan.getId());

        assertThat(Long.parseLong(formData.getData().getFields().get("amount")), is(loanRepay2.getCorpus() + loanRepay2.getExpectedInterest()));
        assertThat(Long.parseLong(formData.getData().getFields().get("order_id").split("X")[0]), is(loanRepay2.getId()));

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepay2.getId());
        assertThat(enabledLoanRepay.getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(enabledLoanRepay.getActualInterest(), is(loanRepay2.getExpectedInterest()));
        assertThat(enabledLoanRepay.getRepayAmount(), is(loanRepay2.getCorpus() + loanRepay2.getExpectedInterest()));
        assertThat(new DateTime(enabledLoanRepay.getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(new DateTime().withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void shouldGenerateFormDataWhenOnePeriodIsOverdue() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.OVERDUE);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1OverdueInterest = 10;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.OVERDUE);
        loanRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        BaseDto<PayFormDataDto> formData = normalRepayService.generateRepayFormData(loan.getId());

        assertThat(Long.parseLong(formData.getData().getFields().get("amount")), is(loanRepay1.getExpectedInterest() + loanRepay1.getDefaultInterest()));
        assertThat(Long.parseLong(formData.getData().getFields().get("order_id").split("X")[0]), is(loanRepay1.getId()));

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepay1.getId());
        assertThat(enabledLoanRepay.getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(enabledLoanRepay.getActualInterest(), is(loanRepay1.getExpectedInterest() + loanRepay1.getDefaultInterest()));
        assertThat(enabledLoanRepay.getRepayAmount(), is(loanRepay1.getExpectedInterest() + loanRepay1.getDefaultInterest()));
        assertThat(new DateTime(enabledLoanRepay.getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(new DateTime().withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void shouldGenerateFormDataWhenTwoPeriodsAreOverdue() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        LoanModel loan = this.getFakeNormalLoan(idGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.OVERDUE);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1OverdueInterest = 20;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.OVERDUE);
        loanRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(idGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.OVERDUE);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        BaseDto<PayFormDataDto> formData = normalRepayService.generateRepayFormData(loan.getId());

        long actualInterest = loanRepay1.getExpectedInterest() + loanRepay1.getDefaultInterest() + loanRepay2.getExpectedInterest();
        assertThat(Long.parseLong(formData.getData().getFields().get("amount")), is(loanRepay2.getCorpus() + actualInterest));
        assertThat(Long.parseLong(formData.getData().getFields().get("order_id").split("X")[0]), is(loanRepay2.getId()));

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepay2.getId());
        assertThat(enabledLoanRepay.getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(enabledLoanRepay.getActualInterest(), is(actualInterest));
        assertThat(enabledLoanRepay.getRepayAmount(), is(loanRepay2.getCorpus() + actualInterest));
        assertThat(new DateTime(enabledLoanRepay.getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(new DateTime().withTimeAtStartOfDay().getMillis()));
    }
}
