package com.tuotiansudai.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseListDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-security.xml"})
@WebAppConfiguration
@Transactional
public class HeroRankingServiceTest {

    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private HeroRankingService heroRankingService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private RandomUtils randomUtils;

    private final static String MYSTERIOUSREDISKEY = "console:mysteriousPrize";

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;
    

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void shouldFindHeroRankingByReferrer() {
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new DateTime(2016, 7, 1, 0, 0, 0).toDate()));
        date.add(sdf.format(new DateTime(2016, 7, 31, 23, 59, 59).toDate()));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod", date);
        UserModel loaner = createUserByUserId("loaner");
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");

        AccountModel accountModel1 = new AccountModel(investor1.getLoginName(), "userName1", "identityNumber1", "payUserId1", "payAccountId1", new Date());
        accountMapper.create(accountModel1);
        AccountModel accountModel2 = new AccountModel(investor2.getLoginName(), "userName2", "identityNumber2", "payUserId2", "payAccountId2", new Date());
        accountMapper.create(accountModel2);
        AccountModel accountModel3 = new AccountModel(investor3.getLoginName(), "userName3", "identityNumber3", "payUserId3", "payAccountId3", new Date());
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

        LoanModel loanModel = createLoan(loaner.getLoginName(),idGenerator.generate() , ActivityType.NORMAL,LoanStatus.REPAYING);
        loanMapper.create(loanModel);

        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName(),loanModel.getId());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName(),loanModel.getId());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel3);

        BaseListDataDto<HeroRankingView> baseListDataDto = heroRankingService.findHeroRankingByReferrer(new DateTime(2016, 7, 5, 0, 0, 0).toDate(), investor2.getLoginName(), 1, 10);

        assertThat(baseListDataDto.getRecords().get(0).getSumAmount(), is(4000l));
    }

    @Test
    public void shouldObtainHeroRankingIsSuccess(){
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new DateTime(2016, 7, 1, 0, 0, 0).toDate()));
        date.add(sdf.format(new DateTime(2016, 7, 31, 23, 59, 59).toDate()));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod", date);
        UserModel loaner = createUserByUserId("loaner");
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");
        AccountModel accountModel1 = new AccountModel(investor1.getLoginName(), "userName1", "identityNumber1", "payUserId1", "payAccountId1", new Date());
        accountMapper.create(accountModel1);
        AccountModel accountModel2 = new AccountModel(investor2.getLoginName(), "userName2", "identityNumber2", "payUserId2", "payAccountId2", new Date());
        accountMapper.create(accountModel2);
        AccountModel accountModel3 = new AccountModel(investor3.getLoginName(), "userName3", "identityNumber3", "payUserId3", "payAccountId3", new Date());
        accountMapper.create(accountModel3);

        LoanModel loanModel = createLoan(loaner.getLoginName(),idGenerator.generate() , ActivityType.NORMAL,LoanStatus.REPAYING);
        loanMapper.create(loanModel);

        InvestModel investModel1 = this.getFakeInvestModelByLoginName(investor1.getLoginName(),loanModel.getId());
        investModel1.setAmount(2000);
        investModel1.setTradingTime(new DateTime("2016-07-07").toDate());
        investMapper.create(investModel1);
        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName(),loanModel.getId());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-07").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName(),loanModel.getId());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-07").toDate());
        investMapper.create(investModel3);
        List<HeroRankingView> heroRankingViews = heroRankingService.obtainHeroRanking(new DateTime(2016, 7, 7, 0, 0, 0).toDate());

        assertEquals(3,heroRankingViews.size());
        assertEquals(investModel3.getLoginName(), heroRankingViews.get(0).getLoginName());
        assertEquals(investModel3.getAmount(), heroRankingViews.get(0).getSumAmount());
        assertEquals(accountModel3.getUserName(), heroRankingViews.get(0).getUserName());
        assertEquals(investor3.getMobile(),heroRankingViews.get(0).getMobile());

        assertEquals(investModel1.getLoginName(),heroRankingViews.get(1).getLoginName());
        assertEquals(investModel1.getAmount(),heroRankingViews.get(1).getSumAmount());
        assertEquals(accountModel1.getUserName(),heroRankingViews.get(1).getUserName());
        assertEquals(investor1.getMobile(),heroRankingViews.get(1).getMobile());

        assertEquals(investModel2.getLoginName(),heroRankingViews.get(2).getLoginName());
        assertEquals(investModel2.getAmount(),heroRankingViews.get(2).getSumAmount());
        assertEquals(accountModel2.getUserName(),heroRankingViews.get(2).getUserName());
        assertEquals(investor2.getMobile(),heroRankingViews.get(2).getMobile());

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

    private InvestModel getFakeInvestModelByLoginName(String loginName,long loanId){
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1000l, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null,0.1);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
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

    @Test
    public void shouldObtainHeroRankingByLoginNameIsSuccess(){
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new DateTime(2016, 7, 1, 0, 0, 0).toDate()));
        date.add(sdf.format(new DateTime(2016, 7, 31, 23, 59, 59).toDate()));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod", date);
        UserModel loaner = createUserByUserId("loaner");
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");
        AccountModel accountModel1 = new AccountModel(investor1.getLoginName(), "userName1", "identityNumber1", "payUserId1", "payAccountId1", new Date());
        accountMapper.create(accountModel1);
        AccountModel accountModel2 = new AccountModel(investor2.getLoginName(), "userName2", "identityNumber2", "payUserId2", "payAccountId2", new Date());
        accountMapper.create(accountModel2);
        AccountModel accountModel3 = new AccountModel(investor3.getLoginName(), "userName3", "identityNumber3", "payUserId3", "payAccountId3", new Date());
        accountMapper.create(accountModel3);

        LoanModel loanModel = createLoan(loaner.getLoginName(), idGenerator.generate(), ActivityType.NORMAL, LoanStatus.REPAYING);
        loanMapper.create(loanModel);

        InvestModel investModel1 = this.getFakeInvestModelByLoginName(investor1.getLoginName(),loanModel.getId());
        investModel1.setAmount(2000);
        investModel1.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel1);
        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName(),loanModel.getId());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName(),loanModel.getId());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel3);
        Integer ranking1 = heroRankingService.obtainHeroRankingByLoginName(new DateTime(2016,7,5,23,59,59).toDate(),investModel3.getLoginName());

        assertEquals(1, ranking1.intValue());
        Integer ranking2 = heroRankingService.obtainHeroRankingByLoginName(new DateTime(2016,7,5,23,59,59).toDate(),investModel1.getLoginName());

        assertEquals(2, ranking2.intValue());

        Integer ranking3 = heroRankingService.obtainHeroRankingByLoginName(new DateTime(2016,7,5,23,59,59).toDate(),investModel2.getLoginName());

        assertEquals(3, ranking3.intValue());
    }

    @Test
    public void shouldSaveMysteriousPrizeIsSuccess(){
        MysteriousPrizeDto mysteriousPrizeDto = new MysteriousPrizeDto();
        String now = new DateTime().withTimeAtStartOfDay().toString("yyyy-MM-dd");
        mysteriousPrizeDto.setImageUrl("imageUrl");
        mysteriousPrizeDto.setPrizeName("name");
        mysteriousPrizeDto.setPrizeDate(new DateTime().withTimeAtStartOfDay().toDate());
        heroRankingService.saveMysteriousPrize(mysteriousPrizeDto);
        MysteriousPrizeDto mysteriousPrizeDtoReturn = (MysteriousPrizeDto)redisWrapperClient.hgetSeri(MYSTERIOUSREDISKEY,now);
        assertEquals(mysteriousPrizeDto.getImageUrl(),mysteriousPrizeDtoReturn.getImageUrl());
        assertEquals(mysteriousPrizeDto.getPrizeName(),mysteriousPrizeDtoReturn.getPrizeName());
        redisWrapperClient.hdelSeri(MYSTERIOUSREDISKEY,now);


    }

    @Test
    public void shouldReceiveMembershipIsEqualsNoTime() throws ParseException {
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(DateUtils.addDays(new Date(),1)));
        date.add(sdf.format(DateUtils.addMonths(new Date(),1)));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod" ,date);
        UserModel fakeUser = getFakeUser("testReceive");
        GivenMembership GivenMembership = heroRankingService.receiveMembership(fakeUser.getLoginName());
        assertThat(GivenMembership.NO_TIME,is(GivenMembership));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsNoLogin(){
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new Date()));
        date.add(sdf.format(DateUtils.addMonths(new Date(),1)));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod" ,date);
        GivenMembership GivenMembership = heroRankingService.receiveMembership("");
        assertThat(GivenMembership.NO_LOGIN,is(GivenMembership));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsNoRegister() throws ParseException {
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new Date()));
        date.add(sdf.format(DateUtils.addMonths(new Date(),1)));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod" ,date);
        UserModel fakeUser = getFakeUser("testReceive");
        GivenMembership GivenMembership = heroRankingService.receiveMembership(fakeUser.getLoginName());
        assertThat(GivenMembership.NO_REGISTER,is(GivenMembership));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsAlreadyReceived(){
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new Date()));
        date.add(sdf.format(DateUtils.addMonths(new Date(),1)));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod" ,date);
        UserModel fakeUser = getFakeUser("testReceive");
        accountMapper.create(new AccountModel(fakeUser.getLoginName(), "username", "11234", "", "", new Date()));
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), createMembership(1).getId(), new DateTime().plusDays(130).toDate() , UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);
        GivenMembership GivenMembership = heroRankingService.receiveMembership(fakeUser.getLoginName());
        assertThat(GivenMembership.ALREADY_RECEIVED,is(GivenMembership));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsAlreadyRegisterNotInvest1000(){
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new Date()));
        date.add(sdf.format(DateUtils.addMonths(new Date(),1)));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod" ,date);
        UserModel fakeUser = getFakeUser("testReceive");
        long loanId = idGenerator.generate();
        createLoanByUserId(fakeUser.getLoginName(),loanId);
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 100, fakeUser.getLoginName(), new Date(), Source.WEB, null,0);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        accountMapper.create(new AccountModel(fakeUser.getLoginName(), "username", "11234", "", "", DateUtils.addDays(new Date(),-1)));
        GivenMembership GivenMembership = heroRankingService.receiveMembership(fakeUser.getLoginName());
        assertThat(GivenMembership.ALREADY_REGISTER_NOT_INVEST_1000,is(GivenMembership));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsAlreadyRegisterAlreadyInvest1000(){
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new Date()));
        date.add(sdf.format(DateUtils.addMonths(new Date(),1)));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod" ,date);
        UserModel fakeUser = getFakeUser("testReceive");
        long loanId = idGenerator.generate();
        createLoanByUserId(fakeUser.getLoginName(),loanId);
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1000000, fakeUser.getLoginName(), new Date(), Source.WEB, null,0);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        accountMapper.create(new AccountModel(fakeUser.getLoginName(), "username", "11234", "", "", DateUtils.addDays(new Date(),-1)));
        GivenMembership GivenMembership = heroRankingService.receiveMembership(fakeUser.getLoginName());
        UserMembershipModel userMembershipModel = userMembershipMapper.findActiveByLoginName(fakeUser.getLoginName());
        assertThat(GivenMembership.ALREADY_REGISTER_ALREADY_INVEST_1000,is(GivenMembership));
        assertNotNull(userMembershipModel);
    }

    @Test
    public void shouldReceiveMembershipIsEqualsAlreadyStartActivityRegister(){
        List<String> date = Lists.newArrayList();
        date.add(sdf.format(new Date()));
        date.add(sdf.format(DateUtils.addMonths(new Date(),1)));
        ReflectionTestUtils.setField(heroRankingService, "heroRankingActivityPeriod" ,date);
        UserModel fakeUser = getFakeUser("testReceive");
        long loanId = idGenerator.generate();
        createLoanByUserId(fakeUser.getLoginName(),loanId);
        accountMapper.create(new AccountModel(fakeUser.getLoginName(), "username", "11234", "", "", DateUtils.addDays(new Date(),+1)));
        GivenMembership GivenMembership = heroRankingService.receiveMembership(fakeUser.getLoginName());
        List<UserMembershipModel> userMembershipModel = userMembershipMapper.findByLoginName(fakeUser.getLoginName());
        assertThat(GivenMembership.AFTER_START_ACTIVITY_REGISTER,is(GivenMembership));
        assertNotNull(userMembershipModel);
    }

    private void createLoanByUserId(String userId, long loanId) {
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
        loanDto.setActivityType(ActivityType.NORMAL);
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
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
    }

    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("fakeUsr@tuotiansudai.com");
        fakeUser.setMobile("11900000000");
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        return fakeUser;
    }

    private MembershipModel createMembership(int id){
        MembershipModel membershipModel = membershipMapper.findById(id);
        return membershipModel;
    }

}
