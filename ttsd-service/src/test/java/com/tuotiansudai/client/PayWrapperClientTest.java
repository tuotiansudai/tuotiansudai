package com.tuotiansudai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PayWrapperClientTest {

    private MockWebServer server;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Before
    public void setUp() throws Exception {
        this.server = new MockWebServer();
        this.server.start();
    }

    @After
    public void tearDown() throws Exception {
        this.server.shutdown();
    }

    @Test
    public void shouldRegister() throws Exception {
        MockResponse mockResponse = new MockResponse();
        BaseDto baseDto = new BaseDto();
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(true);
        dataDto.setCode("0000");
        dataDto.setMessage("success");
        baseDto.setData(dataDto);

        mockResponse.setBody(objectMapper.writeValueAsString(baseDto));
        server.enqueue(mockResponse);
        payWrapperClient.setHost(server.getHostName());
        payWrapperClient.setPort(String.valueOf(server.getPort()));
        payWrapperClient.setApplicationContext("");

        RegisterAccountDto dto = new RegisterAccountDto();
        dto.setLoginName("loginName");
        dto.setUserName("userName");
        dto.setIdentityNumber("identityNumber");
        dto.setMobile("mobile");
        BaseDto<PayDataDto> actualBaseDto = payWrapperClient.register(dto);

        assertTrue(actualBaseDto.isSuccess());
        assertTrue(actualBaseDto.getData().getStatus());
        assertThat(actualBaseDto.getData().getCode(), is(dataDto.getCode()));
        assertThat(actualBaseDto.getData().getMessage(), is(dataDto.getMessage()));
    }

    @Test
    public void shouldMonitor() throws Exception {
        MockResponse mockResponse = new MockResponse();
        BaseDto<MonitorDataDto> baseDto = new BaseDto<>();
        MonitorDataDto dataDto = new MonitorDataDto();
        dataDto.setStatus(true);
        dataDto.setDatabaseStatus(true);
        dataDto.setRedisStatus(true);
        baseDto.setData(dataDto);

        mockResponse.setBody(objectMapper.writeValueAsString(baseDto));
        server.enqueue(mockResponse);
        payWrapperClient.setHost(server.getHostName());
        payWrapperClient.setPort(String.valueOf(server.getPort()));
        payWrapperClient.setApplicationContext("");

        BaseDto<MonitorDataDto> actualBaseDto = payWrapperClient.monitor();

        assertTrue(actualBaseDto.isSuccess());
        assertTrue(actualBaseDto.getData().getStatus());
        assertTrue(actualBaseDto.getData().isDatabaseStatus());
        assertTrue(actualBaseDto.getData().isRedisStatus());

    }

    @Test
    public void shouldCreateLoanTest() throws Exception {
        createMockUser("xiangjie");

        LoanTitleModel loanTitleModel = new LoanTitleModel();
        long titleId = idGenerator.generate();
        loanTitleModel.setId(titleId);
        loanTitleModel.setType(LoanTitleType.BASE_TITLE_TYPE);
        loanTitleModel.setTitle("房产证");
        loanTitleMapper.create(loanTitleModel);

        LoanDto loanDto = new LoanDto();
        loanDto.setId(idGenerator.generate());
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("5000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setProjectName("店铺资金周转更新");
        loanDto.setActivityRate("12.00");
        loanDto.setBasicRate("16.00");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RECHECK);
        loanDto.setProductType(ProductType._30);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        List<LoanTitleRelationModel> loanTitleRelationModelList = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanDto.getId());
            loanTitleRelationModel.setTitleId(titleId);
            loanTitleRelationModel.setApplicationMaterialUrls("www.baidu.com,www.google.com");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanTitleRelationMapper.create(loanTitleRelationModelList);
        MockResponse mockResponse = new MockResponse();
        BaseDto baseDto = new BaseDto();
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(true);
        dataDto.setCode("0000");
        dataDto.setMessage("success");
        baseDto.setData(dataDto);
        mockResponse.setBody(objectMapper.writeValueAsString(baseDto));
        server.enqueue(mockResponse);
        payWrapperClient.setHost(server.getHostName());
        payWrapperClient.setPort(String.valueOf(server.getPort()));
        payWrapperClient.setApplicationContext("");
        BaseDto<PayDataDto> actualBaseDto = payWrapperClient.createLoan(loanDto);
        assertTrue(actualBaseDto.isSuccess());
        assertTrue(actualBaseDto.getData().getStatus());
    }

    private UserModel createMockUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("139" + RandomStringUtils.randomNumeric(8));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }
}
