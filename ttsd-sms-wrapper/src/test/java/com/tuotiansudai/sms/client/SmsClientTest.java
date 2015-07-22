package com.tuotiansudai.sms.client;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import okio.Buffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.text.MessageFormat;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
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
        String actualResultCode = this.smsClient.sendSMS(mobile, content);

        RecordedRequest recordedRequest = server.takeRequest();
        Buffer body = recordedRequest.getBody();

        String requestBody = new String(body.readByteArray(), "UTF8");

        assertTrue(requestBody.contains("mobile=" + mobile));
        assertTrue(requestBody.contains("content=" + content));
        assertThat(actualResultCode, is(resultCode));
    }

    @Test
    public void shouldGetNullWhenMobileIsEmptyOrNull() throws Exception {
        String resultCode1 = this.smsClient.sendSMS("", "content");

        assertNull(resultCode1);

        String resultCode2 = this.smsClient.sendSMS(null, "content");

        assertNull(resultCode2);
    }

    @Test
    public void shouldGetNullWhenContentIsEmptyOrNull() throws Exception {
        String resultCode1 = this.smsClient.sendSMS("13900000000", "");

        assertNull(resultCode1);

        String resultCode2 = this.smsClient.sendSMS("13900000000", null);

        assertNull(resultCode2);
    }
}
