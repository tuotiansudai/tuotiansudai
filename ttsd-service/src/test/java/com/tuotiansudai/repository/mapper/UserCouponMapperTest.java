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
    private LoanMapper loanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;


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

    @Test
    public void shouldFindUseCouponByInvestIddIsOk(){
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponModel couponModel = fakeCouponModel();
        couponMapper.create(couponModel);
        LoanModel lm = createLoanModel(userModel.getLoginName());
        loanMapper.create(lm);
        InvestModel model = createInvest(userModel.getLoginName(),lm.getId());
        investMapper.create(model);
        UserCouponModel userCouponModel = createUserCouponModel(userModel.getLoginName(),couponModel.getId(),lm.getId(),model.getId());
        userCouponMapper.create(userCouponModel);
        userCouponMapper.update(userCouponModel);

        List<UserCouponModel> userCouponViewList = userCouponMapper.findUseCouponByInvestId(userModel.getLoginName(),model.getId());
        assertEquals(1, userCouponViewList.size());
    }

    private UserCouponModel fakeUserCouponModel(long couponId) {
        return new UserCouponModel("couponTest", couponId, new Date(), new Date());
    }

    private CouponModel fakeCouponModel() {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000l);
        couponModel.setActivatedBy("couponTest");
        couponModel.setActive(false);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy("couponTest");
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType.JYF, ProductType.SYL));
        couponModel.setId(idGenerator.generate());

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

    private LoanModel createLoanModel(String loanerLoginName) {
        LoanModel lm = new LoanModel();
        lm.setId(idGenerator.generate());
        lm.setName("test loan");
        lm.setDescriptionHtml("fdjakf");
        lm.setDescriptionText("fdjakf");
        lm.setPeriods(1);
        lm.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        lm.setActivityRate(0.1);
        lm.setMinInvestAmount(1);
        lm.setMaxInvestAmount(1000000);
        lm.setLoanAmount(1);
        lm.setLoanerLoginName(loanerLoginName);
        lm.setLoanerUserName("借款人");
        lm.setLoanerIdentityNumber("111111111111111111");
        lm.setAgentLoginName(loanerLoginName);
        lm.setBaseRate(0.2);
        lm.setActivityType(ActivityType.NORMAL);
        lm.setCreatedTime(new Date());
        lm.setFundraisingStartTime(new Date());
        lm.setFundraisingEndTime(new Date());
        lm.setStatus(LoanStatus.RAISING);
        return lm;
    }

    private InvestModel createInvest(String loginName,long loanId){
        InvestModel model = new InvestModel();
        model.setAmount(1000000);
        // 舍弃毫秒数
        Date currentDate = new Date((new Date().getTime() / 1000) * 1000);
        model.setCreatedTime(currentDate);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setCreatedTime(DateUtils.addHours(new Date(), -1));
        return model;
    }

    private UserCouponModel createUserCouponModel(String loginName,long couponId,long loanId,long investId){
        UserCouponModel model = new UserCouponModel();
        model.setId(idGenerator.generate());
        model.setLoginName(loginName);
        model.setCouponId(couponId);
        model.setStartTime(new Date());
        model.setEndTime(new Date());
        model.setLoanId(loanId);
        model.setUsedTime(new Date());
        model.setExpectedInterest(1);
        model.setActualInterest(1);
        model.setDefaultInterest(1);
        model.setExpectedFee(1);
        model.setActualFee(1);
        model.setInvestId(investId);
        model.setStatus(InvestStatus.SUCCESS);
        model.setExchangeCode("1");
        model.setCreatedTime(new Date());
        return model;
    }


}
