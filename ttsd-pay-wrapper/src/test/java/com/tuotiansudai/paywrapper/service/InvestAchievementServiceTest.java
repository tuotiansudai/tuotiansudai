package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
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
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestAchievementServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestAchievementService investAchievementService;

    @Test
    public void shouldAwardFirstInvestAchievement() throws Exception {
        LoanModel fakeLoan = getFakeLoan(getFakeUser("loaner"));

        UserModel investor = getFakeUser("investor");
        InvestModel investModel = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, 1, investor.getLoginName(), null, Source.WEB, null);
        investModel.setTradingTime(new Date());
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);

        investAchievementService.awardAchievement(investModel);

        InvestModel actualInvestModel = investMapper.findById(investModel.getId());

        assertTrue(actualInvestModel.getAchievements().contains(InvestAchievement.FIRST_INVEST));
        assertTrue(actualInvestModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT));

        LoanModel actualLoanModel = loanMapper.findById(fakeLoan.getId());
        assertThat(actualLoanModel.getFirstInvestAchievementId(), is(investModel.getId()));
        assertThat(actualLoanModel.getMaxAmountAchievementId(), is(investModel.getId()));
        assertNull(actualLoanModel.getLastInvestAchievementId());

    }

    @Test
    public void shouldNotAwardFirstInvestAchievementWhenInvestIsNotFirstInvest() throws Exception {
        LoanModel fakeLoan = getFakeLoan(getFakeUser("loaner"));

        UserModel investor = getFakeUser("investor");
        InvestModel investModel1 = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, 1, investor.getLoginName(), null, Source.WEB, null);
        investModel1.setTradingTime(new Date());
        investModel1.setStatus(InvestStatus.SUCCESS);
        investModel1.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST));
        investMapper.create(investModel1);
        InvestModel investModel2 = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, 1, investor.getLoginName(), null, Source.WEB, null);
        investModel2.setTradingTime(new Date());
        investModel2.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel2);

        investAchievementService.awardAchievement(investModel2);

        InvestModel actualInvestModel2 = investMapper.findById(investModel2.getId());

        assertFalse(actualInvestModel2.getAchievements().contains(InvestAchievement.FIRST_INVEST));
    }

    @Test
    public void shouldNotAwardFirstInvestAchievementWhenInvestHasMoreThanThreeTimesFirstInvestOnThisMonth() throws Exception {
        UserModel loaner = getFakeUser("loaner");
        LoanModel fakeLoan1 = getFakeLoan(loaner);
        LoanModel fakeLoan2 = getFakeLoan(loaner);
        LoanModel fakeLoan3 = getFakeLoan(loaner);
        LoanModel fakeLoan4 = getFakeLoan(loaner);

        UserModel investor1 = getFakeUser("investor1");
        InvestModel loan1InvestModel = new InvestModel(idGenerator.generate(), fakeLoan1.getId(), null, 1, investor1.getLoginName(), null, Source.WEB, null);
        loan1InvestModel.setTradingTime(new DateTime().dayOfMonth().withMinimumValue().plusHours(1).toDate());
        loan1InvestModel.setStatus(InvestStatus.SUCCESS);
        loan1InvestModel.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST));
        investMapper.create(loan1InvestModel);

        InvestModel loan2InvestModel = new InvestModel(idGenerator.generate(), fakeLoan2.getId(), null, 1, investor1.getLoginName(), null, Source.WEB, null);
        loan2InvestModel.setTradingTime(new DateTime().dayOfMonth().withMinimumValue().plusHours(2).toDate());
        loan2InvestModel.setStatus(InvestStatus.SUCCESS);
        loan2InvestModel.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST));
        investMapper.create(loan2InvestModel);

        InvestModel loan3InvestModel = new InvestModel(idGenerator.generate(), fakeLoan3.getId(), null, 1, investor1.getLoginName(), null, Source.WEB, null);
        loan3InvestModel.setTradingTime(new DateTime().dayOfMonth().withMinimumValue().plusHours(3).toDate());
        loan3InvestModel.setStatus(InvestStatus.SUCCESS);
        loan3InvestModel.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST));
        investMapper.create(loan3InvestModel);

        InvestModel loan4InvestModel1 = new InvestModel(idGenerator.generate(), fakeLoan4.getId(), null, 1, investor1.getLoginName(), null, Source.WEB, null);
        loan4InvestModel1.setTradingTime(new Date());
        loan4InvestModel1.setStatus(InvestStatus.SUCCESS);
        investMapper.create(loan4InvestModel1);

        investAchievementService.awardAchievement(loan4InvestModel1);

        UserModel investor2 = getFakeUser("investor2");
        InvestModel loan4InvestModel2 = new InvestModel(idGenerator.generate(), fakeLoan4.getId(), null, 1, investor2.getLoginName(), null, Source.WEB, null);
        loan4InvestModel2.setTradingTime(new Date());
        loan4InvestModel2.setStatus(InvestStatus.SUCCESS);
        investMapper.create(loan4InvestModel2);

        investAchievementService.awardAchievement(loan4InvestModel2);

        InvestModel actualInvest4Model1 = investMapper.findById(loan4InvestModel1.getId());
        assertFalse(actualInvest4Model1.getAchievements().contains(InvestAchievement.FIRST_INVEST));

        InvestModel actualInvest4Model2 = investMapper.findById(loan4InvestModel2.getId());
        assertTrue(actualInvest4Model2.getAchievements().contains(InvestAchievement.FIRST_INVEST));

        LoanModel actualLoanModel = loanMapper.findById(fakeLoan4.getId());
        assertThat(actualLoanModel.getFirstInvestAchievementId(), is(loan4InvestModel2.getId()));
    }

    @Test
    public void shouldAwardMaxAmountAchievement() throws Exception {
        LoanModel fakeLoan = getFakeLoan(getFakeUser("loaner"));

        UserModel investor1 = getFakeUser("investor1");
        InvestModel investModel1 = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, 1, investor1.getLoginName(), null, Source.WEB, null);
        investModel1.setTradingTime(new Date());
        investModel1.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel1);
        investAchievementService.awardAchievement(investModel1);

        UserModel investor2 = getFakeUser("investor2");
        InvestModel investModel2 = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, 2, investor2.getLoginName(), null, Source.WEB, null);
        investModel2.setTradingTime(new Date());
        investModel2.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel2);
        investAchievementService.awardAchievement(investModel2);

        InvestModel actualInvestModel1 = investMapper.findById(investModel1.getId());
        assertFalse(actualInvestModel1.getAchievements().contains(InvestAchievement.MAX_AMOUNT));

        InvestModel actualInvestModel2 = investMapper.findById(investModel2.getId());
        assertTrue(actualInvestModel2.getAchievements().contains(InvestAchievement.MAX_AMOUNT));

        LoanModel actualLoanModel = loanMapper.findById(fakeLoan.getId());
        assertThat(actualLoanModel.getMaxAmountAchievementId(), is(investModel2.getId()));
    }

    @Test
    public void shouldAwardMaxAmountAchievementWhenEarlierInvest() throws Exception {
        LoanModel fakeLoan = getFakeLoan(getFakeUser("loaner"));

        UserModel investor1 = getFakeUser("investor1");
        InvestModel investModel1 = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, 1, investor1.getLoginName(), null, Source.WEB, null);
        investModel1.setTradingTime(new Date());
        investModel1.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel1);
        investAchievementService.awardAchievement(investModel1);

        UserModel investor2 = getFakeUser("investor2");
        InvestModel investModel2 = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, 1, investor2.getLoginName(), null, Source.WEB, null);
        investModel2.setTradingTime(new Date());
        investModel2.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel2);
        investAchievementService.awardAchievement(investModel2);

        InvestModel actualInvestModel1 = investMapper.findById(investModel1.getId());
        assertTrue(actualInvestModel1.getAchievements().contains(InvestAchievement.MAX_AMOUNT));

        InvestModel actualInvestModel2 = investMapper.findById(investModel2.getId());
        assertFalse(actualInvestModel2.getAchievements().contains(InvestAchievement.MAX_AMOUNT));

        LoanModel actualLoanModel = loanMapper.findById(fakeLoan.getId());
        assertThat(actualLoanModel.getMaxAmountAchievementId(), is(investModel1.getId()));
    }

    @Test
    public void shouldAwardLastInvestAchievement() throws Exception {
        LoanModel fakeLoan = getFakeLoan(getFakeUser("loaner"));

        UserModel investor = getFakeUser("investor");
        InvestModel investModel1 = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, fakeLoan.getLoanAmount() - 1, investor.getLoginName(), null, Source.WEB, null);
        investModel1.setTradingTime(new Date());
        investModel1.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel1);
        investAchievementService.awardAchievement(investModel1);

        InvestModel investModel2 = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, 1, investor.getLoginName(), null, Source.WEB, null);
        investModel2.setTradingTime(new Date());
        investModel2.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel2);
        investAchievementService.awardAchievement(investModel2);

        InvestModel actualInvestModel1 = investMapper.findById(investModel1.getId());
        assertFalse(actualInvestModel1.getAchievements().contains(InvestAchievement.LAST_INVEST));

        InvestModel actualInvestModel2 = investMapper.findById(investModel2.getId());
        assertTrue(actualInvestModel2.getAchievements().contains(InvestAchievement.LAST_INVEST));

        LoanModel actualLoanModel = loanMapper.findById(fakeLoan.getId());
        assertThat(actualLoanModel.getLastInvestAchievementId(), is(investModel2.getId()));
    }

    @Test
    public void shouldAwardThreeInvestAchievement() throws Exception {
        LoanModel fakeLoan = getFakeLoan(getFakeUser("loaner"));

        UserModel investor = getFakeUser("investor");
        InvestModel investModel = new InvestModel(idGenerator.generate(), fakeLoan.getId(), null, fakeLoan.getLoanAmount(), investor.getLoginName(), null, Source.WEB, null);
        investModel.setTradingTime(new Date());
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);
        investAchievementService.awardAchievement(investModel);

        InvestModel actualInvestModel = investMapper.findById(investModel.getId());
        assertTrue(actualInvestModel.getAchievements().contains(InvestAchievement.FIRST_INVEST));
        assertTrue(actualInvestModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT));
        assertTrue(actualInvestModel.getAchievements().contains(InvestAchievement.LAST_INVEST));

        LoanModel actualLoanModel = loanMapper.findById(fakeLoan.getId());
        assertThat(actualLoanModel.getFirstInvestAchievementId(), is(investModel.getId()));
        assertThat(actualLoanModel.getMaxAmountAchievementId(), is(investModel.getId()));
        assertThat(actualLoanModel.getLastInvestAchievementId(), is(investModel.getId()));
    }

    protected UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setMobile(loginName);
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        return fakeUser;
    }

    protected LoanModel getFakeLoan(UserModel loaner) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanAmount(10000);
        fakeLoanModel.setLoanerLoginName(loaner.getLoginName());
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("id");
        fakeLoanModel.setAgentLoginName(loaner.getLoginName());
        fakeLoanModel.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(1);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setBaseRate(0.1);
        fakeLoanModel.setActivityRate(0);
        fakeLoanModel.setInvestFeeRate(0.1);
        fakeLoanModel.setFundraisingStartTime(new DateTime().plusDays(10).toDate());
        fakeLoanModel.setFundraisingEndTime(new DateTime().plusDays(10).toDate());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(new Date());
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }
}
