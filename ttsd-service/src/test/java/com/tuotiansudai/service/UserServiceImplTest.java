package com.tuotiansudai.service;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by hourglasskoala on 15/6/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
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
    public void testIsExistEmail() throws Exception{
        UserModel userModel = new UserModel();
        userModel.setEmail("123@abc.com");
        when(userMapper.findUserByEmail(anyString())).thenReturn(userModel);
        boolean isExistEmail = userServiceImpl.isExistEmail(anyString());
        boolean testResult = true;
        assertThat(isExistEmail, is(testResult));
    }
    @Test
    public void testIsExistMobileNumber() throws Exception{
        UserModel userModel = new UserModel();
        userModel.setMobileNumber("18610361804");
        when(userMapper.findUserByMobileNumber(anyString())).thenReturn(userModel);
        boolean isExistMobileNumber = userServiceImpl.isExistMobileNumber(anyString());
        boolean testResult = true;
        assertThat(isExistMobileNumber, is(testResult));

    }
    @Test
    public void testIsExistReferrer() throws Exception{
        UserModel userModel = new UserModel();
        userModel.setLoginName("hourglass");
        when(userMapper.findUserByLoginName(anyString())).thenReturn(userModel);
        boolean isExistReferrer = userServiceImpl.isExistReferrer(anyString());
        boolean testResult = true;
        assertThat(isExistReferrer, is(testResult));

    }
}
