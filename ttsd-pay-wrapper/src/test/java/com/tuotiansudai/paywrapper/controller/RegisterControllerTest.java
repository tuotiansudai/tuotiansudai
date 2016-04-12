package com.tuotiansudai.paywrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.paywrapper.service.RegisterService;
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
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml"})
public class RegisterControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private RegisterService registerService;

    private ObjectMapper objectMapper = new ObjectMapper();

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
    public void shouldRegisterAccount() throws Exception {
        RegisterAccountDto accountDto = new RegisterAccountDto();
        accountDto.setLoginName("loginName");
        accountDto.setUserName("userName");
        accountDto.setMobile("13900000000");
        accountDto.setIdentityNumber("123456789012345678");

        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(true);
        dataDto.setCode("0000");
        dataDto.setMessage("message");
        BaseDto baseDto = new BaseDto();
        baseDto.setData(dataDto);
        when(registerService.register(any(RegisterAccountDto.class))).thenReturn(baseDto);

        String requestJson = objectMapper.writeValueAsString(accountDto);
        this.mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true))
                .andExpect(jsonPath("$.data.code").value("0000"))
                .andExpect(jsonPath("$.data.message").value("message"));


    }

    @Test
    public void shouldBadRequest() throws Exception {
        RegisterAccountDto accountDto = new RegisterAccountDto();
        accountDto.setUserName("userName");
        accountDto.setMobile("13900000000");
        accountDto.setIdentityNumber("identityNumber");

        String requestJson = objectMapper.writeValueAsString(accountDto);
        this.mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson))
                .andExpect(status().isBadRequest());

    }
}
