package com.tuotiansudai.smswrapper.client;

import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.repository.mapper.RegisterCaptchaMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SmsClientTest {

    @Autowired
    private SmsClient smsClient;

    private MockWebServer server;

    private String SUCCESS_RESPONSE_BODY = "{\"code\":200,\"msg\":\"sendid\",\"obj\":1}";

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
    @Transactional
    public void shouldGetResultCode() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(SUCCESS_RESPONSE_BODY);
        server.enqueue(mockResponse);
        URL url = server.getUrl("/webservice.asmx/mdSmsSend_u");
        this.smsClient.setUrl(url.toString());

        String mobile = "13900000000";
        BaseDto<SmsDataDto> dto = this.smsClient.sendSMS(RegisterCaptchaMapper.class, Lists.newArrayList(mobile), SmsTemplate.SMS_BIRTHDAY_NOTIFY_TEMPLATE, Lists.newArrayList(), "127.0.0.1");

        assertTrue(dto.isSuccess());
    }
}
