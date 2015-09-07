package com.tuotiansudai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.MonitorDataDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
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
        payWrapperClient.setContext("");

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

    @Test
    public void shouldMonitor() throws Exception {
        MockResponse mockResponse = new MockResponse();
        BaseDto<MonitorDataDto> baseDto = new BaseDto<>();
        MonitorDataDto dataDto = new MonitorDataDto();
        dataDto.setStatus(true);
        dataDto.setDatabaseStatus(true);
        dataDto.setRedisStatus(true);
        baseDto.setData(dataDto);

        mockResponse.setBody(objectMapper.writeValueAsString(baseDto));
        server.enqueue(mockResponse);
        payWrapperClient.setHost(server.getHostName());
        payWrapperClient.setPort(String.valueOf(server.getPort()));
        payWrapperClient.setContext("");

        BaseDto<MonitorDataDto> actualBaseDto = payWrapperClient.monitor();

        assertTrue(actualBaseDto.isSuccess());
        assertTrue(actualBaseDto.getData().getStatus());
        assertTrue(actualBaseDto.getData().isDatabaseStatus());
        assertTrue(actualBaseDto.getData().isRedisStatus());
    }
}
