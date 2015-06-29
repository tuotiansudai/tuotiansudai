package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserMapper userMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldUserEmailIsExisted() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setEmail("1234@abc.com");
        when(userMapper.findUserByEmail(anyString())).thenReturn(userModel);
        boolean isExistedEmail = userServiceImpl.userEmailIsExisted(anyString());
        assertTrue(isExistedEmail);
    }

    @Test
    public void shouldUserEmailIsNotExisted() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setEmail("123@abc.com");

        when(userMapper.findUserByEmail(anyString())).thenReturn(null);

        boolean isExistedEmail = userServiceImpl.userEmailIsExisted(anyString());
        assertFalse(isExistedEmail);
    }

    @Test
    public void shouldUserMobileNumberIsExisted() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setMobileNumber("18610361804");

        when(userMapper.findUserByMobileNumber(anyString())).thenReturn(userModel);

        boolean isExistedMobileNumber = userServiceImpl.userMobileNumberIsExisted(anyString());
        assertTrue(isExistedMobileNumber);

    }

    @Test
    public void shouldUserMobileNumberIsNotExisted() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setMobileNumber("18610361804");

        when(userMapper.findUserByMobileNumber(anyString())).thenReturn(null);

        boolean isExistedMobileNumber = userServiceImpl.userMobileNumberIsExisted(anyString());
        assertFalse(isExistedMobileNumber);

    }

    @Test
    public void shouldReferrerIsExisted() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setLoginName("hourglass");

        when(userMapper.findUserByLoginName(anyString())).thenReturn(userModel);

        boolean isExistedReferrer = userServiceImpl.referrerIsExisted(anyString());

        assertTrue(isExistedReferrer);
    }

    @Test
    public void shouldReferrerIsNotExisted() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setLoginName("hourglass");

        when(userMapper.findUserByLoginName(anyString())).thenReturn(null);

        boolean isExistedReferrer = userServiceImpl.referrerIsExisted(anyString());

        assertFalse(isExistedReferrer);
    }

    @Test
    public void shouldLoginNameIsExisted() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setLoginName("hourglass");

        when(userMapper.findUserByLoginName(anyString())).thenReturn(userModel);

        boolean isExist = userServiceImpl.loginNameIsExisted(anyString());

        assertTrue(isExist);
    }

    @Test
    public void shouldLoginNameIsNotExisted() throws Exception {

        when(userMapper.findUserByLoginName(anyString())).thenReturn(null);

        boolean isExist = userServiceImpl.loginNameIsExisted(anyString());

        assertFalse(isExist);
    }

    @Test
    public void testRegisterUser() throws Exception{
        UserModel userModel = new UserModel();
        userModel.setLoginName("zourenzheng");
        userModel.setEmail("zourenzheng@tuotiansudai.com");
        userModel.setMobileNumber("13436964915");
        userModel.setPassword("123abc");
        userModel.setStatus(UserStatus.ACTIVE);
        doNothing().when(userMapper).insertUser(any(UserModel.class));

        boolean success = userServiceImpl.registerUser(userModel);
        assertNotNull(userModel.getSalt());
        assertTrue(success);
    }
}
