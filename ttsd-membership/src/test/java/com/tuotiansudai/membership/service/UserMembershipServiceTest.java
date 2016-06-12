package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
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

        assertThat(membershipModel.getLevel(), is(1));
        assertThat(membershipModel.getFee(), is(0.1));
        assertThat(membershipModel.getExperience(), is(5000L));
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

    private MembershipModel createMembership(int level){
        MembershipModel membershipModel = membershipMapper.findById(level);
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
}
