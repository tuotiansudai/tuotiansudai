package com.tuotiansudai.client;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.client.dto.ResultDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration
public class SendSmsClientTest {
    @Autowired
    private SmsClient smsClient;

    private MockWebServer server;

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
    public void jsonConvertToObject() {
        String jsonString = "{\"success\":true,\"data\":{\"status\":false}}";
        ResultDto resultDto = smsClient.jsonConvertToObject(jsonString);

        assertNotNull(resultDto);
        assertEquals(false, resultDto.getData().getStatus());

    }

    @Test
    @Transactional
    public void shouldSendCaptchaIsOk() throws Exception {
        MockResponse mockResponse = new MockResponse();
        String jsonString = "{\"success\":true,\"data\":{\"status\":false}}";
        mockResponse.setBody(jsonString);
        server.enqueue(mockResponse);
        smsClient.setHost("http://"+server.getHostName()+":"+server.getPort());
        String mobile = "13900000000";
        String code = "1000";
        ResultDto resultDto = this.smsClient.sendSms(mobile, code);

        assertNotNull(resultDto);
        assertFalse(resultDto.getData().getStatus());

    }
}
