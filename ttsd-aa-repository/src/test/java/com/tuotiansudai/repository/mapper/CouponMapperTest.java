package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.ExchangeCouponView;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class CouponMapperTest {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;

    private CouponModel fakeCouponModel(){
        CouponModel couponModel = new CouponModel();
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
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponModel.setCouponSource("couponSource");
        return couponModel;
    }

    private UserModel fakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("couponTest");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(RandomStringUtils.randomNumeric(11));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    @Test
    public void shouldCreateCouponIsSuccess() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);

        CouponModel couponModel = fakeCouponModel();
        couponMapper.create(couponModel);

        CouponModel couponModel1 = couponMapper.findById(couponModel.getId());
        assertNotNull(couponModel1.getId());
        assertEquals(1000L, couponModel1.getAmount());
        assertEquals(false, couponModel1.isActive());
        assertNotNull(couponModel1.getStartTime());
        assertThat(couponModel.getProductTypes().size(), is(3));
    }

    private ExchangeCouponView createExchangeCoupon(long totalCount, long issuedCount, boolean active, boolean deleted, CouponType couponType, Date endTime, String comment) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy("couponTest");
        couponModel.setActive(active);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(endTime);
        couponModel.setDeadline(100);
        couponModel.setDeleted(deleted);
        couponModel.setStartTime(new DateTime().minusDays(2).toDate());
        couponModel.setCreatedBy("couponTest");
        couponModel.setTotalCount(totalCount);
        couponModel.setUsedCount(500L);
        couponModel.setIssuedCount(issuedCount);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(couponType);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponModel.setCouponSource("couponSource");
        couponModel.setComment(comment);

        couponMapper.create(couponModel);

        return new ExchangeCouponView();
    }


}
