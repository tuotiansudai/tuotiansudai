package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by hourglasskoala on 15/6/19.
 */
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
        String email = "123123124@abc.com";
        userModel.setEmail(email);
        when(userMapper.findUserByEmail(anyString())).thenReturn(userModel);
        boolean isExistEmailFlag = userServiceImpl.isExistEmail(email);
        System.out.println(isExistEmailFlag);


    }
}
