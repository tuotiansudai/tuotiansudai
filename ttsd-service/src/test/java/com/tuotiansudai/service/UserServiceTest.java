package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserServiceTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Test
    public void shouldReFreshAreaByMobileInJob(){
        UserModel userModel1 = getFakeUser("loginName1");
        userMapper.create(userModel1);
        UserModel userModel2 = getFakeUser("loginName2");
        userModel2.setMobile("13561674892");
        userMapper.create(userModel2);

        userService.refreshAreaByMobileInJob();
        UserModel userModelResult1 = userMapper.findByLoginName("loginName1");
        assertEquals("北京",userModelResult1.getProvince());
        UserModel userModelResult2 = userMapper.findByLoginName("loginName2");
        assertEquals("山东",userModelResult2.getProvince());

    }

    private UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("testbefore@tuotiansudai.com");
        userModelTest.setMobile("18610361805");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }




}
