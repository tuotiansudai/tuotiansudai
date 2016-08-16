package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.ExchangeCouponView;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.joda.time.DateTime;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-security.xml"})
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
        userModelTest.setMobile("13900000000");
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

    private ExchangeCouponView createExchangeCoupon() {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy("couponTest");
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(DateTime.parse("2199-12-31").toDate());
        couponModel.setDeadline(100);
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy("creator");
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setIssuedCount(500L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponModel.setCouponSource("couponSource");

        couponMapper.create(couponModel);

        CouponExchangeModel couponExchangeModel = new CouponExchangeModel();
        couponExchangeModel.setCouponId(couponModel.getId());
        couponExchangeModel.setExchangePoint(1000);
        couponExchangeModel.setSeq(1);

        return new ExchangeCouponView(couponModel, couponExchangeModel.getExchangePoint(), couponExchangeModel.getSeq());
    }

    @Test
    public void testFindExchangeableCouponViews() throws Exception {

    }
//    List<ExchangeCouponView> findExchangeableCouponViews(@Param(value = "index") Integer index, @Param(value = "pageSize") Integer pageSize);
//
//    int findExchangeableCouponViewCount(@Param(value = "index") Integer index, @Param(value = "pageSize") Integer pageSize);
//
//    ExchangeCouponView findExchangeableCouponViewById(long id);
}
