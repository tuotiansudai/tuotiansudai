package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
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
    public void shouldFindUserCouponAndCouponByLoginNameIsSuccess() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);

        CouponModel couponModel = fakeCouponModel();
        couponMapper.create(couponModel);

        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId());
        userCouponMapper.create(userCouponModel);

        List<UserCouponModel> userCouponModelList = userCouponMapper.findUserCouponWithCouponByLoginName("couponTest", null);

        assertNotNull(userCouponModelList);
        assertEquals(1, userCouponModelList.size());

        UserCouponModel userCouponModelDb = userCouponModelList.get(0);
        assertEquals(userCouponModel.getCouponId(), userCouponModelDb.getCouponId());
        assertEquals(userCouponModel.getLoginName(), userCouponModelDb.getLoginName());


        assertEquals(userCouponModelList.get(0).getCoupon().getId(), couponModel.getId());
        assertEquals(userCouponModelList.get(0).getCoupon().getAmount(), couponModel.getAmount());
        assertEquals(String.valueOf(userCouponModelList.get(0).getCoupon().getRate()), String.valueOf(couponModel.getRate()));
        assertEquals(String.valueOf(userCouponModelList.get(0).getCoupon().getBirthdayBenefit()), String.valueOf(couponModel.getBirthdayBenefit()));
        assertEquals(new DateTime(userCouponModelList.get(0).getCoupon().getStartTime()).withTimeAtStartOfDay(),
                new DateTime(couponModel.getStartTime()).withTimeAtStartOfDay());
        assertEquals(new DateTime(userCouponModelList.get(0).getCoupon().getEndTime()).withTimeAtStartOfDay(),
                new DateTime(couponModel.getEndTime()).withTimeAtStartOfDay());
        assertEquals(userCouponModelList.get(0).getCoupon().getDeadline(), couponModel.getDeadline());
        assertEquals(userCouponModelList.get(0).getCoupon().getUsedCount(), couponModel.getUsedCount());
        assertEquals(userCouponModelList.get(0).getCoupon().getTotalCount(), couponModel.getTotalCount());
        assertEquals(userCouponModelList.get(0).getCoupon().isActive(), couponModel.isActive());
        assertEquals(userCouponModelList.get(0).getCoupon().isShared(), couponModel.isShared());
        assertEquals(userCouponModelList.get(0).getCoupon().getIssuedCount(), couponModel.getIssuedCount());
        assertEquals(userCouponModelList.get(0).getCoupon().getInvestLowerLimit(), couponModel.getInvestLowerLimit());
        assertEquals(userCouponModelList.get(0).getCoupon().getCouponType(), couponModel.getCouponType());
        assertEquals(userCouponModelList.get(0).getCoupon().getProductTypes(), couponModel.getProductTypes());
        assertEquals(userCouponModelList.get(0).getCoupon().getUserGroup(), couponModel.getUserGroup());
        assertEquals(userCouponModelList.get(0).getCoupon().isDeleted(), couponModel.isDeleted());
        assertEquals(userCouponModelList.get(0).getCoupon().getCouponSource(), couponModel.getCouponSource());
        assertEquals(userCouponModelList.get(0).getCoupon().getComment(), couponModel.getComment());
    }

    @Test
    public void shouldSumCouponAmountIsOk(){
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);

        CouponModel couponModel1 = fakeCouponModel();
        CouponModel couponModel2 = fakeCouponModel();
        couponMapper.create(couponModel1);
        couponMapper.create(couponModel2);

        UserCouponModel userCouponModel1 = fakeUserCouponModel(couponModel1.getId());
        UserCouponModel userCouponModel2 = fakeUserCouponModel(couponModel2.getId());

        userCouponMapper.create(userCouponModel1);
        userCouponMapper.create(userCouponModel2);


        long couponAmount = userCouponMapper.findSumAmountByCouponId(userModel.getLoginName(), Lists.newArrayList(couponModel1.getId(), couponModel2.getId()));
        assertEquals(couponAmount, couponModel1.getAmount() + couponModel2.getAmount());
    }

    private UserCouponModel fakeUserCouponModel(long couponId) {
        return new UserCouponModel("couponTest", couponId, new Date(), new Date());
    }

    private CouponModel fakeCouponModel() {
        CouponModel couponModel = new CouponModel();
        couponModel.setId(idGenerator.generate());
        couponModel.setAmount(1000L);
        couponModel.setRate(0.1);
        couponModel.setBirthdayBenefit(0.5);
        couponModel.setMultiple(false);
        couponModel.setStartTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setUsedCount(500L);
        couponModel.setTotalCount(1000L);
        couponModel.setActive(false);
        couponModel.setShared(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setCreatedBy("couponTest");
        couponModel.setActivatedBy("couponTest");
        couponModel.setActivatedTime(new Date());
        couponModel.setUpdatedBy("couponTest");
        couponModel.setUpdatedTime(new Date());
        couponModel.setIssuedCount(200);
        couponModel.setActualAmount(120);
        couponModel.setInvestLowerLimit(100);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90));
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setUserGroup(UserGroup.FIRST_INVEST_ACHIEVEMENT);
        couponModel.setTotalInvestAmount(150);
        couponModel.setDeleted(false);
        couponModel.setImportIsRight(true);
        couponModel.setCouponSource("couponSource");
        couponModel.setUserGroup(UserGroup.ALL_USER);

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
}
