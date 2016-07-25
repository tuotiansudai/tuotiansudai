package com.tuotiansudai.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.jsoup.helper.DataUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserCouponServiceTest {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Test
    public void shouldCreateInvestAchievementCouponIsOk() {
        String loginName = "testInvestAchievementUser";
        String firstLoginName = "testFirstInvestUser";
        String maxLoginName = "testMaxInvestUser";
        String LastLoginName = "testLastInvestUser";
        createMockUser(loginName);
        createMockUser(firstLoginName);
        createMockUser(maxLoginName);
        createMockUser(LastLoginName);
        LoanModel loanModel = getFakeExperienceLoan(loginName);
        InvestModel firstInvestModel = createInvest(firstLoginName, loanModel.getId());
        InvestModel maxInvestModel = createInvest(maxLoginName, loanModel.getId());
        InvestModel lastInvestModel = createInvest(LastLoginName, loanModel.getId());
        loanModel.setFirstInvestAchievementId(firstInvestModel.getId());
        loanModel.setMaxAmountAchievementId(maxInvestModel.getId());
        loanModel.setLastInvestAchievementId(lastInvestModel.getId());
        loanMapper.update(loanModel);
        CouponModel firstCoupon = fakeCouponDto(loginName, CouponType.INTEREST_COUPON, UserGroup.FIRST_INVEST_ACHIEVEMENT);
        CouponModel maxCoupon = fakeCouponDto(loginName, CouponType.INTEREST_COUPON, UserGroup.MAX_AMOUNT_ACHIEVEMENT);
        CouponModel lastCoupon = fakeCouponDto(loginName, CouponType.INTEREST_COUPON, UserGroup.LAST_INVEST_ACHIEVEMENT);
        userCouponService.createInvestAchievementCoupon(loanModel.getId());
        List<UserCouponModel> userCouponModelList = userCouponMapper.findByLoginName(firstLoginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertEquals(userCouponModelList.get(0).getCouponId(),firstCoupon.getId());
        userCouponModelList = userCouponMapper.findByLoginName(maxLoginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertEquals(userCouponModelList.get(0).getCouponId(),maxCoupon.getId());
        userCouponModelList = userCouponMapper.findByLoginName(LastLoginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertEquals(userCouponModelList.get(0).getCouponId(),lastCoupon.getId());

    }

    private CouponModel fakeCouponDto(String loginName, CouponType couponType, UserGroup userGroup) {
        Date startDate = DateUtils.addYears(DateTime.now().toDate(),-1);
        Date endDate = DateUtils.addYears(DateTime.now().toDate(),100);
        CouponModel couponModel = new CouponModel();
        couponModel.setStartTime(startDate);
        couponModel.setEndTime(endDate);
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy(loginName);
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setCreatedBy(loginName);
        couponModel.setTotalCount(0l);
        couponModel.setUsedCount(500L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(couponType);
        couponModel.setUserGroup(userGroup);
        couponModel.setDeleted(false);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponMapper.create(couponModel);
        return couponModel;
    }

    private LoanModel getFakeExperienceLoan(String loginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName(loginName);
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

        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    private void createMockUser(String loginName) {
        UserModel um = getFakeUser(loginName);
        userMapper.create(um);
    }

    public UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13" + RandomStringUtils.randomNumeric(9));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null, 0.1);
        investMapper.create(model);
        return model;
    }
}
