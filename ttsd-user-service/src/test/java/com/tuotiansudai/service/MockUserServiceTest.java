package com.tuotiansudai.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.message.WeChatBoundMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.impl.UserServiceImpl;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
    private SmsCaptchaService smsCaptchaService;

    @Mock
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Mock
    private PrepareUserMapper prepareUserMapper;

    @Mock
    private LoginNameGenerator loginNameGenerator;

    @Mock
    private RegisterUserService registerUserService;

    @Mock
    private MQWrapperClient mqWrapperClient;


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
        when(userMapper.create(any(UserModel.class))).thenReturn(1);
        doNothing().when(mqWrapperClient).sendMessage(any(MessageQueue.class), any(WeChatBoundMessage.class));
        when(loginNameGenerator.generate()).thenReturn(loginName);
        when(userMapper.findByLoginName(loginName)).thenReturn(null);
        when(userMapper.findByMobile(mobile)).thenReturn(null);
        when(smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.REGISTER_CAPTCHA)).thenReturn(true);
        when(myShaPasswordEncoder.encodePassword(anyString(), anyString())).thenReturn("salt");
        when(prepareUserMapper.findByMobile(anyString())).thenReturn(null);
        when(registerUserService.register(any(UserModel.class))).thenReturn(true);
        MembershipModel membershipModel = new MembershipModel();
        membershipModel.setId(1);
        membershipModel.setLevel(0);

        boolean success = userService.registerUser(registerUserDto);

        assertTrue(success);
        ArgumentCaptor<UserModel> userModelArgumentCaptor = ArgumentCaptor.forClass(UserModel.class);

        verify(registerUserService, times(1)).register(userModelArgumentCaptor.capture());

    }
}
