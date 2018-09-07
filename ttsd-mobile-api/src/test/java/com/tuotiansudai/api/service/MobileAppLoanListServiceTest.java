package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppLoanListServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppLoanListServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppLoanListServiceImpl mobileAppLoanListService;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private InvestMapper investMapper;
    @Mock
    private CouponService couponService;
    @Mock
    private ExtraLoanRateMapper extraLoanRateMapper;
    @Mock
    private LoanDetailsMapper loanDetailsMapper;
    @Mock
    private UserCouponMapper userCouponMapper;
    @Mock
    private PageValidUtils pageValidUtils;
    @Mock
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Before
    public void before() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setAppVersion("4.2");
        baseParam.setUserId("userId");
        baseParamDto.setBaseParam(baseParam);
        request.setAttribute("baseParam",baseParamDto);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void shouldGenerateLoanListIsOk() {
        ReflectionTestUtils.setField(mobileAppLoanListService, "defaultFee", 0.1);

        List<LoanModel> loanModels = Lists.newArrayList();
        loanModels.add(getFakeLoanModel("test1", ProductType._30));
        loanModels.add(getFakeLoanModel("test2", ProductType.EXPERIENCE));
        LoanModel loanModelNovice = getFakeLoanModel("test3", ProductType._180);
        loanModelNovice.setActivityType(ActivityType.NEWBIE);
        when(loanMapper.findLoanListMobileApp(any(ProductType.class), any(ProductType.class), any(LoanStatus.class), anyDouble(), anyDouble(), anyInt(), anyInt())).thenReturn(loanModels);
        when(loanMapper.findLoanListCountMobileApp(any(ProductType.class), any(LoanStatus.class), anyDouble(), anyDouble())).thenReturn(2);
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(10000L);
        when(membershipPrivilegePurchaseService.obtainServiceFee(anyString())).thenReturn(0.09);
        when(couponService.findExperienceInvestAmount(any(List.class))).thenReturn(1000l);
        when(extraLoanRateMapper.findByLoanId(anyLong())).thenReturn(null);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(null);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);
        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setEndTime(DateTime.now().toDate());
        when(userCouponMapper.findUsedExperienceByLoginName(anyString())).thenReturn(Lists.newArrayList(userCouponModel));

        LoanListRequestDto loanListRequestDto = new LoanListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("testLoan");
        loanListRequestDto.setBaseParam(baseParam);
        loanListRequestDto.setIndex(1);
        loanListRequestDto.setPageSize(10);
        loanListRequestDto.setProductType(ProductType._30.getProductLine());
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListService.generateLoanList(loanListRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), dto.getCode());
        assertEquals(ProductType._30.getProductLine(), dto.getData().getLoanList().get(0).getLoanType());
        assertEquals(String.valueOf(0.09), dto.getData().getLoanList().get(0).getInvestFeeRate());

        assertEquals(String.valueOf(0.0), dto.getData().getLoanList().get(1).getInvestFeeRate());
    }


    @Test
    public void shouldUserNoExistCouponGenerateLoanListIsOk() {
        ReflectionTestUtils.setField(mobileAppLoanListService, "defaultFee", 0.1);

        List<LoanModel> loanModels = Lists.newArrayList();
        loanModels.add(getFakeLoanModel("test2", ProductType.EXPERIENCE));
        loanModels.add(getFakeLoanModel("test1", ProductType._30));
        LoanModel loanModelNovice = getFakeLoanModel("test3", ProductType._180);
        loanModelNovice.setActivityType(ActivityType.NEWBIE);
        when(loanMapper.findLoanListMobileApp(any(ProductType.class), any(ProductType.class), any(LoanStatus.class), anyDouble(), anyDouble(), anyInt(), anyInt())).thenReturn(loanModels);
        when(loanMapper.findLoanListCountMobileApp(any(ProductType.class), any(LoanStatus.class), anyDouble(), anyDouble())).thenReturn(2);
        when(investMapper.sumSuccessInvestAmount(anyLong())).thenReturn(10000L);
        when(membershipPrivilegePurchaseService.obtainServiceFee(anyString())).thenReturn(0.09);
        when(couponService.findExperienceInvestAmount(any(List.class))).thenReturn(1000l);
        when(extraLoanRateMapper.findByLoanId(anyLong())).thenReturn(null);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(null);
        when(userCouponMapper.findUsedExperienceByLoginName(anyString())).thenReturn(null);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);
        LoanListRequestDto loanListRequestDto = new LoanListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("testLoan1");
        baseParam.setAppVersion("4.2");
        loanListRequestDto.setBaseParam(baseParam);
        loanListRequestDto.setIndex(1);
        loanListRequestDto.setPageSize(10);
        loanListRequestDto.setProductType(ProductType._30.getProductLine());
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListService.generateLoanList(loanListRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), dto.getCode());
        assertEquals(ProductType.EXPERIENCE.name(), dto.getData().getLoanList().get(0).getProductNewType());
    }

    private LoanModel getFakeLoanModel(String fakeUserName, ProductType productType) {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(fakeUserName);
        loanModel.setBaseRate(0.16);
        long id = IdGenerator.generate();
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
        loanModel.setProductType(productType);
        loanModel.setPledgeType(PledgeType.NONE);

        return loanModel;
    }

}
