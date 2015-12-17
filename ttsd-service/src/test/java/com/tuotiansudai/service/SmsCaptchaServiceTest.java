package com.tuotiansudai.service;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.SmsCaptchaModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class SmsCaptchaServiceTest {
    private MockWebServer server;

    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

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
    public void shouldSendSmsByMobileRegisterIsOk() throws Exception {
        MockResponse mockResponse = new MockResponse();
        String jsonString = "{\"success\":true,\"data\":{\"status\":true}}";
        mockResponse.setBody(jsonString);
        server.enqueue(mockResponse);
        smsWrapperClient.setHost(server.getHostName());
        smsWrapperClient.setPort(String.valueOf(server.getPort()));
        smsWrapperClient.setApplicationContext("");
        BaseDto<SmsDataDto> dto = smsCaptchaService.sendRegisterCaptcha("13900000000", "127.0.0.1");

        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findByMobile("13900000000");

        assertTrue(dto.getData().getStatus());
        assertNotNull(smsCaptchaModel);

    }

    @Test
    public void shouldSendSmsByMobileRegisterIsFail() throws Exception {
        MockResponse mockResponse = new MockResponse();
        String jsonString = "{\"success\":true,\"data\":{\"status\":false}}";
        mockResponse.setBody(jsonString);
        server.enqueue(mockResponse);
        smsWrapperClient.setHost("http://" + server.getHostName() + ":" + server.getPort());
        BaseDto<SmsDataDto> dto = smsCaptchaService.sendRegisterCaptcha("13900000000", "127.0.0.1");
        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findByMobile("13900000000");

        assertFalse(dto.getData().getStatus());
        assertNotNull(smsCaptchaModel);
    }

    public SmsCaptchaModel getSmsCaptchaModel() {
        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setCaptcha("12345");
        smsCaptchaModel.setMobile("13900000000");
        smsCaptchaModel.setExpiredTime(new Date());
        smsCaptchaModel.setCreatedTime(new Date());
        smsCaptchaModel.setCaptchaType(CaptchaType.REGISTER_CAPTCHA);
        return smsCaptchaModel;
    }


}