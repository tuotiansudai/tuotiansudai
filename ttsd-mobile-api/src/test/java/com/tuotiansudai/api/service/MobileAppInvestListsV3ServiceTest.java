package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.service.v3_0.impl.MobileAppInvestListsV3ServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.service.InvestTransferService;
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

public class MobileAppInvestListsV3ServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppInvestListsV3ServiceImpl mobileAppInvestListsV3Service;

    @Autowired
    private IdGenerator idGenerator;

    @Mock
    private InvestTransferService investTransferService;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private UserCouponMapper userCouponMapper;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private InvestExtraRateMapper investExtraRateMapper;

    @Mock
    private CouponRepayMapper couponRepayMapper;

    @Mock
    private LoanDetailsMapper loanDetailsMapper;

    @Mock
    private PageValidUtils pageValidUtils;

    private LoanModel getFakeLoanModel(String fakeUserName, long loanId){
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(fakeUserName);
        loanModel.setBaseRate(0.16);
        loanModel.setId(loanId);
        loanModel.setName("店铺资金周转");
        loanModel.setActivityRate(0.12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(3);
        loanModel.setActivityType(ActivityType.NORMAL);
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
        loanModel.setStatus(LoanStatus.COMPLETE);
        loanModel.setLoanerLoginName(fakeUserName);
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setProductType(ProductType._30);
        return loanModel;
    }

    private InvestRepayModel getFakeInvestRepay(long investId, int period) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setDefaultInterest(0);
        investRepayModel.setActualFee(0);
        investRepayModel.setExpectedInterest(0);
        investRepayModel.setActualInterest(0);
        investRepayModel.setExpectedFee(0);
        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayModel.setCreatedTime(new Date());
        investRepayModel.setPeriod(period);
        investRepayModel.setActualRepayDate(new Date());
        return investRepayModel;
    }

    private InvestModel generateMockedInvestModel(long loanId,String loginName) {
        InvestModel investModel = new InvestModel();
        investModel.setId(idGenerator.generate());
        investModel.setAmount(1000);
        investModel.setInvestTime(new Date());
        investModel.setInvestTime(new Date());
        investModel.setLoginName(loginName);
        investModel.setSource(Source.IOS);
        investModel.setLoanId(loanId);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTransferStatus(TransferStatus.TRANSFERABLE);
        return investModel;
    }


    @Test
    public void shouldGenerateUserInvestList() {
        long loanId = idGenerator.generate();
        LoanModel loanModel = getFakeLoanModel("loaner", loanId);
        InvestModel investModel = generateMockedInvestModel(loanId, "investor");
        List<InvestModel> investModels = Lists.newArrayList(investModel);

        InvestRepayModel investRepayModel1 = getFakeInvestRepay(investModel.getId(), 1);
        InvestRepayModel investRepayModel2 = getFakeInvestRepay(investModel.getId(), 2);
        InvestRepayModel investRepayModel3 = getFakeInvestRepay(investModel.getId(), 3);

        List<InvestRepayModel> investRepayModels = Lists.newArrayList(investRepayModel1, investRepayModel2, investRepayModel3);

        when(investMapper.findInvestorInvestAndTransferPagination(anyString(), any(LoanStatus.class), anyInt(), anyInt())).thenReturn(investModels);

        when(loanMapper.findById(anyInt())).thenReturn(loanModel);

        when(transferApplicationMapper.findByInvestId(anyLong())).thenReturn(null);

        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(investRepayModels);

        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(investRepayModel3);

        List<UserCouponModel> userCouponModels = Lists.newArrayList();

        when(userCouponMapper.findUserCouponSuccessByInvestId(anyLong())).thenReturn(userCouponModels);

        when(couponMapper.findById(anyLong())).thenReturn(null);

        when(investExtraRateMapper.findByInvestId(anyLong())).thenReturn(null);

        when(couponRepayMapper.findCouponRepayByInvestIdAndPeriod(anyLong(),anyInt())).thenReturn(new CouponRepayModel());

        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(new LoanDetailsModel());

        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);

        UserInvestListRequestDto investListRequestDto = new UserInvestListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("investor");
        investListRequestDto.setBaseParam(baseParam);
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        investListRequestDto.setStatus(LoanStatus.COMPLETE);

        BaseResponseDto<UserInvestListResponseDataDto> baseResponseDto = mobileAppInvestListsV3Service.generateUserInvestList(investListRequestDto);

        assertEquals(baseResponseDto.getData().getInvestList().get(0).getLoanId(), String.valueOf(loanId));
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getInvestId(), String.valueOf(investModel.getId()));
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getExpectedInterest(), "0.00");
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getActualInterest(), "0.00");
        assertEquals(baseResponseDto.getData().getInvestList().get(0).isUsedCoupon(), false);
        assertEquals(baseResponseDto.getData().getInvestList().get(0).isUsedRedEnvelope(), false);
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getProductNewType(), ProductType._30.name());

    }

}
