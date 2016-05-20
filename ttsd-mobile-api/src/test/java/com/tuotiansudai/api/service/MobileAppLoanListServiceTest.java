package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.LoanListRequestDto;
import com.tuotiansudai.api.dto.LoanListResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.impl.MobileAppLoanListServiceImpl;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppLoanListServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppLoanListServiceImpl mobileAppRegisterService;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;
    @Test
    public void shouldGenerateLoanListIsOk(){
        List<LoanModel> loanModels = Lists.newArrayList();
        loanModels.add(getFakeLoanModel("test1"));
        loanModels.add(getFakeLoanModel("test2"));
        LoanModel loanModelNovice = getFakeLoanModel("test3");
        loanModelNovice.setActivityType(ActivityType.NEWBIE);
        when(loanMapper.findLoanListMobileApp(any(ProductType.class), any(LoanStatus.class), anyDouble(), anyDouble(), anyInt())).thenReturn(loanModels);
        when(loanMapper.findLoanListCountMobileApp(any(ProductType.class), any(LoanStatus.class), anyDouble(), anyDouble())).thenReturn(2);
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(10000L);
        LoanListRequestDto loanListRequestDto = new LoanListRequestDto();
        loanListRequestDto.setIndex(1);
        loanListRequestDto.setPageSize(10);
        loanListRequestDto.setProductType(ProductType._30.getProductLine());
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppRegisterService.generateLoanList(loanListRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(),dto.getCode());
        assertEquals(ProductType._30.getProductLine(),dto.getData().getLoanList().get(0).getLoanType());

    }

    private LoanModel getFakeLoanModel(String fakeUserName){
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(fakeUserName);
        loanModel.setBaseRate(0.16);
        long id = idGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
        loanModel.setActivityRate(0.12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(30);
        loanModel.setActivityType(ActivityType.EXCLUSIVE);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestFeeRate(0.15);
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName(fakeUserName);
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setProductType(ProductType._30);

        return loanModel;
    }

}
