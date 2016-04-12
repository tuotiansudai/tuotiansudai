package com.tuotiansudai.paywrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.InvestNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class InvestControllerTest {
    private MockMvc mockMvc;

    private MockWebServer mockPayServer;

    @Autowired
    PayCallbackController payCallbackController;

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
    private AmountTransfer amountTransfer;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    InvestNotifyRequestMapper investNotifyRequestMapper;

    private ObjectMapper objectMapper;
    private MockWebServer mockServer;


    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.objectMapper = new ObjectMapper();
        this.mockServer = mockUmPayService();

        MockPayGateWrapper.injectInto(payAsyncClient);
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

    // case1: 正常投资
    @Test
    public void invest() throws Exception {
        long mockLoanId = 11111111L;
        long mockInitAmount = 1000000;
        long mockLoanAmount = 1000000000;
        long mockInvestAmount = 32436;
        String mockLoanerLoginName = "mock_loaner1";
        String mockInvestLoginName = "mock_invest1";
        String[] mockUserNames = new String[]{mockLoanerLoginName, mockInvestLoginName};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanAmount, mockLoanId, mockLoanerLoginName);

        long orderId = investOneDeal(mockLoanId, mockInvestAmount, mockInvestLoginName);

        List<InvestNotifyRequestModel> investNotifyTodoList = investNotifyRequestMapper.getTodoList(10);
        assert investNotifyTodoList.size() > 0;
        InvestNotifyRequestModel notifyRequestModel = investNotifyTodoList.get(investNotifyTodoList.size()-1);
        assert notifyRequestModel.getOrderId().equals(String.valueOf(orderId));

        this.jobAsyncInvestNotify();

        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount, mockInvestLoginName);

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assert sumSuccessInvestAmount == mockInvestAmount;
    }

    // case2: 满标
    @Test
    public void investToFullLoan() throws Exception {
        long mockLoanId = 22222222L;
        long mockInitAmount = 1000000;
        long mockLoanAmount = 3000000;
        long mockInvestAmount1 = 1000000;
        long mockInvestAmount2 = 1000000;
        long mockInvestAmount3 = 1000000;
        String mockLoanerLoginName = "mock_loaner1";
        String mockInvestLoginName1 = "mock_invest1";
        String mockInvestLoginName2 = "mock_invest2";
        String mockInvestLoginName3 = "mock_invest3";
        String[] mockUserNames = new String[]{mockLoanerLoginName, mockInvestLoginName1, mockInvestLoginName2, mockInvestLoginName3};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanAmount, mockLoanId, mockLoanerLoginName);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);

        this.jobAsyncInvestNotify();

        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount1, mockInvestLoginName1);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount2, mockInvestLoginName2);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount3, mockInvestLoginName3);

        //check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RECHECK));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assert sumSuccessInvestAmount == mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3;
    }

    // case3: 超投，返款成功
    @Test
    public void overInvestPaybackSuccess() throws Exception {
        long mockLoanId = 33333333L;
        long mockInitAmount = 1000000;
        long mockLoanAmount = 3000000;

        String mockLoanerLoginName = "mock_loaner1";

        long mockInvestAmount1 = 1000000;
        long mockInvestAmount2 = 1000000;
        long mockInvestAmount3 = 900000;
        long mockInvestAmount4 = 900000;
        String mockInvestLoginName1 = "mock_invest1";
        String mockInvestLoginName2 = "mock_invest2";
        String mockInvestLoginName3 = "mock_invest3";
        String mockInvestLoginName4 = "mock_invest4";

        String[] mockUserNames = new String[]{mockLoanerLoginName, mockInvestLoginName1, mockInvestLoginName2, mockInvestLoginName3, mockInvestLoginName4};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanAmount, mockLoanId, mockLoanerLoginName);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);
        long orderId4 = investOneDeal(mockLoanId, mockInvestAmount4, mockInvestLoginName4);

        this.generateMockResponse_success(1); // 返款成功
        this.jobAsyncInvestNotify();

        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount1, mockInvestLoginName1);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount2, mockInvestLoginName2);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount3, mockInvestLoginName3);
        verifyInvestorAmount_fail(mockInitAmount, mockInvestAmount4, mockInvestLoginName4);

        // check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RAISING));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assert sumSuccessInvestAmount == mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3;

        // 超投返款
        overInvestPaybackNotify(orderId4, "0000");

        List<InvestModel> investModelList4 = investMapper.findByLoginName(mockInvestLoginName4, 0, Integer.MAX_VALUE);
        assert investModelList4.size() > 0;
        InvestModel investModel4 = investModelList4.get(0);
        assertThat(investModel4.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));
    }


    // case4: 多笔超投，返款成功
    @Test
    public void multiOverInvestPaybackSuccess() throws Exception {
        long mockLoanId = 44444444L;
        long mockInitAmount = 1000000;
        long mockLoanAmount = 3000000;

        String mockLoanerLoginName = "mock_loaner1";

        long mockInvestAmount1 = 1000000;
        long mockInvestAmount2 = 1000000;
        long mockInvestAmount3 = 900000;
        long mockInvestAmount4 = 900000;
        long mockInvestAmount5 = 900000;
        String mockInvestLoginName1 = "mock_invest1";
        String mockInvestLoginName2 = "mock_invest2";
        String mockInvestLoginName3 = "mock_invest3";
        String mockInvestLoginName4 = "mock_invest4";
        String mockInvestLoginName5 = "mock_invest5";

        String[] mockUserNames = new String[]{mockLoanerLoginName, mockInvestLoginName1, mockInvestLoginName2, mockInvestLoginName3,mockInvestLoginName4,mockInvestLoginName5};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanAmount, mockLoanId, mockLoanerLoginName);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);
        long orderId4 = investOneDeal(mockLoanId, mockInvestAmount4, mockInvestLoginName4);
        long orderId5 = investOneDeal(mockLoanId, mockInvestAmount5, mockInvestLoginName5);

        this.generateMockResponse_success(2); // 返款成功
        this.jobAsyncInvestNotify();

        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount1, mockInvestLoginName1);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount2, mockInvestLoginName2);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount3, mockInvestLoginName3);
        verifyInvestorAmount_fail(mockInitAmount, mockInvestAmount4, mockInvestLoginName4);
        verifyInvestorAmount_fail(mockInitAmount, mockInvestAmount5, mockInvestLoginName5);

        // check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RAISING));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assert sumSuccessInvestAmount == mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3;

        // 第4，5笔投资超投，返款回调
        this.overInvestPaybackNotify(orderId4, "0000");
        this.overInvestPaybackNotify(orderId5, "0000");

        List<InvestModel> investModelList4 = investMapper.findByLoginName(mockInvestLoginName4, 0, Integer.MAX_VALUE);
        assert investModelList4.size() > 0;
        InvestModel investModel4 = investModelList4.get(0);
        assertThat(investModel4.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));

        List<InvestModel> investModelList5 = investMapper.findByLoginName(mockInvestLoginName5, 0, Integer.MAX_VALUE);
        assert investModelList5.size() > 0;
        InvestModel investModel5 = investModelList5.get(0);
        assertThat(investModel5.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));
    }

    // case5: 超投后，又投资满标
    @Test
    public void investSuccessAfterOverInvest() throws Exception {
        long mockLoanId = 55555555L;
        long mockInitAmount = 1000000;
        long mockLoanAmount = 3000000;

        String mockLoanerLoginName = "mock_loaner1";

        long mockInvestAmount1 = 1000000;
        long mockInvestAmount2 = 1000000;
        long mockInvestAmount3 = 900000;
        long mockInvestAmount4 = 900000;
        long mockInvestAmount5 = 100000;
        String mockInvestLoginName1 = "mock_invest1";
        String mockInvestLoginName2 = "mock_invest2";
        String mockInvestLoginName3 = "mock_invest3";
        String mockInvestLoginName4 = "mock_invest4";
        String mockInvestLoginName5 = "mock_invest5";

        String[] mockUserNames = new String[]{mockLoanerLoginName, mockInvestLoginName1, mockInvestLoginName2, mockInvestLoginName3,mockInvestLoginName4,mockInvestLoginName5};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanAmount, mockLoanId, mockLoanerLoginName);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);
        long orderId4 = investOneDeal(mockLoanId, mockInvestAmount4, mockInvestLoginName4);
        long orderId5 = investOneDeal(mockLoanId, mockInvestAmount5, mockInvestLoginName5);

        this.generateMockResponse_success(1); // 返款成功
        this.jobAsyncInvestNotify();

        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount1, mockInvestLoginName1);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount2, mockInvestLoginName2);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount3, mockInvestLoginName3);
        verifyInvestorAmount_fail(mockInitAmount, mockInvestAmount4, mockInvestLoginName4);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount5, mockInvestLoginName5);

        // check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RECHECK));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assert sumSuccessInvestAmount == mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3 + mockInvestAmount5;

        // 第4笔投资超投，返款回调
        this.overInvestPaybackNotify(orderId4, "0000");
//        this.overInvestPaybackNotify(orderId5, "0000");

        List<InvestModel> investModelList4 = investMapper.findByLoginName(mockInvestLoginName4, 0, Integer.MAX_VALUE);
        assert investModelList4.size() > 0;
        InvestModel investModel4 = investModelList4.get(0);
        assertThat(investModel4.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));

        List<InvestModel> investModelList5 = investMapper.findByLoginName(mockInvestLoginName5, 0, Integer.MAX_VALUE);
        assert investModelList5.size() > 0;
        InvestModel investModel5 = investModelList5.get(0);
        assertThat(investModel5.getStatus(), is(InvestStatus.SUCCESS));
    }

    // case6: 超投，返款失败，当投资成功处理
    @Test
    public void overInvestPaybackFail() throws Exception {
        long mockLoanId = 66666666L;
        long mockInitAmount = 1000000;
        long mockLoanAmount = 3000000;

        String mockLoanerLoginName = "mock_loaner1";

        long mockInvestAmount1 = 1000000;
        long mockInvestAmount2 = 1000000;
        long mockInvestAmount3 = 900000;
        long mockInvestAmount4 = 900000;
        String mockInvestLoginName1 = "mock_invest1";
        String mockInvestLoginName2 = "mock_invest2";
        String mockInvestLoginName3 = "mock_invest3";
        String mockInvestLoginName4 = "mock_invest4";

        String[] mockUserNames = new String[]{mockLoanerLoginName, mockInvestLoginName1, mockInvestLoginName2, mockInvestLoginName3, mockInvestLoginName4};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanAmount, mockLoanId, mockLoanerLoginName);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);
        long orderId4 = investOneDeal(mockLoanId, mockInvestAmount4, mockInvestLoginName4);

        this.generateMockResponse_fail(1);
        this.jobAsyncInvestNotify();

        InvestModel investModel4_b = investMapper.findByLoginName(mockInvestLoginName4, 0, Integer.MAX_VALUE).get(0);
        assertThat(investModel4_b.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK_FAIL));

        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount1, mockInvestLoginName1);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount2, mockInvestLoginName2);
        verifyInvestorAmount_success(mockInitAmount, mockInvestAmount3, mockInvestLoginName3);
        verifyInvestorAmount_fail(mockInitAmount, mockInvestAmount4, mockInvestLoginName4);

        // check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RAISING));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assert sumSuccessInvestAmount == mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3;

        // 超投返款回调－失败，order4当作投资成功处理
        this.overInvestPaybackNotify(orderId4, "0001");

        InvestModel investModel4_a = investMapper.findByLoginName(mockInvestLoginName4, 0, Integer.MAX_VALUE).get(0);
        assertThat(investModel4_a.getStatus(), is(InvestStatus.SUCCESS));

        // check loan status
        LoanModel lm_after = loanMapper.findById(mockLoanId);
        assertThat(lm_after.getStatus(), is(LoanStatus.RECHECK));

        long sumSuccessInvestAmount_after = investMapper.sumSuccessInvestAmount(mockLoanId);
        assert sumSuccessInvestAmount_after == mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3 + mockInvestAmount4;
    }


    private void jobAsyncInvestNotify() throws Exception {
        // job 触发投资 notify 处理
        this.mockMvc.perform(post("/job/async_invest_notify")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    private void overInvestPaybackNotify(long orderId, String retCode) throws Exception {
        // 超投返款回调
        this.mockMvc.perform(get("/callback/over_invest_payback_notify?mer_check_date=20150902&" +
                "mer_date=20150902&mer_id=7099088&order_id={order_id}&ret_code={ret_code}&" +
                "ret_msg=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F%E3%80%82&service=project_tranfer_notify&" +
                "trade_no=1509025074065552&version=4.0&" +
                "sign=uCQvu1tGvZuJAl%2FGoQHgZYjceelWgQ71ubOOtjqgw%2BOMOsh6VfZcukgtuAk1Pjh00HnfXeRi%2BXfT50bcaesv1NKjJ%2FgFp6oMPZh0rqL32FqugCBZFDrz4HNPjJGljUuhatUmJJvvZMUMMJmlnU7j61ByZ77mvukZ%2Fk4v0AEsi5s%3D&" +
                "sign_type=RSA", orderId+"X"+System.currentTimeMillis(), retCode))
                .andExpect(status().isOk());
    }


    private void verifyInvestorAmount_success(long mockInitAmount, long mockInvestAmount1, String mockInvestLoginName1) {
        AccountModel am = accountMapper.findByLoginName(mockInvestLoginName1);
        assert am.getBalance() == mockInitAmount - mockInvestAmount1;
        assert am.getFreeze() == mockInvestAmount1;
    }

    private void verifyInvestorAmount_fail(long mockInitAmount, long mockInvestAmount1, String mockInvestLoginName1) {
        AccountModel am = accountMapper.findByLoginName(mockInvestLoginName1);
        assert am.getBalance() == mockInitAmount;
        assert am.getFreeze() == 0;
    }

    private long investOneDeal(long mockLoanId, long mockInvestAmount, String mockInvestLoginName) throws Exception {
        InvestDto investDto = new InvestDto();
        investDto.setLoanId(String.valueOf(mockLoanId));
        investDto.setSource(Source.WEB);
        investDto.setAmount(AmountConverter.convertCentToString(mockInvestAmount));
        investDto.setLoginName(mockInvestLoginName);

        String requestJson = objectMapper.writeValueAsString(investDto);

        this.mockMvc.perform(post("/invest").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));


        List<InvestModel> investModelList = investMapper.findByLoginName(mockInvestLoginName, 0, Integer.MAX_VALUE);
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
        return investModel.getId();
    }

    private void mockUsers(String[] loginNames) {
        long mobile=17610361805L;
        for(String loginName : loginNames) {
            mockUser(loginName, String.valueOf(mobile++), "zhanglong@tuotiansudai.com");
        }
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

    private void mockAccounts(String[] loginNames, long initAmount) throws AmountTransferException {
        for (String loginName : loginNames) {
            mockAccount(loginName, initAmount);
        }
    }

    private void mockAccount(String loginName, long initAmount) throws AmountTransferException {
        AccountModel am = new AccountModel(loginName, loginName, loginName, loginName, loginName, new Date());
        accountMapper.create(am);
        amountTransfer.transferInBalance(loginName, idGenerator.generate(), initAmount, UserBillBusinessType.RECHARGE_SUCCESS, null, null);
    }

    private void mockLoan(long loanAmount, long loanId, String loanerLoginName) {
        LoanModel lm = new LoanModel();
        lm.setId(loanId);
        lm.setName("test loan");
        lm.setDescriptionHtml("fdjakf");
        lm.setDescriptionText("fdjakf");
        lm.setPeriods(1);
        lm.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        lm.setActivityRate(0.1);
        lm.setMinInvestAmount(1);
        lm.setMaxInvestAmount(1000000);
        lm.setLoanAmount(loanAmount);
        lm.setLoanerLoginName(loanerLoginName);
        lm.setLoanerUserName("借款人");
        lm.setLoanerIdentityNumber("111111111111111111");
        lm.setAgentLoginName(loanerLoginName);
        lm.setBaseRate(0.2);
        lm.setActivityType(ActivityType.NORMAL);
        lm.setCreatedTime(new Date());
        lm.setFundraisingStartTime(new Date());
        lm.setFundraisingEndTime(new Date());
        lm.setStatus(LoanStatus.RAISING);
//        lm.setUpdateTime(new Date());
        loanMapper.create(lm);
    }

    private Map<String, String> getFakeCallbackParamsMap(String orderId) {
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("service", "project_transfer_notify")
                .put("sign_type", "RSA")
                .put("sign", "sign")
                .put("mer_id", "mer_id")
                .put("version", "1.0")
                .put("trade_no", "trade_no")
                .put("order_id", orderId)
                .put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .put("ret_code", "0000")
                .build());
    }

    private void generateMockResponse_success(int times) {
        for (int index = 0; index < times; index++){
            MockResponse mockResponse = new MockResponse();
            mockResponse.setBody("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <META NAME=\"MobilePayPlatform\" CONTENT=\"mer_id=7099088&ret_code=0000&ret_msg=成功&sign_type=RSA&version=1.0&sign=rqxyL+LrtzdGba4k4rFd1cs232Kcc4aQaUHTQlfZ0y9ayowzpxMwnbrbKyVHPGRxVz/UzLdo6uhNjPmGHND8F/yT0TDXkF1K8KW5AEjCzOwq39dWhEpLon62a1K4fchubLrpdeAx45X1YqpqL0s6uug/jb4SeWAYPi0ktnlHFVE=\">\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "  </body>\n" +
                    "</html>");
            mockResponse.setResponseCode(200);
            this.mockPayServer.enqueue(mockResponse);
        }
    }


    private void generateMockResponse_fail(int times) {
        for (int index = 0; index < times; index++){
            MockResponse mockResponse = new MockResponse();
            mockResponse.setBody("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <META NAME=\"MobilePayPlatform\" CONTENT=\"mer_id=7099088&ret_code=0001&ret_msg=成功&sign_type=RSA&version=1.0&sign=rqxyL+LrtzdGba4k4rFd1cs232Kcc4aQaUHTQlfZ0y9ayowzpxMwnbrbKyVHPGRxVz/UzLdo6uhNjPmGHND8F/yT0TDXkF1K8KW5AEjCzOwq39dWhEpLon62a1K4fchubLrpdeAx45X1YqpqL0s6uug/jb4SeWAYPi0ktnlHFVE=\">\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "  </body>\n" +
                    "</html>");
            mockResponse.setResponseCode(200);
            this.mockPayServer.enqueue(mockResponse);
        }
    }
}
