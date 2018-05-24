package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillMessageType;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.message.SystemBillMessage;
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

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class NormalRepayInvestFeeCallbackTest extends RepayBaseTest {

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
    private SystemBillMapper systemBillMapper;

    @Autowired
    private NormalRepayService normalRepayService;

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
        loanMapper.create(loan);
        long loanRepay1ExpectedInterest = 1000;
        long loanRepay2ExpectedInterest = 1000;
        LoanRepayModel loanRepay1 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 1, 0, loanRepay1ExpectedInterest, new DateTime().withTime(23, 59, 59, 0).toDate(), new Date(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(loanRepay1ExpectedInterest);
        loanRepay1.setActualRepayDate(new Date());
        LoanRepayModel loanRepay2 = this.getFakeLoanRepayModel(IdGenerator.generate(), loan.getId(), 2, loan.getLoanAmount(), loanRepay2ExpectedInterest, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), null, RepayStatus.REPAYING);
        loanRepayMapper.create(Lists.newArrayList(loanRepay1, loanRepay2));

        InvestModel invest = new InvestModel(IdGenerator.generate(), loan.getId(), null, investor.getLoginName(), 10000, membershipModel.getFee(), false, new Date(), Source.WEB, null);
        invest.setStatus(InvestStatus.SUCCESS);
        investMapper.create(invest);
        InvestRepayModel investRepay1 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 1, 0, loanRepay1ExpectedInterest, 100, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        investRepay1.setActualInterest(loanRepay1ExpectedInterest);
        investRepay1.setActualFee(investRepay1.getActualFee());

        InvestRepayModel investRepay2 = new InvestRepayModel(IdGenerator.generate(), invest.getId(), 2, invest.getAmount(), loanRepay2ExpectedInterest, 100, loanRepay2.getRepayDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepay1, investRepay2));

        normalRepayService.investFeeCallback(this.getFakeCallbackParamsMap(loanRepay1.getId(), "project_transfer_notify"), "");

        verifySystemBillMessage(loanRepay1, investRepay1);
    }

    private void verifySystemBillMessage(LoanRepayModel loanRepay1, InvestRepayModel investRepay1) throws IOException {
        String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.SystemBill.getQueueName()));
        SystemBillMessage message = JsonConverter.readValue(messageBody, SystemBillMessage.class);
        assertThat(message.getAmount(), CoreMatchers.is(loanRepay1.getActualInterest() - investRepay1.getActualInterest() + investRepay1.getActualFee()));
        assertThat(message.getOrderId(), CoreMatchers.is(loanRepay1.getId()));
        assertThat(message.getBusinessType(), CoreMatchers.is(SystemBillBusinessType.INVEST_FEE));
        assertThat(message.getMessageType(), CoreMatchers.is(SystemBillMessageType.TRANSFER_IN));
    }
}
