package com.tuotiansudai.smswrapper.client;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.repository.mapper.RegisterCaptchaMapper;
import okio.Buffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.text.MessageFormat;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SmsClientTest {

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
    @Transactional
    public void shouldGetResultCode() throws Exception {
        MockResponse mockResponse = new MockResponse();
        String responseBodyTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<string xmlns=\"http://tempuri.org/\">{0}</string>";
        String resultCode = "resultCode";
        mockResponse.setBody(MessageFormat.format(responseBodyTemplate, resultCode));
        server.enqueue(mockResponse);
        URL url = server.getUrl("/webservice.asmx/mdSmsSend_u");
        this.smsClient.setUrl(url.toString());

        String mobile = "13900000000";
        String content = "content";
        BaseDto<SmsDataDto> dto = this.smsClient.sendSMS(RegisterCaptchaMapper.class, mobile, content, "127.0.0.1");

        RecordedRequest recordedRequest = server.takeRequest();
        Buffer body = recordedRequest.getBody();

        String requestBody = new String(body.readByteArray(), "UTF8");

        assertTrue(requestBody.contains("mobile=" + mobile));
        assertTrue(requestBody.contains("content=" + content));
        assertTrue(dto.getData().getStatus());
    }
}
