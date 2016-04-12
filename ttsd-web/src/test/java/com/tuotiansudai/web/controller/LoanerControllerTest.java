package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml", "classpath:spring-security.xml"})
@WebAppConfiguration
@Transactional
public class LoanerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilter;

    @Autowired
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(this.springSecurityFilter, "/*")
                .build();
    }

    @Test
    public void shouldGetCompletedLoanerLoanData() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        String loginName = fakeUser.getLoginName();
        String rawPassword = fakeUser.getPassword();
        String encodePassword = myShaPasswordEncoder.encodePassword(rawPassword, fakeUser.getSalt());
        fakeUser.setPassword(encodePassword);
        userMapper.create(fakeUser);

        LoanModel fakeCompletedLoan1 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.COMPLETE);
        LoanModel fakeCompletedLoan2 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.COMPLETE);
        LoanModel fakeCompletedLoan3 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.COMPLETE);
        LoanModel fakeCanceledLoan = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.CANCEL);

        loanMapper.create(fakeCompletedLoan1);
        loanMapper.create(fakeCompletedLoan2);
        loanMapper.create(fakeCompletedLoan3);
        loanMapper.create(fakeCanceledLoan);

        LoanRepayModel completedLoan1Repay1 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 1, 2).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan1Repay2 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 2, 2).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan1Repay3 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 3, 2).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 3, 1).withTimeAtStartOfDay().toDate(), 1, 2, 1, 0);

        LoanRepayModel completedLoan2Repay1 = this.getFakeLoanRepayModel(fakeCompletedLoan2, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 4, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 4, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan2Repay2 = this.getFakeLoanRepayModel(fakeCompletedLoan2, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 5, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 5, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan2Repay3 = this.getFakeLoanRepayModel(fakeCompletedLoan2, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 6, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 6, 1).withTimeAtStartOfDay().toDate(), 10, 2, 1, 0);

        LoanRepayModel completedLoan3Repay1 = this.getFakeLoanRepayModel(fakeCompletedLoan3, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 7, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 7, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan3Repay2 = this.getFakeLoanRepayModel(fakeCompletedLoan3, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 8, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 8, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan3Repay3 = this.getFakeLoanRepayModel(fakeCompletedLoan3, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 9, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 9, 1).withTimeAtStartOfDay().toDate(), 20, 2, 1, 0);

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(completedLoan1Repay1, completedLoan1Repay2, completedLoan1Repay3,
                completedLoan2Repay1, completedLoan2Repay2, completedLoan2Repay3,
                completedLoan3Repay1, completedLoan3Repay2, completedLoan3Repay3);

        loanRepayMapper.create(loanRepayModels);

        HttpSession session = mockMvc.perform(post("/login-handler").param("username", loginName).param("password", rawPassword))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.mockMvc.perform(get("/loaner/loan-list-data?index=1&pageSize=2&status=COMPLETE")
                .session((MockHttpSession) session)
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true))
                .andExpect(jsonPath("$.data.index").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(2))
                .andExpect(jsonPath("$.data.count").value(3))
                .andExpect(jsonPath("$.data.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.data.hasNextPage").value(true))
                .andExpect(jsonPath("$.data.records[0].loanId").value(fakeCompletedLoan3.getId()))
                .andExpect(jsonPath("$.data.records[0].completedDate").value(dateFormat.format(new DateTime().withDate(2000, 9, 1).withTimeAtStartOfDay().toDate())))
                .andExpect(jsonPath("$.data.records[0].expectedRepayAmount").value("0.26"))
                .andExpect(jsonPath("$.data.records[0].actualRepayAmount").value("0.23"))
                .andExpect(jsonPath("$.data.records[1].loanId").value(fakeCompletedLoan2.getId()))
                .andExpect(jsonPath("$.data.records[1].completedDate").value(dateFormat.format(new DateTime().withDate(2000, 6, 1).withTimeAtStartOfDay().toDate())))
                .andExpect(jsonPath("$.data.records[1].expectedRepayAmount").value("0.16"))
                .andExpect(jsonPath("$.data.records[1].actualRepayAmount").value("0.13"));
    }

    @Test
    public void shouldGetRepayingLoanerLoanData() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        String loginName = fakeUser.getLoginName();
        String rawPassword = fakeUser.getPassword();
        String encodePassword = myShaPasswordEncoder.encodePassword(rawPassword, fakeUser.getSalt());
        fakeUser.setPassword(encodePassword);
        userMapper.create(fakeUser);

        LoanModel fakeRepayingLoan1 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.REPAYING);
        LoanModel fakeRepayingLoan2 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.REPAYING);
        LoanModel fakeRepayingLoan3 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.REPAYING);
        LoanModel fakeCompletedLoan1 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.COMPLETE);

        loanMapper.create(fakeRepayingLoan1);
        loanMapper.create(fakeRepayingLoan2);
        loanMapper.create(fakeRepayingLoan3);
        loanMapper.create(fakeCompletedLoan1);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(), new DateTime().withTimeAtStartOfDay().withDate(2000, 1, 1).toDate(), 0, 1, 1, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 2, RepayStatus.REPAYING, new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan1Repay3 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 3, RepayStatus.REPAYING, new DateTime().withDate(2000, 3, 1).withTimeAtStartOfDay().toDate(), null, 10, 1, 0, 0);

        LoanRepayModel repayingLoan2Repay1 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 1, RepayStatus.REPAYING, new DateTime().withDate(2000, 4, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan2Repay2 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 2, RepayStatus.REPAYING, new DateTime().withDate(2000, 5, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan2Repay3 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 3, RepayStatus.REPAYING, new DateTime().withDate(2000, 6, 1).withTimeAtStartOfDay().toDate(), null, 20, 1, 0, 0);

        LoanRepayModel repayingLoan3Repay1 = this.getFakeLoanRepayModel(fakeRepayingLoan3, 1, RepayStatus.REPAYING, new DateTime().withDate(2000, 7, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 1, 0);
        LoanRepayModel repayingLoan3Repay2 = this.getFakeLoanRepayModel(fakeRepayingLoan3, 2, RepayStatus.REPAYING, new DateTime().withDate(2000, 8, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 1, 0);
        LoanRepayModel repayingLoan3Repay3 = this.getFakeLoanRepayModel(fakeRepayingLoan3, 3, RepayStatus.REPAYING, new DateTime().withDate(2000, 9, 1).withTimeAtStartOfDay().toDate(), null, 1, 1, 1, 0);

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2, repayingLoan1Repay3,
                repayingLoan2Repay1, repayingLoan2Repay2, repayingLoan2Repay3,
                repayingLoan3Repay1, repayingLoan3Repay2, repayingLoan3Repay3);

        loanRepayMapper.create(loanRepayModels);

        HttpSession session = mockMvc.perform(post("/login-handler").param("username", loginName).param("password", rawPassword))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        this.mockMvc.perform(get("/loaner/loan-list-data?index=1&pageSize=2&status=REPAYING")
                .session((MockHttpSession) session)
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true))
                .andExpect(jsonPath("$.data.index").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(2))
                .andExpect(jsonPath("$.data.count").value(3))
                .andExpect(jsonPath("$.data.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.data.hasNextPage").value(true))
                .andExpect(jsonPath("$.data.records[0].loanId").value(fakeRepayingLoan1.getId()))
                .andExpect(jsonPath("$.data.records[0].nextRepayDate").value(dateFormat.format(new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate())))
                .andExpect(jsonPath("$.data.records[0].unpaidAmount").value("0.12"))
                .andExpect(jsonPath("$.data.records[1].loanId").value(fakeRepayingLoan2.getId()))
                .andExpect(jsonPath("$.data.records[1].nextRepayDate").value(dateFormat.format(new DateTime().withDate(2000, 4, 1).withTimeAtStartOfDay().toDate())))
                .andExpect(jsonPath("$.data.records[1].unpaidAmount").value("0.23"));
    }

    @Test
    public void shouldGetCanceledLoanerLoanData() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        String loginName = fakeUser.getLoginName();
        String rawPassword = fakeUser.getPassword();
        String encodePassword = myShaPasswordEncoder.encodePassword(rawPassword, fakeUser.getSalt());
        fakeUser.setPassword(encodePassword);
        userMapper.create(fakeUser);

        LoanModel fakeCanceledLoan1 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.CANCEL);
        LoanModel fakeCanceledLoan2 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.CANCEL);
        LoanModel fakeCanceledLoan3 = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.CANCEL);
        LoanModel fakeCompletedLoan = this.getFakeLoan(fakeUser.getLoginName(), fakeUser.getLoginName(), LoanStatus.COMPLETE);

        loanMapper.create(fakeCanceledLoan1);
        fakeCanceledLoan1.setRecheckTime(new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate());
        loanMapper.update(fakeCanceledLoan1);

        loanMapper.create(fakeCanceledLoan2);
        fakeCanceledLoan2.setRecheckTime(new DateTime().withDate(2000, 1, 2).withTimeAtStartOfDay().toDate());
        loanMapper.update(fakeCanceledLoan2);

        loanMapper.create(fakeCanceledLoan3);
        fakeCanceledLoan3.setRecheckTime(new DateTime().withDate(2000, 1, 3).withTimeAtStartOfDay().toDate());
        loanMapper.update(fakeCanceledLoan3);

        loanMapper.create(fakeCompletedLoan);

        LoanRepayModel completedLoanRepay1 = this.getFakeLoanRepayModel(fakeCompletedLoan, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(), 0, 1, 1, 0);
        LoanRepayModel completedLoanRepay2 = this.getFakeLoanRepayModel(fakeCompletedLoan, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate(), 0, 1, 1, 0);
        LoanRepayModel completedLoanRepay3 = this.getFakeLoanRepayModel(fakeCompletedLoan, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 3, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 3, 1).withTimeAtStartOfDay().toDate(), 1, 1, 1, 0);

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(completedLoanRepay1, completedLoanRepay2, completedLoanRepay3);

        loanRepayMapper.create(loanRepayModels);

        HttpSession session = mockMvc.perform(post("/login-handler").param("username", loginName).param("password", rawPassword))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.mockMvc.perform(get("/loaner/loan-list-data?index=1&pageSize=2&status=CANCEL")
                .session((MockHttpSession) session)
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true))
                .andExpect(jsonPath("$.data.index").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(2))
                .andExpect(jsonPath("$.data.count").value(3))
                .andExpect(jsonPath("$.data.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.data.hasNextPage").value(true))
                .andExpect(jsonPath("$.data.records[0].loanId").value(fakeCanceledLoan3.getId()))
                .andExpect(jsonPath("$.data.records[0].recheckTime").value(dateFormat.format(new DateTime().withDate(2000, 1, 3).withTimeAtStartOfDay().toDate())))
                .andExpect(jsonPath("$.data.records[1].loanId").value(fakeCanceledLoan2.getId()))
                .andExpect(jsonPath("$.data.records[1].recheckTime").value(dateFormat.format(new DateTime().withDate(2000, 1, 2).withTimeAtStartOfDay().toDate())));
    }

    private UserModel getFakeUser() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("loginName");
        userModelTest.setPassword("password");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        return fakeLoanModel;
    }

    private LoanRepayModel getFakeLoanRepayModel(LoanModel fakeLoanModel,
                                                 int period,
                                                 RepayStatus repayStatus,
                                                 Date repayDate,
                                                 Date actualRepayDate,
                                                 long corpus,
                                                 long expectedInterest,
                                                 long actualInterest,
                                                 long defaultInterest
    ) {
        LoanRepayModel fakeLoanRepayModel = new LoanRepayModel();
        fakeLoanRepayModel.setId(idGenerator.generate());
        fakeLoanRepayModel.setPeriod(period);
        fakeLoanRepayModel.setStatus(repayStatus);
        fakeLoanRepayModel.setLoanId(fakeLoanModel.getId());
        fakeLoanRepayModel.setRepayDate(repayDate);
        fakeLoanRepayModel.setActualRepayDate(actualRepayDate);
        fakeLoanRepayModel.setCorpus(corpus);
        fakeLoanRepayModel.setExpectedInterest(expectedInterest);
        fakeLoanRepayModel.setActualInterest(actualInterest);
        fakeLoanRepayModel.setDefaultInterest(defaultInterest);
        return fakeLoanRepayModel;
    }
}
