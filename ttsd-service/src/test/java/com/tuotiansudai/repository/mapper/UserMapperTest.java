package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldFindUserByEmailIsNotNull() throws Exception {
        userMapper.create(this.getUserModelTest());

        UserModel userModel = userMapper.findByEmail("12345@abc.com");
        assertNotNull(userModel);

    }

    @Test
    public void shouldFindUserByEmailIsNull() throws Exception {
        UserModel userModel = userMapper.findByEmail("22345@abc.com");

        assertNull(userModel);
    }

    @Test
    public void shouldFindUserByMobileIsNotNull() throws Exception {
        userMapper.create(this.getUserModelTest());

        UserModel userModel = userMapper.findByMobile("13900000000");
        assertNotNull(userModel);
    }

    @Test
    public void shouldFindUserByMobileIsNull() throws Exception {
        UserModel userModel = userMapper.findByMobile("13800000000");
        assertNull(userModel);
    }

    @Test
    public void shouldFindUserByLoginNameIsNotNull() throws Exception {
        userMapper.create(this.getUserModelTest());

        UserModel userModel = userMapper.findByLoginName("helloworld");
        assertNotNull(userModel);
    }

    @Test
    public void shouldFindUserByLoginNameIsNull() throws Exception {
        UserModel userModel = userMapper.findByLoginName("helloworld1");
        assertNull(userModel);
    }

    @Test
    public void shouldInsertUser() throws Exception {
        userMapper.create(this.getUserModelTest());

        UserModel userModel = userMapper.findByLoginName("helloworld");
        assertNotNull(userModel);

    }

    public UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
