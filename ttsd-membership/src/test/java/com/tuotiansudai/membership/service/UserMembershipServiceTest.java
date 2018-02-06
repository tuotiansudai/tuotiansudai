package com.tuotiansudai.membership.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserMembershipServiceTest {

    @Autowired
    private FakeUserHelper userMapper;

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
    public void shouldGetMembershipByLevel() {
        MembershipModel membershipModel = userMembershipService.getMembershipByLevel(createMembership(1).getLevel());

        assertThat(membershipModel.getLevel(), is(0));
        assertThat(membershipModel.getFee(), is(0.1));
        assertThat(membershipModel.getExperience(), is(0L));
    }

    @Test
    public void shouldGetProgressBarPercentByLoginNameWhenLevelEqualsV0() {
        AccountModel accountModel = createAccount(4000);
        MembershipModel membershipModel = createMembership(1);
        UserMembershipModel userMembershipModel = createUserMembership(membershipModel.getId());
        int process = userMembershipService.getProgressBarPercent(accountModel.getLoginName());
        assertThat(membershipModel.getLevel(), is(0));
        assertThat(process, is(16));
    }

    @Test
    public void shouldGetProgressBarPercentByLoginNameWhenLevelMoreThanV5() {
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

    private MembershipModel createMembership(int id) {
        MembershipModel membershipModel = membershipMapper.findById(id);
        return membershipModel;
    }

    private UserMembershipModel createUserMembership(long membershipId) {
        UserMembershipModel userMembershipModel = new UserMembershipModel("testuser", membershipId, DateTime.parse("2040-06-30T12:30").toDate(), UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel);
        return userMembershipModel;
    }

    private AccountModel createAccount(long membershipPoint) {
        AccountModel accountModel = new AccountModel(getFakeUser("testuser").getLoginName(), "", "", new Date());
        accountModel.setMembershipPoint(membershipPoint);
        accountMapper.create(accountModel);
        return accountModel;
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
        AccountModel accountModel = new AccountModel(userModel.getLoginName(), RandomStringUtils.randomNumeric(32), RandomStringUtils.randomNumeric(14), userModel.getRegisterTime());
        accountModel.setMembershipPoint(membershipPoint);
        accountMapper.create(accountModel);
        return accountModel;
    }

    private UserMembershipModel createUserMembershipModel(String loginName, UserMembershipType userMembershipType, int level) {
        UserMembershipModel userMembershipModel = new UserMembershipModel();
        userMembershipModel.setLoginName(loginName);
        userMembershipModel.setCreatedTime(new Date());
        userMembershipModel.setExpiredTime(DateTime.parse("2040-06-30T12:30").toDate());
        userMembershipModel.setType(userMembershipType);
        userMembershipModel.setMembershipId(membershipMapper.findByLevel(level).getId());
        userMembershipMapper.create(userMembershipModel);
        return userMembershipModel;
    }

    private UserMembershipItemDto combineUserMembershipItemModel(UserModel userModel, AccountModel accountModel, UserMembershipModel userMembershipModel) {
        UserMembershipItemDto userMembershipItemDto = new UserMembershipItemDto();
        userMembershipItemDto.setLoginName(userModel.getLoginName());
        userMembershipItemDto.setMobile(userModel.getMobile());
        userMembershipItemDto.setRealName(userModel.getUserName());
        userMembershipItemDto.setRegisterTime(userModel.getRegisterTime());
        userMembershipItemDto.setUserMembershipType(userMembershipModel.getType());
        userMembershipItemDto.setMembershipLevel(membershipMapper.findById(userMembershipModel.getMembershipId()).getLevel());
        userMembershipItemDto.setMembershipPoint(accountModel.getMembershipPoint());
        return userMembershipItemDto;
    }

    private List<UserMembershipItemDto> prepareUserMembershipData() {
        UserModel userModel1 = createFakeUser("userMembershipTestUser1", "18612340001", DateTime.parse("2010-06-30T12:30").withTimeAtStartOfDay().toDate());
        AccountModel accountModel1 = createFakeAccount(userModel1, 1);
        UserMembershipModel userMembershipModel1 = createUserMembershipModel(userModel1.getLoginName(), UserMembershipType.UPGRADE, 0);

        UserModel userModel2 = createFakeUser("userMembershipTestUser2", "18612340002", DateTime.parse("2010-07-30T12:30").withTimeAtStartOfDay().toDate());
        AccountModel accountModel2 = createFakeAccount(userModel2, 2);
        UserMembershipModel userMembershipModel2 = createUserMembershipModel(userModel2.getLoginName(), UserMembershipType.GIVEN, 5);

        UserModel userModel3 = createFakeUser("userMembershipTestUser3", "18612340003", DateTime.parse("2010-08-30T12:30").withTimeAtStartOfDay().toDate());
        AccountModel accountModel3 = createFakeAccount(userModel3, 3);
        UserMembershipModel userMembershipModel3 = createUserMembershipModel(userModel3.getLoginName(), UserMembershipType.UPGRADE, 0);

        List<UserMembershipItemDto> userMembershipItemDtos = new ArrayList<>();
        userMembershipItemDtos.add(combineUserMembershipItemModel(userModel1, accountModel1, userMembershipModel1));
        userMembershipItemDtos.add(combineUserMembershipItemModel(userModel2, accountModel2, userMembershipModel2));
        userMembershipItemDtos.add(combineUserMembershipItemModel(userModel3, accountModel3, userMembershipModel3));

        return userMembershipItemDtos;
    }

    @Test
    public void testGetUserMembershipItems() throws Exception {
        List<UserMembershipItemDto> originUserMembershipItemList = prepareUserMembershipData();

        List<UserMembershipItemDto> userMembershipItemDtos;
        userMembershipItemDtos = userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), null, null, null, null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10);
        assertEquals(1, userMembershipItemDtos.size());
        assertEquals(originUserMembershipItemList.get(0).getLoginName(), userMembershipItemDtos.get(0).getLoginName());
        assertEquals(originUserMembershipItemList.get(0).getMobile(), userMembershipItemDtos.get(0).getMobile());
        assertEquals(originUserMembershipItemList.get(0).getRealName(), userMembershipItemDtos.get(0).getRealName());
        assertEquals(originUserMembershipItemList.get(0).getUserMembershipType(), userMembershipItemDtos.get(0).getUserMembershipType());
        assertEquals(originUserMembershipItemList.get(0).getMembershipLevel(), userMembershipItemDtos.get(0).getMembershipLevel());
        assertEquals(originUserMembershipItemList.get(0).getMembershipPoint(), userMembershipItemDtos.get(0).getMembershipPoint());
        assertEquals(originUserMembershipItemList.get(0).getRegisterTime(), userMembershipItemDtos.get(0).getRegisterTime());

        userMembershipItemDtos = userMembershipService.getUserMembershipItems(null, originUserMembershipItemList.get(0).getMobile(), null, null, null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10);
        assertEquals(1, userMembershipItemDtos.size());
        assertEquals(originUserMembershipItemList.get(0).getMobile(), userMembershipItemDtos.get(0).getMobile());

        userMembershipItemDtos = userMembershipService.getUserMembershipItems(null, null, null, originUserMembershipItemList.get(2).getRegisterTime(), null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10);
        assertEquals(3, userMembershipItemDtos.size());
        userMembershipItemDtos = userMembershipService.getUserMembershipItems(null, null, originUserMembershipItemList.get(0).getRegisterTime(), originUserMembershipItemList.get(0).getRegisterTime(), null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10);
        assertEquals(1, userMembershipItemDtos.size());
        userMembershipItemDtos = userMembershipService.getUserMembershipItems(null, null, originUserMembershipItemList.get(1).getRegisterTime(), originUserMembershipItemList.get(2).getRegisterTime(), null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10);
        assertEquals(2, userMembershipItemDtos.size());

        userMembershipItemDtos = userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(1).getLoginName(), null, null, null, UserMembershipType.GIVEN, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10);
        assertEquals(1, userMembershipItemDtos.size());
        assertEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemDtos.get(0).getLoginName());

        userMembershipItemDtos = userMembershipService.getUserMembershipItems(null, null, null, null, null, Lists.newArrayList(0), 0, 10);
        assertEquals(2, userMembershipItemDtos.size());
        assertNotEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemDtos.get(0).getLoginName());
        assertNotEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemDtos.get(1).getLoginName());
        userMembershipItemDtos = userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(1).getLoginName(), null, null, null, null, Lists.newArrayList(5), 0, 10);
        assertEquals(1, userMembershipItemDtos.size());
        assertEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemDtos.get(0).getLoginName());

        assertEquals(0, userMembershipService.getUserMembershipItems("noUser", null, null, null, null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10).size());
        assertEquals(0, userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), "noMobile", null, null, null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10).size());
        assertEquals(0, userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), originUserMembershipItemList.get(0).getMobile(), originUserMembershipItemList.get(1).getRegisterTime(), null, null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10).size());
        assertEquals(0, userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(2).getLoginName(), originUserMembershipItemList.get(2).getMobile(), originUserMembershipItemList.get(1).getRegisterTime(), originUserMembershipItemList.get(1).getRegisterTime(), null, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10).size());
        assertEquals(0, userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), originUserMembershipItemList.get(0).getMobile(), null, null, UserMembershipType.GIVEN, Lists.newArrayList(0, 1, 2, 3, 4, 5), 0, 10).size());
        assertEquals(0, userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), originUserMembershipItemList.get(0).getMobile(), null, null, originUserMembershipItemList.get(0).getUserMembershipType(), Lists.newArrayList(3), 0, 10).size());
        assertEquals(1, userMembershipService.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), null, null, null, null, Lists.newArrayList(0, 1, 5), 0, 10).size());
    }

    @Test
    public void testGetAllLevels() throws Exception {
        List<Integer> levels = userMembershipService.getAllLevels();
        assertThat(levels.size(), is(6));
        assertThat(levels.get(0), is(0));
        assertThat(levels.get(1), is(1));
        assertThat(levels.get(2), is(2));
        assertThat(levels.get(3), is(3));
        assertThat(levels.get(4), is(4));
        assertThat(levels.get(5), is(5));
    }
}
