package com.tuotiansudai.current.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.current.dto.RedeemResponseDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RedeemDataDto;
import com.tuotiansudai.repository.model.Source;
import org.apache.log4j.MDC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CurrentRestClientTest {

    @Value("${current.rest.server}")
    private String currentRestServerUrl;

    @Autowired
    private CurrentRestClient currentRestClient;

    private MockWebServer mockServer;
    private ObjectMapper objectMapper;

    @Before
    public void mockCurrentRestService() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.mockServer = new MockWebServer();
        URL url = new URL(currentRestServerUrl);
        this.mockServer.start(InetAddress.getByName(url.getHost()), url.getPort());
    }

    @Test
    public void shouldInvest() throws JsonProcessingException {
        String currentUserId = "yyyyyy";
        MDC.put("requestId", "xxxxxxx");
        MDC.put("userId", currentUserId);
        DepositRequestDto requestDto = new DepositRequestDto("loginName", 10000, Source.ANDROID, false);

        this.mockServer.enqueue(buildCreatePayFormResponse());
        BaseDto<PayFormDataDto> data = currentRestClient.deposit(requestDto);
        assertTrue(data.isSuccess());
        PayFormDataDto formDataDto = data.getData();
        assertEquals("url", formDataDto.getUrl());
        assertEquals("OK", formDataDto.getMessage());
        assertTrue(formDataDto.getFields().isEmpty());
    }

    @Test
    public void shouldNoPasswordInvest() throws JsonProcessingException {
        String currentUserId = "yyyyyy";
        MDC.put("requestId", "xxxxxxx");
        MDC.put("userId", currentUserId);
        DepositRequestDto requestDto = new DepositRequestDto("loginName", 10000, Source.ANDROID, false);

        this.mockServer.enqueue(buildCreatePayDataResponse());
        BaseDto<PayDataDto> data = currentRestClient.noPasswordDeposit(requestDto);
        assertTrue(data.isSuccess());
        PayDataDto payData = data.getData();
        assertEquals("0000", payData.getCode());
        assertEquals("OK", payData.getMessage());
        assertTrue(payData.getExtraValues().isEmpty());
    }

    @Test
    public void shouldRedeemCreate() throws JsonProcessingException {
        String currentUserId = "yyyyyy";
        MDC.put("requestId", "xxxxxxx");
        MDC.put("userId", currentUserId);
        RedeemRequestDto requestDto = new RedeemRequestDto("loginName", 10000, Source.ANDROID);

        this.mockServer.enqueue(buildCreateRedeemResponse());
        RedeemResponseDto dataDto = currentRestClient.redeem(requestDto);
        assertEquals(10000, dataDto.getAmount());
        assertEquals("ANDROID", dataDto.getSource());
    }

    private MockResponse buildCreatePayFormResponse() throws JsonProcessingException {
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(true);
        payFormDataDto.setMessage("OK");
        payFormDataDto.setUrl("url");
        payFormDataDto.setFields(Collections.emptyMap());
        BaseDto<PayFormDataDto> responseDto = new BaseDto<>(true, payFormDataDto);
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.setBody(objectMapper.writeValueAsString(responseDto));
        return mockResponse;
    }

    private MockResponse buildCreatePayDataResponse() throws JsonProcessingException {
        PayDataDto payData = new PayDataDto();
        payData.setStatus(true);
        payData.setMessage("OK");
        payData.setCode("0000");
        payData.setExtraValues(Collections.emptyMap());
        BaseDto<PayDataDto> responseDto = new BaseDto<>(true, payData);
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.setBody(objectMapper.writeValueAsString(responseDto));
        return mockResponse;
    }

    private MockResponse buildCreateRedeemResponse() throws JsonProcessingException {
        RedeemResponseDto responseDto = new RedeemResponseDto();
        responseDto.setAmount(10000);
        responseDto.setSource("ANDROID");
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.setBody(objectMapper.writeValueAsString(responseDto));
        return mockResponse;
    }

    @After
    public void shutdownCurrentRestServer() throws IOException {
        this.mockServer.shutdown();
    }
}
