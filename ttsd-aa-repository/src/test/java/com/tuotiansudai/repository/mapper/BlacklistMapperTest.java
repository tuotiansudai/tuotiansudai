package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BlacklistModel;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class BlacklistMapperTest {

    @Autowired
    BlacklistMapper blacklistMapper;

    @Autowired
    FakeUserHelper userMapper;

    private UserModel createUserByLoginName(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private List<BlacklistModel> prepareData() {
        createUserByLoginName("testUser1");
        createUserByLoginName("testUser2");
        createUserByLoginName("testUser3");

        List<BlacklistModel> blacklistModels = new ArrayList<>();
        blacklistModels.add(new BlacklistModel("testUser1", DateTime.parse("2010-06-30T01:20").toDate(), false));
        blacklistModels.add(new BlacklistModel("testUser2", DateTime.parse("2011-06-30T01:20").toDate(), false));
        blacklistModels.add(new BlacklistModel("testUser3", DateTime.parse("2012-06-30T01:20").toDate(), true));
        return blacklistModels;
    }

    @Test
    public void testMassInsertBlackListAndFindBlacklistModelByLoginName() {
        List<BlacklistModel> blacklistModels = prepareData();

        blacklistMapper.massInsertBlacklist(blacklistModels);

        BlacklistModel blacklistModel = blacklistMapper.findBlacklistModelByLoginName("testUser1");
        assertEquals("testUser1", blacklistModel.getLoginName());
        assertEquals(false, blacklistModel.isDeleted());
        assertEquals(DateTime.parse("2010-06-30T01:20").toDate(), blacklistModel.getCreatedTime());
        assertEquals(DateTime.parse("2010-06-30T01:20").toDate(), blacklistModel.getUpdatedTime());

        blacklistModel = blacklistMapper.findBlacklistModelByLoginName("testUser2");
        assertEquals("testUser2", blacklistModel.getLoginName());
        assertEquals(false, blacklistModel.isDeleted());
        assertEquals(DateTime.parse("2011-06-30T01:20").toDate(), blacklistModel.getCreatedTime());
        assertEquals(DateTime.parse("2011-06-30T01:20").toDate(), blacklistModel.getUpdatedTime());

        blacklistModel = blacklistMapper.findBlacklistModelByLoginName("testUser3");
        assertEquals("testUser3", blacklistModel.getLoginName());
        assertEquals(true, blacklistModel.isDeleted());
        assertEquals(DateTime.parse("2012-06-30T01:20").toDate(), blacklistModel.getCreatedTime());
        assertEquals(DateTime.parse("2012-06-30T01:20").toDate(), blacklistModel.getUpdatedTime());
    }

    @Test
    public void testUserIsInBlacklist() {
        List<BlacklistModel> blacklistModels = prepareData();
        blacklistMapper.massInsertBlacklist(blacklistModels);

        assertTrue(blacklistMapper.userIsInBlacklist("testUser1"));
        assertTrue(blacklistMapper.userIsInBlacklist("testUser2"));
        assertFalse(blacklistMapper.userIsInBlacklist("testUser3"));
    }

    @Test
    public void testUpdateBlacklist() {
        List<BlacklistModel> blacklistModels = prepareData();
        blacklistMapper.massInsertBlacklist(blacklistModels);

        long originId = blacklistMapper.findBlacklistModelByLoginName("testUser1").getId();

        BlacklistModel modifiedBlacklistModel = new BlacklistModel();
        modifiedBlacklistModel.setLoginName("testUser1");
        modifiedBlacklistModel.setCreatedTime(DateTime.parse("2015-06-30T01:20").toDate());
        modifiedBlacklistModel.setUpdatedTime(DateTime.parse("2016-06-30T01:20").toDate());
        modifiedBlacklistModel.setDeleted(true);

        blacklistMapper.updateBlacklist(modifiedBlacklistModel);

        BlacklistModel updatedBlacklistModel = blacklistMapper.findBlacklistModelByLoginName("testUser1");
        assertEquals(originId, updatedBlacklistModel.getId());
        assertEquals(modifiedBlacklistModel.getLoginName(), updatedBlacklistModel.getLoginName());
        assertEquals(modifiedBlacklistModel.getCreatedTime(), updatedBlacklistModel.getCreatedTime());
        assertEquals(modifiedBlacklistModel.getUpdatedTime(), updatedBlacklistModel.getUpdatedTime());
        assertEquals(modifiedBlacklistModel.isDeleted(), updatedBlacklistModel.isDeleted());
    }
}
