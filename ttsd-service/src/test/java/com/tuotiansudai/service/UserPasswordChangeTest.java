package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserPasswordChangeTest {

    @Autowired
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldChangePassword() throws Exception{
        String mobile = "100000000";
        String loginName = "loginname";
        String password = "password";
        String newPassword = "password2";
        String salt = myShaPasswordEncoder.generateSalt();
        String encodePassword = myShaPasswordEncoder.encodePassword(password, salt);

        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setMobile(mobile);
        userModel.setPassword(password);
        userModel.setSalt(salt);
        userModel.setPassword(encodePassword);
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setEmail("ssss@tuotiansudai.com");
        userModel.setId(427589425942L);
        userModel.setRegisterTime(new Date());

        userMapper.create(userModel);

        UserModel um1 = userMapper.findByLoginName(loginName);
        assertNotNull(um1);

        userService.changePassword(loginName, mobile, password, newPassword);

        UserModel um = userMapper.findByLoginName(loginName);
        assertEquals(
                um.getPassword(),
                myShaPasswordEncoder.encodePassword(newPassword, um.getSalt())
        );
    }
}
