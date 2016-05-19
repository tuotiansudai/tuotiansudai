package com.tuotiansudai.smswrapper.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.SmsClient;
import com.tuotiansudai.smswrapper.repository.mapper.TurnOffNoPasswordInvestCaptchaMapper;
import com.tuotiansudai.smswrapper.repository.mapper.RegisterCaptchaMapper;
import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class SmsServiceTest {

    private MockWebServer server;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsClient smsClient;

    @Autowired
    private RegisterCaptchaMapper registerCaptchaMapper;

    @Autowired
    private TurnOffNoPasswordInvestCaptchaMapper noPasswordInvestMapper;

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
    public void shouldSendRegisterCaptcha() throws Exception {
        MockResponse mockResponse = new MockResponse();
        String responseBodyTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<string xmlns=\"http://tempuri.org/\">{0}</string>";
        String resultCode = "1";
        mockResponse.setBody(MessageFormat.format(responseBodyTemplate, resultCode));
        server.enqueue(mockResponse);
        URL url = server.getUrl("/webservice.asmx/mdSmsSend_u");
        this.smsClient.setUrl(url.toString());
        String mobile = String.valueOf(new BigDecimal(Math.random() * 9 + 1).multiply(new BigDecimal(10000000000L)).longValue());
        String captcha = "9999";

        this.smsService.sendRegisterCaptcha(mobile, captcha, null);

        List<SmsModel> records = this.registerCaptchaMapper.findByMobile(mobile);

        assert records.size() == 1;

        SmsModel record = records.get(0);

        assertThat(record.getMobile(), is(mobile));

        List<String> paramList = ImmutableList.<String>builder().add(captcha).build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.generateContent(paramList);
        assertThat(record.getContent(), is(content));
        assertThat(record.getResultCode(), is(resultCode));
    }

    @Test
    @Transactional
    public void shouldSendNoPasswordInvestCaptcha() throws Exception {
        MockResponse mockResponse = new MockResponse();
        String responseBodyTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<string xmlns=\"http://tempuri.org/\">{0}</string>";
        String resultCode = "1";
        mockResponse.setBody(MessageFormat.format(responseBodyTemplate, resultCode));
        server.enqueue(mockResponse);
        URL url = server.getUrl("/webservice.asmx/mdSmsSend_u");
        this.smsClient.setUrl(url.toString());
        String mobile = String.valueOf(new BigDecimal(Math.random() * 9 + 1).multiply(new BigDecimal(10000000000L)).longValue());
        String captcha = "9999";

        this.smsService.sendNoPasswordInvestCaptcha(mobile, captcha, null);

        List<SmsModel> records = this.noPasswordInvestMapper.findByMobile(mobile);

        assert records.size() == 1;

        SmsModel record = records.get(0);

        assertThat(record.getMobile(), is(mobile));

        List<String> paramList = ImmutableList.<String>builder().add(captcha).build();
        String content = SmsTemplate.SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE.generateContent(paramList);
        assertThat(record.getContent(), is(content));
        assertThat(record.getResultCode(), is(resultCode));
    }
}
