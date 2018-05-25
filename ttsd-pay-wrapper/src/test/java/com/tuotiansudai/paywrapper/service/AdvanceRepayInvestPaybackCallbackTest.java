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
import com.tuotiansudai.paywrapper.repository.mapper.AdvanceRepayNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.AdvanceRepayNotifyRequestModel;
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
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class AdvanceRepayInvestPaybackCallbackTest extends RepayBaseTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipMapper membershipMapper;

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

    @Autowired
    private AdvanceRepayNotifyMapper advanceRepayNotifyMapper;

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
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), loanRepay1.getActualRepayDate(), RepayStatus.COMPLETE);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, investor.getLoginName(), 10000, membershipModel.getFee(), false, new Date(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay1.setActualInterest(investRepay1.getExpectedInterest());
        investRepay1.setActualFee(investRepay1.getExpectedFee());
        investRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 100, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        AdvanceRepayNotifyRequestModel advanceRepayNotifyRequestModel = this.getFakeAdvanceRepayNotifyRequestModel(investRepay1.getId());
        advanceRepayNotifyMapper.create(advanceRepayNotifyRequestModel);

        advanceRepayService.asyncAdvanceRepayPaybackCallback(advanceRepayNotifyRequestModel.getId());

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());

        verifyAmountTransferMessage(investor, invest, actualInvestRepay1);

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
        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 2000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).minusDays(30).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(5).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay2.setActualInterest(loanRepay2ExpectedInterest);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, investor.getLoginName(), 10000, membershipModel.getFee(), false, new Date(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setActualInterest(investRepay1.getExpectedInterest());
        investRepay1.setActualFee(investRepay1.getExpectedFee());
        investRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 200, loanRepay2.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay2.setActualInterest(investRepay2.getExpectedInterest());
        investRepay2.setActualFee(investRepay2.getExpectedFee());
        investRepay2.setActualRepayDate(loanRepay2.getActualRepayDate());
        investRepay2.setCorpus(invest.getAmount());

        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        AdvanceRepayNotifyRequestModel advanceRepayNotifyRequestModel = this.getFakeAdvanceRepayNotifyRequestModel(investRepay2.getId());
        advanceRepayNotifyMapper.create(advanceRepayNotifyRequestModel);

        advanceRepayService.asyncAdvanceRepayPaybackCallback(advanceRepayNotifyRequestModel.getId());

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());

        verifyAmountTransferMessage(investor, invest, actualInvestRepay2);

        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualInvestRepay1.getActualRepayDate().getTime(), is(loanRepay1.getActualRepayDate().getTime()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.COMPLETE));
        assertThat(actualInvestRepay2.getActualRepayDate().getTime(), is(loanRepay2.getActualRepayDate().getTime()));
    }

    private void verifyAmountTransferMessage(UserModel investor, InvestModel invest, InvestRepayModel actualInvestRepay2) throws IOException {
        String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
        AmountTransferMessage interestMessage = JsonConverter.readValue(messageBody, AmountTransferMessage.class);
        assertThat(interestMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
        assertThat(interestMessage.getAmount(), CoreMatchers.is(invest.getAmount() + actualInvestRepay2.getActualInterest()));
        assertThat(interestMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.ADVANCE_REPAY));
        assertThat(interestMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_IN_BALANCE));

        AmountTransferMessage feeMessage = interestMessage.getNext();
        assertThat(feeMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
        assertThat(feeMessage.getAmount(), CoreMatchers.is(actualInvestRepay2.getActualFee()));
        assertThat(feeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_FEE));
        assertThat(feeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));
    }
}
