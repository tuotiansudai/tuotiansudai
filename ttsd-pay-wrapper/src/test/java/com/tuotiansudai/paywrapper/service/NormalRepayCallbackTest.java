package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class NormalRepayCallbackTest extends RepayBaseTest {

    @Autowired
    private FakeUserHelper userMapper;

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
    private NormalRepayService normalRepayService;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private UserMembershipMapper userMembershipMapper;
    @Autowired
    private MembershipMapper membershipMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

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
        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        loanRepay1.setRepayAmount(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, membershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId(), "project_transfer_notify"), "");

        verifyAmountTransferMessage(loan, loanRepay1.getActualInterest(), UserBillBusinessType.NORMAL_REPAY);

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        assertThat(actualInvestRepay1.getActualInterest(), is(investRepay1.getExpectedInterest()));
        assertThat(actualInvestRepay1.getActualFee(), is(investRepay1.getExpectedFee()));
        assertThat(actualInvestRepay1.getActualRepayDate(), is(loanRepay1.getActualRepayDate()));
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.WAIT_PAY));
    }

    @Test
    public void shouldCallbackFirstPeriodWhenLoanIsRepayingAndTransfer() throws Exception {
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

        UserMembershipModel investorUserMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(investorUserMembershipModel);
        UserMembershipModel transfereeUserMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.GIVEN, 2);
        userMembershipMapper.create(transfereeUserMembershipModel);

        MembershipModel investorMembershipModel = membershipMapper.findById(investorUserMembershipModel.getMembershipId());
        MembershipModel transfereeMembershipModel = membershipMapper.findById(transfereeUserMembershipModel.getMembershipId());


        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        loanRepay1.setRepayAmount(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, investorMembershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        invest.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.create(invest);

        InvestModel investTransferee = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, transferee.getLoginName(), new Date(), Source.WEB, null, transfereeMembershipModel.getFee());
        investTransferee.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investTransferee);

        Date dateLine = new DateTime().plusDays(5).toDate();
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(invest, "name", 2, 9800, 200, dateLine, 5, Source.WEB);
        transferApplicationModel.setInvestId(investTransferee.getId());
        transferApplicationMapper.create(transferApplicationModel);

        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.COMPLETE);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        InvestRepayModel investRepayTransferee1 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepayTransferee2 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 2, investTransferee.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayTransferee2.setCorpus(investTransferee.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepayTransferee1, investRepayTransferee2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId(), "project_transfer_notify"), "");

        verifyAmountTransferMessage(loan, loanRepay1.getActualInterest(), UserBillBusinessType.NORMAL_REPAY);

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepayTransferee1 = investRepayMapper.findById(investRepayTransferee1.getId());
        assertThat(actualInvestRepayTransferee1.getActualInterest(), is(investRepayTransferee1.getExpectedInterest()));
        assertThat(actualInvestRepayTransferee1.getActualFee(), is(investRepayTransferee1.getExpectedFee()));
        assertNotNull(actualInvestRepayTransferee1.getActualFee());
        assertThat(actualInvestRepayTransferee1.getStatus(), is(RepayStatus.WAIT_PAY));

        InvestRepayModel actualInvestRepayTransferrer1 = investRepayMapper.findById(investRepay1.getId());
        assertThat(actualInvestRepayTransferrer1.getActualInterest(), is(0L));
        assertThat(actualInvestRepayTransferrer1.getActualFee(), is(is(0L)));
        assertNull(actualInvestRepayTransferrer1.getActualRepayDate());
        assertThat(actualInvestRepayTransferrer1.getStatus(), is(RepayStatus.COMPLETE));
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

        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().minusDays(30).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay2.setActualInterest(loanRepay2ExpectedInterest);
        loanRepay2.setRepayAmount(loanRepay2.getCorpus() + loanRepay2ExpectedInterest);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, membershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setActualInterest(loanRepay1ExpectedInterest);
        investRepay1.setActualFee(100);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId(), "project_transfer_notify"), "");

        verifyAmountTransferMessage(loan, loanRepay2.getCorpus() + loanRepay2ExpectedInterest, UserBillBusinessType.NORMAL_REPAY);

        assertThat(loanRepayMapper.findById(loanRepay2.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());
        assertThat(actualInvestRepay2.getActualInterest(), is(investRepay2.getExpectedInterest()));
        assertThat(actualInvestRepay2.getActualFee(), is(investRepay2.getExpectedFee()));
        assertThat(actualInvestRepay2.getActualRepayDate(), is(loanRepay2.getActualRepayDate()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.WAIT_PAY));
    }


    @Test
    public void shouldCallbackLastPeriodWhenLoanIsRepayingAndTransfer() throws Exception {
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

        UserMembershipModel investorUserMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(investorUserMembershipModel);
        UserMembershipModel transfereeUserMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.GIVEN, 2);
        userMembershipMapper.create(transfereeUserMembershipModel);

        MembershipModel investorMembershipModel = membershipMapper.findById(investorUserMembershipModel.getMembershipId());
        MembershipModel transfereeMembershipModel = membershipMapper.findById(transfereeUserMembershipModel.getMembershipId());

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().minusDays(30).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay2.setActualInterest(loanRepay2ExpectedInterest);
        loanRepay2.setRepayAmount(loanRepay2.getCorpus() + loanRepay2ExpectedInterest);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, investorMembershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        invest.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setActualInterest(loanRepay1ExpectedInterest);
        investRepay1.setActualFee(100);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.COMPLETE);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        InvestModel investTransferee = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, transferee.getLoginName(), new Date(), Source.WEB, null, transfereeMembershipModel.getFee());
        investTransferee.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investTransferee);
        InvestRepayModel investRepayTransferee1 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepayTransferee1.setActualInterest(loanRepay1ExpectedInterest);
        investRepayTransferee1.setActualFee(100);
        InvestRepayModel investRepayTransferee2 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 2, investTransferee.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayTransferee2.setCorpus(investTransferee.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepayTransferee1, investRepayTransferee2));

        Date dateLine = new DateTime().plusDays(5).toDate();
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(invest, "name", 2, 9800, 200, dateLine, 5, Source.WEB);
        transferApplicationModel.setInvestId(investTransferee.getId());
        transferApplicationMapper.create(transferApplicationModel);

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId(), "project_transfer_notify"), "");

        verifyAmountTransferMessage(loan, loanRepay2.getCorpus() + loanRepay2ExpectedInterest, UserBillBusinessType.NORMAL_REPAY);

        assertThat(loanRepayMapper.findById(loanRepay2.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepayTransferee2.getId());
        assertThat(actualInvestRepay2.getActualInterest(), is(investRepay2.getExpectedInterest()));
        assertThat(actualInvestRepay2.getActualFee(), is(investRepay2.getExpectedFee()));
        assertThat(actualInvestRepay2.getActualRepayDate(), is(loanRepay2.getActualRepayDate()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.WAIT_PAY));

        InvestRepayModel actualInvestRepayTransferrer2 = investRepayMapper.findById(investRepay2.getId());
        assertThat(actualInvestRepayTransferrer2.getActualInterest(), is(0L));
        assertThat(actualInvestRepayTransferrer2.getActualFee(), is(is(0L)));
        assertNull(actualInvestRepayTransferrer2.getActualRepayDate());
        assertThat(actualInvestRepayTransferrer2.getStatus(), is(RepayStatus.COMPLETE));

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

        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.OVERDUE);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1OverdueInterest = 20;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest + loanRepay1OverdueInterest);
        loanRepay1.setRepayAmount(loanRepay1ExpectedInterest + loanRepay1OverdueInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, membershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.OVERDUE);
        investRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId(), "project_transfer_notify"), "");

        verifyAmountTransferMessage(loan, loanRepay1ExpectedInterest + loanRepay1OverdueInterest, UserBillBusinessType.OVERDUE_REPAY);

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        assertThat(actualInvestRepay1.getActualInterest(), is(investRepay1.getExpectedInterest() + investRepay1.getDefaultInterest()));
        assertThat(actualInvestRepay1.getActualFee(), is(investRepay1.getExpectedFee()));
        assertThat(actualInvestRepay1.getActualRepayDate(), is(loanRepay1.getActualRepayDate()));
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.WAIT_PAY));
    }

    @Test
    public void shouldCallbackOverdueWhenOnePeriodIsOverdueTransfer() throws Exception {
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

        UserMembershipModel investorUserMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(investorUserMembershipModel);
        UserMembershipModel transfereeUserMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.GIVEN, 2);
        userMembershipMapper.create(transfereeUserMembershipModel);

        MembershipModel investorMembershipModel = membershipMapper.findById(investorUserMembershipModel.getMembershipId());
        MembershipModel transfereeMembershipModel = membershipMapper.findById(transfereeUserMembershipModel.getMembershipId());

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.OVERDUE);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1OverdueInterest = 20;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest + loanRepay1OverdueInterest);
        loanRepay1.setRepayAmount(loanRepay1ExpectedInterest + loanRepay1OverdueInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, investorMembershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        invest.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.COMPLETE);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        InvestModel investTransferee = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, transfereeMembershipModel.getFee());
        investTransferee.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investTransferee);
        InvestRepayModel investRepayTransferee1 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.OVERDUE);
        investRepayTransferee1.setDefaultInterest(loanRepay1OverdueInterest);
        InvestRepayModel investRepayTransferee2 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 2, investTransferee.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepayTransferee1, investRepayTransferee2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId(), "project_transfer_notify"), "");

        verifyAmountTransferMessage(loan, loanRepay1ExpectedInterest + loanRepay1OverdueInterest, UserBillBusinessType.OVERDUE_REPAY);

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepayTransferrer1 = investRepayMapper.findById(investRepayTransferee1.getId());
        assertThat(actualInvestRepayTransferrer1.getActualInterest(), is(investRepayTransferee1.getExpectedInterest() + investRepayTransferee1.getDefaultInterest()));
        assertThat(actualInvestRepayTransferrer1.getActualFee(), is(investRepay1.getExpectedFee()));
        assertThat(actualInvestRepayTransferrer1.getActualRepayDate(), is(loanRepay1.getActualRepayDate()));
        assertThat(actualInvestRepayTransferrer1.getStatus(), is(RepayStatus.WAIT_PAY));

        InvestRepayModel actualInvestRepayTransferee1 = investRepayMapper.findById(investRepay1.getId());
        assertThat(actualInvestRepayTransferee1.getActualInterest(), is(0L));
        assertThat(actualInvestRepayTransferee1.getActualFee(), is(0L));
        assertNull(actualInvestRepayTransferee1.getActualRepayDate());
        assertThat(actualInvestRepayTransferee1.getStatus(), is(RepayStatus.COMPLETE));
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

        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.OVERDUE);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1OverdueInterest = 20;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.OVERDUE);
        loanRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay2.setActualInterest(loanRepay1ExpectedInterest + loanRepay2ExpectedInterest + loanRepay1OverdueInterest);
        loanRepay2.setRepayAmount(loanRepay2.getCorpus() + loanRepay2.getActualInterest());
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, membershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.OVERDUE);
        investRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.OVERDUE);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId(), "project_transfer_notify"), "");

        verifyAmountTransferMessage(loan, loanRepay2.getCorpus() + loanRepay1ExpectedInterest + loanRepay2ExpectedInterest + loanRepay1OverdueInterest, UserBillBusinessType.OVERDUE_REPAY);

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));
        assertNotNull(loanRepayMapper.findById(loanRepay1.getId()).getActualRepayDate());
        assertThat(loanRepayMapper.findById(loanRepay2.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());
        assertThat(actualInvestRepay2.getActualInterest(), is(investRepay1.getExpectedInterest() + investRepay1.getDefaultInterest() + investRepay2.getExpectedInterest()+ InterestCalculator.calculateLoanInterest(loan.getBaseRate(),loan.getLoanAmount(),new DateTime(actualInvestRepay2.getRepayDate()),new DateTime())));
        assertThat(actualInvestRepay2.getActualFee(), is(investRepay1.getExpectedFee() + investRepay2.getExpectedFee()));
        assertThat(actualInvestRepay2.getActualRepayDate(), is(loanRepay2.getActualRepayDate()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.WAIT_PAY));
    }

    @Test
    public void shouldCallbackOverdueWhenTwoPeriodsAreOverdueTransfer() throws Exception {
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

        UserMembershipModel investorUserMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(investorUserMembershipModel);
        UserMembershipModel transfereeUserMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.GIVEN, 2);
        userMembershipMapper.create(transfereeUserMembershipModel);

        MembershipModel investorMembershipModel = membershipMapper.findById(investorUserMembershipModel.getMembershipId());
        MembershipModel transfereeMembershipModel = membershipMapper.findById(transfereeUserMembershipModel.getMembershipId());

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.OVERDUE);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1OverdueInterest = 20;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.OVERDUE);
        loanRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay2.setActualInterest(loanRepay1ExpectedInterest + loanRepay2ExpectedInterest + loanRepay1OverdueInterest);
        loanRepay2.setRepayAmount(loanRepay2.getCorpus() + loanRepay2.getActualInterest());
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, investorMembershipModel.getFee());
        invest.setStatus(InvestStatus.SUCCESS);
        invest.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.COMPLETE);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        InvestModel investTransferee = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, transfereeMembershipModel.getFee());
        investTransferee.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investTransferee);
        InvestRepayModel investRepayTransferee1 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.OVERDUE);
        investRepay1.setDefaultInterest(loanRepay1OverdueInterest);
        InvestRepayModel investRepayTransferee2 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 2, investTransferee.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.OVERDUE);
        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepayTransferee1, investRepayTransferee2));

        normalRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay2.getId(), "project_transfer_notify"), "");

        verifyAmountTransferMessage(loan, loanRepay2.getCorpus() + loanRepay1ExpectedInterest + loanRepay2ExpectedInterest + loanRepay1OverdueInterest, UserBillBusinessType.OVERDUE_REPAY);

        assertThat(loanRepayMapper.findById(loanRepay1.getId()).getStatus(), is(RepayStatus.COMPLETE));
        assertNotNull(loanRepayMapper.findById(loanRepay1.getId()).getActualRepayDate());
        assertThat(loanRepayMapper.findById(loanRepay2.getId()).getStatus(), is(RepayStatus.COMPLETE));

        InvestRepayModel actualInvestRepayTransferrer2 = investRepayMapper.findById(investRepayTransferee2.getId());
        assertThat(actualInvestRepayTransferrer2.getActualInterest(), is(investRepayTransferee1.getExpectedInterest() + investRepayTransferee1.getDefaultInterest() + investRepayTransferee2.getExpectedInterest()+InterestCalculator.calculateLoanInterest(loan.getBaseRate(),invest.getAmount(),new DateTime(investRepayTransferee2.getRepayDate()),new DateTime())));
        assertThat(actualInvestRepayTransferrer2.getActualFee(), is(investRepayTransferee1.getExpectedFee() + investRepayTransferee2.getExpectedFee()));
        assertThat(actualInvestRepayTransferrer2.getActualRepayDate(), is(loanRepay2.getActualRepayDate()));
        assertThat(actualInvestRepayTransferrer2.getStatus(), is(RepayStatus.WAIT_PAY));

        InvestRepayModel actualInvestRepayTransferee2 = investRepayMapper.findById(investRepay2.getId());
        assertThat(actualInvestRepayTransferee2.getActualInterest(), is(0l));
        assertThat(actualInvestRepayTransferee2.getActualFee(), is(0l));
        assertNull(actualInvestRepayTransferee2.getActualRepayDate());
        assertThat(actualInvestRepayTransferee2.getStatus(), is(RepayStatus.COMPLETE));
    }

    private void verifyAmountTransferMessage(LoanModel loan, long amount, UserBillBusinessType businessType) throws IOException {
        String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
        AmountTransferMessage message = JsonConverter.readValue(messageBody, AmountTransferMessage.class);
        assertThat(message.getLoginName(), CoreMatchers.is(loan.getAgentLoginName()));
        assertThat(message.getAmount(), CoreMatchers.is(amount));
        assertThat(message.getBusinessType(), CoreMatchers.is(businessType));
        assertThat(message.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));
    }

}