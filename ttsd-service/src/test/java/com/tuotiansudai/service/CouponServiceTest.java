package com.tuotiansudai.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class CouponServiceTest {
    @Autowired
    private CouponService couponService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CouponMapper couponMapper;

    @Test
    public void shouldCreateCouponIsSuccess() throws CreateCouponException {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponDto couponDto = fakeCouponDto();
        DateTime dateTime = new DateTime().plusDays(1);
        couponDto.setStartTime(dateTime.toDate());
        couponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", couponDto);

    }
    @Test
    public void shouldCreateCouponAmountIsInvalid(){
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponDto couponDto = fakeCouponDto();
        couponDto.setAmount("0.00");
        try {
            couponService.createCoupon("couponTest", couponDto);
        } catch (CreateCouponException e) {
            assertEquals("投资体验券金额应大于0!",e.getMessage());
        }

    }

    @Test
    public void shouldCreateCouponStartTimeIsInvalid()  {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponDto couponDto = fakeCouponDto();
        DateTime dateTime = new DateTime().plusDays(-1);
        couponDto.setStartTime(dateTime.toDate());
        try {
            couponService.createCoupon("couponTest", couponDto);
        } catch (CreateCouponException e) {
            assertEquals("活动起期不能早于当前日期!", e.getMessage());
        }
    }
    @Test
    public void shouldAfterReturningUserRegisteredIsSuccess() throws CreateCouponException {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponDto couponDto = fakeCouponDto();
        DateTime startDateTime = new DateTime().plusDays(-1);
        DateTime endDateTime = new DateTime().plusDays(1);
        couponDto.setStartTime(startDateTime.toDate());
        couponDto.setEndTime(endDateTime.toDate());
        CouponModel couponModel = new CouponModel(couponDto);
        couponModel.setCreateUser("couponTest");
        couponModel.setActive(true);
        System.out.println("start" + couponDto.getStartTime().toString());
        System.out.println("end" + couponDto.getEndTime().toString());
        couponMapper.create(couponModel);

        CouponModel couponModel1 = couponMapper.findCouponByName("优惠券");
        long id = couponModel1.getId();
        couponService.afterReturningUserRegistered("couponTest");
        CouponModel couponModel2 = couponMapper.findCouponById(id);

        assertEquals(1,couponModel2.getIssuedCount());

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

    private CouponDto fakeCouponDto(){
        CouponDto couponDto = new CouponDto();
        couponDto.setAmount("1000.00");
        couponDto.setTotalCount("100");
        couponDto.setEndTime(new Date());
        couponDto.setStartTime(new Date());
        couponDto.setName("优惠券");
        return couponDto;
    }


}
