package com.tuotiansudai.service;

import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.impl.UserServiceImpl;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.MyShaPasswordEncoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.CapturesArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MockUserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Mock
    private MyShaPasswordEncoder myShaPasswordEncoder;
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldUserEmailIsExist() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setEmail("1234@abc.com");
        when(userMapper.findByEmail(anyString())).thenReturn(userModel);
        boolean emailIsExist = userService.emailIsExist(anyString());
        assertTrue(emailIsExist);
    }

    @Test
    public void shouldUserEmailIsNotExist() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setEmail("123@abc.com");

        when(userMapper.findByEmail(anyString())).thenReturn(null);

        boolean emailIsExist = userService.emailIsExist(anyString());
        assertFalse(emailIsExist);
    }

    @Test
    public void shouldUserMobileIsExist() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setMobile("18610361804");

        when(userMapper.findByMobile(anyString())).thenReturn(userModel);

        boolean mobileIsExist = userService.mobileIsExist(anyString());
        assertTrue(mobileIsExist);

    }

    @Test
    public void shouldUserMobileIsNotExist() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setMobile("18610361804");

        when(userMapper.findByMobile(anyString())).thenReturn(null);

        boolean mobileIsExist = userService.mobileIsExist(anyString());
        assertFalse(mobileIsExist);

    }

    @Test
    public void shouldLoginNameIsExist() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setLoginName("hourglass");

        when(userMapper.findByLoginName(anyString())).thenReturn(userModel);

        boolean isExist = userService.loginNameIsExist(anyString());

        assertTrue(isExist);
    }

    @Test
    public void shouldLoginNameIsNotExist() throws Exception {
        when(userMapper.findByLoginName(anyString())).thenReturn(null);

        boolean isExist = userService.loginNameIsExist(anyString());

        assertFalse(isExist);
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        String mobile = "mobile";
        String captcha = "123456";
        String loginName = "loginName";
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setLoginName(loginName);
        registerUserDto.setMobile(mobile);
        registerUserDto.setCaptcha(captcha);
        registerUserDto.setPassword("password");
        doNothing().when(userMapper).create(any(UserModel.class));
        when(userMapper.findByLoginName(loginName)).thenReturn(null);
        when(userMapper.findByMobile(mobile)).thenReturn(null);
        when(smsCaptchaService.verifyRegisterCaptcha(mobile, captcha)).thenReturn(true);
        when(myShaPasswordEncoder.encodePassword(anyString(), anyString())).thenReturn("salt");

        boolean success = userService.registerUser(registerUserDto);

        assertTrue(success);
        ArgumentCaptor<UserRoleModel> userRoleModelArgumentCaptor = ArgumentCaptor.forClass(UserRoleModel.class);
        verify(userRoleMapper, times(1)).create(userRoleModelArgumentCaptor.capture());
        assertThat(userRoleModelArgumentCaptor.getValue().getRole(), is(Role.USER));
    }

    @Test
    public void shouldSaveReferrerRelations() {
        UserModel user1 = new UserModel();
        user1.setId(idGenerator.generate());
        user1.setLoginName("test1");
        user1.setPassword("123");
        user1.setMobile("13900000000");
        user1.setRegisterTime(new Date());
        user1.setLastLoginTime(new Date());
        user1.setLastModifiedTime(new Date());
        user1.setStatus(UserStatus.ACTIVE);
        user1.setSalt("123");
        userMapper.create(user1);
        UserModel user2 = new UserModel();
        user2.setId(idGenerator.generate());
        user2.setLoginName("test2");
        user2.setPassword("123");
        user2.setMobile("13900000001");
        user2.setRegisterTime(new Date());
        user2.setLastLoginTime(new Date());
        user2.setLastModifiedTime(new Date());
        user2.setStatus(UserStatus.ACTIVE);
        user2.setSalt("123");
        user2.setReferrer("test1");
        userMapper.create(user2);

        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName("test1");
        referrerRelationModel.setLoginName("test2");
        referrerRelationModel.setLevel(1);

        referrerRelationMapper.create(referrerRelationModel);


        UserModel user3 = new UserModel();
        user3.setId(idGenerator.generate());
        user3.setLoginName("test3");
        user3.setPassword("123");
        user3.setMobile("13900000002");
        user3.setRegisterTime(new Date());
        user3.setLastLoginTime(new Date());
        user3.setLastModifiedTime(new Date());
        user3.setStatus(UserStatus.ACTIVE);
        user3.setSalt("123");
        user3.setReferrer("test2");
        userMapper.create(user3);

        userService.saveReferrerRelations("test2", "test3");

        List<ReferrerRelationModel> models = referrerRelationMapper.findByLoginName("test3");

        for (ReferrerRelationModel model : models) {
            if ("test1".equals(model.getReferrerLoginName())) {
                assertEquals(2, model.getLevel());
            }
        }

    }
}
