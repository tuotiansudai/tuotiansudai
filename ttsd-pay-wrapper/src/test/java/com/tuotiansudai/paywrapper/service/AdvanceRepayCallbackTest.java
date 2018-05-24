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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class AdvanceRepayCallbackTest extends RepayBaseTest {

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
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private AdvanceRepayService advanceRepayService;

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

        DateTime recheckTime = new DateTime().minusDays(5);
        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, recheckTime.plusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setActualInterest(23L);
        loanRepay1.setRepayAmount(loan.getLoanAmount() + loanRepay1.getActualInterest());
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(60).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, investor.getLoginName(), loan.getLoanAmount(), membershipModel.getFee(), false, recheckTime.minusDays(1).toDate(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        invest.setTradingTime(invest.getInvestTime());
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId(), "project_transfer_notify"), "");

        verifyAgentUserBillMessage(loaner, loanRepay1, loanRepay2);

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
        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());

        UserModel transferee = this.getFakeUser("transferee");
        AccountModel transfereeAccount = this.getFakeAccount(transferee);
        userMapper.create(transferee);
        accountMapper.create(transfereeAccount);

        DateTime recheckTime = new DateTime().minusDays(5);
        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), recheckTime.toDate());
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, recheckTime.plusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.WAIT_PAY);
        loanRepay1.setActualInterest(23L);
        loanRepay1.setRepayAmount(loan.getLoanAmount() + loanRepay1.getActualInterest());
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(60).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanMapper.create(loan);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, investor.getLoginName(), loan.getLoanAmount(), membershipModel.getFee(), false, recheckTime.minusDays(1).toDate(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        invest.setTransferStatus(TransferStatus.SUCCESS);
        invest.setTradingTime(invest.getInvestTime());
        investMapper.create(invest);

        InvestModel investTransferee = new InvestModel(IdGenerator.generate(), loan.getId(), null, investor.getLoginName(), loan.getLoanAmount(), 0.1, false, recheckTime.minusDays(1).toDate(), Source.WEB, null);
        investTransferee.setTransferInvestId(invest.getId());
        investTransferee.setStatus(InvestStatus.SUCCESS);
        investTransferee.setInvestFeeRate(0.1);
        investMapper.create(investTransferee);

        Date dateLine = new DateTime().plusDays(5).toDate();
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(invest, "name", 2, 9800, 200, dateLine, 5, Source.WEB);
        transferApplicationModel.setInvestId(investTransferee.getId());
        transferApplicationMapper.create(transferApplicationModel);


        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.COMPLETE);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        InvestRepayModel investRepayTransferee1 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.REPAYING);
        InvestRepayModel investRepayTransferee2 = new InvestRepayModel(IdGenerator.generate(), investTransferee.getId(), 2, investTransferee.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepayTransferee1, investRepayTransferee2));

        advanceRepayService.repayCallback(this.getFakeCallbackParamsMap(loanRepay1.getId(), "project_transfer_notify"), "");

        verifyAgentUserBillMessage(loaner, loanRepay1, loanRepay2);

        LoanRepayModel actualLoanRepay1 = loanRepayMapper.findById(loanRepay1.getId());
        assertThat(actualLoanRepay1.getStatus(), is(RepayStatus.COMPLETE));
        LoanRepayModel actualLoanRepay2 = loanRepayMapper.findById(loanRepay2.getId());
        assertThat(actualLoanRepay2.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualLoanRepay2.getActualRepayDate().getTime(), is(loanRepay1.getActualRepayDate().getTime()));

        InvestRepayModel actualInvestRepayTransferee1 = investRepayMapper.findById(investRepayTransferee1.getId());
        assertThat(actualInvestRepayTransferee1.getActualInterest(), is(23L));
        assertThat(actualInvestRepayTransferee1.getActualFee(), is(2L));
        assertThat(actualInvestRepayTransferee1.getActualRepayDate(), is(loanRepay1.getActualRepayDate()));
        assertThat(actualInvestRepayTransferee1.getStatus(), is(RepayStatus.WAIT_PAY));

        InvestRepayModel actualInvestRepayTransferrer1 = investRepayMapper.findById(investRepay1.getId());
        assertThat(actualInvestRepayTransferrer1.getActualInterest(), is(0L));
        assertThat(actualInvestRepayTransferrer1.getActualFee(), is(0L));
        assertNull(actualInvestRepayTransferrer1.getActualRepayDate());
        assertThat(actualInvestRepayTransferrer1.getStatus(), is(RepayStatus.COMPLETE));

    }

    private void verifyAgentUserBillMessage(UserModel loaner, LoanRepayModel loanRepay1, LoanRepayModel loanRepay2) {
        try {
            String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage transferFeeMessage = JsonConverter.readValue(messageBody, AmountTransferMessage.class);
            assertThat(transferFeeMessage.getLoginName(), CoreMatchers.is(loaner.getLoginName()));
            assertThat(transferFeeMessage.getAmount(), CoreMatchers.is(loanRepay2.getCorpus() + loanRepay1.getActualInterest()));
            assertThat(transferFeeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.ADVANCE_REPAY));
            assertThat(transferFeeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));
        } catch (IOException e) {
            assert false;
        }
    }
}
