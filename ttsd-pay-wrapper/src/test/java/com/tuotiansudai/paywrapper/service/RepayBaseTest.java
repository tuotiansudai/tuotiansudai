package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.model.*;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class RepayBaseTest {

    @Autowired
    protected PaySyncClient paySyncClient;

    @Autowired
    protected PayAsyncClient payAsyncClient;

    protected MockWebServer mockPayServer;

    @Before
    public void setUp() throws Exception {
        this.mockPayServer = new MockWebServer();
        this.mockPayServer.start();

        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.injectInto(payAsyncClient);
        MockPayGateWrapper.setUrl(this.mockPayServer.getUrl("/").toString());
    }

    @After
    public void tearDown() throws Exception {
        this.mockPayServer.shutdown();
    }

    protected UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setMobile(loginName);
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }

    protected AccountModel getFakeAccount(UserModel userModel) {
        AccountModel fakeAccount = new AccountModel(userModel.getLoginName(), userModel.getLoginName(), "ID", "payUserId", "payAccountId", new Date());
        fakeAccount.setBalance(1000000);
        return fakeAccount;
    }

    protected LoanModel getFakeNormalLoan(long loanId, LoanType loanType, long amount, int periods, double baseRate, double activityRate, double investFeeRate, String loginName, Date recheckTime) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(loanId);
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanAmount(amount);
        fakeLoanModel.setLoanerLoginName(loginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("id");
        fakeLoanModel.setAgentLoginName(loginName);
        fakeLoanModel.setType(loanType);
        fakeLoanModel.setPeriods(periods);
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setBaseRate(baseRate);
        fakeLoanModel.setActivityRate(activityRate);
        fakeLoanModel.setInvestFeeRate(investFeeRate);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(recheckTime);
        return fakeLoanModel;
    }

    protected LoanRepayModel getFakeLoanRepayModel(long loanRepayId, long loanId, int period, long corpus, long expectedInterest, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        LoanRepayModel fakeLoanRepay = new LoanRepayModel(loanRepayId, loanId, period, 0, expectedRepayDate, repayStatus);
        fakeLoanRepay.setCorpus(corpus);
        fakeLoanRepay.setExpectedInterest(expectedInterest);
        fakeLoanRepay.setActualRepayDate(actualRepayDate);
        return fakeLoanRepay;
    }

    protected Map<String, String> getFakeCallbackParamsMap(long orderId) {
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("service", "project_transfer_notify")
                .put("sign_type", "RSA")
                .put("sign", "sign")
                .put("mer_id", "mer_id")
                .put("version", "1.0")
                .put("trade_no", "trade_no")
                .put("order_id", String.valueOf(orderId))
                .put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .put("ret_code", "0000")
                .build());
    }
}
