package com.tuotiansudai.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.security.CaptchaVerifier;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml"})
public class RegisterControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private UserService userService;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Mock
    private CaptchaVerifier captchaVerifier;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".ftl");

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.registerController)
                .setViewResolvers(viewResolver)
                .build();
    }


    @Test
    public void shouldMobileIsExist() throws Exception {
        when(userService.mobileIsExist(anyString())).thenReturn(true);

        this.mockMvc.perform(get("/register/mobile/13900000000/isexist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldMobileIsNotExist() throws Exception {
        when(userService.mobileIsExist(anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/mobile/13900000000/isexist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }

    @Test
    public void shouldLoginNameIsExist() throws Exception {
        when(userService.loginNameIsExist(anyString())).thenReturn(true);

        this.mockMvc.perform(get("/register/loginName/loginName/isexist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldLoginNameIsNotExist() throws Exception {
        when(userService.loginNameIsExist(anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/loginName/loginName/isexist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }

    @Test
    public void shouldRegisterUser() throws Exception{
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setLoginName("loginName");
        registerUserDto.setMobile("13900000000");
        registerUserDto.setPassword("password");
        registerUserDto.setCaptcha("123456");
        registerUserDto.setAgreement(true);
        String json = objectMapper.writeValueAsString(registerUserDto);

        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(true);

        this.mockMvc.perform(post("/register/user").contentType("application/json; charset=UTF-8").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldVerifyCaptchaIsValid() throws Exception {
        when(smsCaptchaService.verifyRegisterCaptcha(anyString(), anyString())).thenReturn(true);

        this.mockMvc.perform(get("/register/mobile/13900000000/captcha/123456/verify")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));

    }

    @Test
    public void shouldVerifyCaptchaIsInValid() throws Exception {
        when(smsCaptchaService.verifyRegisterCaptcha(anyString(), anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/mobile/13900000000/captcha/123456/verify")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));

    }

    @Test
    public void shouldSendRegisterCaptchaSuccess() throws Exception {
        when(smsCaptchaService.sendRegisterCaptcha(anyString())).thenReturn(true);
        when(captchaVerifier.registerPhotoCaptchaVerify(anyString())).thenReturn(true);
        this.mockMvc.perform(get("/register/mobile/13900000000/123456/sendregistercaptcha")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldSendRegisterCaptchaFailed() throws Exception {
        when(smsCaptchaService.sendRegisterCaptcha(anyString())).thenReturn(true);
        when(captchaVerifier.registerPhotoCaptchaVerify(anyString())).thenReturn(false);
        this.mockMvc.perform(get("/register/mobile/13900000000/123456/sendregistercaptcha")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));

    }

    @Test
    public void shouldPhotoCaptchaVerify() throws Exception {
        when(captchaVerifier.registerPhotoCaptchaVerify(anyString())).thenReturn(true);

        this.mockMvc.perform(get("/photo/captcha/123456/verify")).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }

    @Test
    public void shouldNotFoundWhenMobileIsInvalid() throws Exception {
        when(smsCaptchaService.sendRegisterCaptcha(anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/mobile/abc/sendRegisterCaptcha"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDisplayRegisterTemplate() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("/register"));
    }
}
