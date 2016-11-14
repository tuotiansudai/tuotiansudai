package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppLoanListServiceImpl;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class MobileAppLoanListServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppLoanListServiceImpl mobileAppLoanListService;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserCouponMapper userCouponMapper;

    @Test
    public void shouldUserIsAlreadyCouponGenerateLoanListIsOk() {
        ReflectionTestUtils.setField(mobileAppLoanListService, "defaultFee", 0.1);
        UserModel userModel = getUserModelTest();
        userMapper.create(userModel);

        LoanModel loanModel = getFakeLoanModel(userModel.getLoginName(), ProductType._30);
        LoanModel loanModel1 = getFakeLoanModel(userModel.getLoginName(), ProductType.EXPERIENCE);
        loanMapper.create(loanModel);
        loanMapper.create(loanModel1);

        InvestModel investModel = getFakeInvestModel(loanModel1.getId(), userModel.getLoginName());
        investMapper.create(investModel);

        CouponModel couponModel = fakeCouponModel(userModel.getLoginName());
        couponMapper.create(couponModel);

        UserCouponModel userCouponModel = getUserCouponModel(couponModel.getId(),loanModel.getId(),investModel.getId(),userModel.getLoginName(),null);
        userCouponMapper.create(userCouponModel);

        BaseResponseDto<LoanListResponseDataDto> loanList = mobileAppLoanListService.generateLoanList(getLoanListRequest(userModel.getLoginName()));
        List<LoanResponseDataDto> list = loanList.getData().getLoanList();
        assertTrue(CollectionUtils.isNotEmpty(list));
        assertTrue(list.get(0).getProductNewType() == ProductType.EXPERIENCE.name());
    }

    @Test
    public void shouldUserIsUsedCouponGenerateLoanListIsOk() {
        ReflectionTestUtils.setField(mobileAppLoanListService, "defaultFee", 0.1);
        UserModel userModel = getUserModelTest();
        userMapper.create(userModel);

        LoanModel loanModel = getFakeLoanModel(userModel.getLoginName(), ProductType._30);
        LoanModel loanModel1 = getFakeLoanModel(userModel.getLoginName(), ProductType.EXPERIENCE);
        loanMapper.create(loanModel);
        loanMapper.create(loanModel1);

        InvestModel investModel = getFakeInvestModel(loanModel1.getId(), userModel.getLoginName());
        investMapper.create(investModel);

        CouponModel couponModel = fakeCouponModel(userModel.getLoginName());
        couponMapper.create(couponModel);

        UserCouponModel userCouponModel = getUserCouponModel(couponModel.getId(),loanModel.getId(),investModel.getId(),userModel.getLoginName(),InvestStatus.SUCCESS);
        userCouponMapper.create(userCouponModel);
        userCouponMapper.update(userCouponModel);

        BaseResponseDto<LoanListResponseDataDto> loanList = mobileAppLoanListService.generateLoanList(getLoanListRequest(userModel.getLoginName()));
        List<LoanResponseDataDto> list = loanList.getData().getLoanList();
        assertTrue(CollectionUtils.isNotEmpty(list));
        assertTrue(list.get(0).getProductNewType() != ProductType.EXPERIENCE.name());
    }

    @Test
    public void shouldUserIsNotExitsCouponGenerateLoanListIsOk() {
        ReflectionTestUtils.setField(mobileAppLoanListService, "defaultFee", 0.1);
        UserModel userModel = getUserModelTest();
        userMapper.create(userModel);

        LoanModel loanModel = getFakeLoanModel(userModel.getLoginName(), ProductType._30);
        LoanModel loanModel1 = getFakeLoanModel(userModel.getLoginName(), ProductType.EXPERIENCE);
        loanMapper.create(loanModel);
        loanMapper.create(loanModel1);

        InvestModel investModel = getFakeInvestModel(loanModel1.getId(), userModel.getLoginName());
        investMapper.create(investModel);

        BaseResponseDto<LoanListResponseDataDto> loanList = mobileAppLoanListService.generateLoanList(getLoanListRequest(userModel.getLoginName()));
        List<LoanResponseDataDto> list = loanList.getData().getLoanList();
        assertTrue(CollectionUtils.isNotEmpty(list));
        assertTrue(list.get(0).getProductNewType() != ProductType.EXPERIENCE.name());
    }

    private UserCouponModel getUserCouponModel(long couponId,long loanId,long investId,String loginName, InvestStatus investStatus){
        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(idGenerator.generate());
        userCouponModel.setCouponId(couponId);
        userCouponModel.setLoanId(loanId);
        userCouponModel.setInvestId(investId);
        userCouponModel.setExpectedFee(10);
        userCouponModel.setCreatedTime(DateTime.now().toDate());
        userCouponModel.setLoanName(loginName);
        userCouponModel.setLoginName(loginName);
        userCouponModel.setStartTime(DateTime.now().toDate());
        userCouponModel.setEndTime(DateTime.now().toDate());
        userCouponModel.setStatus(investStatus);
        return userCouponModel;
    }

    private CouponModel fakeCouponModel(String loginName){
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy(loginName);
        couponModel.setActive(false);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy(loginName);
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.NEWBIE_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType.EXPERIENCE));
        couponModel.setCouponSource("couponSource");
        return couponModel;
    }

    private LoanListRequestDto getLoanListRequest(String loginName) {
        LoanListRequestDto loanListRequestDto = new LoanListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        loanListRequestDto.setIndex(1);
        loanListRequestDto.setPageSize(100);
        loanListRequestDto.setBaseParam(baseParam);
        return loanListRequestDto;
    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1000000L, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
    }

    public UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("testLoanList");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private LoanModel getFakeLoanModel(String fakeUserName, ProductType productType) {
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
        loanModel.setPledgeType(PledgeType.HOUSE);

        return loanModel;
    }

}
