package com.tuotiansudai.membership.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
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
public class UserMembershipEvaluatorTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

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

    private UserMembershipItemDto combineUserMembershipItemModel(UserModel userModel, AccountModel accountModel, UserMembershipModel userMembershipModel) {
        UserMembershipItemDto userMembershipItemDto = new UserMembershipItemDto();
        userMembershipItemDto.setLoginName(userModel.getLoginName());
        userMembershipItemDto.setMobile(userModel.getMobile());
        userMembershipItemDto.setRealName(accountModel.getUserName());
        userMembershipItemDto.setRegisterTime(userModel.getRegisterTime());
        userMembershipItemDto.setUserMembershipType(userMembershipModel.getType());
        userMembershipItemDto.setMembershipLevel(membershipMapper.findById(userMembershipModel.getMembershipId()).getLevel());
        userMembershipItemDto.setMembershipPoint(accountModel.getMembershipPoint());
        return userMembershipItemDto;
    }

    private List<UserMembershipItemDto> prepareUserMembershipData() {
        UserModel userModel1 = createFakeUser("testUser1", "18612340001", DateTime.parse("2010-06-30T12:30").toDate());
        AccountModel accountModel1 = createFakeAccount(userModel1, "userName1", 1);
        UserMembershipModel userMembershipModel1 = createUserMembershipModel("testUser1", UserMembershipType.UPGRADE, 0);

        UserModel userModel2 = createFakeUser("testUser2", "18612340002", DateTime.parse("2010-07-30T12:30").toDate());
        AccountModel accountModel2 = createFakeAccount(userModel2, "userName2", 2);
        UserMembershipModel userMembershipModel2 = createUserMembershipModel("testUser2", UserMembershipType.GIVEN, 5);

        UserModel userModel3 = createFakeUser("testUser3", "18612340003", DateTime.parse("2010-08-30T12:30").toDate());
        AccountModel accountModel3 = createFakeAccount(userModel3, "userName3", 3);
        UserMembershipModel userMembershipModel3 = createUserMembershipModel("testUser3", UserMembershipType.UPGRADE, 0);

        List<UserMembershipItemDto> userMembershipItemDtos = new ArrayList<>();
        userMembershipItemDtos.add(combineUserMembershipItemModel(userModel1, accountModel1, userMembershipModel1));
        userMembershipItemDtos.add(combineUserMembershipItemModel(userModel2, accountModel2, userMembershipModel2));
        userMembershipItemDtos.add(combineUserMembershipItemModel(userModel3, accountModel3, userMembershipModel3));

        return userMembershipItemDtos;
    }

    @Test
    public void testGetUserMembershipItems() throws Exception {
        List<UserMembershipItemDto> originUserMembershipItemList = prepareUserMembershipData();

        BasePaginationDataDto<UserMembershipItemDto> basePaginationDataDto = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, null, null, 1, 2);
        assertEquals(0, basePaginationDataDto.getCount());
        assertEquals(1, basePaginationDataDto.getIndex());
        assertEquals(2, basePaginationDataDto.getPageSize());
        assertTrue(basePaginationDataDto.isHasNextPage());
        assertFalse(basePaginationDataDto.isHasPreviousPage());

        basePaginationDataDto = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, null, null, 2, 2);
        assertEquals(0, basePaginationDataDto.getCount());
        assertEquals(2, basePaginationDataDto.getIndex());
        assertEquals(2, basePaginationDataDto.getPageSize());
        assertFalse(basePaginationDataDto.isHasNextPage());
        assertTrue(basePaginationDataDto.isHasPreviousPage());

        basePaginationDataDto = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, null, null, 1, 10);
        assertEquals(0, basePaginationDataDto.getCount());
        assertEquals(1, basePaginationDataDto.getIndex());
        assertEquals(10, basePaginationDataDto.getPageSize());
        assertFalse(basePaginationDataDto.isHasNextPage());
        assertFalse(basePaginationDataDto.isHasPreviousPage());

        basePaginationDataDto = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, null, null, 10, 10);
        assertEquals(0, basePaginationDataDto.getCount());
        assertEquals(10, basePaginationDataDto.getIndex());
        assertEquals(10, basePaginationDataDto.getPageSize());
        assertFalse(basePaginationDataDto.isHasPreviousPage());
        assertFalse(basePaginationDataDto.isHasNextPage());


        List<UserMembershipItemDto> userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), null, null, null, null, null, 1, 10).getRecords();
        assertEquals(1, userMembershipItemModels.size());
        assertEquals(originUserMembershipItemList.get(0).getLoginName(), userMembershipItemModels.get(0).getLoginName());
        assertEquals(originUserMembershipItemList.get(0).getMobile(), userMembershipItemModels.get(0).getMobile());
        assertEquals(originUserMembershipItemList.get(0).getRealName(), userMembershipItemModels.get(0).getRealName());
        assertEquals(originUserMembershipItemList.get(0).getUserMembershipType(), userMembershipItemModels.get(0).getUserMembershipType());
        assertEquals(originUserMembershipItemList.get(0).getMembershipLevel(), userMembershipItemModels.get(0).getMembershipLevel());
        assertEquals(originUserMembershipItemList.get(0).getMembershipPoint(), userMembershipItemModels.get(0).getMembershipPoint());
        assertEquals(originUserMembershipItemList.get(0).getRegisterTime(), userMembershipItemModels.get(0).getRegisterTime());

        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, originUserMembershipItemList.get(0).getMobile(), null, null, null, null, 1, 10).getRecords();
        assertEquals(1, userMembershipItemModels.size());
        assertEquals(originUserMembershipItemList.get(0).getMobile(), userMembershipItemModels.get(0).getMobile());

        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, originUserMembershipItemList.get(0).getRegisterTime(), null, null, null, 1, 10).getRecords();
        assertEquals(3, userMembershipItemModels.size());
        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, null, originUserMembershipItemList.get(2).getRegisterTime(), null, null, 1, 10).getRecords();
        assertEquals(3, userMembershipItemModels.size());
        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, originUserMembershipItemList.get(0).getRegisterTime(), originUserMembershipItemList.get(0).getRegisterTime(), null, null, 1, 10).getRecords();
        assertEquals(1, userMembershipItemModels.size());
        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, originUserMembershipItemList.get(1).getRegisterTime(), originUserMembershipItemList.get(2).getRegisterTime(), null, null, 1, 10).getRecords();
        assertEquals(2, userMembershipItemModels.size());

        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, UserMembershipType.GIVEN, null, 1, 10).getRecords();
        assertEquals(1, userMembershipItemModels.size());
        assertEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemModels.get(0).getLoginName());
        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, UserMembershipType.UPGRADE, null, 1, 10).getRecords();
        assertEquals(2, userMembershipItemModels.size());
        assertNotEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemModels.get(0).getLoginName());
        assertNotEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemModels.get(1).getLoginName());
        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, null, null, 1, 10).getRecords();
        assertEquals(3, userMembershipItemModels.size());

        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, null, Lists.newArrayList(0), 1, 10).getRecords();
        assertEquals(2, userMembershipItemModels.size());
        assertNotEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemModels.get(0).getLoginName());
        assertNotEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemModels.get(1).getLoginName());
        userMembershipItemModels = userMembershipEvaluator.getUserMembershipItems(null, null, null, null, null, Lists.newArrayList(5), 1, 10).getRecords();
        assertEquals(1, userMembershipItemModels.size());
        assertEquals(originUserMembershipItemList.get(1).getLoginName(), userMembershipItemModels.get(0).getLoginName());

        assertEquals(0, userMembershipEvaluator.getUserMembershipItems("noUser", null, null, null, null, null, 1, 10).getRecords().size());
        assertEquals(0, userMembershipEvaluator.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), "noMobile", null, null, null, null, 1, 10).getRecords().size());
        assertEquals(0, userMembershipEvaluator.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), originUserMembershipItemList.get(0).getMobile(), originUserMembershipItemList.get(1).getRegisterTime(), null, null, null, 1, 10).getRecords().size());
        assertEquals(0, userMembershipEvaluator.getUserMembershipItems(originUserMembershipItemList.get(2).getLoginName(), originUserMembershipItemList.get(2).getMobile(), originUserMembershipItemList.get(1).getRegisterTime(), originUserMembershipItemList.get(1).getRegisterTime(), null, null, 1, 10).getRecords().size());
        assertEquals(0, userMembershipEvaluator.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), originUserMembershipItemList.get(0).getMobile(), null, null, UserMembershipType.GIVEN, null, 1, 10).getRecords().size());
        assertEquals(0, userMembershipEvaluator.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), originUserMembershipItemList.get(0).getMobile(), null, null, originUserMembershipItemList.get(0).getUserMembershipType(), Lists.newArrayList(3), 1, 10).getRecords().size());
        assertEquals(1, userMembershipEvaluator.getUserMembershipItems(originUserMembershipItemList.get(0).getLoginName(), null, null, null, null, Lists.newArrayList(0, 1, 5), 1, 10).getRecords().size());
    }

    @Test
    public void testGetAllLevels() throws Exception {
        List<Integer> levels = userMembershipEvaluator.getAllLevels();
        assertThat(levels.size(), is(6));
        assertThat(levels.get(0), is(0));
        assertThat(levels.get(1), is(1));
        assertThat(levels.get(2), is(2));
        assertThat(levels.get(3), is(3));
        assertThat(levels.get(4), is(4));
        assertThat(levels.get(5), is(5));
    }
}
