package com.tuotiansudai.sms.service;

import com.google.common.collect.ImmutableMap;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.sms.SmsTemplate;
import com.tuotiansudai.sms.client.SmsClient;
import com.tuotiansudai.sms.repository.mapper.RegisterCaptchaMapper;
import com.tuotiansudai.sms.repository.model.RegisterCaptchaModel;
import com.tuotiansudai.sms.service.impl.SmsServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@TransactionConfiguration
@Transactional
public class SmsServiceTest {

    private MockWebServer server;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsClient smsClient;

    @Autowired
    private RegisterCaptchaMapper registerCaptchaMapper;

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
    public void shouldSendRegisterCaptcha() throws Exception {
        MockResponse mockResponse = new MockResponse();
        String responseBodyTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<string xmlns=\"http://tempuri.org/\">{0}</string>";
        String resultCode = "1";
        mockResponse.setBody(MessageFormat.format(responseBodyTemplate, resultCode));
        server.enqueue(mockResponse);
        URL url = server.getUrl("/webservice.asmx/mdSmsSend_u");
        this.smsClient.setUrl(url.toString());

        String mobile = "13900000000";
        String captcha = "9999";

        this.smsService.sendRegisterCaptcha(mobile, captcha);

        List<RegisterCaptchaModel> records = this.registerCaptchaMapper.findByMobile(mobile);

        assertThat(records.size(), is(1));

        RegisterCaptchaModel record = records.get(0);

        assertThat(record.getMobile(), is(mobile));

        Map<String, String> map = ImmutableMap.<String, String>builder().put("captcha", captcha).build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.generateContent(map);
        assertThat(record.getContent(), is(content));
        assertThat(record.getResultCode(), is(resultCode));
    }
}
