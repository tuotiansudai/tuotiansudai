package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRepayListRequestDto;
import com.tuotiansudai.api.dto.v1_0.InvestRepayListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppInvestRepayListServiceImpl;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppInvestRepayListServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppInvestRepayListServiceImpl mobileAppInvestRepayListService;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private LoanMapper loanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGenerateUserInvestRepayList() {
        when(investMapper.findById(anyLong())).thenReturn(generateMockedInvestModel());
        when(loanMapper.findById(anyLong())).thenReturn(generateMockedLoanModel());
        when(investRepayMapper.findByLoginNameAndStatus(anyString(), anyString(), anyInt(), anyInt())).thenReturn(generateMockedInvestRepayModel());
        when(investRepayMapper.findCountByLoginNameAndStatus(anyString(), anyString())).thenReturn(230L);
        InvestRepayListRequestDto requestDto = new InvestRepayListRequestDto();
        requestDto.setPageSize(10);
        requestDto.setIndex(1);
        requestDto.setStatus("paid");
        requestDto.setBaseParam(BaseParamTest.getInstance());
        BaseResponseDto<InvestRepayListResponseDataDto> responseDto = mobileAppInvestRepayListService.generateUserInvestRepayList(requestDto);

        assertEquals(230, responseDto.getData().getTotalCount().intValue());
        assertEquals(10, responseDto.getData().getRecordList().size());
    }

    private InvestModel generateMockedInvestModel() {
        InvestModel investModel = new InvestModel();
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setAmount(3159L);
        investModel.setCreatedTime(new Date());
        investModel.setId(idGenerator.generate());
        investModel.setLoanId(idGenerator.generate());
        return investModel;
    }

    private LoanModel generateMockedLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(idGenerator.generate());
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setLoanAmount(543925803L);
        loanModel.setName("test Loan");
        return loanModel;
    }

    private List<InvestRepayModel> generateMockedInvestRepayModel() {
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            InvestRepayModel investRepayModel = new InvestRepayModel();
            investRepayModel.setId(idGenerator.generate());
            investRepayModel.setInvestId(31231231L);
            investRepayModel.setPeriod(i + 1);
            investRepayModel.setRepayDate(new Date());
            investRepayModel.setStatus(RepayStatus.REPAYING);
            investRepayModel.setActualRepayDate(investRepayModel.getRepayDate());
            investRepayModels.add(investRepayModel);
        }
        return investRepayModels;
    }
}
