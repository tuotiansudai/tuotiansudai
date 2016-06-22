package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class FinanceReportMapperTest {
    @Autowired
    FinanceReportMapper financeReportMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    InvestMapper investMapper;

    @Autowired
    InvestRepayMapper investRepayMapper;

    @Autowired
    IdGenerator idGenerator;

    private UserModel createUserModel(String loginName, String referrer) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("123abc");
        userModel.setEmail("12345@abc.com");
        userModel.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userModel.setReferrer(referrer);
        userMapper.create(userModel);
        return userModel;
    }

    private AccountModel createAccountModel(UserModel userModel, String userName) {
        AccountModel accountModel = new AccountModel(userModel.getLoginName(), userName, String.valueOf(RandomStringUtils.randomNumeric(18)), String.valueOf(RandomStringUtils.randomNumeric(32)),
                String.valueOf(RandomStringUtils.randomNumeric(14)), new Date());
        accountMapper.create(accountModel);
        return accountModel;
    }

    private LoanModel createLoanModel(long loanId, String loanName, UserModel agent, AccountModel loaner, LoanType type, LoanStatus loanStatus) {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(loanId);
        loanModel.setName(loanName);
        loanModel.setType(type);
        loanModel.setAgentLoginName(agent.getLoginName());
        loanModel.setLoanerUserName(loaner.getUserName());
        loanModel.setBaseRate(12.5);
        loanModel.setActivityRate(3.2);
        loanModel.setDuration(90);
        loanModel.setLoanAmount(1000000L);
        loanModel.setVerifyTime(DateTime.parse("2015-06-12T01:20").toDate());
        loanModel.setRecheckTime(DateTime.parse("2016-06-12T01:20").toDate());

        loanModel.setLoanerLoginName(loaner.getLoginName());
        loanModel.setLoanerIdentityNumber(loaner.getLoginName());
        loanModel.setPeriods(6);
        loanModel.setDescriptionText("text");
        loanModel.setDescriptionHtml("html");
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setStatus(loanStatus);

        loanMapper.create(loanModel);
        return loanModel;
    }

    private InvestModel createInvestModel(long investId, long loanId, AccountModel investor, Date investTime) {
        InvestModel investModel = new InvestModel(investId, loanId, null, 1000L, investor.getLoginName(), investTime, Source.WEB, "channel");
        investMapper.create(investModel);
        return investModel;
    }

    private InvestRepayModel createInvestRepayModel(long investId, int period, RepayStatus repayStatus) {
        InvestRepayModel investRepayModel = new InvestRepayModel(idGenerator.generate(), investId, period, 900L, 9999L, 99L, DateTime.parse("2015-03-12T01:20").toDate(), repayStatus);
        if (RepayStatus.COMPLETE == repayStatus) {
            investRepayModel.setActualFee(2000L);
            investRepayModel.setActualInterest(8000L);
            investRepayModel.setRepayAmount(6000L);
        } else {
            investRepayModel.setActualFee(0L);
            investRepayModel.setActualInterest(0L);
            investRepayModel.setRepayAmount(0L);
        }

        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        return investRepayModel;
    }

    private FinanceReportView combineFinanceReportModel(LoanModel loanModel, InvestModel investModel, AccountModel investAccount, InvestRepayModel investRepayModel) {
        FinanceReportView financeReportView = new FinanceReportView();
        financeReportView.setLoanId(loanModel.getId());
        financeReportView.setLoanName(loanModel.getName());
        financeReportView.setLoanType(loanModel.getType());
        financeReportView.setLoanerUserName(loanModel.getLoanerUserName());
        financeReportView.setAgentLoginName(loanModel.getAgentLoginName());
        financeReportView.setBaseRate(loanModel.getBaseRate());
        financeReportView.setActivityRate(loanModel.getActivityRate());
        financeReportView.setDuration(loanModel.getDuration());
        financeReportView.setLoanAmount(loanModel.getLoanAmount());
        financeReportView.setVerifyTime(loanModel.getVerifyTime());
        financeReportView.setRecheckTime(loanModel.getRecheckTime());
        financeReportView.setInvestTime(investModel.getInvestTime());
        financeReportView.setInvestLoginName(investModel.getLoginName());
        financeReportView.setInvestRealName(investAccount.getUserName());
        financeReportView.setReferrer("referrer");
        financeReportView.setInvestAmount(investModel.getAmount());
        financeReportView.setRepayTime(investRepayModel.getRepayDate());
        financeReportView.setPeriod(investRepayModel.getPeriod());
        financeReportView.setActualInterest(investRepayModel.getActualInterest());
        financeReportView.setActualInterest(investRepayModel.getActualFee());
        financeReportView.setActualRepayAmount(investRepayModel.getRepayAmount());

        return financeReportView;
    }

    private List<FinanceReportView> prepareData() {
        UserModel referrerUserModel = createUserModel("referrer", null);
        AccountModel referrerAccountModel = createAccountModel(referrerUserModel, "推荐员");

        UserModel loanerUserModel = createUserModel("loaner", null);
        AccountModel loanerAccountModel = createAccountModel(loanerUserModel, "借款人");

        UserModel agentUserModel = createUserModel("agent", null);
        AccountModel agentAccountModel = createAccountModel(agentUserModel, "代理人");

        UserModel investorWithReferrerUserModel = createUserModel("investorWithReferrer", "referrer");
        AccountModel investorWithReferrerAccountModel = createAccountModel(investorWithReferrerUserModel, "有推荐投资人");

        UserModel investorWithoutReferrerUserModel = createUserModel("investorWithoutReferrer", null);
        AccountModel investorWithoutReferrerAccountModel = createAccountModel(investorWithoutReferrerUserModel, "无推荐投资人");

        List<FinanceReportView> financeReportViews = new ArrayList<>();

        LoanModel loanModel = createLoanModel(1, "房产", agentUserModel, loanerAccountModel, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, LoanStatus.COMPLETE);
        InvestModel investModel = createInvestModel(1, loanModel.getId(), investorWithReferrerAccountModel, DateTime.parse("2016-10-12T01:20").toDate());
        InvestRepayModel investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerAccountModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 2, RepayStatus.COMPLETE);
        financeReportViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerAccountModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 3, RepayStatus.COMPLETE);
        financeReportViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerAccountModel, investRepayModel));
        investModel = createInvestModel(2, loanModel.getId(), investorWithoutReferrerAccountModel, DateTime.parse("2016-11-12T01:20").toDate());
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportViews.add(combineFinanceReportModel(loanModel, investModel, investorWithoutReferrerAccountModel, investRepayModel));

        loanModel = createLoanModel(2, "车辆", agentUserModel, loanerAccountModel, LoanType.LOAN_INTEREST_LUMP_SUM_REPAY, LoanStatus.COMPLETE);
        investModel = createInvestModel(3, loanModel.getId(), investorWithReferrerAccountModel, DateTime.parse("2016-10-12T01:20").toDate());
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerAccountModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 2, RepayStatus.COMPLETE);
        financeReportViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerAccountModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 3, RepayStatus.REPAYING);
        financeReportViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerAccountModel, investRepayModel));
        investModel = createInvestModel(4, loanModel.getId(), investorWithoutReferrerAccountModel, DateTime.parse("2016-11-12T01:20").toDate());
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportViews.add(combineFinanceReportModel(loanModel, investModel, investorWithoutReferrerAccountModel, investRepayModel));

        return financeReportViews;
    }

    @Test
    public void testFindFinanceReportModelsByLoanIdAndLoanNameAndPeriodAndInvestLoginNameAndInvestTime() throws Exception {
        List<FinanceReportView> originalFinanceReportViewList = prepareData();
        List<FinanceReportView> financeReportViews = financeReportMapper.findFinanceReportViews(null, null, null, null, null);
        assertEquals(originalFinanceReportViewList.size(), financeReportViews.size());

        financeReportViews = financeReportMapper.findFinanceReportViews(1L, null, null, null, null);
        assertEquals(4, financeReportViews.size());
        for (FinanceReportView financeReportView : financeReportViews) {
            assertEquals(1L, financeReportView.getLoanId());
            if (financeReportView.getInvestLoginName().equals("investorWithReferrer")) {
                assertEquals(1, financeReportView.getInvestId());
            } else if (financeReportView.getInvestLoginName().equals("investorWithoutReferrer")) {
                assertEquals(2, financeReportView.getInvestId());
            }
        }

        financeReportViews = financeReportMapper.findFinanceReportViews(null, 2, null, null, null);
        assertEquals(2, financeReportViews.size());
        for (FinanceReportView financeReportView : financeReportViews) {
            assertEquals(2, financeReportView.getPeriod());
        }
        financeReportViews = financeReportMapper.findFinanceReportViews(null, 1, null, null, null);
        assertEquals(4, financeReportViews.size());
        for (FinanceReportView financeReportView : financeReportViews) {
            assertEquals(1, financeReportView.getPeriod());
        }

        financeReportViews = financeReportMapper.findFinanceReportViews(null, null, "investorWithReferrer", null, null);
        assertEquals(6, financeReportViews.size());
        for (FinanceReportView financeReportView : financeReportViews) {
            assertEquals("investorWithReferrer", financeReportView.getInvestLoginName());
        }

        financeReportViews = financeReportMapper.findFinanceReportViews(null, null, null, DateTime.parse("2016-11-12T00:20").toDate(), null);
        assertEquals(2, financeReportViews.size());
        for (FinanceReportView financeReportView : financeReportViews) {
            assertTrue(financeReportView.getInvestTime().after(DateTime.parse("2016-11-12T00:20").toDate()));
        }

        financeReportViews = financeReportMapper.findFinanceReportViews(null, null, null, null, DateTime.parse("2016-10-12T02:20").toDate());
        assertEquals(6, financeReportViews.size());
        for (FinanceReportView financeReportView : financeReportViews) {
            assertTrue(financeReportView.getInvestTime().before(DateTime.parse("2016-10-12T02:20").toDate()));
        }

        financeReportViews = financeReportMapper.findFinanceReportViews(null, null, null, DateTime.parse("2016-10-12T00:20").toDate(), DateTime.parse("2016-11-12T02:20").toDate());
        assertEquals(8, financeReportViews.size());
        for (FinanceReportView financeReportView : financeReportViews) {
            assertTrue(financeReportView.getInvestTime().after(DateTime.parse("2016-10-12T00:20").toDate()));
            assertTrue(financeReportView.getInvestTime().before(DateTime.parse("2016-11-12T02:20").toDate()));
        }

        financeReportViews = financeReportMapper.findFinanceReportViews(1L, 1, "investorWithReferrer", null, null);
        assertEquals(1, financeReportViews.size());
        assertEquals(1L, financeReportViews.get(0).getLoanId());
        assertEquals(1, financeReportViews.get(0).getPeriod());
        assertEquals("investorWithReferrer", financeReportViews.get(0).getInvestLoginName());

        financeReportViews = financeReportMapper.findFinanceReportViews(1L, 1, "investorWithReferrer", DateTime.parse("2016-11-12T00:20").toDate(), null);
        assertEquals(0, financeReportViews.size());
        financeReportViews = financeReportMapper.findFinanceReportViews(1L, null, null, null, null);
        assertEquals(0, financeReportViews.size());
        financeReportViews = financeReportMapper.findFinanceReportViews(3L, null, null, null, null);
        assertEquals(0, financeReportViews.size());
        financeReportViews = financeReportMapper.findFinanceReportViews(1L, 2, "investorWithoutReferrer", null, null);
        assertEquals(0, financeReportViews.size());
    }
}
