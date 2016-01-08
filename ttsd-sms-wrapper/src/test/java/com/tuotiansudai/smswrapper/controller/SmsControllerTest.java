package com.tuotiansudai.smswrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.SmsCaptchaDto;
import com.tuotiansudai.dto.SmsFatalNotifyDto;
import com.tuotiansudai.smswrapper.client.SmsClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class SmsControllerTest {

    private MockMvc mockMvc;

    private MockWebServer server;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SmsClient smsClient;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.server = new MockWebServer();
        this.server.start();
    }

    @After
    public void tearDown() throws Exception {
        this.server.shutdown();
    }

    @Test
    public void shouldGetResultCode() throws Exception {
        String responseBodyTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<string xmlns=\"http://tempuri.org/\">{0}</string>";
        String resultCode = "1234";

        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.setBody(MessageFormat.format(responseBodyTemplate, resultCode));
        mockResponse.setHeader("content-type", "application/json; charset=UTF-8");

        server.enqueue(mockResponse);
        URL url = server.getUrl("/");

        this.smsClient.setUrl(url.toString());

        String fakeIp = String.valueOf(new Date().getTime());

        SmsCaptchaDto dto = new SmsCaptchaDto("18611445119", "100022", fakeIp);
        String requestData = this.objectMapper.writeValueAsString(dto);
        jsonPath(requestData);

        this.mockMvc.perform(post("/sms/register-captcha")
                .contentType("application/json; charset=UTF-8")
                .content(requestData)
                .contentType(MediaType.parseMediaType("application/json; charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    @Test
    public void shouldReceiveMessage() throws Exception {
        String responseBodyTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<string xmlns=\"http://tempuri.org/\">{0}</string>";
        String resultCode = "1234";

        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.setBody(MessageFormat.format(responseBodyTemplate, resultCode));
        mockResponse.setHeader("content-type", "application/json; charset=UTF-8");

        server.enqueue(mockResponse);
        URL url = server.getUrl("/");

        this.smsClient.setUrl(url.toString());

        SmsFatalNotifyDto dto = new SmsFatalNotifyDto("pay_back_notify_fail,take_as_invest_success,orderId:12341234123412341234,LoginName:zhoubaoxin,amount:100000000,loanId:1000000");
        String requestData = this.objectMapper.writeValueAsString(dto);
        jsonPath(requestData);

        this.mockMvc.perform(post("/sms/invest-fatal-notify")
                .contentType("application/json; charset=UTF-8")
                .content(requestData)
                .contentType(MediaType.parseMediaType("application/json; charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

}
