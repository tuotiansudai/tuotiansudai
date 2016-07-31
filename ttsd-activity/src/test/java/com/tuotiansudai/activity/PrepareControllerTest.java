package com.tuotiansudai.activity;


import com.tuotiansudai.activity.controller.PrepareController;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.service.PrepareService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml", "classpath:spring-security.xml"})
@WebAppConfiguration
@Transactional
public class PrepareControllerTest {

    @Mock
    private PrepareService prepareService;


    private MockMvc mockMvc;

    @InjectMocks
    private PrepareController prepareController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.prepareController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void shouldPrepareRegister() throws Exception {
        BaseDataDto dataDto = new BaseDataDto(true, null);

        when(prepareService.prepareRegister(any(PrepareRegisterRequestDto.class))).thenReturn(dataDto);

        this.mockMvc.perform(post("/prepare/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("referrerMobile", "18999999999")
                .param("mobile", "18988888888")
                .param("channel", "IOS"))
                .andExpect(status().isOk());

    }

}
