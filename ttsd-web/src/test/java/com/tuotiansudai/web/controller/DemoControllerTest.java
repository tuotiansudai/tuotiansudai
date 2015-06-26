package com.tuotiansudai.web.controller;


import com.tuotiansudai.repository.model.DemoModel;
import com.tuotiansudai.service.DemoService;
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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml"})
public class DemoControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private DemoController demoController;

    @Mock
    private DemoService demoService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.demoController).build();
    }

    @Test
    public void shouldGetHelloWorld() throws Exception {
        DemoModel demoModel = new DemoModel();
        demoModel.setId("demoId");
        when(demoService.getDemoById(anyString())).thenReturn(demoModel);

        this.mockMvc.perform(get("/helloworld")).
                andExpect(status().isOk()).
                andExpect(model().attribute("placeHolder", "demoId")).
                andExpect(view().name("/view.helloworld"));
    }
}
