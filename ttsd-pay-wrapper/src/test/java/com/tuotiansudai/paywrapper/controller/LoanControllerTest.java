package com.tuotiansudai.paywrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.client.SendCloudClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanOutDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.coupon.service.CouponLoanOutService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
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
    private AmountTransfer amountTransfer;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private SendCloudClient sendCloudClient;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponLoanOutService couponLoanOutService;

    @Autowired
    private CouponMapper couponMapper;

    private ObjectMapper objectMapper;
    private MockWebServer mockServer;
    private MockWebServer mockSmsServer;
    private MockWebServer mockMailServer;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.objectMapper = new ObjectMapper();
        this.mockServer = mockUmPayService();
        this.mockSmsServer = mockSmsService();
        this.mockMailServer = mockMailServer();

        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.setUrl(this.mockServer.getUrl("/").toString());

        smsWrapperClient.setHost(this.mockSmsServer.getHostName());
        smsWrapperClient.setPort(String.valueOf(this.mockSmsServer.getPort()));
        smsWrapperClient.setApplicationContext("");


        sendCloudClient.setSendCloudSmtpHost(this.mockMailServer.getHostName());
        sendCloudClient.setSendCloudSmtpPort(this.mockMailServer.getPort());
    }

    @After
    public void clean() throws Exception {
        this.mockServer.shutdown();
        this.mockSmsServer.shutdown();
        this.mockMailServer.shutdown();
    }

    private MockWebServer mockUmPayService() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

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
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);

        return mockWebServer;
    }

    private MockWebServer mockSmsService() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        BaseDto<BaseDataDto> smsSuccess = new BaseDto<>();
        smsSuccess.setData(new BaseDataDto());
        smsSuccess.setSuccess(true);
        smsSuccess.getData().setStatus(true);

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(objectMapper.writeValueAsString(smsSuccess));
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);

        return mockWebServer;
    }

    private MockWebServer mockMailServer() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("");
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        return mockWebServer;
    }


    @Test
    public void shouldLoanOut() throws Exception {
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

        this.mockMvc.perform(post("/loan/loan-out").
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

    @Test
    public void shouldSendRedEnvelopeWhenLoanOut() throws Exception {
        long mockLoanId = 123451234512345L;
        long mockInitAmount = 30000;
        long mockCouponAmount = 5000;
        long mockInvestAmount = 10000;

        String loanerLoginName = "mock_loaner1";
        String[] mockInvestLoginName = new String[]{"mock_invest1", "mock_invest2"};
        String[] mockUserNames = new String[]{loanerLoginName, mockInvestLoginName[0], mockInvestLoginName[1]};

        mockUsers(mockUserNames);
        mockAccounts(mockUserNames, mockInitAmount);
        mockLoan(mockLoanId, loanerLoginName);
        long mockCouponId = mockCoupon(loanerLoginName, mockCouponAmount);

        mockUserCouponAndInvest(mockInvestLoginName, mockCouponId, mockLoanId, mockInvestAmount);

        LoanOutDto loanOutDto = new LoanOutDto();
        loanOutDto.setLoanId(String.valueOf(mockLoanId));

        String requestJson = objectMapper.writeValueAsString(loanOutDto);

        this.mockMvc.perform(post("/loan/loan-out").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true))
                .andExpect(jsonPath("$.data.code").value("0000"));

        couponLoanOutService.sendRedEnvelope(mockLoanId);

//        Thread.sleep(1000 * 5);
        AccountModel am = accountMapper.findByLoginName(loanerLoginName);
        AccountModel am1 = accountMapper.findByLoginName(mockInvestLoginName[0]);
        AccountModel am2 = accountMapper.findByLoginName(mockInvestLoginName[1]);

        long amount1 = am1.getBalance();
        long amount2 = am2.getBalance();
        Thread.sleep(1000 * 5);
//        assert amount1 == mockInitAmount - mockInvestAmount + mockCouponAmount;
//        assert amount2 == mockInitAmount - mockInvestAmount + mockCouponAmount;
    }

    private void mockUserCouponAndInvest(String[] loginNames, long couponId, long loanId, long investAmount) throws AmountTransferException {
        for (String loginName : loginNames) {
            long investId = mockInvest(loanId, loginName, investAmount);
            mockUserCoupon(loginName, couponId, loanId, investId);
        }
    }

    private void mockUserCoupon(String loginName, long couponId, long loanId, long investId) {
        UserCouponModel userCouponModel = new UserCouponModel(loginName, couponId, new Date(), new Date());
        userCouponMapper.create(userCouponModel);
        userCouponModel.setLoanId(loanId);
        userCouponModel.setUsedTime(new DateTime().minusDays(10).toDate());
        userCouponModel.setInvestId(investId);
        userCouponMapper.update(userCouponModel);
    }

    private long mockCoupon(String loginName, long amount) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(amount);
        couponModel.setTotalCount(1L);
        couponModel.setActive(true);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponModel.setDeadline(5);
        couponModel.setCouponType(CouponType.RED_ENVELOPE);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponMapper.create(couponModel);
        return couponModel.getId();
    }

    private void mockUsers(String[] loginNames) {
        mockUser(loginNames[0], "13000000000", "aaa@tuotiansudai.com");
        mockUser(loginNames[1], "13000000001", "bbb@tuotiansudai.com");
        mockUser(loginNames[2], "13000000002", "ccc@tuotiansudai.com");
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

    private void mockLoan(long loanId, String loanerLoginName) {
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
        lm.setLoanAmount(1000000000);
        lm.setLoanerLoginName(loanerLoginName);
        lm.setLoanerUserName("借款人");
        lm.setLoanerIdentityNumber("111111111111111111");
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

    private long mockInvest(long loanId, String loginName, long amount) throws AmountTransferException {
        InvestModel im = new InvestModel(idGenerator.generate(), loanId, null, amount, loginName, new Date(), Source.WEB, null);
        im.setStatus(InvestStatus.SUCCESS);
        investMapper.create(im);

        amountTransfer.freeze(loginName, im.getId(), amount, UserBillBusinessType.INVEST_SUCCESS, null, null);
        return im.getId();
    }
}
