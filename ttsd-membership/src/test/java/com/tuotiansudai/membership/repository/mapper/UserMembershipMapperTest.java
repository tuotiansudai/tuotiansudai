package com.tuotiansudai.membership.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.membership.repository.model.UserMembershipItemView;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class UserMembershipMapperTest {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldCreateUserMembership() throws Exception {

        UserModel fakeUser = createFakeUser("loginName");
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), UserMembershipType.UPGRADE);

        userMembershipMapper.create(userMembershipModel);

        assertThat(userMembershipModel.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(userMembershipModel.getMembershipId(), is(2L));
        assertThat(userMembershipModel.getType(), is(UserMembershipType.UPGRADE));
    }

    @Test
    public void shouldUserMembershipFindById() throws Exception {

        UserModel fakeUser = createFakeUser("loginName");
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);

        UserMembershipModel membershipModel1 = userMembershipMapper.findById(userMembershipModel.getId());

        assertThat(membershipModel1.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(membershipModel1.getMembershipId(), is(2L));
        assertThat(membershipModel1.getType(), is(UserMembershipType.GIVEN));

    }

    @Test
    public void shouldCountMembershipByLevel() throws Exception {
        long existingLevelZeroCount = userMembershipMapper.countMembershipByLevel(0);
        long existingLevelOneCount = userMembershipMapper.countMembershipByLevel(1);
        long existingLevelTwoCount = userMembershipMapper.countMembershipByLevel(2);

        UserModel user1 = createFakeUser("user1");
        UserMembershipModel userMembershipModel1 = new UserMembershipModel(user1.getLoginName(), membershipMapper.findByLevel(0).getId(), new DateTime().minusDays(10).toDate(), UserMembershipType.UPGRADE);
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(user1.getLoginName(), membershipMapper.findByLevel(1).getId(), new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);
        UserMembershipModel userMembershipModel3 = new UserMembershipModel(user1.getLoginName(), membershipMapper.findByLevel(2).getId(), new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);
        userMembershipMapper.create(userMembershipModel3);

        long levelZeroCount = userMembershipMapper.countMembershipByLevel(0);
        long levelOneCount = userMembershipMapper.countMembershipByLevel(1);
        long levelTwoCount = userMembershipMapper.countMembershipByLevel(2);

        assertThat(levelZeroCount - existingLevelZeroCount, is(0L));
        assertThat(levelOneCount - existingLevelOneCount, is(0L));
        assertThat(levelTwoCount - existingLevelTwoCount, is(1L));
    }

    private UserModel createFakeUser(String loginName) {
        UserModel model = new UserModel();
        model.setLoginName(loginName);
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile("13900000000");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }

    private UserModel createFakeUser(String loginName, String mobile, Date registerTime) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("password");
        userModel.setEmail(loginName + "@email.com");
        userModel.setMobile(mobile);
        userModel.setRegisterTime(registerTime);
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModel);
        return userModel;
    }

    private AccountModel createFakeAccount(UserModel userModel, long membershipPoint) {
        AccountModel accountModel = new AccountModel(userModel.getLoginName(),
                RandomStringUtils.randomNumeric(32),
                RandomStringUtils.randomNumeric(14),
                userModel.getRegisterTime());
        accountModel.setMembershipPoint(membershipPoint);
        accountMapper.create(accountModel);
        return accountModel;
    }

    private UserMembershipModel createUserMembershipModel(String loginName, UserMembershipType userMembershipType, int level) {
        UserMembershipModel userMembershipModel = new UserMembershipModel();
        userMembershipModel.setLoginName(loginName);
        userMembershipModel.setCreatedTime(new Date());
        userMembershipModel.setExpiredTime(DateTime.parse("2099-06-30T01:20").toDate());
        userMembershipModel.setType(userMembershipType);
        userMembershipModel.setMembershipId(membershipMapper.findByLevel(level).getId());
        userMembershipMapper.create(userMembershipModel);
        return userMembershipModel;
    }

    private UserMembershipItemView combineUserMembershipItemModel(UserModel userModel, AccountModel accountModel, UserMembershipModel userMembershipModel) {
        UserMembershipItemView userMembershipItemView = new UserMembershipItemView();
        userMembershipItemView.setLoginName(userModel.getLoginName());
        userMembershipItemView.setMobile(userModel.getMobile());
        userMembershipItemView.setRealName(userModel.getUserName());
        userMembershipItemView.setRegisterTime(userModel.getRegisterTime());
        userMembershipItemView.setUserMembershipType(userMembershipModel.getType());
        userMembershipItemView.setMembershipLevel(membershipMapper.findById(userMembershipModel.getMembershipId()).getLevel());
        userMembershipItemView.setMembershipPoint(accountModel.getMembershipPoint());
        return userMembershipItemView;
    }

    private List<UserMembershipItemView> prepareUserMembershipData() {
        UserModel userModel1 = createFakeUser("fakeUser1", "18612340001", DateTime.parse("2000-06-30T12:30").toDate());
        AccountModel accountModel1 = createFakeAccount(userModel1, 1);
        UserMembershipModel userMembershipModel1 = createUserMembershipModel(userModel1.getLoginName(), UserMembershipType.UPGRADE, 0);

        UserModel userModel2 = createFakeUser("fakeUser2", "18612340002", DateTime.parse("2000-07-30T12:30").toDate());
        AccountModel accountModel2 = createFakeAccount(userModel2, 2);
        UserMembershipModel userMembershipModel2 = createUserMembershipModel(userModel2.getLoginName(), UserMembershipType.GIVEN, 5);

        UserModel userModel3 = createFakeUser("fakeUser3", "18612340003", DateTime.parse("2000-08-30T12:30").toDate());
        AccountModel accountModel3 = createFakeAccount(userModel3, 3);
        UserMembershipModel userMembershipModel3 = createUserMembershipModel(userModel3.getLoginName(), UserMembershipType.UPGRADE, 0);

        List<UserMembershipItemView> userMembershipItemViews = new ArrayList<>();
        userMembershipItemViews.add(combineUserMembershipItemModel(userModel1, accountModel1, userMembershipModel1));
        userMembershipItemViews.add(combineUserMembershipItemModel(userModel2, accountModel2, userMembershipModel2));
        userMembershipItemViews.add(combineUserMembershipItemModel(userModel3, accountModel3, userMembershipModel3));

        return userMembershipItemViews;
    }

    @Test
    public void testFindUserMembershipItemViews() throws Exception {
        List<UserMembershipItemView> originUserMembershipItemViews = prepareUserMembershipData();

        List<UserMembershipItemView> userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), null, null, null, null, null, 0, 10);
        assertEquals(originUserMembershipItemViews.get(0).getLoginName(), userMembershipItemViews.get(0).getLoginName());
        assertEquals(originUserMembershipItemViews.get(0).getMobile(), userMembershipItemViews.get(0).getMobile());
        assertEquals(originUserMembershipItemViews.get(0).getRealName(), userMembershipItemViews.get(0).getRealName());
        assertEquals(originUserMembershipItemViews.get(0).getUserMembershipType(), userMembershipItemViews.get(0).getUserMembershipType());
        assertEquals(originUserMembershipItemViews.get(0).getMembershipLevel(), userMembershipItemViews.get(0).getMembershipLevel());
        assertEquals(originUserMembershipItemViews.get(0).getMembershipPoint(), userMembershipItemViews.get(0).getMembershipPoint());
        assertEquals(originUserMembershipItemViews.get(0).getRegisterTime(), userMembershipItemViews.get(0).getRegisterTime());

        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, originUserMembershipItemViews.get(0).getMobile(), null, null, null, null, 0, 10);
        assertEquals(1, userMembershipItemViews.size());
        assertEquals(originUserMembershipItemViews.get(0).getMobile(), userMembershipItemViews.get(0).getMobile());

        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, originUserMembershipItemViews.get(0).getRegisterTime(), originUserMembershipItemViews.get(0).getRegisterTime(), null, null, 0, 10);
        assertEquals(1, userMembershipItemViews.size());
        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, originUserMembershipItemViews.get(1).getRegisterTime(), originUserMembershipItemViews.get(2).getRegisterTime(), null, null, 0, 10);
        assertEquals(2, userMembershipItemViews.size());

        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(1).getLoginName(), null, null, null, UserMembershipType.GIVEN, null, 0, 10);
        assertEquals(1, userMembershipItemViews.size());
        assertEquals(originUserMembershipItemViews.get(1).getLoginName(), userMembershipItemViews.get(0).getLoginName());
        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), null, null, null, UserMembershipType.UPGRADE, null, 0, 10);
        assertEquals(1, userMembershipItemViews.size());
        assertEquals(originUserMembershipItemViews.get(0).getLoginName(), userMembershipItemViews.get(0).getLoginName());

        assertEquals(0, userMembershipMapper.findUserMembershipItemViews("noUser", null, null, null, null, null, 0, 10).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), "noMobile", null, null, null, null, 0, 10).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), originUserMembershipItemViews.get(0).getMobile(), originUserMembershipItemViews.get(1).getRegisterTime(), null, null, null, 0, 10).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(2).getLoginName(), originUserMembershipItemViews.get(2).getMobile(), originUserMembershipItemViews.get(1).getRegisterTime(), originUserMembershipItemViews.get(1).getRegisterTime(), null, null, 0, 10).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), originUserMembershipItemViews.get(0).getMobile(), null, null, UserMembershipType.GIVEN, null, 0, 10).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), originUserMembershipItemViews.get(0).getMobile(), null, null, originUserMembershipItemViews.get(0).getUserMembershipType(), Lists.newArrayList(3), 0, 10).size());
        assertEquals(1, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), null, null, null, null, Lists.newArrayList(0, 1, 5), 0, 10).size());
    }

    @Test
    public void testFindCurrentMaxByLoginName() throws Exception {
        prepareUserMembershipData();
        createUserMembershipModel("fakeUser1", UserMembershipType.UPGRADE, 1);
        createUserMembershipModel("fakeUser1", UserMembershipType.UPGRADE, 2);
        createUserMembershipModel("fakeUser1", UserMembershipType.GIVEN, 3);

        UserMembershipModel userMembershipModel = userMembershipMapper.findCurrentMaxByLoginName("fakeUser1");
        assertEquals(3, membershipMapper.findById(userMembershipModel.getMembershipId()).getLevel());
    }

    @Test
    public void shouldFindExpiredUserMembership() throws Exception {
        UserModel fakeUser = this.createFakeUser("expiredMembership", RandomStringUtils.randomNumeric(11), new Date());
        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), membershipMapper.findByLevel(5).getId(), new DateTime().toDate(), UserMembershipType.GIVEN);
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), membershipMapper.findByLevel(5).getId(), new DateTime().minusDays(1).toDate(), UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);

        List<UserMembershipModel> actualExpiredUserMembership = userMembershipMapper.findExpiredUserMembership(new DateTime().plusDays(100).toDate());
        assertThat(actualExpiredUserMembership.size(), is(0));

        List<UserMembershipModel> actualExpiredUserMembership2 = userMembershipMapper.findExpiredUserMembership(new Date());
        assertThat(actualExpiredUserMembership2.size(), is(1));
        assertThat(actualExpiredUserMembership2.get(0).getId(), is(userMembershipModel1.getId()));

        List<UserMembershipModel> actualExpiredUserMembership3 = userMembershipMapper.findExpiredUserMembership(new DateTime().minusDays(1).toDate());
        assertThat(actualExpiredUserMembership3.size(), is(1));
        assertThat(actualExpiredUserMembership3.get(0).getId(), is(userMembershipModel2.getId()));
    }

    @Test
    public void shouldFindLoginNameMembershipByLevelIsOk() {
        List<String> level0 = userMembershipMapper.findLoginNameMembershipByLevel(1);
        List<String> level1 = userMembershipMapper.findLoginNameMembershipByLevel(2);
        List<String> level3 = userMembershipMapper.findLoginNameMembershipByLevel(3);
        List<String> level4 = userMembershipMapper.findLoginNameMembershipByLevel(5);
        UserModel fakeUser = this.createFakeUser("expiredMembership", RandomStringUtils.randomNumeric(11), new Date());
        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), membershipMapper.findByLevel(5).getId(), new DateTime().toDate(), UserMembershipType.GIVEN);
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), membershipMapper.findByLevel(1).getId(), new DateTime().toDate(), UserMembershipType.GIVEN);
        UserMembershipModel userMembershipModel3 = new UserMembershipModel(fakeUser.getLoginName(), membershipMapper.findByLevel(2).getId(), new DateTime().toDate(), UserMembershipType.GIVEN);
        UserMembershipModel userMembershipModel5 = new UserMembershipModel(fakeUser.getLoginName(), membershipMapper.findByLevel(3).getId(), new DateTime().minusDays(1).toDate(), UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);
        userMembershipMapper.create(userMembershipModel3);
        userMembershipMapper.create(userMembershipModel5);


        List<String> loginNames = userMembershipMapper.findLoginNameMembershipByLevel(1);
        assertTrue(loginNames.containsAll(level0));
        loginNames = userMembershipMapper.findLoginNameMembershipByLevel(2);
        assertTrue(loginNames.containsAll(level1));
        loginNames = userMembershipMapper.findLoginNameMembershipByLevel(3);
        assertTrue(loginNames.containsAll(level3));
        loginNames = userMembershipMapper.findLoginNameMembershipByLevel(5);
        assertTrue(loginNames.containsAll(level4));
    }

    @Test
    public void testUpdate() {
        prepareUserMembershipData();

        UserMembershipModel originChangeUserMembershipModel = userMembershipMapper.findByLoginName("fakeUser1").get(0);
        UserMembershipModel originOtherUserMembershipModel = userMembershipMapper.findByLoginName("fakeUser2").get(0);

        originChangeUserMembershipModel.setLoginName("fakeUser3");
        originChangeUserMembershipModel.setMembershipId(3);
        originChangeUserMembershipModel.setExpiredTime(DateTime.parse("1999-06-30T01:20").toDate());
        originChangeUserMembershipModel.setCreatedTime(DateTime.parse("1989-06-30T01:20").toDate());
        originChangeUserMembershipModel.setType(UserMembershipType.GIVEN);

        userMembershipMapper.update(originChangeUserMembershipModel);

        UserMembershipModel updateChangeUserMembershipModel = userMembershipMapper.findByLoginNameByType("fakeUser3", UserMembershipType.GIVEN);
        assertEquals(originChangeUserMembershipModel.getLoginName(), updateChangeUserMembershipModel.getLoginName());
        assertEquals(originChangeUserMembershipModel.getMembershipId(), updateChangeUserMembershipModel.getMembershipId());
        assertEquals(originChangeUserMembershipModel.getExpiredTime(), updateChangeUserMembershipModel.getExpiredTime());
        assertEquals(originChangeUserMembershipModel.getCreatedTime(), updateChangeUserMembershipModel.getCreatedTime());
        assertEquals(originChangeUserMembershipModel.getType(), updateChangeUserMembershipModel.getType());

        UserMembershipModel updateOtherUserMembershipModel = userMembershipMapper.findByLoginName("fakeUser2").get(0);
        assertEquals(originOtherUserMembershipModel.getLoginName(), updateOtherUserMembershipModel.getLoginName());
        assertEquals(originOtherUserMembershipModel.getMembershipId(), updateOtherUserMembershipModel.getMembershipId());
        assertEquals(originOtherUserMembershipModel.getExpiredTime(), updateOtherUserMembershipModel.getExpiredTime());
        assertEquals(originOtherUserMembershipModel.getCreatedTime(), updateOtherUserMembershipModel.getCreatedTime());
        assertEquals(originOtherUserMembershipModel.getType(), updateOtherUserMembershipModel.getType());
    }
}
