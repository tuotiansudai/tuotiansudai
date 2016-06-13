package com.tuotiansudai.membership.service;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipType;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserMembershipServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldEvaluateWhenLoginNameIsNotExist() throws Exception {
        assertNull(userMembershipEvaluator.evaluate("loginNameIsNotExist"));
    }

    @Test
    public void shouldEvaluateWhenUserMembershipIsOnlyOne() throws Exception {
        UserModel fakeUser = this.getFakeUser("level0User");

        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 1, new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);

        userMembershipMapper.create(userMembershipModel);

        assertThat(userMembershipEvaluator.evaluate(fakeUser.getLoginName()).getLevel(), is(0));
    }

    @Test
    public void shouldEvaluateWhenUserMembershipIsMoreThanOne() throws Exception {
        UserModel fakeUser = this.getFakeUser("fakeUser");

        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), 5, new DateTime().minusDays(10).toDate(), UserMembershipType.UPGRADE);
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), 3, new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);
        UserMembershipModel userMembershipModel3 = new UserMembershipModel(fakeUser.getLoginName(), 4, new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);

        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);
        userMembershipMapper.create(userMembershipModel3);

        assertThat(userMembershipEvaluator.evaluate(fakeUser.getLoginName()).getLevel(), is(3));
    }


    @Test
    public void shouldGetMembershipByLevel(){
        MembershipModel membershipModel = userMembershipService.getMembershipByLevel(createMembership(1).getLevel());

        assertThat(membershipModel.getLevel(), is(0));
        assertThat(membershipModel.getFee(), is(0.1));
        assertThat(membershipModel.getExperience(), is(0L));
    }

    @Test
    public void shouldGetProgressBarPercentByLoginNameWhenLevelEqualsV0(){
        AccountModel accountModel = createAccount(4000);
        MembershipModel membershipModel = createMembership(1);
        UserMembershipModel userMembershipModel = createUserMembership(membershipModel.getId());
        int process = userMembershipService.getProgressBarPercent(accountModel.getLoginName());
        assertThat(membershipModel.getLevel(), is(0));
        assertThat(process, is(16));
    }

    @Test
    public void shouldGetProgressBarPercentByLoginNameWhenLevelMoreThanV5(){
        AccountModel accountModel = createAccount(6000000);
        MembershipModel membershipModel = createMembership(6);
        UserMembershipModel userMembershipModel = createUserMembership(membershipModel.getId());
        int process = userMembershipService.getProgressBarPercent(accountModel.getLoginName());

        assertThat(membershipModel.getLevel(), is(5));
        assertThat(process, is(100));
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

    private UserMembershipModel createUserMembership(long membershipId){
        UserMembershipModel userMembershipModel = new UserMembershipModel("testuser", membershipId, new DateTime().plusDays(130).toDate() , UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel);
        return userMembershipModel;
    }
    private AccountModel createAccount(long membershipPoint){
        AccountModel accountModel = new AccountModel(getFakeUser("testuser").getLoginName(), "username", "", "", "", new Date());
        accountModel.setMembershipPoint(membershipPoint);
        accountMapper.create(accountModel);
        return accountModel;
    }

    @Test
    public void shouldReceiveMembershipIsEqualsNotToTheTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ReflectionTestUtils.setField(userMembershipService, "membershipStartDate" ,sdf.parse("2016-07-01"));
        UserModel fakeUser = getFakeUser("testReceive");
        MembershipType membershipType = userMembershipService.receiveMembership(fakeUser.getLoginName());
        assertThat(MembershipType.NOT_TO_THE_TIME,is(membershipType));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsNotToLogin(){
        MembershipType membershipType = userMembershipService.receiveMembership("");
        assertThat(MembershipType.NOT_TO_LOGIN,is(membershipType));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsNotToRegister() throws ParseException {
        ReflectionTestUtils.setField(userMembershipService, "membershipStartDate" ,new Date());
        UserModel fakeUser = getFakeUser("testReceive");
        MembershipType membershipType = userMembershipService.receiveMembership(fakeUser.getLoginName());
        assertThat(MembershipType.NOT_TO_REGISTER,is(membershipType));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsAlreadyReceive(){
        ReflectionTestUtils.setField(userMembershipService, "membershipStartDate" ,new Date());
        UserModel fakeUser = getFakeUser("testReceive");
        accountMapper.create(new AccountModel(fakeUser.getLoginName(), "username", "11234", "", "", new Date()));
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), createMembership(1).getId(), new DateTime().plusDays(130).toDate() , UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel);
        MembershipType membershipType = userMembershipService.receiveMembership(fakeUser.getLoginName());
        assertThat(MembershipType.ALREADY_RECEIVE,is(membershipType));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsAlreadyRegisterNotInvest1000(){
        ReflectionTestUtils.setField(userMembershipService, "membershipStartDate" ,new Date());
        UserModel fakeUser = getFakeUser("testReceive");
        long loanId = idGenerator.generate();
        createLoanByUserId(fakeUser.getLoginName(),loanId);
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 100, fakeUser.getLoginName(), new Date(), Source.WEB, null,0);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        accountMapper.create(new AccountModel(fakeUser.getLoginName(), "username", "11234", "", "", DateUtils.addDays(new Date(),-1)));
        MembershipType membershipType = userMembershipService.receiveMembership(fakeUser.getLoginName());
        assertThat(MembershipType.ALREADY_REGISTER_NOT_INVEST_1000,is(membershipType));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsAlreadyRegisterAlreadyInvest1000(){
        ReflectionTestUtils.setField(userMembershipService, "membershipStartDate" ,new Date());
        UserModel fakeUser = getFakeUser("testReceive");
        long loanId = idGenerator.generate();
        createLoanByUserId(fakeUser.getLoginName(),loanId);
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 10000, fakeUser.getLoginName(), new Date(), Source.WEB, null,0);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        accountMapper.create(new AccountModel(fakeUser.getLoginName(), "username", "11234", "", "", DateUtils.addDays(new Date(),-1)));
        MembershipType membershipType = userMembershipService.receiveMembership(fakeUser.getLoginName());
        assertThat(MembershipType.ALREADY_REGISTER_ALREADY_INVEST_1000,is(membershipType));
    }

    @Test
    public void shouldReceiveMembershipIsEqualsAlreadyStartActivityRegister(){
        ReflectionTestUtils.setField(userMembershipService, "membershipStartDate" ,new Date());
        UserModel fakeUser = getFakeUser("testReceive");
        long loanId = idGenerator.generate();
        createLoanByUserId(fakeUser.getLoginName(),loanId);
        accountMapper.create(new AccountModel(fakeUser.getLoginName(), "username", "11234", "", "", DateUtils.addDays(new Date(),+1)));
        MembershipType membershipType = userMembershipService.receiveMembership(fakeUser.getLoginName());
        assertThat(MembershipType.AFTER_START_ACTIVITY_REGISTER,is(membershipType));
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
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
    }

}
