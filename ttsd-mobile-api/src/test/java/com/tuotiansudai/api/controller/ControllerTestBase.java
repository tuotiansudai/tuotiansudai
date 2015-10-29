package com.tuotiansudai.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseParamTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration
@Transactional
public class ControllerTestBase<MobileApiController extends Object> {
    protected MockMvc mockMvc;

    @Autowired
    private MobileApiController mobileApiController;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void baseSetUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(mobileApiController).build();
    }

    protected <T extends BaseParamDto> T generateBaseParam(T requestParamDto) {
        BaseParam baseParam = BaseParamTest.getInstance();
        requestParamDto.setBaseParam(baseParam);
        return requestParamDto;
    }

    protected String generateRequestJson(BaseParamDto requestParamDto) throws JsonProcessingException {
        requestParamDto.setBaseParam(BaseParamTest.getInstance());
        return objectMapper.writeValueAsString(requestParamDto);
    }
}
