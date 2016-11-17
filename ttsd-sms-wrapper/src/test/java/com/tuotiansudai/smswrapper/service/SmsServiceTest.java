package com.tuotiansudai.smswrapper.service;

import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.client.SmsClient;
import com.tuotiansudai.smswrapper.repository.mapper.RegisterCaptchaMapper;
import com.tuotiansudai.smswrapper.repository.mapper.TurnOffNoPasswordInvestCaptchaMapper;
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

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class SmsServiceTest {

    private MockWebServer server;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsClient smsClient;

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
    public void shouldSendRegisterCaptcha() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(SUCCESS_RESPONSE_BODY);
        server.enqueue(mockResponse);
        URL url = server.getUrl("/webservice.asmx/mdSmsSend_u");
        this.smsClient.setUrl(url.toString());
        String mobile = String.valueOf(new BigDecimal(Math.random() * 9 + 1).multiply(new BigDecimal(10000000000L)).longValue());
        String captcha = "9999";

        BaseDto<SmsDataDto> baseDto = this.smsService.sendRegisterCaptcha(mobile, captcha, null);
        assertTrue(baseDto.isSuccess());
    }

    @Test
    @Transactional
    public void shouldSendNoPasswordInvestCaptcha() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(SUCCESS_RESPONSE_BODY);
        server.enqueue(mockResponse);
        URL url = server.getUrl("/webservice.asmx/mdSmsSend_u");
        this.smsClient.setUrl(url.toString());
        String mobile = String.valueOf(new BigDecimal(Math.random() * 9 + 1).multiply(new BigDecimal(10000000000L)).longValue());
        String captcha = "9999";

        BaseDto<SmsDataDto> baseDto = this.smsService.sendNoPasswordInvestCaptcha(mobile, captcha, null);
        assertTrue(baseDto.isSuccess());
    }

    @Test
    @Transactional
    public void shouldSendGenerateContractEoor() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(SUCCESS_RESPONSE_BODY);
        server.enqueue(mockResponse);
        URL url = server.getUrl("/webservice.asmx/mdSmsSend_u");
        this.smsClient.setUrl(url.toString());

        BaseDto<SmsDataDto> baseDto = this.smsService.generateContractNotify(Lists.newArrayList(), 1111L);
        assertTrue(baseDto.isSuccess());
    }
}
