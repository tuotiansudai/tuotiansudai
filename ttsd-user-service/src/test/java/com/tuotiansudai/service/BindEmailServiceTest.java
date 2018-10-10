package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class BindEmailServiceTest {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private BindEmailService bindEmailService;

    @Autowired
    private FakeUserHelper fakeUserHelper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldVerifyEmailIsOk() {
        UserModel fakeUser = getFakeUser("loginname");
        fakeUserHelper.create(fakeUser);
        mockLoginUser("loginname", "11900000000");
        redisWrapperClient.set("web:loginname:uuid", "loginname:testafter@tuotiansudai.com");
        bindEmailService.verifyEmail("loginname", "uuid", "127.0.0.1", "WEB", "");
        UserModel userModel = userMapper.findByLoginName("loginname");
        String value = redisWrapperClient.get("web:loginname:uuid");
        assertNull(value);
        assertNotNull(userModel);
        assertEquals("testafter@tuotiansudai.com", userModel.getEmail());
    }

    private void mockLoginUser(String loginName, String mobile) {
        User user = new User(loginName, "", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_PATRON"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);
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
