package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.repository.model.TransferableInvestPaginationItemDataDto;
import com.tuotiansudai.transfer.service.TransferService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransferControllerTest extends BaseControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TransferController transferController;

    @Mock
    private InvestService investService;

    @Mock
    private TransferService transferService;

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
        TransferableInvestPaginationItemDataDto transferableInvestPaginationItemDataDto = new TransferableInvestPaginationItemDataDto();
        transferableInvestPaginationItemDataDto.setAmount("10000.00");
        transferableInvestPaginationItemDataDto.setNextRepayDate(new DateTime(2016, 8, 8, 0, 0, 0).toDate());
        transferableInvestPaginationItemDataDto.setNextRepayAmount("12000.00");
        transferableInvestPaginationItemDataDto.setBaseRate("12%");
        transferableInvestPaginationItemDataDto.setActivityRate("1%");
        transferableInvestPaginationItemDataDto.setLastRepayDate(new DateTime("2017-01-01").toDate());
        transferableInvestPaginationItemDataDto.setLeftPeriod(3);
        BasePaginationDataDto<TransferableInvestPaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(1, 10, 5, Lists.newArrayList(transferableInvestPaginationItemDataDto));

        when(transferService.generateTransferableInvest(anyString(), anyInt(), anyInt())).thenReturn(basePaginationDataDto);
        mockLoginUser("investor");
        this.mockMvc.perform(get("/transferrer/transfer-application-list-data").param("index", "1").param("pageSize", "10").param("status", TransferStatus.TRANSFERABLE.name()).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.records[0].nextRepayAmount").value(basePaginationDataDto.getRecords().get(0).getNextRepayAmount()))
                .andExpect(jsonPath("$.data.records[0].activityRate").value(transferableInvestPaginationItemDataDto.getActivityRate()))
                .andExpect(jsonPath("$.data.records[0].baseRate").value(transferableInvestPaginationItemDataDto.getBaseRate()))
                .andExpect(jsonPath("$.data.records[0].amount").value(basePaginationDataDto.getRecords().get(0).getAmount()))
                .andExpect(jsonPath("$.data.records[0].leftPeriod").value(basePaginationDataDto.getRecords().get(0).getLeftPeriod()))
                .andExpect(jsonPath("$.data.records[0].nextRepayDate").value(new DateTime(basePaginationDataDto.getRecords().get(0).getNextRepayDate()).toString("yyyy-MM-dd")))
                .andExpect(jsonPath("$.data.records[0].lastRepayDate").value(new DateTime(basePaginationDataDto.getRecords().get(0).getLastRepayDate()).toString("yyyy-MM-dd")));

    }


    private void mockLoginUser(String loginName) {
        User user = new User(loginName, "", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_PATRON"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }
}
