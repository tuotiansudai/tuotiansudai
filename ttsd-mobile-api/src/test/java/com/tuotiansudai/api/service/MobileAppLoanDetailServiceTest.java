package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppLoanDetailServiceImpl;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
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
    @Mock
    private UserMembershipEvaluator userMembershipEvaluator;
    @Mock
    private ExtraLoanRateMapper extraLoanRateMapper;
    @Mock
    private LoanerDetailsMapper loanerDetailsMapper;
    @Mock
    private LoanDetailsMapper loanDetailsMapper;
    @Mock
    private PledgeHouseMapper pledgeHouseMapper;
    @Mock
    private ContractService contractService;

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
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName("loginName");
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setRecheckTime(new Date());
        loanModel.setVerifyTime(new Date());
        loanModel.setUpdateTime(new Date());
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanModel.setRaisingCompleteTime(new Date());
        List<LoanTitleRelationModel> loanTitleRelationModels = new ArrayList<>();
        LoanTitleRelationModel idCardModel = new LoanTitleRelationModel();
        idCardModel.setTitle("身份证");
        idCardModel.setApplicationMaterialUrls("upload/20160331/92041459408741525.jpg,upload/20160331/46731459408741726.jpg");
        LoanTitleRelationModel houseCardModel = new LoanTitleRelationModel();
        houseCardModel.setTitle("房产证");
        houseCardModel.setApplicationMaterialUrls("upload/20160331/92041459408741525.jpg,upload/20160331/46731459408741726.jpg");
        loanTitleRelationModels.add(idCardModel);
        loanTitleRelationModels.add(houseCardModel);

        MembershipModel membershipModel = new MembershipModel(3,2,50000,0.09);

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.countSuccessInvest(anyLong())).thenReturn(6L);
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(10000L);
        when(loanTitleRelationMapper.findLoanTitleRelationAndTitleByLoanId(anyLong())).thenReturn(loanTitleRelationModels);
        when(userMembershipEvaluator.evaluate(anyString())).thenReturn(membershipModel);
        LoanerDetailsModel loanerDetailsModel = new LoanerDetailsModel();
        loanerDetailsModel.setUserName("UserName");
        when(loanerDetailsMapper.getLoanerDetailByLoanId(anyLong())).thenReturn(loanerDetailsModel);
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(loanModel.getId(), "declaration", "",false);
        when(loanDetailsMapper.getLoanDetailsByLoanId(anyLong())).thenReturn(loanDetailsModel);

        PledgeHouseModel pledgeHouseModel = new PledgeHouseModel();
        pledgeHouseModel.setAuthenticAct("AuthenticAct");
        when(pledgeHouseMapper.getPledgeHouseDetailByLoanId(anyLong())).thenReturn(pledgeHouseModel);
        when(contractService.getContract(anyString(), any(HashMap.class))).thenReturn("loanDetail");

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
        when(extraLoanRateMapper.findByLoanId(anyLong())).thenReturn(null);
        when(loanerDetailsMapper.getLoanerDetailByLoanId(anyLong())).thenReturn(new LoanerDetailsModel());
        when(loanDetailsMapper.getLoanDetailsByLoanId(anyLong())).thenReturn(new LoanDetailsModel());
        when(pledgeHouseMapper.getPledgeHouseDetailByLoanId(anyLong())).thenReturn(new PledgeHouseModel());
        when(contractService.getContract(anyString(),anyMap())).thenReturn("");

        List<LoanTitleRelationModel> loanTitleRelationModelList = Lists.newArrayList();

        LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
        loanTitleRelationModel.setId(idGenerator.generate());
        loanTitleRelationModel.setLoanId(id);
        loanTitleRelationModel.setTitleId(123);
        loanTitleRelationModel.setApplicationMaterialUrls("upload/20151029/20151029092336060.jpg,upload/20151029/20151029092336003.jpg");
        loanTitleRelationModelList.add(loanTitleRelationModel);

        LoanDetailRequestDto loanDetailRequestDto = new LoanDetailRequestDto();
        loanDetailRequestDto.setLoanId("300140750356480");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("");
        loanDetailRequestDto.setBaseParam(baseParam);
        BaseResponseDto<LoanDetailResponseDataDto> baseResponseDto = mobileAppLoanDetailService.generateLoanDetail(loanDetailRequestDto);


        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(6L, baseResponseDto.getData().getInvestedCount().longValue());
        assertEquals("100.00",baseResponseDto.getData().getInvestedMoney());
        assertEquals(idCardModel.getTitle(),baseResponseDto.getData().getEvidence().get(0).getTitle());
        assertEquals(houseCardModel.getTitle(),baseResponseDto.getData().getEvidence().get(1).getTitle());
        assertNotNull(baseResponseDto.getData().getEvidence().get(0).getImageUrl());
        assertNotNull(baseResponseDto.getData().getEvidence().get(1).getImageUrl());
        assertNotNull(baseResponseDto.getData().getRaisingPeriod());
        assertNotNull("loanDetail", baseResponseDto.getData().getLoanDetail());
    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        InvestModel model = new InvestModel();
        model.setAmount(50000);
        // 舍弃毫秒数
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.WAIT_PAY);
        model.setInvestTime(new Date());
        return model;
    }


}
