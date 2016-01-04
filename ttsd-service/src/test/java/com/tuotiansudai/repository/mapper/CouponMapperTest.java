package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-security.xml"})
@Transactional
public class CouponMapperTest {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;


    @Test
    public void shouldCreateCouponIsSuccess(){
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);

        CouponModel couponModel = fakeCouponModel();
        couponMapper.create(couponModel);

        CouponModel couponModel1 = couponMapper.findById(couponModel.getId());
        assertNotNull(couponModel1.getId());
        assertEquals("优惠券", couponModel1.getName());
        assertEquals(1000L,couponModel1.getAmount());
        assertEquals(false,couponModel1.isActive());
        assertNotNull(couponModel1.getStartTime());
        assertThat(couponModel.getProductTypes().size(), is(3));
    }

    private CouponModel fakeCouponModel(){
        CouponModel couponModel = new CouponModel();
        couponModel.setName("优惠券");
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy("couponTest");
        couponModel.setActive(false);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy("couponTest");
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setInvestQuota(10000L);
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType.JYF, ProductType.SYL, ProductType.WYX));
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
