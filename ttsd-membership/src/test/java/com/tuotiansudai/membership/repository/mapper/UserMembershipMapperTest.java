package com.tuotiansudai.membership.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.membership.repository.model.UserMembershipItemModel;
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

    private UserMembershipItemModel combineUserMembershipItemModel(UserModel userModel, AccountModel accountModel, UserMembershipModel userMembershipModel) {
        UserMembershipItemModel userMembershipItemModel = new UserMembershipItemModel();
        userMembershipItemModel.setLoginName(userModel.getLoginName());
        userMembershipItemModel.setMobile(userModel.getMobile());
        userMembershipItemModel.setRealName(accountModel.getUserName());
        userMembershipItemModel.setRegisterTime(userModel.getRegisterTime());
        userMembershipItemModel.setUserMembershipType(userMembershipModel.getType());
        userMembershipItemModel.setMembershipLevel(membershipMapper.findById(userMembershipModel.getMembershipId()).getLevel());
        userMembershipItemModel.setMembershipPoint(accountModel.getMembershipPoint());
        return userMembershipItemModel;
    }

    private List<UserMembershipItemModel> prepareUserMembershipData() {
        UserModel userModel1 = createFakeUser("testUser1", "18612340001", DateTime.parse("2010-06-30T12:30").toDate());
        AccountModel accountModel1 = createFakeAccount(userModel1, "userName1", 1);
        UserMembershipModel userMembershipModel1 = createUserMembershipModel("testUser1", UserMembershipType.UPGRADE, 0);

        UserModel userModel2 = createFakeUser("testUser2", "18612340002", DateTime.parse("2010-07-30T12:30").toDate());
        AccountModel accountModel2 = createFakeAccount(userModel2, "userName2", 2);
        UserMembershipModel userMembershipModel2 = createUserMembershipModel("testUser2", UserMembershipType.GIVEN, 5);

        UserModel userModel3 = createFakeUser("testUser3", "18612340003", DateTime.parse("2010-08-30T12:30").toDate());
        AccountModel accountModel3 = createFakeAccount(userModel3, "userName3", 3);
        UserMembershipModel userMembershipModel3 = createUserMembershipModel("testUser3", UserMembershipType.UPGRADE, 0);

        List<UserMembershipItemModel> userMembershipItemModels = new ArrayList<>();
        userMembershipItemModels.add(combineUserMembershipItemModel(userModel1, accountModel1, userMembershipModel1));
        userMembershipItemModels.add(combineUserMembershipItemModel(userModel2, accountModel2, userMembershipModel2));
        userMembershipItemModels.add(combineUserMembershipItemModel(userModel3, accountModel3, userMembershipModel3));

        return userMembershipItemModels;
    }

    @Test
    public void testFindUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel() throws Exception {
        List<UserMembershipItemModel> originUserMembershipItemModels = prepareUserMembershipData();

        List<UserMembershipItemModel> userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(originUserMembershipItemModels.get(0).getLoginName(), null, null, null, null, null);
        assertEquals(1, userMembershipItemModels.size());
        assertEquals(originUserMembershipItemModels.get(0).getLoginName(), userMembershipItemModels.get(0).getLoginName());
        assertEquals(originUserMembershipItemModels.get(0).getMobile(), userMembershipItemModels.get(0).getMobile());
        assertEquals(originUserMembershipItemModels.get(0).getRealName(), userMembershipItemModels.get(0).getRealName());
        assertEquals(originUserMembershipItemModels.get(0).getUserMembershipType(), userMembershipItemModels.get(0).getUserMembershipType());
        assertEquals(originUserMembershipItemModels.get(0).getMembershipLevel(), userMembershipItemModels.get(0).getMembershipLevel());
        assertEquals(originUserMembershipItemModels.get(0).getMembershipPoint(), userMembershipItemModels.get(0).getMembershipPoint());
        assertEquals(originUserMembershipItemModels.get(0).getRegisterTime(), userMembershipItemModels.get(0).getRegisterTime());

        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, originUserMembershipItemModels.get(0).getMobile(), null, null, null, null);
        assertEquals(1, userMembershipItemModels.size());
        assertEquals(originUserMembershipItemModels.get(0).getMobile(), userMembershipItemModels.get(0).getMobile());

        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, originUserMembershipItemModels.get(0).getRegisterTime(), null, null, null);
        assertEquals(3, userMembershipItemModels.size());
        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, null, originUserMembershipItemModels.get(2).getRegisterTime(), null, null);
        assertEquals(3, userMembershipItemModels.size());
        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, originUserMembershipItemModels.get(0).getRegisterTime(), originUserMembershipItemModels.get(0).getRegisterTime(), null, null);
        assertEquals(1, userMembershipItemModels.size());
        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, originUserMembershipItemModels.get(1).getRegisterTime(), originUserMembershipItemModels.get(2).getRegisterTime(), null, null);
        assertEquals(2, userMembershipItemModels.size());

        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, null, null, UserMembershipType.GIVEN, null);
        assertEquals(1, userMembershipItemModels.size());
        assertEquals(originUserMembershipItemModels.get(1).getLoginName(), userMembershipItemModels.get(0).getLoginName());
        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, null, null, UserMembershipType.UPGRADE, null);
        assertEquals(2, userMembershipItemModels.size());
        assertNotEquals(originUserMembershipItemModels.get(1).getLoginName(), userMembershipItemModels.get(0).getLoginName());
        assertNotEquals(originUserMembershipItemModels.get(1).getLoginName(), userMembershipItemModels.get(1).getLoginName());
        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, null, null, null, null);
        assertEquals(3, userMembershipItemModels.size());

        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, null, null, null, Lists.newArrayList(0));
        assertEquals(2, userMembershipItemModels.size());
        assertNotEquals(originUserMembershipItemModels.get(1).getLoginName(), userMembershipItemModels.get(0).getLoginName());
        assertNotEquals(originUserMembershipItemModels.get(1).getLoginName(), userMembershipItemModels.get(1).getLoginName());
        userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(null, null, null, null, null, Lists.newArrayList(5));
        assertEquals(1, userMembershipItemModels.size());
        assertEquals(originUserMembershipItemModels.get(1).getLoginName(), userMembershipItemModels.get(0).getLoginName());

        assertEquals(0, userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel("noUser", null, null, null, null, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(originUserMembershipItemModels.get(0).getLoginName(), "noMobile", null, null, null, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(originUserMembershipItemModels.get(0).getLoginName(), originUserMembershipItemModels.get(0).getMobile(), originUserMembershipItemModels.get(1).getRegisterTime(), null, null, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(originUserMembershipItemModels.get(2).getLoginName(), originUserMembershipItemModels.get(2).getMobile(), originUserMembershipItemModels.get(1).getRegisterTime(), originUserMembershipItemModels.get(1).getRegisterTime(), null, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(originUserMembershipItemModels.get(0).getLoginName(), originUserMembershipItemModels.get(0).getMobile(), null, null, UserMembershipType.GIVEN, null).size());
        assertEquals(0, userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(originUserMembershipItemModels.get(0).getLoginName(), originUserMembershipItemModels.get(0).getMobile(), null, null, originUserMembershipItemModels.get(0).getUserMembershipType(), Lists.newArrayList(3)).size());
        assertEquals(1, userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(originUserMembershipItemModels.get(0).getLoginName(), null, null, null, null, Lists.newArrayList(0, 1, 5)).size());
    }
}
