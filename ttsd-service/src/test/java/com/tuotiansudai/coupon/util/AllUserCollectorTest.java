package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class AllUserCollectorTest {

    @Autowired
    private UserCollector allUserCollector;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Test
    public void shouldVerifyFalseWhenCouponOrUserIsNull() {
        assertFalse(allUserCollector.contains(null, new UserModel()));
        assertFalse(allUserCollector.contains(new CouponModel(), null));
    }

    @Test
    public void shouldVerifyTrueWhenCouponIsNotBirthdayCoupon() {
        UserModel userTodayIsNotBirthday = this.fakeUserModel(new DateTime().plusDays(10).toDate());
        CouponModel couponModel = this.fakeCouponModel(userTodayIsNotBirthday.getLoginName(), CouponType.RED_ENVELOPE);
        assertTrue(allUserCollector.contains(couponModel, userTodayIsNotBirthday));
    }

    @Test
    public void shouldVerifyTrueWhenCouponIsBirthdayCouponAndUserTodayIsBirthday() {
        UserModel userTodayIsBirthday = this.fakeUserModel(new Date());
        CouponModel couponModel = this.fakeCouponModel(userTodayIsBirthday.getLoginName(), CouponType.BIRTHDAY_COUPON);
        assertTrue(allUserCollector.contains(couponModel, userTodayIsBirthday));
    }

    @Test
    public void shouldVerifyFalseWhenCouponIsBirthdayCouponAndUserTodayIsNotBirthday() {
        UserModel userTodayIsBirthday = this.fakeUserModel(new DateTime().plusMonths(1).toDate());
        CouponModel couponModel = this.fakeCouponModel(userTodayIsBirthday.getLoginName(), CouponType.BIRTHDAY_COUPON);
        assertFalse(allUserCollector.contains(couponModel, userTodayIsBirthday));
    }

    private UserModel fakeUserModel(Date birthday) {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(RandomStringUtils.randomAlphabetic(5));
        fakeUserModel.setPassword("123abc");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile(RandomStringUtils.randomNumeric(11));
        fakeUserModel.setIdentityNumber("xxxxxx" + new DateTime(birthday).toString("yyyyMMdd") + "xxxx");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        return fakeUserModel;
    }

    private CouponModel fakeCouponModel(String activatedBy, CouponType couponType) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1L);
        couponModel.setActivatedBy(activatedBy);
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setStartTime(new Date());
        couponModel.setEndTime(new DateTime(couponModel.getStartTime()).plusDays(10).toDate());
        couponModel.setDeadline(10);
        couponModel.setCreatedBy(activatedBy);
        couponModel.setTotalCount(1L);
        couponModel.setUsedCount(0L);
        couponModel.setInvestLowerLimit(0L);
        couponModel.setCouponType(couponType);
        couponModel.setCouponSource("source");
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponMapper.create(couponModel);
        return couponModel;
    }
}
