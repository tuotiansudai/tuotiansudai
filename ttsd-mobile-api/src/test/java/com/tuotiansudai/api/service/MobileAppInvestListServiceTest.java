package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppInvestListServiceImpl;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppInvestListServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppInvestListServiceImpl mobileAppInvestListService;

    @Autowired
    private IdGenerator idGenerator;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestService investService;

    @Mock
    private LoanService loanService;

    @Mock
    private RandomUtils randomUtils;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private InvestTransferService investTransferService;

    private static int INVEST_COUNT = 110;
    private static long INTEREST = 1100L;

    @Test
    public void shouldGenerateInvestListIsOk() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setAmount(1000000L);
        investModel1.setInvestTime(new Date());
        investModel1.setInvestTime(new Date());
        investModel1.setId(idGenerator.generate());
        investModel1.setIsAutoInvest(false);
        investModel1.setLoginName("loginName1");
        investModel1.setLoanId(idGenerator.generate());
        investModel1.setSource(Source.ANDROID);
        investModel1.setStatus(InvestStatus.SUCCESS);

        InvestModel investModel2 = new InvestModel();
        investModel2.setAmount(1100000L);
        investModel2.setInvestTime(new Date());
        investModel2.setInvestTime(new Date());
        investModel2.setId(idGenerator.generate());
        investModel2.setIsAutoInvest(false);
        investModel2.setLoginName("loginName2");
        investModel2.setLoanId(idGenerator.generate());
        investModel2.setSource(Source.WEB);
        investModel2.setStatus(InvestStatus.SUCCESS);

        InvestModel investModel3 = new InvestModel();
        investModel3.setAmount(1200000L);
        investModel3.setInvestTime(new Date());
        investModel3.setInvestTime(new Date());
        investModel3.setId(idGenerator.generate());
        investModel3.setIsAutoInvest(false);
        investModel3.setLoginName("loginName3");
        investModel3.setLoanId(idGenerator.generate());
        investModel3.setSource(Source.IOS);
        investModel3.setStatus(InvestStatus.SUCCESS);

        List<InvestModel> investModels = Lists.newArrayList();
        investModels.add(investModel1);
        investModels.add(investModel2);
        investModels.add(investModel3);


        when(investMapper.findByStatus(anyLong(), anyInt(), anyInt(), any(InvestStatus.class))).thenReturn(investModels);

        when(investMapper.findCountByStatus(anyLong(), any(InvestStatus.class))).thenReturn(3L);

        when(randomUtils.encryptLoginName(anyString(),anyString(),anyInt(),anyLong())).thenReturn("log***");

        InvestListRequestDto investListRequestDto = new InvestListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("");
        investListRequestDto.setBaseParam(baseParam);
        investListRequestDto.setLoanId("1111");
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        BaseResponseDto<InvestListResponseDataDto> baseResponseDto = mobileAppInvestListService.generateInvestList(investListRequestDto);


        assertEquals("10000.00", baseResponseDto.getData().getInvestRecord().get(0).getInvestMoney());
        assertEquals("log***", baseResponseDto.getData().getInvestRecord().get(0).getUserName());
        assertEquals("11000.00", baseResponseDto.getData().getInvestRecord().get(1).getInvestMoney());
        assertEquals("log***", baseResponseDto.getData().getInvestRecord().get(1).getUserName());
        assertEquals("12000.00", baseResponseDto.getData().getInvestRecord().get(2).getInvestMoney());
        assertEquals("log***", baseResponseDto.getData().getInvestRecord().get(2).getUserName());
    }


    private List<InvestModel> generateMockedInvestList() {
        List<InvestModel> investModelList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            InvestModel investModel = generateMockedInvestModel();
            investModelList.add(investModel);
        }
        return investModelList;
    }

    private InvestModel generateMockedInvestModel() {
        InvestModel investModel = new InvestModel();
        investModel.setId(idGenerator.generate());
        investModel.setAmount(1000);
        investModel.setInvestTime(new Date());
        investModel.setInvestTime(new Date());
        investModel.setLoginName("loginName");
        investModel.setSource(Source.IOS);
        investModel.setLoanId(1213L);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTransferStatus(TransferStatus.TRANSFERABLE);
        return investModel;
    }

    private LoanModel generateMockedLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setName("test loan name");
        loanModel.setCreatedTime(new Date());
        loanModel.setId(idGenerator.generate());
        return loanModel;
    }

    @Test
    public void shouldGenerateUserInvestList() {
        when(investMapper.findByLoginNameExceptTransfer(anyString(), anyInt(), anyInt(),anyBoolean())).thenReturn(generateMockedInvestList());
        when(investMapper.findCountByLoginNameExceptTransfer(anyString())).thenReturn((long) INVEST_COUNT);
        when(loanMapper.findById(anyLong())).thenReturn(generateMockedLoanModel());
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(Lists.<InvestRepayModel>newArrayList());
        when(investService.estimateInvestIncome(anyLong(), anyLong())).thenReturn(INTEREST);
        when(investTransferService.isTransferable(anyLong())).thenReturn(true);
        when(loanRepayMapper.findEnabledLoanRepayByLoanId(anyLong())).thenReturn(null);

        UserInvestListRequestDto requestDto = new UserInvestListRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        requestDto.setIndex(1);
        requestDto.setPageSize(10);
        requestDto.setTransferStatus(Lists.newArrayList(TransferStatus.TRANSFERABLE, TransferStatus.TRANSFERRING, TransferStatus.SUCCESS));
        BaseResponseDto<UserInvestListResponseDataDto> responseDto = mobileAppInvestListService.generateUserInvestList(requestDto);
        UserInvestListResponseDataDto dataDto = responseDto.getData();

        assertEquals(INVEST_COUNT, dataDto.getTotalCount().intValue());
        assertEquals(10, dataDto.getInvestList().size());
        assertEquals(com.tuotiansudai.api.dto.v1_0.InvestStatus.BID_SUCCESS.getCode(), dataDto.getInvestList().get(0).getInvestStatus());
        assertEquals(com.tuotiansudai.api.dto.v1_0.LoanStatus.RAISING.getCode(), dataDto.getInvestList().get(0).getLoanStatus());
        assertThat(dataDto.getInvestList().get(0).getTransferStatus(), is(TransferStatus.TRANSFERABLE.name()));
    }
}
