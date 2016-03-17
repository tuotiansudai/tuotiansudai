package com.tuotiansudai.paywrapper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:dispatcher-servlet.xml"})
@Transactional
public class InvestServiceTest {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    private MockWebServer mockServer;

    @Autowired
    private PaySyncClient paySyncClient;

    private AccountModel createAccountByUserId(String userId) {
        AccountModel accountModel = new AccountModel(userId,userId,"120101198810012010","","",new Date());
        accountModel.setAutoInvest(true);
        accountModel.setBalance(10000);
        accountModel.setFreeze(10000);
        return accountModel;
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }

    private AutoInvestPlanModel createUserAutoInvestPlan(String userId, int periods, int diffDays){
        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setEnabled(true);
        model.setLoginName(userId);
        model.setRetentionAmount(200);
        model.setAutoInvestPeriods(periods);
        model.setCreatedTime(DateUtils.addDays(new Date(), diffDays));
        model.setId(idGenerator.generate());
        model.setMaxInvestAmount(1000);
        model.setMinInvestAmount(100);
        autoInvestPlanMapper.create(model);
        return model;
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

        return mockWebServer;
    }

    @Before
    public void setup() throws Exception {
<<<<<<< HEAD
=======
        ObjectMapper objectMapper = new ObjectMapper();
>>>>>>> master

        this.mockServer = mockUmPayService();

        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.setUrl(this.mockServer.getUrl("/").toString());
    }

    @After
    public void clean() throws Exception {
        this.mockServer.shutdown();
    }

    @Test
    public void shouldFindValidPlanByPeriod()
    {
        createUserByUserId("test00001");
        createUserAutoInvestPlan("test00001",
                AutoInvestMonthPeriod.Month_1.getPeriodValue(), 0);
        createUserByUserId("test00002");
        createUserAutoInvestPlan("test00002", AutoInvestMonthPeriod.merge(
                AutoInvestMonthPeriod.Month_1,
                AutoInvestMonthPeriod.Month_2).getPeriodValue(), -1);
        createUserByUserId("test00003");
        createUserAutoInvestPlan("test00003",AutoInvestMonthPeriod.merge(
                AutoInvestMonthPeriod.Month_1,
                AutoInvestMonthPeriod.Month_2,
                AutoInvestMonthPeriod.Month_3,
                AutoInvestMonthPeriod.Month_4).getPeriodValue(), -2);
        createUserByUserId("test00004");
        createUserAutoInvestPlan("test00004",AutoInvestMonthPeriod.merge(
                AutoInvestMonthPeriod.Month_1,
                AutoInvestMonthPeriod.Month_2,
                AutoInvestMonthPeriod.Month_3,
                AutoInvestMonthPeriod.Month_4).getPeriodValue(), -9);
        createUserByUserId("test00005");
        createUserAutoInvestPlan("test00005",AutoInvestMonthPeriod.merge(
                AutoInvestMonthPeriod.Month_6,
                AutoInvestMonthPeriod.Month_12).getPeriodValue(), -8);
        createUserByUserId("test00006");
        createUserAutoInvestPlan("test00006",
                AutoInvestMonthPeriod.Month_6.getPeriodValue(), 0);

        List<AutoInvestPlanModel> models = investService.findValidPlanByPeriod(AutoInvestMonthPeriod.Month_1);
        assert models.size() == 3;

        models = investService.findValidPlanByPeriod(AutoInvestMonthPeriod.Month_6);
        assert models.size() == 1;

        models = investService.findValidPlanByPeriod(AutoInvestMonthPeriod.Month_3);
        assert models.size() == 2;
    }

    @Test
    public void shouldAutoInvest(){
        long loanId = this.idGenerator.generate();
        this.createUserByUserId("testLoan");
        this.createUserByUserId("testInvest");
        this.createUserByUserId("testInvest1");
        this.createUserByUserId("testInvest2");
        this.createUserByUserId("testInvest3");

        createAccountByUserId("testInvest1");
        createAccountByUserId("testInvest2");
        createAccountByUserId("testInvest3");

        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("testLoan");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName("testLoan");
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(1);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("1000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RAISING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);

        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 100L, "testInvest", Source.WEB, null);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);

        createUserAutoInvestPlan("testInvest1",AutoInvestMonthPeriod.Month_1.getPeriodValue(), 0);
        createUserAutoInvestPlan("testInvest2",AutoInvestMonthPeriod.Month_1.getPeriodValue(), 0);
        createUserAutoInvestPlan("testInvest3",AutoInvestMonthPeriod.Month_1.getPeriodValue(), 0);

        this.investService.autoInvest(loanId);

        long amount = investMapper.sumSuccessInvestAmount(loanId);

        assert amount == 100;
    }

    @Test
    public void shouldName() throws Exception {
        long loanId = this.idGenerator.generate();
        this.createUserByUserId("loaner");
        this.createUserByUserId("investor");

        createAccountByUserId("loaner");
        AccountModel investorAccountModel = createAccountByUserId("investor");
        accountMapper.create(investorAccountModel);

        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("loaner");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("id");
        loanDto.setAgentLoginName("loaner");
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(1);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("1000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RAISING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);

        InvestModel investModel = new InvestModel();
        investModel.setAmount(100);
        investModel.setCreatedTime(new Date());
        investModel.setId(this.idGenerator.generate());
        investModel.setIsAutoInvest(false);
        investModel.setLoginName("investor");
        investModel.setLoanId(loanId);
        investModel.setSource(Source.WEB);
        investModel.setStatus(InvestStatus.WAIT_PAY);
        investMapper.create(investModel);

        investService.investSuccess(investModel.getId(), investModel, investModel.getLoginName());
    }
}
