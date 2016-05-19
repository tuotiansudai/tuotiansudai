package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.InvestPaginationItemDataDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.security.MyUser;
import com.tuotiansudai.service.InvestService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Date;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml", "classpath:spring-security.xml"})
@WebAppConfiguration
@Transactional
public class TransferControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TransferController transferController;

    @Mock
    private InvestService investService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".ftl");

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.transferController)
                .setViewResolvers(viewResolver)
                .build();
    }
    @Test
    public void shouldTransferrerListDataIsSuccess() throws Exception {
        InvestPaginationItemView investPaginationItemView = new InvestPaginationItemView();
        investPaginationItemView.setInvestTime(new Date());
        investPaginationItemView.setAmount(10000L);
        investPaginationItemView.setNextRepayDate(new DateTime(2016, 8, 8, 0, 0, 0).toDate());
        investPaginationItemView.setNextRepayAmount(12000L);
        investPaginationItemView.setLoanBaseRate(0.12);
        investPaginationItemView.setLoanActivityRate(0.01);
        investPaginationItemView.setSource(Source.IOS);
        investPaginationItemView.setLoanType(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY);
        investPaginationItemView.setStatus(InvestStatus.SUCCESS);
        InvestPaginationItemDataDto investPaginationItemDataDto = new InvestPaginationItemDataDto(investPaginationItemView);
        investPaginationItemDataDto.setLastRepayDate(new DateTime("2017-01-01").toDate());
        investPaginationItemDataDto.setLeftPeriod(3);
        BasePaginationDataDto<InvestPaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(1,10,5,Lists.newArrayList(investPaginationItemDataDto));

        when(investService.getTransferApplicationTransferablePagination(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), any(LoanStatus.class))).thenReturn(basePaginationDataDto);
        mockLoginUser("investor", "13900000000");
        this.mockMvc.perform(get("/transferrer/transfer-application-list-data").param("index", "1").param("pageSize", "10").param("status", TransferStatus.TRANSFERABLE.name()).contentType("application/json;charset=UTF-8"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.data.records[0].nextRepayAmount").value(basePaginationDataDto.getRecords().get(0).getNextRepayAmount()))
                        .andExpect(jsonPath("$.data.records[0].activityRate").value(investPaginationItemView.getLoanActivityRatePercent()))
                        .andExpect(jsonPath("$.data.records[0].baseRate").value(investPaginationItemView.getLoanBaseRatePercent()))
                        .andExpect(jsonPath("$.data.records[0].amount").value(basePaginationDataDto.getRecords().get(0).getAmount()))
                        .andExpect(jsonPath("$.data.records[0].leftPeriod").value(basePaginationDataDto.getRecords().get(0).getLeftPeriod()))
                        .andExpect(jsonPath("$.data.records[0].nextRepayDate").value(new DateTime(basePaginationDataDto.getRecords().get(0).getNextRepayDate()).toString("yyyy-MM-dd")))
                        .andExpect(jsonPath("$.data.records[0].lastRepayDate").value(new DateTime(basePaginationDataDto.getRecords().get(0).getLastRepayDate()).toString("yyyy-MM-dd")));


    }


    private void mockLoginUser(String loginName, String mobile){
        MyUser user = new MyUser(loginName,"", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_PATRON"), mobile, "fdafdsa");
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }
}
