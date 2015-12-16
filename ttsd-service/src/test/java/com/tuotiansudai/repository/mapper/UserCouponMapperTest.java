package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponStatus;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
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


    @Test
    public void shouldCreateUserCoupon() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);

        CouponModel couponModel = fakeCouponModel();
        couponMapper.create(couponModel);

        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId());
        userCouponMapper.create(userCouponModel);

        List<UserCouponModel> userCouponModelList = userCouponMapper.findByLoginName("couponTest");

        assertNotNull(userCouponModelList);
        assertEquals(1, userCouponModelList.size());

        UserCouponModel userCouponModelDb = userCouponModelList.get(0);
        assertEquals(userCouponModel.getCouponId(), userCouponModelDb.getCouponId());
        assertEquals(userCouponModel.getLoginName(), userCouponModelDb.getLoginName());
    }

    private UserCouponModel fakeUserCouponModel(long couponId) {
        return new UserCouponModel("couponTest", couponId);
    }

    private CouponModel fakeCouponModel() {
        CouponModel couponModel = new CouponModel();
        couponModel.setName("优惠券");
        couponModel.setAmount(1000l);
        couponModel.setActiveUser("couponTest");
        couponModel.setActive(false);
        couponModel.setCreateTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setStartTime(new Date());
        couponModel.setCreateUser("couponTest");
        couponModel.setTotalCount(1000l);
        couponModel.setUsedCount(500l);

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
