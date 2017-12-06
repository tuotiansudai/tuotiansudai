package com.tuotiansudai.paywrapper.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class AdvanceRepayGenerateFormDataTest extends RepayBaseTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private AdvanceRepayService advanceRepayService;

    @Test
    public void shouldGenerateFirstPeriodFormDataWhenLoanIsRepaying() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);
        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());

        DateTime recheckTime = new DateTime().minusDays(5);
        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, recheckTime.plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(60).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, loan.getLoanAmount(), investor.getLoginName(), recheckTime.minusDays(1).toDate(), Source.WEB, null, 0.1);
        invest.setStatus(InvestStatus.SUCCESS);
        invest.setTradingTime(invest.getInvestTime());
        invest.setInvestFeeRate(membershipModel.getFee());
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        BaseDto<PayFormDataDto> formData = advanceRepayService.generateRepayFormData(loan.getId());

        assertThat(Long.parseLong(formData.getData().getFields().get("order_id").split("X")[0]), is(loanRepay1.getId()));
        assertThat(Long.parseLong(formData.getData().getFields().get("amount")), is(loan.getLoanAmount() + 23));

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepay1.getId());
        assertThat(enabledLoanRepay.getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(enabledLoanRepay.getActualInterest(), is(23L));
        assertThat(enabledLoanRepay.getRepayAmount(), is(10000L + 23L));
        assertThat(new DateTime(enabledLoanRepay.getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(new DateTime().withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void shouldGenerateFormDataWhenCurrentIsRepayDateAndIsComplete() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);

        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());

        DateTime recheckTime = new DateTime().minusDays(5);
        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new Date(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, loan.getLoanAmount(), investor.getLoginName(), recheckTime.minusDays(1).toDate(), Source.WEB, null, membershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setActualInterest(loanRepay1ExpectedInterest);
        investRepay1.setActualFee(100);
        investRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        BaseDto<PayFormDataDto> formData = advanceRepayService.generateRepayFormData(loan.getId());

        assertThat(Long.parseLong(formData.getData().getFields().get("order_id").split("X")[0]), is(loanRepay2.getId()));
        assertThat(Long.parseLong(formData.getData().getFields().get("amount")), is(loan.getLoanAmount()));

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepay2.getId());
        assertThat(enabledLoanRepay.getStatus(), is(RepayStatus.WAIT_PAY));
        assertThat(enabledLoanRepay.getActualInterest(), is(0L));
        assertThat(enabledLoanRepay.getRepayAmount(), is(10000L));
        assertThat(new DateTime(enabledLoanRepay.getActualRepayDate()).withTimeAtStartOfDay().getMillis(), is(new DateTime().withTimeAtStartOfDay().getMillis()));
    }
}
