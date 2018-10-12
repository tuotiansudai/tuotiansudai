package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v3_0.LoanListResponseDataDto;
import com.tuotiansudai.api.dto.v3_0.LoanResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppLoanListV3Service;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class MobileAppLoanListV3ServiceTest extends ServiceTestBase {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private MobileAppLoanListV3Service mobileAppLoanListV3Service;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

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

    private UserModel createUser(String loginName) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("123abc");
        userModel.setEmail("12345@abc.com");
        userModel.setMobile(String.valueOf(IdGenerator.generate()));
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModel);

        return userModel;
    }

    private LoanModel createLoan(String loanerLoginName, ActivityType activityType, ProductType productType, LoanStatus loanStatus, Date raisingCompleteTime) {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(IdGenerator.generate());
        loanModel.setName(loanerLoginName);
        loanModel.setLoanerLoginName(loanerLoginName);
        loanModel.setLoanerUserName(loanerLoginName);
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setAgentLoginName(loanerLoginName);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setPeriods(productType.getPeriods());
        loanModel.setStatus(loanStatus);
        loanModel.setActivityType(activityType);
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setCreatedTime(new Date());
        loanModel.setProductType(productType);
        loanModel.setActivityType(activityType);
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanModel.setRaisingCompleteTime(raisingCompleteTime);
        loanModel.setRecheckTime(new Date());
        loanModel.setVerifyTime(new Date());
        loanMapper.create(loanModel);
        loanMapper.update(loanModel);

        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(loanModel.getId(), "", Lists.newArrayList(Source.MOBILE,Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel);

        return loanModel;
    }

    private InvestModel getInvestModel(String loginName, long loanId) {
        InvestModel investModel = new InvestModel(IdGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null, 0.1);
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);
        return investModel;
    }

    @Test
    public void testGenerateIndexLoan() throws Exception {
        UserModel user = createUser("testUser");
        createUser("loaner");
        getFakeExperienceLoan("loaner");
        CouponModel fakeNewbieCoupon = getFakeNewbieCoupon(user);
        getFakeUserCoupon(user, fakeNewbieCoupon);
        //数据库中默认有新手体验标
        final long experienceLoanId = 1L;
        //没有出借过的
        BaseResponseDto baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        LoanListResponseDataDto loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        LoanResponseDataDto loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType.EXPERIENCE.name(), loanResponseDataDto.getProductNewType());
        assertEquals(ActivityType.NEWBIE.name(), loanResponseDataDto.getActivityType());
        //参数为空
        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(null);
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType.EXPERIENCE.name(), loanResponseDataDto.getProductNewType());
        assertEquals(ActivityType.NEWBIE.name(), loanResponseDataDto.getActivityType());

        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan("");
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType.EXPERIENCE.name(), loanResponseDataDto.getProductNewType());
        assertEquals(ActivityType.NEWBIE.name(), loanResponseDataDto.getActivityType());

        Date now = DateTime.now().toDate();
        //没有可投标 && 只出借过体验标
        createLoan("loaner", ActivityType.NORMAL, ProductType._30, LoanStatus.COMPLETE, now);
        createLoan("loaner", ActivityType.NORMAL, ProductType._90, LoanStatus.COMPLETE, now);
        createLoan("loaner", ActivityType.NORMAL, ProductType._180, LoanStatus.COMPLETE, now);
        LoanModel loanModel = createLoan("loaner", ActivityType.NORMAL, ProductType._360, LoanStatus.COMPLETE, now);

        InvestModel investModel = getInvestModel(user.getLoginName(), experienceLoanId);
        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertNotEquals(ProductType.EXPERIENCE.name(), loanResponseDataDto.getProductNewType());
        //没有可投标 && 出借过其它标
        investModel.setLoanId(loanModel.getId());
        investMapper.update(investModel);
        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());

        Date newDate = DateTime.now().toDate();
        //有可投标 && 出借过其它标
        createLoan("loaner", ActivityType.NORMAL, ProductType._30, LoanStatus.RAISING, newDate);
        createLoan("loaner", ActivityType.NORMAL, ProductType._90, LoanStatus.RAISING, newDate);
        createLoan("loaner", ActivityType.NORMAL, ProductType._360, LoanStatus.RAISING, newDate);

        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType._360.name(), loanResponseDataDto.getProductNewType());
        assertEquals(LoanStatus.RAISING.name().toLowerCase(), loanResponseDataDto.getLoanStatus());
        //有可投标 && 只出借过体验标
        investModel.setLoanId(experienceLoanId);
        investMapper.update(investModel);

        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType._30.name(), loanResponseDataDto.getProductNewType());
        assertEquals(LoanStatus.RAISING.name().toLowerCase(), loanResponseDataDto.getLoanStatus());
    }

    @Test
    public void shouldValidInterestPerTenThousandsIsOk(){
        String loginName = "testExtraRate";
        long loanId = IdGenerator.generate();
        UserModel userModel = getUserModelTest(loginName);
        userMapper.create(userModel);
        LoanModel loanModel = createLoanByUserId(loginName, loanId);
        List<ExtraLoanRateModel> extraLoanRateModels = createExtraLoanRate(loanId);
        extraLoanRateMapper.create(extraLoanRateModels);
        loanDetailsMapper.create(createLoanDetails(loanId));

        InvestModel model = new InvestModel(IdGenerator.generate(), loanModel.getId(), null, 1000000L, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV3Service.generateIndexLoan(loginName);

        List<LoanResponseDataDto> loanList = dto.getData().getLoanList();
        assertTrue(CollectionUtils.isNotEmpty(loanList));
        LoanResponseDataDto loanResponseDataDto = loanList.get(0);

        long expectedInterest = investService.estimateInvestIncome(Long.parseLong(loanResponseDataDto.getLoanId()), model.getInvestFeeRate(), loginName, 1000000, new Date());
        assertTrue(String.valueOf(expectedInterest).equals(loanResponseDataDto.getInterestPerTenThousands()));
    }

    private UserModel getUserModelTest(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private LoanDetailsModel createLoanDetails(long loanId){
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setId(IdGenerator.generate());
        loanDetailsModel.setDeclaration("声明材料");
        loanDetailsModel.setExtraSource(Lists.newArrayList(Source.WEB));
        loanDetailsModel.setLoanId(loanId);
        return loanDetailsModel;
    }

    private List<ExtraLoanRateModel> createExtraLoanRate(long loanId) {
        ExtraLoanRateModel model = new ExtraLoanRateModel();
        model.setLoanId(loanId);
        model.setExtraRateRuleId(100001);
        model.setCreatedTime(new Date());
        model.setMinInvestAmount(100000);
        model.setMaxInvestAmount(1000000);
        model.setRate(0.1);
        ExtraLoanRateModel model2 = new ExtraLoanRateModel();
        model2.setLoanId(loanId);
        model2.setExtraRateRuleId(100001);
        model2.setCreatedTime(new Date());
        model2.setMinInvestAmount(1000000);
        model2.setMaxInvestAmount(5000000);
        model2.setRate(0.3);
        ExtraLoanRateModel model3 = new ExtraLoanRateModel();
        model3.setLoanId(loanId);
        model3.setExtraRateRuleId(100001);
        model3.setCreatedTime(new Date());
        model3.setMinInvestAmount(5000000);
        model3.setMaxInvestAmount(0);
        model3.setRate(0.5);
        List<ExtraLoanRateModel> list = Lists.newArrayList();
        list.add(model);
        list.add(model2);
        list.add(model3);
        return list;
    }


    private LoanModel createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RAISING);
        loanDto.setProductType(ProductType._360);
        loanDto.setPledgeType(PledgeType.HOUSE);
        loanDto.setVerifyTime(DateTime.now().toDate());
        loanDto.setRecheckTime(DateTime.now().toDate());
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.RAISING);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private LoanModel getFakeExperienceLoan(String loginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanAmount(10000L);
        fakeLoanModel.setLoanerLoginName(loginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("id");
        fakeLoanModel.setAgentLoginName(loginName);
        fakeLoanModel.setType(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY);
        fakeLoanModel.setPeriods(1);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NEWBIE);
        fakeLoanModel.setProductType(ProductType.EXPERIENCE);
        fakeLoanModel.setBaseRate(0.15);
        fakeLoanModel.setActivityRate(0);
        fakeLoanModel.setDuration(3);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(new Date());
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);

        loanMapper.create(fakeLoanModel);

        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(fakeLoanModel.getId(), "", Lists.newArrayList(Source.MOBILE,Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel);

        return fakeLoanModel;
    }

    private CouponModel getFakeNewbieCoupon(UserModel creator) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(588800);
        couponModel.setActivatedBy(creator.getLoginName());
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        couponModel.setEndTime(new DateTime(couponModel.getStartTime()).plusDays(1).toDate());
        couponModel.setDeadline(10);
        couponModel.setCreatedBy(creator.getLoginName());
        couponModel.setTotalCount(10L);
        couponModel.setUsedCount(0L);
        couponModel.setInvestLowerLimit(0L);
        couponModel.setUserGroup(UserGroup.EXPERIENCE_INVEST_SUCCESS);
        couponModel.setCouponType(CouponType.NEWBIE_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType.EXPERIENCE));
        couponModel.setCouponSource("couponSource");
        couponMapper.create(couponModel);
        return couponModel;
    }

    private UserCouponModel getFakeUserCoupon(UserModel investor, CouponModel couponModel) {
        UserCouponModel userCouponModel = new UserCouponModel(investor.getLoginName(), couponModel.getId(), new Date(), new DateTime().plusDays(couponModel.getDeadline()).toDate());
        userCouponMapper.create(userCouponModel);
        return userCouponModel;
    }
}
