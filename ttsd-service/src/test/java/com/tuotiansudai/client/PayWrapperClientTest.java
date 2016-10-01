package com.tuotiansudai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PayWrapperClientTest {

    private MockWebServer server;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Before
    public void setUp() throws Exception {
        this.server = new MockWebServer();
        this.server.start();
    }

    @After
    public void tearDown() throws Exception {
        this.server.shutdown();
    }

    @Test
    public void shouldRegister() throws Exception {
        MockResponse mockResponse = new MockResponse();
        BaseDto baseDto = new BaseDto();
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(true);
        dataDto.setCode("0000");
        dataDto.setMessage("success");
        baseDto.setData(dataDto);

        mockResponse.setBody(objectMapper.writeValueAsString(baseDto));
        server.enqueue(mockResponse);
        payWrapperClient.setHost(server.getHostName());
        payWrapperClient.setPort(String.valueOf(server.getPort()));
        payWrapperClient.setApplicationContext("");

        RegisterAccountDto dto = new RegisterAccountDto();
        dto.setLoginName("loginName");
        dto.setUserName("userName");
        dto.setIdentityNumber("identityNumber");
        dto.setMobile("mobile");
        BaseDto<PayDataDto> actualBaseDto = payWrapperClient.register(dto);

        assertTrue(actualBaseDto.isSuccess());
        assertTrue(actualBaseDto.getData().getStatus());
        assertThat(actualBaseDto.getData().getCode(), is(dataDto.getCode()));
        assertThat(actualBaseDto.getData().getMessage(), is(dataDto.getMessage()));
    }
}
