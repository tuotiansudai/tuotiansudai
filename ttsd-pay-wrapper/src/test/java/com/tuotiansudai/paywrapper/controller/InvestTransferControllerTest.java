package com.tuotiansudai.paywrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
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
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class InvestTransferControllerTest {


    private MockMvc mockMvc;

    private MockWebServer mockPayServer;

    @Autowired
    PayCallbackController payCallbackController;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    private ObjectMapper objectMapper;
    private MockWebServer mockServer;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;


    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.objectMapper = new ObjectMapper();
        this.mockServer = mockUmPayService();

        MockPayGateWrapper.injectInto(payAsyncClient);
        MockPayGateWrapper.injectInto(paySyncClient);
        this.mockPayServer = new MockWebServer();
        this.mockPayServer.start();

        MockPayGateWrapper.setUrl(this.mockPayServer.getUrl("/").toString());
        MockitoAnnotations.initMocks(this);

        this.generateMockResponse_success(5); // 返款成功
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

    // 超投，返款成功
    @Test
    public void overInvestPaybackSuccess() throws Exception {

        DateTime recheckTime = new DateTime().withDate(2016, 3, 1);
        LoanModel fakeLoan = this.createFakeLoan(LoanType.LOAN_INTEREST_MONTHLY_REPAY, 1000000, 2, 0.12, recheckTime.toDate());
        UserModel transferrer = this.createFakeUser("transferrer", 0, 0);

        UserModel transferee1 = this.createFakeUser("transferee1", 1000000, 0);
        UserModel transferee2 = this.createFakeUser("transferee2", 1000000, 0);

        InvestModel fakeTransferInvest = this.createFakeInvest(fakeLoan.getId(), null, 1000000, transferrer.getLoginName(), recheckTime.minusDays(10).toDate(), InvestStatus.SUCCESS, TransferStatus.TRANSFERRING);
        TransferApplicationModel fakeTransferApplication = this.createFakeTransferApplication(fakeTransferInvest, 1, 900000, 100);
        InvestRepayModel fakeTransferInvestRepay1 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 1, 0, 10000, 10, new DateTime().withDate(2016, 3, 31).toDate(), null, RepayStatus.REPAYING);
        InvestRepayModel fakeTransferInvestRepay2 = this.createFakeInvestRepay(fakeTransferInvest.getId(), 2, 1000000, 10000, 10, new DateTime().withDate(2016, 4, 30).toDate(), null, RepayStatus.REPAYING);

        long investId1 = investOneDeal(transferee1.getLoginName(), fakeTransferApplication.getId());
        long investId2 = investOneDeal(transferee2.getLoginName(), fakeTransferApplication.getId());

        this.jobAsyncInvestNotify();

        assert (investMapper.findById(investId1).getStatus() == InvestStatus.SUCCESS);
        assert (investMapper.findById(investId2).getStatus() == InvestStatus.WAIT_PAY);

        // 超投返款
        overInvestPaybackNotify(investId2, "0000");

        List<InvestModel> investModelList2 = investMapper.findByLoginName(transferee2.getLoginName(), 0, Integer.MAX_VALUE,true);
        assert investModelList2.size() > 0;
        InvestModel investModel2 = investModelList2.get(0);
        assertThat(investModel2.getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK));
    }

    private long investOneDeal(String mockInvestLoginName, long transferInvestId) throws Exception {

        InvestDto investDto = new InvestDto();
        investDto.setLoanId("11111");
        investDto.setAmount("10000");
        investDto.setLoginName(mockInvestLoginName);
        investDto.setTransferInvestId(String.valueOf(transferInvestId));
        investDto.setSource(Source.WEB);

        String requestJson = objectMapper.writeValueAsString(investDto);

        this.mockMvc.perform(post("/invest-transfer/no-password-purchase").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));


        List<InvestModel> investModelList = investMapper.findByLoginName(mockInvestLoginName, 0, Integer.MAX_VALUE,true);
        assert investModelList.size() > 0;
        InvestModel investModel = investModelList.get(0);

        this.mockMvc.perform(get("/callback/invest_transfer_notify?mer_check_date=20150902&" +
                "mer_date=20150902&mer_id=7099088&order_id={order_id}&ret_code=0000&" +
                "ret_msg=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F%E3%80%82&service=project_tranfer_notify&" +
                "trade_no=1509025074065552&version=4.0&" +
                "sign=uCQvu1tGvZuJAl%2FGoQHgZYjceelWgQ71ubOOtjqgw%2BOMOsh6VfZcukgtuAk1Pjh00HnfXeRi%2BXfT50bcaesv1NKjJ%2FgFp6oMPZh0rqL32FqugCBZFDrz4HNPjJGljUuhatUmJJvvZMUMMJmlnU7j61ByZ77mvukZ%2Fk4v0AEsi5s%3D&" +
                "sign_type=RSA", investModel.getId()))
                .andExpect(status().isOk());
        return investModel.getId();
    }

    private UserModel createFakeUser(String loginName, long balance, long freeze) {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(loginName);
        fakeUserModel.setPassword("password");
        fakeUserModel.setMobile(loginName);
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        AccountModel accountModel = new AccountModel(loginName, loginName, "id", "payUserId", "payAccountId", new Date());
        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.create(accountModel);
        return fakeUserModel;
    }

    private LoanModel createFakeLoan(LoanType loanType, long amount, int periods, double baseRate, Date recheckTime) {
        UserModel loaner = this.createFakeUser("loaner", 0, 0);
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanAmount(amount);
        fakeLoanModel.setLoanerLoginName(loaner.getLoginName());
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("id");
        fakeLoanModel.setAgentLoginName(loaner.getLoginName());
        fakeLoanModel.setType(loanType);
        fakeLoanModel.setPeriods(periods);
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setBaseRate(baseRate);
        fakeLoanModel.setInvestFeeRate(0.1);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(recheckTime);
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    private InvestModel createFakeInvest(long loanId, Long transferInvestId, long amount, String loginName, Date investTime, InvestStatus investStatus, TransferStatus transferStatus) {
        InvestModel fakeInvestModel = new InvestModel(idGenerator.generate(), loanId, transferInvestId, amount, loginName, new Date(), Source.WEB, null);
        fakeInvestModel.setStatus(investStatus);
        fakeInvestModel.setTransferStatus(transferStatus);
        investMapper.create(fakeInvestModel);
        return fakeInvestModel;
    }

    private InvestRepayModel createFakeInvestRepay(long investId, int period, long corpus, long expectedInterest, long expectedFee, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        InvestRepayModel fakeInvestRepayModel = new InvestRepayModel(idGenerator.generate(), investId, period, corpus, expectedInterest, expectedFee, expectedRepayDate, repayStatus);
        fakeInvestRepayModel.setActualRepayDate(actualRepayDate);
        investRepayMapper.create(Lists.newArrayList(fakeInvestRepayModel));
        return fakeInvestRepayModel;
    }

    private TransferApplicationModel createFakeTransferApplication(InvestModel investModel, int period, long transferAmount, long transferFee) {
        TransferApplicationModel fakeTransferApplication = new TransferApplicationModel(investModel, "name", period, transferAmount, transferFee, new DateTime().plusDays(1).toDate(), 3);
        transferApplicationMapper.create(fakeTransferApplication);
        return fakeTransferApplication;
    }

    private void jobAsyncInvestNotify() throws Exception {
        // job 触发投资 notify 处理
        this.mockMvc.perform(post("/job/async_invest_transfer_notify")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true));
    }

    private void overInvestPaybackNotify(long orderId, String retCode) throws Exception {
        // 超投返款回调
        this.mockMvc.perform(get("/callback/over_invest_transfer_payback_notify?mer_check_date=20150902&" +
                "mer_date=20150902&mer_id=7099088&order_id={order_id}&ret_code={ret_code}&" +
                "ret_msg=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F%E3%80%82&service=project_tranfer_notify&" +
                "trade_no=1509025074065552&version=4.0&" +
                "sign=uCQvu1tGvZuJAl%2FGoQHgZYjceelWgQ71ubOOtjqgw%2BOMOsh6VfZcukgtuAk1Pjh00HnfXeRi%2BXfT50bcaesv1NKjJ%2FgFp6oMPZh0rqL32FqugCBZFDrz4HNPjJGljUuhatUmJJvvZMUMMJmlnU7j61ByZ77mvukZ%2Fk4v0AEsi5s%3D&" +
                "sign_type=RSA", orderId + "X" + System.currentTimeMillis(), retCode))
                .andExpect(status().isOk());
    }

    private void generateMockResponse_success(int times) {
        for (int index = 0; index < times; index++) {
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

}
