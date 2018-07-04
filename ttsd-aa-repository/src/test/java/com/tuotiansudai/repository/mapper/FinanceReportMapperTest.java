package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class FinanceReportMapperTest {
    @Autowired
    FinanceReportMapper financeReportMapper;

    @Autowired
    FakeUserHelper userMapper;

    @Autowired
    BankAccountMapper bankAccountMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    InvestMapper investMapper;

    @Autowired
    InvestRepayMapper investRepayMapper;

    private UserModel createUserModel(String loginName, String referrer) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("123abc");
        userModel.setEmail("12345@abc.com");
        userModel.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModel.setUserName("userName");
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userModel.setReferrer(referrer);
        userMapper.create(userModel);
        return userModel;
    }

    private BankAccountModel createAccountModel(UserModel userModel) {
        BankAccountModel accountModel = new BankAccountModel(userModel.getLoginName(), String.valueOf(RandomStringUtils.randomNumeric(32)),
                String.valueOf(RandomStringUtils.randomNumeric(14)), "111", "111");
        bankAccountMapper.createInvestor(accountModel);
        return accountModel;
    }

    private LoanModel createLoanModel(long loanId, String loanName, UserModel agent, BankAccountModel loaner, LoanType type, LoanStatus loanStatus) {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(loanId);
        loanModel.setName(loanName);
        loanModel.setType(type);
        loanModel.setAgentLoginName(agent.getLoginName());
        loanModel.setLoanerUserName(agent.getUserName());
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
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private InvestModel createInvestModel(long investId, long loanId, BankAccountModel investor, Date investTime) {
        InvestModel investModel = new InvestModel(investId, loanId, null, investor.getLoginName(), 1000L, 0.01, false, investTime, Source.WEB, "channel");
        investMapper.create(investModel);
        return investModel;
    }

    private InvestRepayModel createInvestRepayModel(long investId, int period, RepayStatus repayStatus) {
        InvestRepayModel investRepayModel = new InvestRepayModel(IdGenerator.generate(), investId, period, 900L, 9999L, 99L, DateTime.parse("2015-03-12T01:20").toDate(), repayStatus);
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

    private FinanceReportItemView combineFinanceReportModel(LoanModel loanModel, InvestModel investModel, UserModel userModel, InvestRepayModel investRepayModel) {
        FinanceReportItemView financeReportItemView = new FinanceReportItemView();
        financeReportItemView.setLoanId(loanModel.getId());
        financeReportItemView.setLoanName(loanModel.getName());
        financeReportItemView.setLoanType(loanModel.getType());
        financeReportItemView.setLoanerUserName(loanModel.getLoanerUserName());
        financeReportItemView.setAgentLoginName(loanModel.getAgentLoginName());
        financeReportItemView.setBaseRate(loanModel.getBaseRate());
        financeReportItemView.setActivityRate(loanModel.getActivityRate());
        financeReportItemView.setDuration(loanModel.getDuration());
        financeReportItemView.setLoanAmount(loanModel.getLoanAmount());
        financeReportItemView.setVerifyTime(loanModel.getVerifyTime());
        financeReportItemView.setRecheckTime(loanModel.getRecheckTime());
        financeReportItemView.setInvestTime(investModel.getInvestTime());
        financeReportItemView.setInvestLoginName(investModel.getLoginName());
        financeReportItemView.setInvestRealName(userModel.getUserName());
        financeReportItemView.setReferrer("referrer");
        financeReportItemView.setInvestAmount(investModel.getAmount());
        financeReportItemView.setRepayTime(investRepayModel.getRepayDate());
        financeReportItemView.setPeriod(investRepayModel.getPeriod());
        financeReportItemView.setActualInterest(investRepayModel.getActualInterest());
        financeReportItemView.setActualInterest(investRepayModel.getActualFee());
        financeReportItemView.setActualRepayAmount(investRepayModel.getRepayAmount());

        return financeReportItemView;
    }

    private List<FinanceReportItemView> prepareData() {
        UserModel referrerUserModel = createUserModel("referrer", null);
        BankAccountModel referrerAccountModel = createAccountModel(referrerUserModel);

        UserModel loanerUserModel = createUserModel("loaner", null);
        BankAccountModel loanerAccountModel = createAccountModel(loanerUserModel);

        UserModel agentUserModel = createUserModel("agent", null);
        BankAccountModel agentAccountModel = createAccountModel(agentUserModel);

        UserModel investorWithReferrerUserModel = createUserModel("investorWithReferrer", "referrer");
        BankAccountModel investorWithReferrerAccountModel = createAccountModel(investorWithReferrerUserModel);

        UserModel investorWithoutReferrerUserModel = createUserModel("investorWithoutReferrer", null);
        BankAccountModel investorWithoutReferrerAccountModel = createAccountModel(investorWithoutReferrerUserModel);

        List<FinanceReportItemView> financeReportItemViews = new ArrayList<>();

        LoanModel loanModel = createLoanModel(1000, "房产", agentUserModel, loanerAccountModel, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, LoanStatus.COMPLETE);
        InvestModel investModel = createInvestModel(1000, loanModel.getId(), investorWithReferrerAccountModel, DateTime.parse("2016-10-12T01:20").toDate());
        InvestRepayModel investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 2, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 3, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investModel = createInvestModel(2, loanModel.getId(), investorWithoutReferrerAccountModel, DateTime.parse("2016-11-12T01:20").toDate());
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));

        loanModel = createLoanModel(2, "车辆", agentUserModel, loanerAccountModel, LoanType.LOAN_INTEREST_LUMP_SUM_REPAY, LoanStatus.COMPLETE);
        investModel = createInvestModel(3, loanModel.getId(), investorWithReferrerAccountModel, DateTime.parse("2016-10-12T01:20").toDate());
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 2, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 3, RepayStatus.REPAYING);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investModel = createInvestModel(4, loanModel.getId(), investorWithoutReferrerAccountModel, DateTime.parse("2016-11-12T01:20").toDate());
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));

        return financeReportItemViews;
    }

    @Test
    public void testFindFinanceReportViews() throws Exception {
        List<FinanceReportItemView> originalFinanceReportItemViewList = prepareData();
        List<FinanceReportItemView> financeReportItemViews = financeReportMapper.findFinanceReportViews(null, null, null, null, null, null, 0, 100);
        assertEquals(originalFinanceReportItemViewList.size(), financeReportItemViews.size());

        financeReportItemViews = financeReportMapper.findFinanceReportViews(1000L, null, null, null, null, null, 0, 100);
        assertEquals(4, financeReportItemViews.size());
        for (FinanceReportItemView financeReportItemView : financeReportItemViews) {
            assertEquals(1000L, financeReportItemView.getLoanId());
            if (financeReportItemView.getInvestLoginName().equals("investorWithReferrer")) {
                assertEquals(1000, financeReportItemView.getInvestId());
            } else if (financeReportItemView.getInvestLoginName().equals("investorWithoutReferrer")) {
                assertEquals(2, financeReportItemView.getInvestId());
            }
        }

        financeReportItemViews = financeReportMapper.findFinanceReportViews(null, 2, null, null, null, null, 0, 100);
        assertEquals(2, financeReportItemViews.size());
        for (FinanceReportItemView financeReportItemView : financeReportItemViews) {
            assertEquals(2, financeReportItemView.getPeriod());
        }
        financeReportItemViews = financeReportMapper.findFinanceReportViews(null, 1, null, null, null, null, 0, 100);
        assertEquals(4, financeReportItemViews.size());
        for (FinanceReportItemView financeReportItemView : financeReportItemViews) {
            assertEquals(1, financeReportItemView.getPeriod());
        }

        financeReportItemViews = financeReportMapper.findFinanceReportViews(null, null, "investorWithReferrer", null, null, null, 0, 100);
        assertEquals(6, financeReportItemViews.size());
        for (FinanceReportItemView financeReportItemView : financeReportItemViews) {
            assertEquals("investorWithReferrer", financeReportItemView.getInvestLoginName());
        }

        financeReportItemViews = financeReportMapper.findFinanceReportViews(null, null, null, DateTime.parse("2016-11-12T00:20").toDate(), null, null, 0, 100);
        assertEquals(2, financeReportItemViews.size());
        for (FinanceReportItemView financeReportItemView : financeReportItemViews) {
            assertTrue(financeReportItemView.getInvestTime().after(DateTime.parse("2016-11-12T00:20").toDate()));
        }

        financeReportItemViews = financeReportMapper.findFinanceReportViews(null, null, null, null, DateTime.parse("2016-10-12T02:20").toDate(), null, 0, 100);
        assertEquals(6, financeReportItemViews.size());
        for (FinanceReportItemView financeReportItemView : financeReportItemViews) {
            assertTrue(financeReportItemView.getInvestTime().before(DateTime.parse("2016-10-12T02:20").toDate()));
        }

        financeReportItemViews = financeReportMapper.findFinanceReportViews(null, null, null, DateTime.parse("2016-10-12T00:20").toDate(), DateTime.parse("2016-11-12T02:20").toDate(), null, 0, 100);
        assertEquals(8, financeReportItemViews.size());
        for (FinanceReportItemView financeReportItemView : financeReportItemViews) {
            assertTrue(financeReportItemView.getInvestTime().after(DateTime.parse("2016-10-12T00:20").toDate()));
            assertTrue(financeReportItemView.getInvestTime().before(DateTime.parse("2016-11-12T02:20").toDate()));
        }

        financeReportItemViews = financeReportMapper.findFinanceReportViews(1000L, 1, "investorWithReferrer", null, null, null, 0, 100);
        assertEquals(1, financeReportItemViews.size());
        assertEquals(1000L, financeReportItemViews.get(0).getLoanId());
        assertEquals(1, financeReportItemViews.get(0).getPeriod());
        assertEquals("investorWithReferrer", financeReportItemViews.get(0).getInvestLoginName());

        financeReportItemViews = financeReportMapper.findFinanceReportViews(1000L, 1, "investorWithReferrer", DateTime.parse("2016-11-12T00:20").toDate(), null, null, 0, 100);
        assertEquals(0, financeReportItemViews.size());
        financeReportItemViews = financeReportMapper.findFinanceReportViews(1000L, 5, null, null, null, null, 0, 100);
        assertEquals(0, financeReportItemViews.size());
        financeReportItemViews = financeReportMapper.findFinanceReportViews(3L, null, "user", null, null, null, 0, 100);
        assertEquals(0, financeReportItemViews.size());
        financeReportItemViews = financeReportMapper.findFinanceReportViews(1000L, 2, "investorWithoutReferrer", null, null, null, 0, 100);
        assertEquals(0, financeReportItemViews.size());
    }

    @Test
    public void testFindCountFinanceReportViews() throws Exception {
        List<FinanceReportItemView> originalFinanceReportItemViewList = prepareData();
        assertEquals(originalFinanceReportItemViewList.size(), financeReportMapper.findCountFinanceReportViews(null, null, null, null, null, null));
        assertEquals(4, financeReportMapper.findCountFinanceReportViews(1000L, null, null, null, null, null));
        assertEquals(2, financeReportMapper.findCountFinanceReportViews(null, 2, null, null, null, null));
        assertEquals(4, financeReportMapper.findCountFinanceReportViews(null, 1, null, null, null, null));
        assertEquals(6, financeReportMapper.findCountFinanceReportViews(null, null, "investorWithReferrer", null, null, null));
        assertEquals(2, financeReportMapper.findCountFinanceReportViews(null, null, null, DateTime.parse("2016-11-12T00:20").toDate(), null, null));
        assertEquals(6, financeReportMapper.findCountFinanceReportViews(null, null, null, null, DateTime.parse("2016-10-12T02:20").toDate(), null));
        assertEquals(8, financeReportMapper.findCountFinanceReportViews(null, null, null, DateTime.parse("2016-10-12T00:20").toDate(), DateTime.parse("2016-11-12T02:20").toDate(), null));
        assertEquals(1, financeReportMapper.findCountFinanceReportViews(1000L, 1, "investorWithReferrer", null, null, null));

        assertEquals(0, financeReportMapper.findCountFinanceReportViews(1000L, 1, "investorWithReferrer", DateTime.parse("2016-11-12T00:20").toDate(), null, null));
        assertEquals(0, financeReportMapper.findCountFinanceReportViews(1000L, 5, null, null, null, null));
        assertEquals(0, financeReportMapper.findCountFinanceReportViews(3L, null, "user", null, null, null));
        assertEquals(0, financeReportMapper.findCountFinanceReportViews(1000L, 2, "investorWithoutReferrer", null, null, null));
    }
}
