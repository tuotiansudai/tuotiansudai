package com.tuotiansudai.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class HeroRankingServiceTest {
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private HeroRankingService heroRankingService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private FakeUserHelper userMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Ignore
    public void shouldFindHeroRankingByReferrer() {
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new DateTime(2016, 7, 1, 0, 0, 0).toDate()));
        date.add(sdf.format(new DateTime(2016, 7, 31, 23, 59, 59).toDate()));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod", date);
        UserModel loaner = createUserByUserId("loaner");
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");

        AccountModel accountModel1 = new AccountModel(investor1.getLoginName(), "payUserId1", "payAccountId1", new Date());
        accountMapper.create(accountModel1);
        AccountModel accountModel2 = new AccountModel(investor2.getLoginName(), "payUserId2", "payAccountId2", new Date());
        accountMapper.create(accountModel2);
        AccountModel accountModel3 = new AccountModel(investor3.getLoginName(), "payUserId3", "payAccountId3", new Date());
        accountMapper.create(accountModel3);

        ReferrerRelationModel referrerRelationModel1 = new ReferrerRelationModel();
        referrerRelationModel1.setLevel(1);
        referrerRelationModel1.setReferrerLoginName(investor1.getLoginName());
        referrerRelationModel1.setLoginName(investor2.getLoginName());
        referrerRelationMapper.create(referrerRelationModel1);

        ReferrerRelationModel referrerRelationModel2 = new ReferrerRelationModel();
        referrerRelationModel2.setLevel(1);
        referrerRelationModel2.setReferrerLoginName(investor1.getLoginName());
        referrerRelationModel2.setLoginName(investor3.getLoginName());
        referrerRelationMapper.create(referrerRelationModel2);

        LoanModel loanModel = createLoan(loaner.getLoginName(), IdGenerator.generate(), ActivityType.NORMAL, LoanStatus.REPAYING);
        loanMapper.create(loanModel);

        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName(), loanModel.getId());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName(), loanModel.getId());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel3);

        BasePaginationDataDto<HeroRankingView> baseListDataDto = heroRankingService.findHeroRankingByReferrer(new DateTime(2016, 7, 5, 0, 0, 0).toDate(), investor2.getLoginName(), 1, 10);

        assertThat(baseListDataDto.getRecords().get(0).getSumAmount(), is(4000l));
    }

    @Ignore
    public void shouldObtainHeroRankingIsSuccess() {
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new DateTime(2016, 7, 1, 0, 0, 0).toDate()));
        date.add(sdf.format(new DateTime(2016, 7, 31, 23, 59, 59).toDate()));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod", date);
        UserModel loaner = createUserByUserId("loaner");
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");
        AccountModel accountModel1 = new AccountModel(investor1.getLoginName(), "payUserId1", "payAccountId1", new Date());
        accountMapper.create(accountModel1);
        AccountModel accountModel2 = new AccountModel(investor2.getLoginName(), "payUserId2", "payAccountId2", new Date());
        accountMapper.create(accountModel2);
        AccountModel accountModel3 = new AccountModel(investor3.getLoginName(), "payUserId3", "payAccountId3", new Date());
        accountMapper.create(accountModel3);

        LoanModel loanModel = createLoan(loaner.getLoginName(), IdGenerator.generate(), ActivityType.NORMAL, LoanStatus.REPAYING);
        loanMapper.create(loanModel);

        InvestModel investModel1 = this.getFakeInvestModelByLoginName(investor1.getLoginName(), loanModel.getId());
        investModel1.setAmount(2000);
        investModel1.setTradingTime(new DateTime("2016-07-07").toDate());
        investMapper.create(investModel1);
        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName(), loanModel.getId());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-07").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName(), loanModel.getId());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-07").toDate());
        investMapper.create(investModel3);
        List<HeroRankingView> heroRankingViews = heroRankingService.obtainHeroRanking(ActivityCategory.HERO_RANKING, new DateTime(2016, 7, 7, 0, 0, 0).toDate());

        assertEquals(3, heroRankingViews.size());
        assertEquals(investModel3.getLoginName(), heroRankingViews.get(0).getLoginName());
        assertEquals(investModel3.getAmount(), heroRankingViews.get(0).getSumAmount());
        assertEquals(investor3.getUserName(), heroRankingViews.get(0).getUserName());
        assertEquals(investor3.getMobile(), heroRankingViews.get(0).getMobile());

        assertEquals(investModel1.getLoginName(), heroRankingViews.get(1).getLoginName());
        assertEquals(investModel1.getAmount(), heroRankingViews.get(1).getSumAmount());
        assertEquals(investor1.getUserName(), heroRankingViews.get(1).getUserName());
        assertEquals(investor1.getMobile(), heroRankingViews.get(1).getMobile());

        assertEquals(investModel2.getLoginName(), heroRankingViews.get(2).getLoginName());
        assertEquals(investModel2.getAmount(), heroRankingViews.get(2).getSumAmount());
        assertEquals(investor2.getUserName(), heroRankingViews.get(2).getUserName());
        assertEquals(investor2.getMobile(), heroRankingViews.get(2).getMobile());

    }

    @Test
    public void shouldValidActivityTimeIsNotNullIsOk() {
        List<String> activityTime = heroRankingService.getActivityTime();
        assertTrue(activityTime.size() > 1);
    }

    private LoanModel createLoan(String userId, long loanId, ActivityType activityType, LoanStatus loanStatus) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(activityType);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanStatus);
        return loanModel;
    }

    private UserModel createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private InvestModel getFakeInvestModelByLoginName(String loginName, long loanId) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, loginName, 1000l, 0.1, false, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
    }
}
