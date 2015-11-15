package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.LoanDetailRequestDto;
import com.tuotiansudai.api.dto.LoanDetailResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.impl.MobileAppLoanDetailServiceImpl;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppLoanDetailServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppLoanDetailServiceImpl mobileAppLoanDetailService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private LoanTitleRelationMapper loanTitleRelationMapper;
    @Mock
    private InvestMapper investMapper;
    @Mock
    private LoanTitleMapper loanTitleMapper;

    @Test
    public void shouldGenerateLoanDetailIsOk(){
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("loginName");
        loanModel.setBaseRate(16.00);
        long id = idGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(30);
        loanModel.setActivityType(ActivityType.EXCLUSIVE);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestFeeRate(15);
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName("loginName");
        loanModel.setRecheckTime(new Date());
        loanModel.setVerifyTime(new Date());
        loanModel.setUpdateTime(new Date());
        loanModel.setRaisingCompleteTime(new Date());

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.countSuccessInvest(anyLong())).thenReturn(6L);
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(10000L);
        InvestModel investModel1 = getFakeInvestModel(id, "loginName1");
        investModel1.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel2 = getFakeInvestModel(id, "loginName2");
        investModel2.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel3 = getFakeInvestModel(id, "loginName3");
        investModel3.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel4 = getFakeInvestModel(id, "loginName4");
        investModel3.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel5 = getFakeInvestModel(id, "loginName5");
        investModel3.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel6 = getFakeInvestModel(id, "loginName6");
        investModel3.setStatus(InvestStatus.SUCCESS);

        List<InvestModel> investModels = Lists.newArrayList();
        investModels.add(investModel1);
        investModels.add(investModel2);
        investModels.add(investModel3);
        investModels.add(investModel4);
        investModels.add(investModel5);
        investModels.add(investModel6);

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investModels);

        List<LoanTitleRelationModel> loanTitleRelationModelList = Lists.newArrayList();

        LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
        loanTitleRelationModel.setId(idGenerator.generate());
        loanTitleRelationModel.setLoanId(id);
        loanTitleRelationModel.setTitleId(123);
        loanTitleRelationModel.setApplicationMaterialUrls("upload/20151029/20151029092336060.jpg,upload/20151029/20151029092336003.jpg");
        loanTitleRelationModelList.add(loanTitleRelationModel);
        when(loanTitleRelationMapper.findByLoanId(anyLong())).thenReturn(loanTitleRelationModelList);
        LoanDetailRequestDto loanDetailRequestDto = new LoanDetailRequestDto();
        loanDetailRequestDto.setLoanId("1111");
        BaseResponseDto<LoanDetailResponseDataDto> baseResponseDto = mobileAppLoanDetailService.generateLoanDetail(loanDetailRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
        assertEquals(6L,baseResponseDto.getData().getInvestedCount().longValue());
        assertEquals("100.00",baseResponseDto.getData().getInvestedMoney());
        assertEquals(5,baseResponseDto.getData().getInvestRecord().size());


    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        InvestModel model = new InvestModel();
        model.setAmount(50000);
        // 舍弃毫秒数
        Date currentDate = new Date((new Date().getTime() / 1000) * 1000);
        model.setCreatedTime(currentDate);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.WAITING);
        model.setCreatedTime(new Date());
        return model;
    }


}
