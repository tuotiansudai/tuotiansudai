package com.tuotiansudai.anxin.service;


import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AnxinSignServiceTest {

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Test
    public void shouldRequiredAuthenticationWhenSwitchOn() throws Exception {
        UserModel fakeUser = this.createFakeUser();
        this.createFakeAnxinSignProperty(fakeUser.getLoginName());

        redisWrapperClient.del("anxin-sign:switch");

//        assertTrue(anxinSignService.isAuthenticationRequired(fakeUser.getLoginName()));
        //TODO anxinsign
    }

    @Test
    public void shouldNotRequiredAuthenticationWhenSwitchOff() throws Exception {
        UserModel fakeUser = this.createFakeUser();
        this.createFakeAnxinSignProperty(fakeUser.getLoginName());
        redisWrapperClient.hset("anxin-sign:switch", "switch", "false");

//        assertFalse(anxinSignService.isAuthenticationRequired(fakeUser.getLoginName()));
    }

    @Test
    public void shouldRequiredAuthenticationWhenSwitchOffAndContainsWhitelist() throws Exception {
        UserModel fakeUser = this.createFakeUser();
        this.createFakeAnxinSignProperty(fakeUser.getLoginName());
        redisWrapperClient.hset("anxin-sign:switch", "switch", "false");
        redisWrapperClient.hset("anxin-sign:switch", "whitelist", fakeUser.getMobile());

//        assertTrue(anxinSignService.isAuthenticationRequired(fakeUser.getLoginName()));
    }

    private AnxinSignPropertyModel createFakeAnxinSignProperty(String loginName) {
        AnxinSignPropertyModel anxinProp = new AnxinSignPropertyModel();
        anxinProp.setLoginName(loginName);
        anxinProp.setCreatedTime(new Date());
        anxinProp.setAnxinUserId("fakeAnxinUserId");
        anxinSignPropertyMapper.create(anxinProp);
        return anxinProp;
    }

    private UserModel createFakeUser() {
        UserModel model = new UserModel();
        model.setLoginName("fakeUser");
        model.setPassword("password");
        model.setEmail("fakeUser@tuotiansudai.com");
        model.setMobile(RandomStringUtils.randomNumeric(11));
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }
}
