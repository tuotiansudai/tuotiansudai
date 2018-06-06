package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.loanout.CouponLoanOutService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class CouponLoanOutServiceAspectTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponLoanOutService couponLoanOutService;

    private MockWebServer mockServer;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Before
    public void setup() throws Exception {
        this.mockServer = mockUmPayService();

        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.setUrl(this.mockServer.getUrl("/").toString());
    }

    private MockWebServer mockUmPayService() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                "<html>\n" +
                "  <head>\n" +
                "\t<META NAME=\"MobilePayPlatform\" CONTENT=\"ret_code=0000&ret_msg=成功&sign=HvmQ2sW1pHii6FtvjfhXU/q2kvkb8hWYbCes2WV1jL/XCA38va1Il6eIlirj3/PrIPOexKpjhnYNQymUWRb2Jdwd6DfQ249dxNffENlgMSr3B4S5r/nZ9F+qeh27SyekDUfWfha84vElgIzLyBE/Rl/ISdLGth/9u9GWA5Wd2nM=\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "  </body>\n" +
                "</html>");
        mockResponse.setResponseCode(200);

        for (int i = 0; i < 8; i++)
            mockWebServer.enqueue(mockResponse);

        return mockWebServer;
    }

    @After
    public void clean() throws Exception {
        this.mockServer.shutdown();
    }

    @Test
    public void shouldGetRedEnvelopeMoney() throws Exception {
        long mockLoanId = 123451234512345L;
        long mockInitAmount = 1000000;
        long mockCouponAmount = 1000L;
        long investAmount = 2000L;

        String couponCreater = "couponCreater";
        String loginName1 = "mock_invest1";
        String loginName2 = "mock_invest2";
        String loginName3 = "mock_invest3";
        String[] mockInvestUserNames = new String[]{couponCreater, loginName1, loginName2, loginName3};

        mockUsers(mockInvestUserNames);
        mockAccounts(mockInvestUserNames, mockInitAmount);
        mockLoan(mockLoanId, couponCreater);
        long mockCouponId = mockCoupon(couponCreater, mockCouponAmount);
        List<Long> userCouponIds = mockUserCouponAndInvest(mockInvestUserNames, mockCouponId, mockLoanId, investAmount);

        couponLoanOutService.sendRedEnvelope(mockLoanId);

        userCouponIds.stream().forEach(userCouponId -> {
            couponLoanOutService.sendRedEnvelopTransferInBalanceCallBack(userCouponId);
        });

        // 测试幂等性，多次调用，结果一致
        couponLoanOutService.sendRedEnvelope(mockLoanId);

        verifySendRedEnveloperSuccessAmountTransferMessage(mockCouponAmount, loginName3);
        verifySendRedEnveloperSuccessAmountTransferMessage(mockCouponAmount, loginName2);
        verifySendRedEnveloperSuccessAmountTransferMessage(mockCouponAmount, loginName1);
    }


    private void verifySendRedEnveloperSuccessAmountTransferMessage(long mockCouponAmount, String loginName) {
        try {
            String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
            AmountTransferMessage message = JsonConverter.readValue(messageBody, AmountTransferMessage.class);
            assertThat(message.getLoginName(), CoreMatchers.is(loginName));
            assertThat(message.getAmount(), CoreMatchers.is(mockCouponAmount));
            assertThat(message.getBusinessType(), CoreMatchers.is(UserBillBusinessType.RED_ENVELOPE));
            assertThat(message.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_IN_BALANCE));
        } catch (IOException e) {
            assert false;
        }
    }

    private long mockCoupon(String loginName, long amount) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(amount);
        couponModel.setTotalCount(1L);
        couponModel.setActive(true);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setCouponType(CouponType.RED_ENVELOPE);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponModel.setCouponSource("couponSource");
        couponMapper.create(couponModel);
        return couponModel.getId();
    }

    private List<Long> mockUserCouponAndInvest(String[] loginNames, long couponId, long loanId, long investAmount) throws AmountTransferException {
        List<Long> userCouponIds = Lists.newArrayList();
        for (String loginName : loginNames) {
            long investId = mockInvest(loanId, loginName, investAmount);
            userCouponIds.add(mockUserCoupon(loginName, couponId, loanId, investId));
        }

        return userCouponIds;
    }

    private long mockUserCoupon(String loginName, long couponId, long loanId, long investId) {
        UserCouponModel userCouponModel = new UserCouponModel(loginName, couponId, new Date(), new Date());
        userCouponMapper.create(userCouponModel);
        userCouponModel.setLoanId(loanId);
        userCouponModel.setUsedTime(new DateTime().minusDays(10).toDate());
        userCouponModel.setInvestId(investId);
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userCouponModel);
        return userCouponModel.getId();
    }


    private void mockUsers(String[] loginNames) {
        for (int i = 0; i < loginNames.length; i++) {
            mockUser(loginNames[i], "1300000000" + i, "aaa" + i + "@tuotiansudai.com");
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
        lm.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(lm);
    }

    private long mockInvest(long loanId, String loginName, long amount) throws AmountTransferException {
        InvestModel im = new InvestModel(IdGenerator.generate(), loanId, null, amount, loginName, new Date(), Source.WEB, null, 0.1);
        im.setStatus(InvestStatus.SUCCESS);
        investMapper.create(im);

        AccountModel account = accountMapper.findByLoginName(loginName);
        account.setBalance(account.getBalance() - amount);
        account.setFreeze(account.getFreeze() + amount);
        accountMapper.update(account);
        return im.getId();
    }
}
