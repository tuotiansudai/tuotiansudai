package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.BlacklistMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class BlacklistServiceTest {
    @Autowired
    BlacklistService blacklistService;

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
    public void testUserIsInBlacklist() {
        List<BlacklistModel> blacklistModels = prepareData();
        blacklistMapper.massInsertBlacklist(blacklistModels);
        assertTrue(blacklistService.userIsInBlacklist("testUser1"));
        assertTrue(blacklistService.userIsInBlacklist("testUser2"));
        assertFalse(blacklistService.userIsInBlacklist("testUser3"));
    }
}
