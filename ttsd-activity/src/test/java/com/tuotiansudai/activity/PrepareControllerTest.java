package com.tuotiansudai.activity;


import com.tuotiansudai.activity.controller.PrepareController;
import com.tuotiansudai.service.PrepareService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldPrepareRegister
}
