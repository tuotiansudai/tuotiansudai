package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldFindUserByEmailIsNotNull() throws Exception {
        UserModel userModelTest = new UserModel();
        userModelTest = this.getUserModelTest();
        userMapper.insertUser(userModelTest);

        UserModel userModel = userMapper.findUserByEmail("12345@abc.com");
        assertNotNull(userModel);

    }

    @Test
    public void shouldFindUserByEmailIsNull() throws Exception {
        UserModel userModel = userMapper.findUserByEmail("22345@abc.com");
        assertNull(userModel);

    }

    @Test
    public void shouldFindUserByMobileNumberIsNotNull() throws Exception {
        UserModel userModelTest = new UserModel();
        userModelTest = this.getUserModelTest();
        userMapper.insertUser(userModelTest);

        UserModel userModel = userMapper.findUserByMobileNumber("13900000000");
        assertNotNull(userModel);
    }

    @Test
    public void shouldFindUserByMobileNumberIsNull() throws Exception {
        UserModel userModel = userMapper.findUserByMobileNumber("13800000000");
        assertNull(userModel);
    }

    @Test
    public void shouldFindUserByLoginNameIsNotNull() throws Exception {
        UserModel userModelTest = new UserModel();
        userModelTest = this.getUserModelTest();
        userMapper.insertUser(userModelTest);

        UserModel userModel = userMapper.findUserByLoginName("helloworld");
        assertNotNull(userModel);
    }

    @Test
    public void shouldFindUserByLoginNameIsNull() throws Exception {

        UserModel userModel = userMapper.findUserByLoginName("helloworld1");
        assertNull(userModel);
    }

    @Test
    public void shouldInsertUser() throws Exception {
        UserModel userModelTest = new UserModel();
        userModelTest = this.getUserModelTest();
        userMapper.insertUser(userModelTest);

        UserModel userModel = userMapper.findUserByLoginName("helloworld");
        assertNotNull(userModel);

    }

    public UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setAddress("tuotiansudai");
        userModelTest.setMobileNumber("13900000000");
        userModelTest.setLastLoginTime(new Date());
        userModelTest.setRegisterTime(new Date());
        userModelTest.setLastModifiedTime(new Date());
        userModelTest.setLastModifiedUser("nihao");
        userModelTest.setForbiddenTime(new Date());
        userModelTest.setAvatar("avatar");
        userModelTest.setStatus("right");
        userModelTest.setReferrer("100001");
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
