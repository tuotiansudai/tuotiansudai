package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class AutoInvestControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private InvestController registerController;

    @Mock
    private InvestService investService;

    @Autowired
    private IdGenerator idGenerator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.registerController).build();
    }

    @Test
    public void shouldAutoInvest() throws Exception {
        doNothing().when(investService).autoInvest(anyLong());

        this.mockMvc.perform(post("/auto-invest").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(String.valueOf(idGenerator.generate())))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }
}
