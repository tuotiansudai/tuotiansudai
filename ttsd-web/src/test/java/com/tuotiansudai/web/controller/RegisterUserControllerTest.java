package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.PrepareUserService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.security.CaptchaHelper;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegisterUserControllerTest extends BaseControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RegisterUserController registerUserController;

    @Mock
    private UserService userService;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Mock
    private CaptchaHelper captchaHelper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PrepareUserService prepareService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".ftl");

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.registerUserController)
                .setViewResolvers(viewResolver)
                .build();
    }


    @Test
    public void shouldPrepareRegister() throws Exception {
        BaseDataDto dataDto = new BaseDataDto(true, null);

        when(prepareService.prepareRegister(any(PrepareRegisterRequestDto.class))).thenReturn(dataDto);

        this.mockMvc.perform(post("/register/user/shared-prepare")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("referrerMobile", "18999999999")
                .param("mobile", "18988888888")
                .param("channel", "IOS"))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldRegister() throws Exception {
        BaseDataDto dataDto = new BaseDataDto(true, null);
        when(prepareService.register(any(RegisterUserDto.class))).thenReturn(dataDto);
        this.mockMvc.perform(post("/register/user/shared")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("referrerMobile", "18999999999")
                .param("mobile", "18988888888")
                .param("password", "123abc")
                .param("captcha", "98765"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldLoginNameIsExist() throws Exception {
        when(userService.loginNameIsExist(anyString())).thenReturn(true);

        this.mockMvc.perform(post("/register/user/login-name/loginName/is-exist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldLoginNameIsNotExist() throws Exception {
        when(userService.loginNameIsExist(anyString())).thenReturn(false);

        this.mockMvc.perform(post("/register/user/login-name/loginName/is-exist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }

    @Test
    public void shouldVerifyCaptchaIsValid() throws Exception {
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(SmsCaptchaType.class))).thenReturn(true);

        this.mockMvc.perform(get("/register/user/mobile/13900000000/captcha/123456/verify")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));

    }

    @Test
    public void shouldVerifyCaptchaIsInValid() throws Exception {
        when(smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(SmsCaptchaType.class))).thenReturn(false);

        this.mockMvc.perform(get("/register/user/mobile/13900000000/captcha/123456/verify")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));

    }

    @Test
    public void shouldSendRegisterCaptchaSuccess() throws Exception {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(true);

        when(smsCaptchaService.sendRegisterCaptcha(anyString(), anyBoolean(), anyString())).thenReturn(baseDto);
        when(captchaHelper.captchaVerify(anyString(), anyString(), anyString())).thenReturn(true);
        this.mockMvc.perform(post("/register/user/send-register-captcha")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("mobile", "13900000000").param("imageCaptcha", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldSendRegisterCaptchaIsSuccess() throws Exception {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);

        dataDto.setStatus(true);
        when(userService.mobileIsRegister(anyString())).thenReturn(false);
        when(smsCaptchaService.sendRegisterCaptcha(anyString(), anyBoolean(), anyString())).thenReturn(baseDto);
        this.mockMvc.perform(get("/register/user/13900000000/send-register-captcha")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldSendRegisterCaptchaFailed() throws Exception {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);

        when(smsCaptchaService.sendRegisterCaptcha(anyString(), anyBoolean(), anyString())).thenReturn(baseDto);
        when(captchaHelper.captchaVerify(anyString(), anyString(), anyString())).thenReturn(false);
        this.mockMvc.perform(post("/register/user/send-register-captcha")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("mobile", "13900000000").param("imageCaptcha", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }

    @Test
    public void shouldNotFoundWhenMobileIsInvalid() throws Exception {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);

        when(smsCaptchaService.sendRegisterCaptcha(anyString(), anyBoolean(), anyString())).thenReturn(baseDto);

        this.mockMvc.perform(get("/register/user/mobile/abc/sendRegisterCaptcha"))
                .andExpect(status().isNotFound());
    }
}
