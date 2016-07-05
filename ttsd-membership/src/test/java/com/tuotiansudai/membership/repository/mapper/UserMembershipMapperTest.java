package com.tuotiansudai.membership.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.membership.repository.model.UserMembershipItemView;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserMembershipMapperTest {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMapper userMapper;

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
    public void shouldUpdateUserMembership() throws Exception {

        UserModel fakeUser = createFakeUser("loginName");
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel);

        UserMembershipModel updateUserMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 5, new Date(), UserMembershipType.GIVEN);
        userMembershipMapper.update(updateUserMembershipModel);

        assertThat(updateUserMembershipModel.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(updateUserMembershipModel.getMembershipId(), is(5L));
        assertThat(updateUserMembershipModel.getType(), is(UserMembershipType.GIVEN));
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
    public void shouldFindActiveByLoginName() throws Exception {

        UserModel fakeUser = createFakeUser("loginName");
        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), 2, new DateTime().plusDays(2).toDate(), UserMembershipType.GIVEN);
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), 3, new DateTime().plusDays(-2).toDate(), UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);

        UserMembershipModel membershipModel1 = userMembershipMapper.findActiveByLoginName(fakeUser.getLoginName());

        assertThat(membershipModel1.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(membershipModel1.getMembershipId(), is(2L));
        assertThat(membershipModel1.getType(), is(UserMembershipType.GIVEN));
    }

    @Test
    public void shouldFindRateByLoginName() throws Exception {

        UserModel fakeUser = createFakeUser("loginName");
        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), 1, new DateTime().plusDays(-2).toDate(), UserMembershipType.GIVEN);
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), 3, new DateTime().plusDays(2).toDate(), UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);

        double rate = userMembershipMapper.findRateByLoginName(fakeUser.getLoginName());

        assertThat(rate, is(0.09));
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

    private AccountModel createFakeAccount(UserModel userModel, String userName, long membershipPoint) {
        AccountModel accountModel = new AccountModel(userModel.getLoginName(), userName, RandomStringUtils.randomNumeric(18),
                RandomStringUtils.randomNumeric(32), RandomStringUtils.randomNumeric(14), userModel.getRegisterTime());
        accountModel.setMembershipPoint(membershipPoint);
        accountMapper.create(accountModel);
        return accountModel;
    }

    private UserMembershipModel createUserMembershipModel(String loginName, UserMembershipType userMembershipType, int level) {
        UserMembershipModel userMembershipModel = new UserMembershipModel();
        userMembershipModel.setLoginName(loginName);
        userMembershipModel.setCreatedTime(new Date());
        userMembershipModel.setExpiredTime(new Date());
        userMembershipModel.setType(userMembershipType);
        userMembershipModel.setMembershipId(membershipMapper.findByLevel(level).getId());
        userMembershipMapper.create(userMembershipModel);
        return userMembershipModel;
    }

    private UserMembershipItemView combineUserMembershipItemModel(UserModel userModel, AccountModel accountModel, UserMembershipModel userMembershipModel) {
        UserMembershipItemView userMembershipItemView = new UserMembershipItemView();
        userMembershipItemView.setLoginName(userModel.getLoginName());
        userMembershipItemView.setMobile(userModel.getMobile());
        userMembershipItemView.setRealName(accountModel.getUserName());
        userMembershipItemView.setRegisterTime(userModel.getRegisterTime());
        userMembershipItemView.setUserMembershipType(userMembershipModel.getType());
        userMembershipItemView.setMembershipLevel(membershipMapper.findById(userMembershipModel.getMembershipId()).getLevel());
        userMembershipItemView.setMembershipPoint(accountModel.getMembershipPoint());
        return userMembershipItemView;
    }

    private List<UserMembershipItemView> prepareUserMembershipData() {
        UserModel userModel1 = createFakeUser("testUser1", "18612340001", DateTime.parse("2010-06-30T12:30").toDate());
        AccountModel accountModel1 = createFakeAccount(userModel1, "userName1", 1);
        UserMembershipModel userMembershipModel1 = createUserMembershipModel("testUser1", UserMembershipType.UPGRADE, 0);

        UserModel userModel2 = createFakeUser("testUser2", "18612340002", DateTime.parse("2010-07-30T12:30").toDate());
        AccountModel accountModel2 = createFakeAccount(userModel2, "userName2", 2);
        UserMembershipModel userMembershipModel2 = createUserMembershipModel("testUser2", UserMembershipType.GIVEN, 5);

        UserModel userModel3 = createFakeUser("testUser3", "18612340003", DateTime.parse("2010-08-30T12:30").toDate());
        AccountModel accountModel3 = createFakeAccount(userModel3, "userName3", 3);
        UserMembershipModel userMembershipModel3 = createUserMembershipModel("testUser3", UserMembershipType.UPGRADE, 0);

        List<UserMembershipItemView> userMembershipItemViews = new ArrayList<>();
        userMembershipItemViews.add(combineUserMembershipItemModel(userModel1, accountModel1, userMembershipModel1));
        userMembershipItemViews.add(combineUserMembershipItemModel(userModel2, accountModel2, userMembershipModel2));
        userMembershipItemViews.add(combineUserMembershipItemModel(userModel3, accountModel3, userMembershipModel3));

        return userMembershipItemViews;
    }

    @Test
    public void testFindUserMembershipItemViews() throws Exception {
        List<UserMembershipItemView> originUserMembershipItemViews = prepareUserMembershipData();

        List<UserMembershipItemView> userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), null, null, null, null, null);
        assertEquals(1, userMembershipItemViews.size());
        assertEquals(originUserMembershipItemViews.get(0).getLoginName(), userMembershipItemViews.get(0).getLoginName());
        assertEquals(originUserMembershipItemViews.get(0).getMobile(), userMembershipItemViews.get(0).getMobile());
        assertEquals(originUserMembershipItemViews.get(0).getRealName(), userMembershipItemViews.get(0).getRealName());
        assertEquals(originUserMembershipItemViews.get(0).getUserMembershipType(), userMembershipItemViews.get(0).getUserMembershipType());
        assertEquals(originUserMembershipItemViews.get(0).getMembershipLevel(), userMembershipItemViews.get(0).getMembershipLevel());
        assertEquals(originUserMembershipItemViews.get(0).getMembershipPoint(), userMembershipItemViews.get(0).getMembershipPoint());
        assertEquals(originUserMembershipItemViews.get(0).getRegisterTime(), userMembershipItemViews.get(0).getRegisterTime());

        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, originUserMembershipItemViews.get(0).getMobile(), null, null, null, null);
        assertEquals(1, userMembershipItemViews.size());
        assertEquals(originUserMembershipItemViews.get(0).getMobile(), userMembershipItemViews.get(0).getMobile());

        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, originUserMembershipItemViews.get(0).getRegisterTime(), null, null, null);
        assertEquals(3, userMembershipItemViews.size());
        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, null, originUserMembershipItemViews.get(2).getRegisterTime(), null, null);
        assertEquals(3, userMembershipItemViews.size());
        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, originUserMembershipItemViews.get(0).getRegisterTime(), originUserMembershipItemViews.get(0).getRegisterTime(), null, null);
        assertEquals(1, userMembershipItemViews.size());
        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, originUserMembershipItemViews.get(1).getRegisterTime(), originUserMembershipItemViews.get(2).getRegisterTime(), null, null);
        assertEquals(2, userMembershipItemViews.size());

        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, null, null, UserMembershipType.GIVEN, null);
        assertEquals(1, userMembershipItemViews.size());
        assertEquals(originUserMembershipItemViews.get(1).getLoginName(), userMembershipItemViews.get(0).getLoginName());
        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, null, null, UserMembershipType.UPGRADE, null);
        assertEquals(2, userMembershipItemViews.size());
        assertNotEquals(originUserMembershipItemViews.get(1).getLoginName(), userMembershipItemViews.get(0).getLoginName());
        assertNotEquals(originUserMembershipItemViews.get(1).getLoginName(), userMembershipItemViews.get(1).getLoginName());
        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, null, null, null, null);
        assertEquals(3, userMembershipItemViews.size());

        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, null, null, null, Lists.newArrayList(0));
        assertEquals(2, userMembershipItemViews.size());
        assertNotEquals(originUserMembershipItemViews.get(1).getLoginName(), userMembershipItemViews.get(0).getLoginName());
        assertNotEquals(originUserMembershipItemViews.get(1).getLoginName(), userMembershipItemViews.get(1).getLoginName());
        userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(null, null, null, null, null, Lists.newArrayList(5));
        assertEquals(1, userMembershipItemViews.size());
        assertEquals(originUserMembershipItemViews.get(1).getLoginName(), userMembershipItemViews.get(0).getLoginName());

        assertEquals(0, userMembershipMapper.findUserMembershipItemViews("noUser", null, null, null, null, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), "noMobile", null, null, null, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), originUserMembershipItemViews.get(0).getMobile(), originUserMembershipItemViews.get(1).getRegisterTime(), null, null, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(2).getLoginName(), originUserMembershipItemViews.get(2).getMobile(), originUserMembershipItemViews.get(1).getRegisterTime(), originUserMembershipItemViews.get(1).getRegisterTime(), null, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), originUserMembershipItemViews.get(0).getMobile(), null, null, UserMembershipType.GIVEN, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), originUserMembershipItemViews.get(0).getMobile(), null, null, originUserMembershipItemViews.get(0).getUserMembershipType(), Lists.newArrayList(3)).size());
        assertEquals(1, userMembershipMapper.findUserMembershipItemViews(originUserMembershipItemViews.get(0).getLoginName(), null, null, null, null, Lists.newArrayList(0, 1, 5)).size());
    }
}
