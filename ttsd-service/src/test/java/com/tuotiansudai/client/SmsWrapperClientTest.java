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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SmsWrapperClientTest {

    @Autowired
    private SmsWrapperClient smsWrapperClient;

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
        ResultDto resultDto = smsWrapperClient.jsonConvertToObject(jsonString);

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
        smsWrapperClient.setHost("http://"+server.getHostName()+":"+server.getPort());
        String mobile = "13900000000";
        String code = "1000";
        ResultDto resultDto = this.smsWrapperClient.sendSms(mobile, code);

        assertNotNull(resultDto);
        assertFalse(resultDto.getData().getStatus());

    }
}
