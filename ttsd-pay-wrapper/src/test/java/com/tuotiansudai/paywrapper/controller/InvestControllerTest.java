package com.tuotiansudai.paywrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.AmountUtil;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class InvestControllerTest {
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
    private PayAsyncClient payAsyncClient;


    private ObjectMapper objectMapper;
    private MockWebServer mockServer;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.objectMapper = new ObjectMapper();

        this.mockServer = mockUmPayService();

        MockPayGateWrapper.injectInto(payAsyncClient);
    }

    @After
    public void clean() throws Exception {
        this.mockServer.shutdown();
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
    public void shouldInvest() throws Exception {
        long mockLoanId = 123451234512345L;
        long mockInitAmount = 1000000;
        long mockInvestAmount = 32436;
        String mockloanerLoginName = "mock_loaner1";
        String mockInvestLoginName = "mock_invest1";
        String[] mockUserNames = new String[]{mockloanerLoginName, mockInvestLoginName};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanId, mockloanerLoginName);

        InvestDto investDto = new InvestDto();
        investDto.setLoanId(String.valueOf(mockLoanId));
        investDto.setInvestSource(InvestSource.WEB);
        investDto.setAmount(AmountUtil.convertCentToString(mockInvestAmount));
        investDto.setLoginName(mockInvestLoginName);

        String requestJson = objectMapper.writeValueAsString(investDto);

        this.mockMvc.perform(post("/invest").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));


        List<InvestModel> investModelList = investMapper.findByLoginNameOrderByTime(mockInvestLoginName,SortStyle.Desc);
        assert investModelList.size() > 0;
        InvestModel investModel = investModelList.get(0);
        assert investModel.getAmount() == mockInvestAmount;

        this.mockMvc.perform(get("/callback/invest_notify?mer_check_date=20150902&" +
                "mer_date=20150902&mer_id=7099088&order_id={order_id}&ret_code=0000&" +
                "ret_msg=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F%E3%80%82&service=project_tranfer_notify&" +
                "trade_no=1509025074065552&version=4.0&" +
                "sign=uCQvu1tGvZuJAl%2FGoQHgZYjceelWgQ71ubOOtjqgw%2BOMOsh6VfZcukgtuAk1Pjh00HnfXeRi%2BXfT50bcaesv1NKjJ%2FgFp6oMPZh0rqL32FqugCBZFDrz4HNPjJGljUuhatUmJJvvZMUMMJmlnU7j61ByZ77mvukZ%2Fk4v0AEsi5s%3D&" +
                "sign_type=RSA", investModel.getId()))
                .andExpect(status().isOk());
        AccountModel am = accountMapper.findByLoginName(mockInvestLoginName);

        assert am.getBalance() == mockInitAmount - mockInvestAmount;
        assert am.getFreeze() == mockInvestAmount;
    }

    private void mockUsers(String[] loginNames) {
        mockUser(loginNames[0], "17610361805", "zhanglong@tuotiansudai.com");
        mockUser(loginNames[1], "17601215859", "zhangzhigang@tuotiansudai.com");
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
        lm.setPeriods(1);
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
}
