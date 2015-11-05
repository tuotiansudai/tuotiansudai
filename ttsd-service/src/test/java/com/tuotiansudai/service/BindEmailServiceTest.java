package com.tuotiansudai.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.security.MyUser;
import com.tuotiansudai.utils.UUIDGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class BindEmailServiceTest {
    @Autowired
    private BindEmailService bindEmailService;
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldVerifyEmailIsOk(){
        UserModel fakeUser = getFakeUser("adminTest");
        userMapper.create(fakeUser);
        mockLoginUser("adminTest", "11900000000");
        redisWrapperClient.set("web:adminTest:uuid", "adminTest:testafter@tuotiansudai.com");
        bindEmailService.verifyEmail("uuid");
        UserModel userModel = userMapper.findByLoginName("adminTest");
        assertNotNull(userModel);
        assertEquals("testafter@tuotiansudai.com",userModel.getEmail());
    }

    private void mockLoginUser(String loginName, String mobile){
        MyUser user = new MyUser(loginName,"", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_PATRON"), mobile, "fdafdsa");
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }

    private UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("testbefore@tuotiansudai.com");
        userModelTest.setMobile("11900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }




}
