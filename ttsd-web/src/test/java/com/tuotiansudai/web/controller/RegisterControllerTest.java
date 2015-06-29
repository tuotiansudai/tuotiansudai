package com.tuotiansudai.web.controller;

import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private UserService userService;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.registerController).build();
    }

    @Test
    public void shouldUserEmailIsExisted() throws Exception {

        when(userService.userEmailIsExisted(anyString())).thenReturn(true);

        this.mockMvc.perform(get("/register/email/123@abc.com/verify"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldUserEmailIsNotExisted() throws Exception {

        when(userService.userEmailIsExisted(anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/email/123@abc.com/verify"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }

    @Test
    public void shouldMobileNumberIsExisted() throws Exception {

        when(userService.userMobileNumberIsExisted(anyString())).thenReturn(true);

        this.mockMvc.perform(get("/register/mobileNumber/13900000000/verify"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldMobileNumberIsNotExisted() throws Exception {

        when(userService.userEmailIsExisted(anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/mobileNumber/13900000000/verify"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }

    @Test
    public void shouldReferrerIsExisted() throws Exception {

        when(userService.referrerIsExisted(anyString())).thenReturn(true);

        this.mockMvc.perform(get("/register/referrer/hourglass/verify"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldReferrerIsNotExisted() throws Exception {

        when(userService.referrerIsExisted(anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/referrer/hourglass/verify"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }

    @Test
    public void shouldJsonRegisterUser() throws Exception{
        when(userService.registerUser(any(UserModel.class))).thenReturn(true);

        String jsonStr =  "{\"loginName\":\"zourenzheng\",\"password\":\"123abc\",\"email\":\"zourenzheng@tuotiansudai.com\",\"mobileNumber\":\"13436964915\"}";
        this.mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldVerifyCaptchaIsValid() throws Exception {
        when(smsCaptchaService.verifyCaptcha(anyString(), anyString())).thenReturn(true);

        this.mockMvc.perform(get("/register/mobile/13900000000/captcha/123456/verify")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));

    }
    @Test
    public void shouldVerifyCaptchaIsInValid() throws Exception {
        when(smsCaptchaService.verifyCaptcha(anyString(), anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/mobile/13900000000/captcha/123456/verify")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));

    }
    @Test
    public void shouldSendRegisterByMobileNumberSmsIsOk() throws Exception {
        when(smsCaptchaService.sendSmsByMobileNumberRegister(anyString())).thenReturn(true);

        this.mockMvc.perform(get("/register/mobile/13900000000/sendCaptcha")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldSendRegisterByMobileNumberSmsIsFailed() throws Exception {
        when(smsCaptchaService.sendSmsByMobileNumberRegister(anyString())).thenReturn(false);

        this.mockMvc.perform(get("/register/mobile/13900000000/sendCaptcha")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(false));
    }
}
