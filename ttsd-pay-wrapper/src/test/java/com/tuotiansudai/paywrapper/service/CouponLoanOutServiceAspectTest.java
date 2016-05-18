package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class CouponLoanOutServiceAspectTest {

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
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponLoanOutService couponLoanOutService;

    private MockWebServer mockServer;

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
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.enqueue(mockResponse);
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
        String[] mockInvestUserNames = new String[]{couponCreater, "mock_invest1", "mock_invest2", "mock_invest3"};

        mockUsers(mockInvestUserNames);
        mockAccounts(mockInvestUserNames, mockInitAmount);
        mockLoan(mockLoanId, couponCreater);
        long mockCouponId = mockCoupon(couponCreater, mockCouponAmount);
        mockUserCouponAndInvest(mockInvestUserNames, mockCouponId, mockLoanId, investAmount);

        couponLoanOutService.sendRedEnvelope(mockLoanId);

        AccountModel am1 = accountMapper.findByLoginName(mockInvestUserNames[0]);
        AccountModel am2 = accountMapper.findByLoginName(mockInvestUserNames[1]);
        AccountModel am3 = accountMapper.findByLoginName(mockInvestUserNames[2]);

        long amount1 = am1.getBalance();
        long amount2 = am2.getBalance();
        long amount3 = am3.getBalance();

        assert amount1 == mockInitAmount - investAmount + mockCouponAmount;
        assert amount2 == mockInitAmount - investAmount + mockCouponAmount;
        assert amount3 == mockInitAmount - investAmount + mockCouponAmount;
    }

    private long mockCoupon(String loginName, long amount) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(amount);
        couponModel.setTotalCount(1L);
        couponModel.setActive(true);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponModel.setCouponType(CouponType.RED_ENVELOPE);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponMapper.create(couponModel);
        return couponModel.getId();
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
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userCouponModel);
    }


    private void mockUsers(String[] loginNames) {
        for (int i = 0; i < loginNames.length; i++) {
            mockUser(loginNames[i], "1300000000" + i, "aaa" + i + "@tuotiansudai.com");
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

    private long mockInvest(long loanId, String loginName, long amount) throws AmountTransferException {
        InvestModel im = new InvestModel(idGenerator.generate(), loanId, null, amount, loginName, new Date(), Source.WEB, null);
        im.setStatus(InvestStatus.SUCCESS);
        investMapper.create(im);

        amountTransfer.freeze(loginName, im.getId(), amount, UserBillBusinessType.INVEST_SUCCESS, null, null);
        return im.getId();
    }
}
