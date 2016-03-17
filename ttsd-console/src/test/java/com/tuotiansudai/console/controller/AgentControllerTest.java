package com.tuotiansudai.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.dto.AgentDto;
import com.tuotiansudai.exception.CreateAgentException;
import com.tuotiansudai.service.AgentService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:spring-security.xml", "classpath:applicationContext.xml"})
@Transactional
public class AgentControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private AgentController agentController;

    @Mock
    private AgentService agentService;

    @Before
    public void ini(){
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".ftl");
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.agentController).setViewResolvers(viewResolver).build();
    }

    @Test
    public void shouldCreateIsSuccess() throws Exception {

        this.mockMvc.perform(post("/user-manage/agent/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("loginName", "loginName").param("level", "1").param("rate","2.00"))
                .andExpect(redirectedUrl("/user-manage/agents"));
    }
    @Test
    public void shouldCreateIsFail() throws Exception {
        doThrow(new CreateAgentException("hello world")).when(agentService).create(any(AgentDto.class));
        this.mockMvc.perform(post("/user-manage/agent/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("loginName", "loginName").param("level", "1").param("rate","2.00"))
                .andExpect(redirectedUrl("/user-manage/agent/create"));
    }
}
