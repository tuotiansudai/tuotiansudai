package com.tuotiansudai.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.BaseResponseDto;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration
@Transactional
public abstract class ControllerTestBase {
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected BaseResponseDto successResponseDto;

    protected MockMvc mockMvc;

    protected abstract Object getControllerObject();
    @Mock
    protected HttpServletRequest httpServletRequest;


    @Before
    public void baseSetup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(getControllerObject()).build();
        when(httpServletRequest.getAttribute(anyString())).thenReturn("loginName");
        successResponseDto = new BaseResponseDto();
        successResponseDto.setCode("0000");
    }
    
    protected String generateRequestJson(BaseParamDto requestParamDto) throws JsonProcessingException {
        requestParamDto.setBaseParam(BaseParamTest.getInstance());
        return objectMapper.writeValueAsString(requestParamDto);
    }

    protected ResultActions doRequestWithServiceMockedTest(String url) throws Exception {
        url = "/v1.0" + url;
        return mockMvc.perform(post(url).
                contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code").value("0000"));
    }

    protected ResultActions doRequestWithServiceIsOkMockedTest(String url, BaseParamDto requestDto) throws Exception {
        url = "/v1.0" + url;
        String requestJson = generateRequestJson(requestDto);

        return mockMvc.perform(post(url).
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    protected ResultActions doRequestWithServiceMockedTest(String url, BaseParamDto requestDto) throws Exception {
        return doRequestWithServiceIsOkMockedTest(url, requestDto)
                .andExpect(jsonPath("$.code").value("0000"));
    }
    protected ResultActions doRequestWithServiceIsBadRequestMockedTest(String url, BaseParamDto requestDto) throws Exception {
        url = "/v1.0" + url;
        String requestJson = generateRequestJson(requestDto);
        return mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isOk());

    }

}
