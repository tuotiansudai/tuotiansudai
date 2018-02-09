package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserInvestRepayRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestRepayResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppUserInvestRepayServiceImpl;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class MobileAppUserInvestRepayServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppUserInvestRepayServiceImpl mobileAppUserInvestRepayService;
    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private InvestService investService;

    @Mock
    private LoanService loanService;

    @Mock
    private CouponRepayMapper couponRepayMapper;

    @Mock
    private MembershipMapper membershipMapper;

    @Mock
    private UserCouponMapper userCouponMapper;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private UserMembershipEvaluator userMembershipEvaluator;

    @Mock
    private InvestExtraRateMapper investExtraRateMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Test
    public void shouldUserInvestRepayOnePeriodCompleteIsOk(){
        LoanModel loanModel = createLoanModel();
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), "loginuserInvestName");
        investModel.setInvestFeeRate(0.1);
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        InvestRepayModel investRepayModel1 = getFakeInvestReapyModel(investModel.getId(), 1, new DateTime().minusDays(30).toDate(), new DateTime().minusDays(30).toDate(), 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        InvestRepayModel investRepayModel2 = getFakeInvestReapyModel(investModel.getId(), 2, new DateTime().minusDays(-30).toDate(), null, 12, 0, 2, 0, 0, RepayStatus.REPAYING);
        InvestRepayModel investRepayModel3 = getFakeInvestReapyModel(investModel.getId(), 3, new DateTime().minusDays(-60).toDate(), null, 12, 0, 2, 0, 5000, RepayStatus.REPAYING);
        investRepayModels.add(investRepayModel1);
        investRepayModels.add(investRepayModel2);
        investRepayModels.add(investRepayModel3);

        when(investService.findById(anyLong())).thenReturn(investModel);
        when(loanService.findLoanById(anyLong())).thenReturn(loanModel);
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(investRepayModels);
        when(investExtraRateMapper.findByInvestId(anyLong())).thenReturn(new InvestExtraRateModel());
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(userMembershipEvaluator.evaluateSpecifiedDate(anyString(), any(Date.class))).thenReturn(new MembershipModel(0, 1, 0, 0.1));
        when(membershipPrivilegePurchaseService.obtainServiceFee(anyString())).thenReturn(0.1);
        UserInvestRepayRequestDto userInvestRepayRequestDto =  new UserInvestRepayRequestDto();
        userInvestRepayRequestDto.setInvestId(String.valueOf(investModel.getId()));

        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), responseDto.getCode());
        assertEquals("16", responseDto.getData().getBaseRate());
        assertEquals("1", responseDto.getData().getActivityRate());
        assertEquals("90", responseDto.getData().getDuration());
        assertEquals("_90", responseDto.getData().getProductNewType());
        assertEquals("50.00", responseDto.getData().getInvestAmount());
        assertEquals("0.30", responseDto.getData().getExpectedInterest());
        assertEquals("0.10", responseDto.getData().getActualInterest());
        assertEquals(3, responseDto.getData().getInvestRepays().size());
    }

    @Test
    public void shouldUserInvestRepayTwoPeriodCompleteIsOk(){
        LoanModel loanModel = createLoanModel();
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), "loginuserInvestName");
        investModel.setInvestFeeRate(0.1);
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        InvestRepayModel investRepayModel1 = getFakeInvestReapyModel(investModel.getId(), 1, new DateTime().minusDays(30).toDate(), new DateTime().minusDays(30).toDate(), 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        InvestRepayModel investRepayModel2 = getFakeInvestReapyModel(investModel.getId(), 2, new DateTime().minusDays(-30).toDate(), null, 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        InvestRepayModel investRepayModel3 = getFakeInvestReapyModel(investModel.getId(), 3, new DateTime().minusDays(-60).toDate(), null, 12, 0, 2, 0, 5000, RepayStatus.REPAYING);
        investRepayModels.add(investRepayModel1);
        investRepayModels.add(investRepayModel2);
        investRepayModels.add(investRepayModel3);

        when(investService.findById(anyLong())).thenReturn(investModel);
        when(loanService.findLoanById(anyLong())).thenReturn(loanModel);
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(investRepayModels);
        when(userMembershipEvaluator.evaluateSpecifiedDate(anyString(), any(Date.class))).thenReturn(new MembershipModel(0, 1, 0, 0.1));
        when(membershipPrivilegePurchaseService.obtainServiceFee(anyString())).thenReturn(0.1);
        when(investExtraRateMapper.findByInvestId(anyLong())).thenReturn(new InvestExtraRateModel());
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);

        UserInvestRepayRequestDto userInvestRepayRequestDto =  new UserInvestRepayRequestDto();
        userInvestRepayRequestDto.setInvestId(String.valueOf(investModel.getId()));

        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), responseDto.getCode());
        assertEquals("16", responseDto.getData().getBaseRate());
        assertEquals("1", responseDto.getData().getActivityRate());
        assertEquals("90", responseDto.getData().getDuration());
        assertEquals("_90", responseDto.getData().getProductNewType());
        assertEquals("50.00", responseDto.getData().getInvestAmount());
        assertEquals("0.30", responseDto.getData().getExpectedInterest());
        assertEquals("0.20", responseDto.getData().getActualInterest());
        assertEquals(3, responseDto.getData().getInvestRepays().size());
    }

    @Test
    public void shouldUserInvestRepayThreePeriodCompleteIsOk(){
        LoanModel loanModel = createLoanModel();
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), "loginuserInvestName");
        investModel.setInvestFeeRate(0.1);
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        InvestRepayModel investRepayModel1 = getFakeInvestReapyModel(investModel.getId(), 1, new DateTime().minusDays(30).toDate(), new DateTime().minusDays(30).toDate(), 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        InvestRepayModel investRepayModel2 = getFakeInvestReapyModel(investModel.getId(), 2, new DateTime().minusDays(-30).toDate(), null, 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        InvestRepayModel investRepayModel3 = getFakeInvestReapyModel(investModel.getId(), 3, new DateTime().minusDays(-60).toDate(), null, 12, 0, 2, 10, 5000, RepayStatus.COMPLETE);
        investRepayModels.add(investRepayModel1);
        investRepayModels.add(investRepayModel2);
        investRepayModels.add(investRepayModel3);
        List memberships = Lists.newArrayList();
        MembershipModel membershipModel = new MembershipModel();
        membershipModel.setFee(0);
        membershipModel.setExperience(0);
        membershipModel.setLevel(1);
        memberships.add(membershipModel);
        CouponRepayModel couponRepayModel = new CouponRepayModel();
        List userCoupon = Lists.newArrayList();
        UserCouponModel userCouponModel = new UserCouponModel();
        userCoupon.add(userCouponModel);

        when(investService.findById(anyLong())).thenReturn(investModel);
        when(loanService.findLoanById(anyLong())).thenReturn(loanModel);
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(investRepayModels);
        when(couponRepayMapper.findCouponRepayByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(couponRepayModel);
        when(membershipMapper.findAllMembership()).thenReturn(memberships);
        when(userCouponMapper.findByInvestId(anyLong())).thenReturn(userCoupon);
        when(couponMapper.findById(anyLong())).thenReturn(new CouponModel());
        when(investExtraRateMapper.findByInvestId(anyLong())).thenReturn(new InvestExtraRateModel());
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(userMembershipEvaluator.evaluateSpecifiedDate(anyString(), any(Date.class))).thenReturn(new MembershipModel(0, 1, 0, 0.1));
        when(membershipPrivilegePurchaseService.obtainServiceFee(anyString())).thenReturn(0.1);
        UserInvestRepayRequestDto userInvestRepayRequestDto =  new UserInvestRepayRequestDto();
        userInvestRepayRequestDto.setInvestId(String.valueOf(investModel.getId()));

        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), responseDto.getCode());
        assertEquals("16", responseDto.getData().getBaseRate());
        assertEquals("1", responseDto.getData().getActivityRate());
        assertEquals("90", responseDto.getData().getDuration());
        assertEquals("_90", responseDto.getData().getProductNewType());
        assertEquals("50.00", responseDto.getData().getInvestAmount());
        assertEquals("0.30", responseDto.getData().getExpectedInterest());
        assertEquals("0.30", responseDto.getData().getActualInterest());
        assertEquals(3, responseDto.getData().getInvestRepays().size());
    }

    @Test
    public void shouldUserInvestRepayTwoPeriodTransferValueDateIsOk(){
        LoanModel loanModel = createLoanModel();
        InvestModel investModel= getFakeInvestModel(loanModel.getId(), "loginuserInvestName");
        investModel.setInvestFeeRate(0.1);
        InvestModel transferInvestModel = getFakeInvestModel(loanModel.getId(), "loginuserInvestName");
        transferInvestModel.setInvestFeeRate(0.1);
        transferInvestModel.setTransferInvestId(investModel.getId());

        TransferApplicationModel transferApplicationModel=getFakeTransferApplicationModel(investModel.getId(), transferInvestModel.getId(), loanModel.getId(), 2, "loginuserInvestName");
        List<TransferApplicationModel> transferApplicationModels = Arrays.asList(transferApplicationModel);

        InvestRepayModel investRepayModel1 = getFakeInvestReapyModel(investModel.getId(), 1, new DateTime().toDate(), new DateTime().minusDays(30).toDate(), 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        investRepayModel1.setTransferred(false);
        InvestRepayModel investRepayModel2 = getFakeInvestReapyModel(investModel.getId(), 2, new DateTime().plusDays(30).toDate(), null, 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        investRepayModel2.setTransferred(true);
        InvestRepayModel investRepayModel3 = getFakeInvestReapyModel(investModel.getId(), 3, new DateTime().plusDays(60).toDate(), null, 12, 0, 2, 10, 5000, RepayStatus.COMPLETE);
        investRepayModel3.setTransferred(true);
        List<InvestRepayModel> investRepayModels = Arrays.asList(investRepayModel1,investRepayModel2,investRepayModel3);


        InvestRepayModel transferInvestRepayModel2 = getFakeInvestReapyModel(transferInvestModel.getId(), 2, investRepayModel2.getRepayDate(), null, 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        InvestRepayModel transferInvestRepayModel3 = getFakeInvestReapyModel(transferInvestModel.getId(), 3, investRepayModel3.getRepayDate(), null, 12, 0, 2, 10, 5000, RepayStatus.COMPLETE);
        List<InvestRepayModel> transferInvestRepayModels = Lists.newArrayList();
        transferInvestRepayModels.add(transferInvestRepayModel2);
        transferInvestRepayModels.add(transferInvestRepayModel3);

        MembershipModel membershipModel = new MembershipModel();
        membershipModel.setFee(0);
        membershipModel.setExperience(0);
        membershipModel.setLevel(1);
        List memberships = Arrays.asList(membershipModel);
        CouponRepayModel couponRepayModel = new CouponRepayModel();

        UserCouponModel userCouponModel = new UserCouponModel();
        List userCoupon = Arrays.asList(userCouponModel);

        when(investService.findById(anyLong())).thenReturn(transferInvestModel);
        when(transferApplicationMapper.findByInvestId(anyLong())).thenReturn(transferApplicationModel);
        when(transferApplicationMapper.findByTransferInvestId(anyLong(), anyList())).thenReturn(transferApplicationModels);
        when(loanService.findLoanById(anyLong())).thenReturn(loanModel);
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(transferInvestRepayModels);
        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(investRepayModel1);
        when(couponRepayMapper.findCouponRepayByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(couponRepayModel);
        when(membershipMapper.findAllMembership()).thenReturn(memberships);
        when(userCouponMapper.findByInvestId(anyLong())).thenReturn(userCoupon);
        when(couponMapper.findById(anyLong())).thenReturn(new CouponModel());
        when(investExtraRateMapper.findByInvestId(anyLong())).thenReturn(new InvestExtraRateModel());
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(userMembershipEvaluator.evaluateSpecifiedDate(anyString(), any(Date.class))).thenReturn(new MembershipModel(0, 1, 0, 0.1));
        when(membershipPrivilegePurchaseService.obtainServiceFee(anyString())).thenReturn(0.1);
        UserInvestRepayRequestDto userInvestRepayRequestDto =  new UserInvestRepayRequestDto();
        userInvestRepayRequestDto.setInvestId(String.valueOf(transferInvestModel.getId()));

        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);
        assertEquals(new SimpleDateFormat("yyyy/MM/dd").format(new DateTime().plusDays(1).toDate()), responseDto.getData().getRecheckTime());

        investRepayModel1.setTransferred(true);
        InvestRepayModel transferInvestRepayModel1 = getFakeInvestReapyModel(transferInvestModel.getId(), 1, investRepayModel1.getRepayDate(), null, 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        transferInvestRepayModels.add(0,transferInvestRepayModel1);
        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto1 = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);
        assertEquals(new SimpleDateFormat("yyyy/MM/dd").format(new Date()), responseDto1.getData().getRecheckTime());

        loanModel.setType(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY);
        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto2 = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);
        assertEquals(new SimpleDateFormat("yyyy/MM/dd").format(new Date()), responseDto2.getData().getRecheckTime());
    }

    private LoanModel createLoanModel(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("loginName");
        loanModel.setBaseRate(0.16);
        loanModel.setActivityRate(0.01);
        long id = IdGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(3);
        loanModel.setActivityType(ActivityType.EXCLUSIVE);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        //loanModel.setInvestFeeRate(15);
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(1000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName("loginName");
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setRecheckTime(new Date());
        loanModel.setVerifyTime(new Date());
        loanModel.setUpdateTime(new Date());
        loanModel.setRaisingCompleteTime(new Date());
        loanModel.setDuration(90);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setProductType(ProductType._90);
        loanModel.setRecheckTime(new Date());
        return loanModel;
    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        InvestModel model = new InvestModel();
        model.setAmount(5000);
        model.setId(IdGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setInvestTime(new Date());
        return model;
    }

    private InvestRepayModel getFakeInvestReapyModel(long investId, int period, Date repayDate, Date actualRepayDate, long expectedInterest, long defaultInterest, long expectedFee, long repayAmount, long corpus, RepayStatus repayStatus){
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(IdGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayDate(repayDate);
        investRepayModel.setActualRepayDate(actualRepayDate);
        investRepayModel.setExpectedInterest(expectedInterest);
        investRepayModel.setDefaultInterest(defaultInterest);
        investRepayModel.setExpectedFee(expectedFee);
        investRepayModel.setRepayAmount(repayAmount);
        investRepayModel.setCorpus(corpus);
        investRepayModel.setStatus(repayStatus);
        return investRepayModel;
    }

    private TransferApplicationModel getFakeTransferApplicationModel(long investId, long transferInvestId, long loanId, int period, String loginName){
        TransferApplicationModel transferApplicationModel=new TransferApplicationModel();
        transferApplicationModel.setId(IdGenerator.generate());
        transferApplicationModel.setName("ZR20160519-001");
        transferApplicationModel.setInvestId(investId);
        transferApplicationModel.setLoanId(loanId);
        transferApplicationModel.setTransferInvestId(transferInvestId);
        transferApplicationModel.setPeriod(period);
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setLoginName(loginName);
        transferApplicationModel.setInvestAmount(500);
        transferApplicationModel.setTransferAmount(450);
        transferApplicationModel.setTransferTime(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationModel.setLeftPeriod(2);
        transferApplicationModel.setDeadline(new Date());
        return transferApplicationModel;
    }
}
