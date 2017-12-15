package com.tuotiansudai.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.request.RegisterRequestDto;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.message.WeChatBoundMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MockUserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Mock
    private PrepareUserMapper prepareUserMapper;

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
        UserModel newUserModel = new UserModel();
        newUserModel.setLoginName(loginName);
        doNothing().when(mqWrapperClient).sendMessage(any(MessageQueue.class), any(WeChatBoundMessage.class));
        when(userMapper.findByLoginName(loginName)).thenReturn(null);
        when(userMapper.findByMobile(mobile)).thenReturn(null);
        when(smsCaptchaService.verifyMobileCaptcha(mobile, captcha, SmsCaptchaType.REGISTER_CAPTCHA)).thenReturn(true);
        when(prepareUserMapper.findByMobile(anyString())).thenReturn(null);
        when(registerUserService.register(any(RegisterRequestDto.class))).thenReturn(newUserModel);
        MembershipModel membershipModel = new MembershipModel();
        membershipModel.setId(1);
        membershipModel.setLevel(0);

        boolean success = userService.registerUser(registerUserDto);

        assertTrue(success);
        ArgumentCaptor<RegisterRequestDto> userModelArgumentCaptor = ArgumentCaptor.forClass(RegisterRequestDto.class);

        verify(registerUserService, times(1)).register(userModelArgumentCaptor.capture());

    }
}
