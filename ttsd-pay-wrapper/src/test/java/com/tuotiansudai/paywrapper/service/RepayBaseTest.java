package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.AdvanceRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.NormalRepayNotifyRequestModel;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
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
        AccountModel fakeAccount = new AccountModel(userModel.getLoginName(), "payUserId", "payAccountId", new Date());
        fakeAccount.setBalance(1000000);
        fakeAccount.setMembershipPoint(50001);
        return fakeAccount;
    }

    protected UserMembershipModel getFakeUserMemberShip(String loginName, UserMembershipType type, long membershipId) {
        Date expiredTime = new DateTime().plusYears(1).toDate();
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName, membershipId, expiredTime, type);
        return userMembershipModel;
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
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(recheckTime);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        return fakeLoanModel;
    }

    protected LoanRepayModel getFakeLoanRepayModel(long loanRepayId, long loanId, int period, long corpus, long expectedInterest, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        LoanRepayModel fakeLoanRepay = new LoanRepayModel(loanRepayId, loanId, period, corpus, expectedInterest, expectedRepayDate, repayStatus);
        fakeLoanRepay.setActualRepayDate(actualRepayDate);
        return fakeLoanRepay;
    }

    protected Map<String, String> getFakeCallbackParamsMap(long orderId,String service) {
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("service", service)
                .put("sign_type", "RSA")
                .put("sign", "sign")
                .put("mer_id", "mer_id")
                .put("version", "1.0")
                .put("trade_no", "trade_no")
                .put("order_id", String.valueOf(orderId))
                .put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .put("ret_code", "0000")
                .put("status","NOT_DONE")
                .build());
    }

    protected NormalRepayNotifyRequestModel getFakeNormalRepayNotifyRequestModel(Long orderId){
        NormalRepayNotifyRequestModel model = new NormalRepayNotifyRequestModel();
        model.setSign("sign");
        model.setSignType("RSA");
        model.setMerId("mer_id");
        model.setVersion("1.0");
        model.setTradeNo("trade_no");
        model.setOrderId(String.valueOf(orderId));
        model.setStatus(NotifyProcessStatus.NOT_DONE.toString());
        model.setMerDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setService("");
        model.setRetCode("0000");
        model.setRequestData(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setRequestData("mer_date=20161101&mer_id=7099088&order_id="+orderId+"&ret_code=0000&sign_type=RSA&version=1.0&sign=JoP0KGZ1j6hXsovsqFMGfTNwqFXGQFbSMmGp+EfK4vzJtgwAjmESgusrND+KcWPZl+BI1aMiGX6Z6sySa31Xi9+OuTjRfMcWSSnAAcX1PBJdhhEci40XHUw8LRnN3WDwrswu4Zg71kaSrdNT/nGYBaszsvjjwWlhPxslz48cRvc=");
        return model;
    }

    protected AdvanceRepayNotifyRequestModel getFakeAdvanceRepayNotifyRequestModel(Long orderId){
        AdvanceRepayNotifyRequestModel model = new AdvanceRepayNotifyRequestModel();
        model.setSign("sign");
        model.setSignType("RSA");
        model.setMerId("mer_id");
        model.setVersion("1.0");
        model.setTradeNo("trade_no");
        model.setOrderId(String.valueOf(orderId));
        model.setStatus(NotifyProcessStatus.NOT_DONE.toString());
        model.setMerDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setService("");
        model.setRetCode("0000");
        model.setRequestData(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setRequestData("mer_date=20161101&mer_id=7099088&order_id="+orderId+"&ret_code=0000&sign_type=RSA&version=1.0&sign=JoP0KGZ1j6hXsovsqFMGfTNwqFXGQFbSMmGp+EfK4vzJtgwAjmESgusrND+KcWPZl+BI1aMiGX6Z6sySa31Xi9+OuTjRfMcWSSnAAcX1PBJdhhEci40XHUw8LRnN3WDwrswu4Zg71kaSrdNT/nGYBaszsvjjwWlhPxslz48cRvc=");
        return model;
    }

}
