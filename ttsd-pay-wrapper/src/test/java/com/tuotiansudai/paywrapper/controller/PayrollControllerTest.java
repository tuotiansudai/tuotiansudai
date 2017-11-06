package com.tuotiansudai.paywrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.InvestNotifyRequestMapper;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollStatusType;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class PayrollControllerTest {
    private MockMvc mockMvc;

    private MockWebServer mockPayServer;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    private MockWebServer mockServer;


    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.mockServer = mockUmPayService();

        MockPayGateWrapper.injectInto(payAsyncClient);
        MockPayGateWrapper.injectInto(paySyncClient);

        this.mockPayServer = new MockWebServer();
        this.mockPayServer.start();

        MockPayGateWrapper.setUrl("mockURL");
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void clean() throws Exception {
        this.mockServer.shutdown();
        this.mockPayServer.shutdown();
    }

    private MockWebServer mockUmPayService() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start(InetAddress.getLoopbackAddress(), 8091);

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("OK");
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        return mockWebServer;
    }

    @Test
    public void shouldPayroll() throws Exception {
        PayrollModel payrollModel = mockPayroll();

        this.mockMvc.perform(post("/payroll/pay").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(String.valueOf(payrollModel.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    private PayrollModel mockPayroll() {
        PayrollModel payrollModel = new PayrollModel("testPayroll", 900000L, 5);
        payrollModel.setStatus(PayrollStatusType.AUDITED);
        payrollModel.setCreatedBy("loginName");
        payrollModel.setCreatedTime(new Date());
        payrollMapper.create(payrollModel);
        return payrollModel;
    }
}
