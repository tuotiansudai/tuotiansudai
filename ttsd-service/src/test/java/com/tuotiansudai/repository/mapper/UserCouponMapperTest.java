package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.time.DateUtils;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-security.xml"})
@Transactional
public class UserCouponMapperTest {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Test
    public void shouldCreateUserCoupon() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);

        CouponModel couponModel = fakeCouponModel();
        couponMapper.create(couponModel);

        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId());
        userCouponMapper.create(userCouponModel);

        List<UserCouponModel> userCouponModelList = userCouponMapper.findByLoginName("couponTest", null);

        assertNotNull(userCouponModelList);
        assertEquals(1, userCouponModelList.size());

        UserCouponModel userCouponModelDb = userCouponModelList.get(0);
        assertEquals(userCouponModel.getCouponId(), userCouponModelDb.getCouponId());
        assertEquals(userCouponModel.getLoginName(), userCouponModelDb.getLoginName());
    }

    private UserCouponModel fakeUserCouponModel(long couponId) {
        return new UserCouponModel("couponTest", couponId, new Date(), new Date());
    }

    private CouponModel fakeCouponModel() {
        CouponModel couponModel = new CouponModel();
        couponModel.setId(idGenerator.generate());
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy("couponTest");
        couponModel.setActive(false);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy("couponTest");
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90));

        return couponModel;
    }

    private UserModel fakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("couponTest");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    @Test
    public void shouldFindByAchievementLoanIdOrCouponTypeIsOk(){
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponModel couponModel = fakeCouponModel();
        couponMapper.create(couponModel);
        LoanModel fakeLoan = this.getFakeLoan(userModel.getLoginName(), userModel.getLoginName(), LoanStatus.PREHEAT,ActivityType.NEWBIE);
        loanMapper.create(fakeLoan);
        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId());
        userCouponModel.setAchievementLoanId(fakeLoan.getId());
        userCouponMapper.create(userCouponModel);
        List<UserCouponModel> userCouponModelList = userCouponMapper.findByAchievementLoanId(fakeLoan.getId());
        assertTrue(userCouponModelList.size() > 0);
        assertTrue(userCouponModelList.get(0).getAchievementLoanId() == fakeLoan.getId());
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus,ActivityType activityType) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        return fakeLoanModel;
    }
}
