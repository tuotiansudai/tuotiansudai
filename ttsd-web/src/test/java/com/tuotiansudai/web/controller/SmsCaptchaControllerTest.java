package com.tuotiansudai.web.controller;

import com.tuotiansudai.repository.model.UserModel;
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


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml"})
public class SmsCaptchaControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private SmsCaptchaController smsCaptchaController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.smsCaptchaController).build();
    }
    @Test
    public void shouldSendSmsRegisterByMobileNumber(){

    }

}
