package com.tuotiansudai.paywrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.LoanOutDto;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class LoanControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private PaySyncClient paySyncClient;


    private ObjectMapper objectMapper;
    private MockWebServer mockServer;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.objectMapper = new ObjectMapper();
        this.mockServer = mockUmPayService();

        MockPayGateWrapper.inject(paySyncClient);
        MockPayGateWrapper.setUrl(this.mockServer.getUrl("/").toString());
    }

    @After
    public void clean() throws Exception {
        this.mockServer.shutdown();
    }

    private MockWebServer mockUmPayService() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start(InetAddress.getLoopbackAddress(), 8091);

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                "<html>\n" +
                "  <head>\n" +
                "\t<META NAME=\"MobilePayPlatform\" CONTENT=\"mer_id=7099088&ret_code=0000&ret_msg=成功&sign_type=RSA&version=1.0&sign=rqxyL+LrtzdGba4k4rFd1cs232Kcc4aQaUHTQlfZ0y9ayowzpxMwnbrbKyVHPGRxVz/UzLdo6uhNjPmGHND8F/yT0TDXkF1K8KW5AEjCzOwq39dWhEpLon62a1K4fchubLrpdeAx45X1YqpqL0s6uug/jb4SeWAYPi0ktnlHFVE=\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "  </body>\n" +
                "</html>");
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        return mockWebServer;
    }

    @Test
    public void shouldLoanout() throws Exception {
        long mockLoanId = 123451234512345L;
        long mockInitAmount = 1000000;
        String loanerLoginName = "mock_loaner1";
        String[] mockInvestLoginName = new String[]{"mock_invest1", "mock_invest2"};
        String[] mockUserNames = new String[]{loanerLoginName, mockInvestLoginName[0], mockInvestLoginName[1]};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanId, loanerLoginName);
        mockInvests(mockLoanId, mockInvestLoginName);

        LoanOutDto loanOutDto = new LoanOutDto();
        loanOutDto.setLoanId(String.valueOf(mockLoanId));

        String requestJson = objectMapper.writeValueAsString(loanOutDto);

        this.mockMvc.perform(post("/loan/loanout").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true))
                .andExpect(jsonPath("$.data.code").value("0000"));

        AccountModel am = accountMapper.findByLoginName(loanerLoginName);
        AccountModel am1 = accountMapper.findByLoginName(mockInvestLoginName[0]);
        AccountModel am2 = accountMapper.findByLoginName(mockInvestLoginName[1]);

        long amount = am.getBalance();
        long amount1 = am1.getBalance();
        long amount2 = am2.getBalance();

        assert amount > mockInitAmount;
        assert amount1 < mockInitAmount;
        assert amount2 < mockInitAmount;
        assert amount - mockInitAmount == mockInitAmount * 2 - amount1 - amount2;
    }

    private void mockUsers(String[] loginNames) {
        mockUser(loginNames[0], "18610361804", "zhanglong@tuotiansudai.com");
        mockUser(loginNames[1], "13436964915", "zourenzheng@tuotiansudai.com");
        mockUser(loginNames[2], "18601215857", "zhangzhigang@tuotiansudai.com");
    }

    private void mockUser(String loginName, String mobile, String email) {
        UserModel um = new UserModel();
        um.setId(idGenerator.generate());
        um.setLoginName(loginName);
        um.setMobile(mobile);
        um.setEmail(email);
        um.setStatus(UserStatus.ACTIVE);
        um.setPassword(loginName);
        um.setSalt(loginName);
        userMapper.create(um);
    }

    private void mockAccounts(String[] loginNames, long initAmount) {
        for (String loginName : loginNames) {
            mockAccount(loginName, initAmount);
        }
    }

    private void mockAccount(String loginName, long initAmount) {
        AccountModel am = new AccountModel(loginName, loginName, loginName, loginName, loginName, new Date());
        accountMapper.create(am);
        userBillService.transferInBalance(loginName, idGenerator.generate(), initAmount, UserBillBusinessType.RECHARGE_SUCCESS);
    }

    private void mockLoan(long loanId, String loanerLoginName) {
        LoanModel lm = new LoanModel();
        lm.setId(loanId);
        lm.setName("test loan");
        lm.setDescriptionHtml("fdjakf");
        lm.setDescriptionText("fdjakf");
        lm.setPeriods(1L);
        lm.setType(LoanType.LOAN_TYPE_1);
        lm.setActivityRate(0.1);
        lm.setMinInvestAmount(1);
        lm.setMaxInvestAmount(1000000);
        lm.setLoanAmount(1000000000);
        lm.setLoanerLoginName(loanerLoginName);
        lm.setAgentLoginName(loanerLoginName);
        lm.setBaseRate(0.2);
        lm.setActivityType(ActivityType.NORMAL);
        lm.setCreatedTime(new Date());
        lm.setFundraisingStartTime(new Date());
        lm.setFundraisingEndTime(new Date());
        lm.setStatus(LoanStatus.RECHECK);
        loanMapper.create(lm);
    }

    private void mockInvests(long loanId, String[] loginNames) throws AmountTransferException {
        Random rnd = new Random();
        for (String loginName : loginNames) {
            mockInvest(loanId, loginName, Math.abs(rnd.nextInt() % 10000));
        }
    }

    private void mockInvest(long loanId, String loginName, long amount) throws AmountTransferException {
        InvestModel im = new InvestModel();
        im.setAmount(amount);
        im.setCreatedTime(new Date());
        im.setId(idGenerator.generate());
        im.setSource(InvestSource.WEB);
        im.setLoanId(loanId);
        im.setIsAutoInvest(false);
        im.setLoginName(loginName);
        im.setStatus(InvestStatus.SUCCESS);
        investMapper.create(im);

        userBillService.freeze(loginName, im.getId(), amount, UserBillBusinessType.INVEST_SUCCESS);
    }
}
