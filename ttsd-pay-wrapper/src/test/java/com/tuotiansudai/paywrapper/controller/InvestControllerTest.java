package com.tuotiansudai.paywrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.InvestNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class InvestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    PayCallbackController payCallbackController;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    InvestNotifyRequestMapper investNotifyRequestMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    private ObjectMapper objectMapper;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.objectMapper = new ObjectMapper();

        MockitoAnnotations.initMocks(this);
    }

    // case1: 正常出借
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

        mockUserMembership(mockInvestLoginName);

        long investId = investOneDeal(mockLoanId, mockInvestAmount, mockInvestLoginName);

        this.jobAsyncInvestNotify(Lists.newArrayList(investId));

        verifyInvestSuccessAmountTransferMessage(mockInvestAmount, mockInvestLoginName);

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assertEquals(sumSuccessInvestAmount, mockInvestAmount);
    }

    private void verifyInvestSuccessAmountTransferMessage(long mockInvestAmount, String mockInvestLoginName) {
        try {
            String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage message = JsonConverter.readValue(messageBody, AmountTransferMessage.class);
            assertThat(message.getLoginName(), CoreMatchers.is(mockInvestLoginName));
            assertThat(message.getAmount(), CoreMatchers.is(mockInvestAmount));
            assertThat(message.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_SUCCESS));
            assertThat(message.getTransferType(), CoreMatchers.is(TransferType.FREEZE));
        } catch (IOException e) {
            assert false;
        }
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

        mockUserMembership(mockInvestLoginName1);
        mockUserMembership(mockInvestLoginName2);
        mockUserMembership(mockInvestLoginName3);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);

        this.jobAsyncInvestNotify(Lists.newArrayList(orderId1, orderId2, orderId3));

        verifyInvestSuccessAmountTransferMessage(mockInvestAmount3, mockInvestLoginName3);
        verifyInvestSuccessAmountTransferMessage(mockInvestAmount2, mockInvestLoginName2);
        verifyInvestSuccessAmountTransferMessage(mockInvestAmount1, mockInvestLoginName1);

        //check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RECHECK));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assertEquals(sumSuccessInvestAmount, mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3);
    }

    // case3: 超投，返款成功
    @Test
    @Ignore
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

        mockUserMembership(mockInvestLoginName1);
        mockUserMembership(mockInvestLoginName2);
        mockUserMembership(mockInvestLoginName3);
        mockUserMembership(mockInvestLoginName4);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);
        long orderId4 = investOneDeal(mockLoanId, mockInvestAmount4, mockInvestLoginName4);

        this.jobAsyncInvestNotify(Lists.newArrayList(orderId1, orderId2, orderId3, orderId4));

        verifyInvestSuccessAmountTransferMessage(mockInvestAmount3, mockInvestLoginName3);
        verifyInvestSuccessAmountTransferMessage(mockInvestAmount2, mockInvestLoginName2);
        verifyInvestSuccessAmountTransferMessage(mockInvestAmount1, mockInvestLoginName1);

        // check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RAISING));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assertEquals(sumSuccessInvestAmount, mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3);

        // 超投返款
        overInvestPaybackNotify(orderId4, "0000");

        List<InvestModel> investModelList4 = investMapper.findPaginationByLoginName(mockInvestLoginName4, 0, Integer.MAX_VALUE);
        assert investModelList4.size() > 0;
        InvestModel investModel4 = investModelList4.get(0);
        assertThat(investModel4.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));
    }


    // case4: 多笔超投，返款成功
    @Test
    @Ignore
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

        String[] mockUserNames = new String[]{mockLoanerLoginName, mockInvestLoginName1, mockInvestLoginName2, mockInvestLoginName3, mockInvestLoginName4, mockInvestLoginName5};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanAmount, mockLoanId, mockLoanerLoginName);

        mockUserMembership(mockInvestLoginName1);
        mockUserMembership(mockInvestLoginName2);
        mockUserMembership(mockInvestLoginName3);
        mockUserMembership(mockInvestLoginName4);
        mockUserMembership(mockInvestLoginName5);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);
        long orderId4 = investOneDeal(mockLoanId, mockInvestAmount4, mockInvestLoginName4);
        long orderId5 = investOneDeal(mockLoanId, mockInvestAmount5, mockInvestLoginName5);

        this.jobAsyncInvestNotify(Lists.newArrayList(orderId1, orderId2, orderId3, orderId4, orderId5));

        verifyInvestSuccessAmountTransferMessage(mockInvestAmount3, mockInvestLoginName3);
        verifyInvestSuccessAmountTransferMessage(mockInvestAmount2, mockInvestLoginName2);
        verifyInvestSuccessAmountTransferMessage(mockInvestAmount1, mockInvestLoginName1);

        // check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RAISING));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assertEquals(sumSuccessInvestAmount, mockInvestAmount1 + mockInvestAmount2 + mockInvestAmount3);

        // 第4，5笔出借超投，返款回调
        this.overInvestPaybackNotify(orderId4, "0000");
        this.overInvestPaybackNotify(orderId5, "0000");

        List<InvestModel> investModelList4 = investMapper.findPaginationByLoginName(mockInvestLoginName4, 0, Integer.MAX_VALUE);
        assert investModelList4.size() > 0;
        InvestModel investModel4 = investModelList4.get(0);
        assertThat(investModel4.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));

        List<InvestModel> investModelList5 = investMapper.findPaginationByLoginName(mockInvestLoginName5, 0, Integer.MAX_VALUE);
        assert investModelList5.size() > 0;
        InvestModel investModel5 = investModelList5.get(0);
        assertThat(investModel5.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));
    }

    // case5: 超投后，又出借满标
    @Test
    @Ignore
    public void investSuccessAfterOverInvest() throws Exception {
        long mockLoanId = 55555555L;
        long mockInitAmount = 1000000;
        long mockLoanAmount = 3;

        String mockLoanerLoginName = "mock_loaner1";

        long mockInvestAmount1 = 2;
        long mockInvestAmount2 = 3;
        long mockInvestAmount3 = 1;
        String mockInvestLoginName1 = "mock_invest1";
        String mockInvestLoginName2 = "mock_invest2";
        String mockInvestLoginName3 = "mock_invest3";

        String[] mockUserNames = new String[]{mockLoanerLoginName, mockInvestLoginName1, mockInvestLoginName2, mockInvestLoginName3};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanAmount, mockLoanId, mockLoanerLoginName);

        mockUserMembership(mockInvestLoginName1);
        mockUserMembership(mockInvestLoginName2);
        mockUserMembership(mockInvestLoginName3);

        long orderId1 = investOneDeal(mockLoanId, mockInvestAmount1, mockInvestLoginName1);
        long orderId2 = investOneDeal(mockLoanId, mockInvestAmount2, mockInvestLoginName2);
        long orderId3 = investOneDeal(mockLoanId, mockInvestAmount3, mockInvestLoginName3);

        this.jobAsyncInvestNotify(Lists.newArrayList(orderId1, orderId2, orderId3));

        verifyInvestSuccessAmountTransferMessage(mockInvestAmount3, mockInvestLoginName3);
        verifyInvestSuccessAmountTransferMessage(mockInvestAmount1, mockInvestLoginName1);

        // check loan status
        LoanModel lm = loanMapper.findById(mockLoanId);
        assertThat(lm.getStatus(), is(LoanStatus.RECHECK));

        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmount(mockLoanId);
        assertEquals(sumSuccessInvestAmount, mockInvestAmount1 + mockInvestAmount3);

        // 第2笔出借超投，返款回调
        this.overInvestPaybackNotify(orderId2, "0000");

        List<InvestModel> investModelList2 = investMapper.findPaginationByLoginName(mockInvestLoginName2, 0, Integer.MAX_VALUE);
        assert investModelList2.size() > 0;
        InvestModel investModel4 = investModelList2.get(0);
        assertThat(investModel4.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));

        List<InvestModel> investModelList3 = investMapper.findPaginationByLoginName(mockInvestLoginName3, 0, Integer.MAX_VALUE);
        assert investModelList3.size() > 0;
        InvestModel investModel5 = investModelList3.get(0);
        assertThat(investModel5.getStatus(), is(InvestStatus.SUCCESS));
    }

    private void jobAsyncInvestNotify(List<Long> investIds) throws Exception {
        for (Long investId : investIds) {
            this.mockMvc.perform(post("/job/async_invest_notify")
                    .content(String.valueOf(investId))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json; charset=UTF-8"))
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.status").value(true));
        }
    }

    private void overInvestPaybackNotify(long orderId, String retCode) throws Exception {
        // 超投返款回调
        this.mockMvc.perform(get("/callback/over_invest_payback_notify?mer_check_date=20150902&" +
                "mer_date=20150902&mer_id=7099088&order_id={order_id}&ret_code={ret_code}&" +
                "ret_msg=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F%E3%80%82&service=project_tranfer_notify&" +
                "trade_no=1509025074065552&version=4.0&" +
                "sign=uCQvu1tGvZuJAl%2FGoQHgZYjceelWgQ71ubOOtjqgw%2BOMOsh6VfZcukgtuAk1Pjh00HnfXeRi%2BXfT50bcaesv1NKjJ%2FgFp6oMPZh0rqL32FqugCBZFDrz4HNPjJGljUuhatUmJJvvZMUMMJmlnU7j61ByZ77mvukZ%2Fk4v0AEsi5s%3D&" +
                "sign_type=RSA", orderId + "X" + System.currentTimeMillis(), retCode))
                .andExpect(status().isOk());
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


        List<InvestModel> investModelList = investMapper.findPaginationByLoginName(mockInvestLoginName, 0, Integer.MAX_VALUE);
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
        long mobile = 17610361805L;
        for (String loginName : loginNames) {
            mockUser(loginName, String.valueOf(mobile++), "zhanglong@tuotiansudai.com");
        }
    }

    private void mockUser(String loginName, String mobile, String email) {
        UserModel um = new UserModel();
        um.setId(IdGenerator.generate());
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
        AccountModel am = new AccountModel(loginName, loginName, loginName, new Date());
        am.setBalance(initAmount);
        accountMapper.create(am);
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
        lm.setPledgeType(PledgeType.HOUSE);
        lm.setProductType(ProductType._180);
        loanMapper.create(lm);
    }

    private void mockUserMembership(String loginName) {
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName, 1, new DateTime(2200, 1, 1, 1, 1).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel.setCreatedTime(new DateTime().plusDays(-1).toDate());
        userMembershipMapper.create(userMembershipModel);
    }
}
