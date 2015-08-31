package com.tuotiansudai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        URL url = server.getUrl("/register");
        this.payWrapperClient.setHost("http://" + url.getAuthority());

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
    public void shouldCreateLoanTest() throws Exception {
        LoanDto loanDto = new LoanDto();
        loanDto.setId(idGenerator.generate());
        loanDto.setLoanerLoginName("xiangjie");
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
        loanDto.setType(LoanType.LOAN_TYPE_1);
        loanDto.setCreatedTime(new Date());
        loanDto.setStatus(LoanStatus.RECHECK);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);

        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 5; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanDto.getId());
            loanTitleRelationModel.setTitleId(Long.parseLong("12312312312"));
            loanTitleRelationModel.setApplyMetarialUrl("www.baidu.com,www.google.com");
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
        URL url = server.getUrl("/loan");
        this.payWrapperClient.setHost("http://" + url.getAuthority());
        BaseDto<PayDataDto> actualBaseDto = payWrapperClient.loan(loanDto);

        assertTrue(actualBaseDto.isSuccess());
        assertTrue(actualBaseDto.getData().getStatus());
    }
}
