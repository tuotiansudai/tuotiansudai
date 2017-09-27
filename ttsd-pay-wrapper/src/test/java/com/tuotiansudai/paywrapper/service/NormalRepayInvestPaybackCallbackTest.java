package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.repository.mapper.NormalRepayNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.NormalRepayNotifyRequestModel;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class NormalRepayInvestPaybackCallbackTest extends RepayBaseTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

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
    private UserBillMapper userBillMapper;

    @Autowired
    private NormalRepayService normalRepayService;

    @Autowired
    private NormalRepayNotifyMapper normalRepayNotifyMapper;

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

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new Date(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        loanRepay1.setActualRepayDate(new DateTime().withMillisOfSecond(0).toDate());
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        UserMembershipModel userMembershipModel = getFakeUserMemberShip(investor.getLoginName(), UserMembershipType.UPGRADE, 1);
        userMembershipMapper.create(userMembershipModel);

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, 0.1);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay1.setActualInterest(investRepay1.getExpectedInterest());
        investRepay1.setActualFee(investRepay1.getExpectedFee());
        investRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        investRepay1.setRepayAmount(investRepay1.getActualInterest() - investRepay1.getActualFee());

        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 100, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        NormalRepayNotifyRequestModel normalRepayNotifyRequestModel = this.getFakeNormalRepayNotifyRequestModel(investRepay1.getId());
        normalRepayNotifyMapper.create(normalRepayNotifyRequestModel);

        normalRepayService.asyncNormalRepayPaybackCallback(normalRepayNotifyRequestModel.getId());

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());

        try {
            String feeMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage feeMessage = JsonConverter.readValue(feeMessageBody, AmountTransferMessage.class);
            assertThat(feeMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
            assertThat(feeMessage.getAmount(), CoreMatchers.is(actualInvestRepay1.getActualFee()));
            assertThat(feeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_FEE));
            assertThat(feeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));

            String interestMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage interestMessage = JsonConverter.readValue(interestMessageBody, AmountTransferMessage.class);
            assertThat(interestMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
            assertThat(interestMessage.getAmount(), CoreMatchers.is(actualInvestRepay1.getActualInterest()));
            assertThat(interestMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.NORMAL_REPAY));
            assertThat(interestMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_IN_BALANCE));
        } catch (IOException e) {
            assert false;
        }

        assertThat(actualInvestRepay1.getActualInterest(), is(actualInvestRepay1.getExpectedInterest()));
        assertThat(actualInvestRepay1.getActualFee(), is(actualInvestRepay1.getExpectedFee()));
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.COMPLETE));
        assertNotNull(actualInvestRepay1.getActualRepayDate());

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

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).minusDays(30).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay2.setActualInterest(loanRepay2ExpectedInterest);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, 0.1);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setActualInterest(investRepay1.getExpectedInterest());
        investRepay1.setActualFee(investRepay1.getExpectedFee());
        investRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 100, loanRepay2.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay2.setActualInterest(investRepay2.getExpectedInterest());
        investRepay2.setActualFee(investRepay2.getExpectedFee());
        investRepay2.setActualRepayDate(loanRepay2.getActualRepayDate());

        investRepay2.setCorpus(invest.getAmount());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        NormalRepayNotifyRequestModel normalRepayNotifyRequestModel = this.getFakeNormalRepayNotifyRequestModel(investRepay2.getId());
        normalRepayNotifyMapper.create(normalRepayNotifyRequestModel);

        normalRepayService.asyncNormalRepayPaybackCallback(normalRepayNotifyRequestModel.getId());

        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());

        try {
            String feeMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage feeMessage = JsonConverter.readValue(feeMessageBody, AmountTransferMessage.class);
            assertThat(feeMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
            assertThat(feeMessage.getAmount(), CoreMatchers.is(actualInvestRepay2.getActualFee()));
            assertThat(feeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_FEE));
            assertThat(feeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));

            String interestMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage interestMessage = JsonConverter.readValue(interestMessageBody, AmountTransferMessage.class);
            assertThat(interestMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
            assertThat(interestMessage.getAmount(), CoreMatchers.is(invest.getAmount() + actualInvestRepay2.getActualInterest()));
            assertThat(interestMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.NORMAL_REPAY));
            assertThat(interestMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_IN_BALANCE));
        } catch (IOException e) {
            assert false;
        }

        assertThat(actualInvestRepay2.getActualInterest(), is(actualInvestRepay2.getExpectedInterest()));
        assertThat(actualInvestRepay2.getActualFee(), is(actualInvestRepay2.getExpectedFee()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.COMPLETE));
        assertNotNull(actualInvestRepay2.getActualRepayDate());
    }

    @Test
    public void shouldCallbackWhenOnePeriodIsOverdue() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.COMPLETE);
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1DefaultInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        loanRepay1.setDefaultInterest(loanRepay1DefaultInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plus(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, 0.1);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay1.setActualInterest(investRepay1.getExpectedInterest() + loanRepay1DefaultInterest);
        investRepay1.setActualFee(investRepay1.getExpectedFee());
        investRepay1.setDefaultInterest(loanRepay1DefaultInterest);
        investRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 100, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        NormalRepayNotifyRequestModel normalRepayNotifyRequestModel = this.getFakeNormalRepayNotifyRequestModel(investRepay1.getId());
        normalRepayNotifyMapper.create(normalRepayNotifyRequestModel);

        normalRepayService.asyncNormalRepayPaybackCallback(normalRepayNotifyRequestModel.getId());

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());

        try {
            String feeMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage feeMessage = JsonConverter.readValue(feeMessageBody, AmountTransferMessage.class);
            assertThat(feeMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
            assertThat(feeMessage.getAmount(), CoreMatchers.is(actualInvestRepay1.getActualFee()));
            assertThat(feeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_FEE));
            assertThat(feeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));

            String interestMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage interestMessage = JsonConverter.readValue(interestMessageBody, AmountTransferMessage.class);
            assertThat(interestMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
            assertThat(interestMessage.getAmount(), CoreMatchers.is(actualInvestRepay1.getActualInterest()));
            assertThat(interestMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.OVERDUE_REPAY));
            assertThat(interestMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_IN_BALANCE));
        } catch (IOException e) {
            assert false;
        }

        assertThat(actualInvestRepay1.getActualInterest(), is(actualInvestRepay1.getExpectedInterest() + actualInvestRepay1.getDefaultInterest()));
        assertThat(actualInvestRepay1.getActualFee(), is(actualInvestRepay1.getExpectedFee()));
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.COMPLETE));
        assertNotNull(actualInvestRepay1.getActualRepayDate());
    }

    @Test
    public void shouldCallbackWhenTwoPeriodAreOverdue() throws Exception {
        UserModel loaner = this.getFakeUser("loaner");
        AccountModel loanerAccount = this.getFakeAccount(loaner);
        userMapper.create(loaner);
        accountMapper.create(loanerAccount);

        UserModel investor = this.getFakeUser("investor");
        AccountModel investorAccount = this.getFakeAccount(investor);
        userMapper.create(investor);
        accountMapper.create(investorAccount);

        LoanModel loan = this.getFakeNormalLoan(IdGenerator.generate(), LoanType.INVEST_INTEREST_MONTHLY_REPAY, 10000, 2, 0.12, 0, 0.1, loaner.getLoginName(), new Date());
        loan.setStatus(LoanStatus.COMPLETE);
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay1DefaultInterest = 2000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        loanRepay1.setDefaultInterest(loanRepay1DefaultInterest);
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate(), new DateTime().withMillisOfSecond(0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay2ExpectedInterest);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, 10000, investor.getLoginName(), new Date(), Source.WEB, null, 0.1);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.OVERDUE);
        investRepay1.setDefaultInterest(loanRepay1DefaultInterest);
        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 100, loanRepay2.getRepayDate(), RepayStatus.WAIT_PAY);
        investRepay2.setActualInterest(investRepay1.getExpectedInterest() + investRepay2.getExpectedInterest() + loanRepay1DefaultInterest);
        investRepay2.setActualFee(investRepay1.getExpectedFee() + investRepay2.getExpectedFee());
        investRepay2.setActualRepayDate(loanRepay2.getActualRepayDate());
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        NormalRepayNotifyRequestModel normalRepayNotifyRequestModel = this.getFakeNormalRepayNotifyRequestModel(investRepay2.getId());
        normalRepayNotifyMapper.create(normalRepayNotifyRequestModel);

        normalRepayService.asyncNormalRepayPaybackCallback(normalRepayNotifyRequestModel.getId());

        InvestRepayModel actualInvestRepay1 = investRepayMapper.findById(investRepay1.getId());
        InvestRepayModel actualInvestRepay2 = investRepayMapper.findById(investRepay2.getId());

        try {
            String feeMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage feeMessage = JsonConverter.readValue(feeMessageBody, AmountTransferMessage.class);
            assertThat(feeMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
            assertThat(feeMessage.getAmount(), CoreMatchers.is(actualInvestRepay1.getActualFee() + actualInvestRepay2.getActualFee()));
            assertThat(feeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_FEE));
            assertThat(feeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_OUT_BALANCE));

            String interestMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage interestMessage = JsonConverter.readValue(interestMessageBody, AmountTransferMessage.class);
            assertThat(interestMessage.getLoginName(), CoreMatchers.is(investor.getLoginName()));
            assertThat(interestMessage.getAmount(), CoreMatchers.is(actualInvestRepay2.getCorpus() + actualInvestRepay2.getActualInterest()));
            assertThat(interestMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.OVERDUE_REPAY));
            assertThat(interestMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_IN_BALANCE));
        } catch (IOException e) {
            assert false;
        }

        assertThat(actualInvestRepay1.getActualInterest(), is(0L));
        assertThat(actualInvestRepay1.getActualFee(), is(0L));
        assertThat(actualInvestRepay1.getStatus(), is(RepayStatus.COMPLETE));
        assertNotNull(actualInvestRepay1.getActualRepayDate());

        assertThat(actualInvestRepay2.getActualInterest(), is(actualInvestRepay1.getExpectedInterest() + actualInvestRepay2.getExpectedInterest() + actualInvestRepay1.getDefaultInterest()));
        assertThat(actualInvestRepay2.getActualFee(), is(actualInvestRepay1.getActualFee() + actualInvestRepay2.getActualFee()));
        assertThat(actualInvestRepay2.getStatus(), is(RepayStatus.COMPLETE));
        assertNotNull(actualInvestRepay1.getActualRepayDate());
    }
}
