package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserInvestRepayRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestRepayResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppUserInvestRepayServiceImpl;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppUserInvestRepayServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppUserInvestRepayServiceImpl mobileAppUserInvestRepayService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private InvestRepayMapper investRepayMapper;
    @Mock
    private LoanTitleRelationMapper loanTitleRelationMapper;
    @Mock
    private InvestMapper investMapper;
    @Mock
    private LoanTitleMapper loanTitleMapper;

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

    @Test
    public void shouldUserInvestRepayOnePeriodCompleteIsOk(){
        LoanModel loanModel = createLoanModel();
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), "loginuserInvestName");
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

        UserInvestRepayRequestDto userInvestRepayRequestDto =  new UserInvestRepayRequestDto();
        userInvestRepayRequestDto.setInvestId(String.valueOf(investModel.getId()));

        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), responseDto.getCode());
        assertEquals("16", responseDto.getData().getBaseRate());
        assertEquals("1", responseDto.getData().getActivityRate());
        assertEquals("90", responseDto.getData().getDuration());
        assertEquals("_90", responseDto.getData().getProductNewType());
        assertEquals("50.00", responseDto.getData().getInvestAmount());
        assertEquals("50.30", responseDto.getData().getExpectedInterest());
        assertEquals("0.10", responseDto.getData().getActualInterest());
        assertEquals(3, responseDto.getData().getInvestRepays().size());
    }

    @Test
    public void shouldUserInvestRepayTwoPeriodCompleteIsOk(){
        LoanModel loanModel = createLoanModel();
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), "loginuserInvestName");
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

        UserInvestRepayRequestDto userInvestRepayRequestDto =  new UserInvestRepayRequestDto();
        userInvestRepayRequestDto.setInvestId(String.valueOf(investModel.getId()));

        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), responseDto.getCode());
        assertEquals("16", responseDto.getData().getBaseRate());
        assertEquals("1", responseDto.getData().getActivityRate());
        assertEquals("90", responseDto.getData().getDuration());
        assertEquals("_90", responseDto.getData().getProductNewType());
        assertEquals("50.00", responseDto.getData().getInvestAmount());
        assertEquals("50.30", responseDto.getData().getExpectedInterest());
        assertEquals("0.20", responseDto.getData().getActualInterest());
        assertEquals(3, responseDto.getData().getInvestRepays().size());
    }


    @Test
    public void shouldUserInvestRepayThreePeriodCompleteIsOk(){
        LoanModel loanModel = createLoanModel();
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), "loginuserInvestName");
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

        UserInvestRepayRequestDto userInvestRepayRequestDto =  new UserInvestRepayRequestDto();
        userInvestRepayRequestDto.setInvestId(String.valueOf(investModel.getId()));

        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = mobileAppUserInvestRepayService.userInvestRepay(userInvestRepayRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), responseDto.getCode());
        assertEquals("16", responseDto.getData().getBaseRate());
        assertEquals("1", responseDto.getData().getActivityRate());
        assertEquals("90", responseDto.getData().getDuration());
        assertEquals("_90", responseDto.getData().getProductNewType());
        assertEquals("50.00", responseDto.getData().getInvestAmount());
        assertEquals("50.30", responseDto.getData().getExpectedInterest());
        assertEquals("0.30", responseDto.getData().getActualInterest());
        assertEquals(3, responseDto.getData().getInvestRepays().size());
    }

    private LoanModel createLoanModel(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("loginName");
        loanModel.setBaseRate(0.16);
        loanModel.setActivityRate(0.01);
        long id = idGenerator.generate();
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
        model.setId(idGenerator.generate());
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
        investRepayModel.setId(idGenerator.generate());
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


}
