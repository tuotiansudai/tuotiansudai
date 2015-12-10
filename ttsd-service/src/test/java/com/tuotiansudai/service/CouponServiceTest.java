package com.tuotiansudai.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
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

    @Test
    public void shouldCreateCouponIsSuccess() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponDto couponDto = fakeCouponDto();
        BaseDto<PayDataDto> baseDto = couponService.createCoupon("couponTest", couponDto);
        assertEquals(true,baseDto.getData().getStatus());
    }
    @Test
    public void shouldCreateCouponAmountIsInvalid(){
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponDto couponDto = fakeCouponDto();
        couponDto.setAmount("0.00");
        BaseDto<PayDataDto> baseDto = couponService.createCoupon("couponTest", couponDto);
        assertEquals(false,baseDto.getData().getStatus());
        assertEquals("投资体验券金额应大于0!",baseDto.getData().getMessage());
    }

    @Test
    public void shouldCreateCouponStartTimeIsInvalid(){
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        CouponDto couponDto = fakeCouponDto();
        DateTime dateTime = new DateTime().plusDays(-1);
        couponDto.setStartTime(dateTime.toDate());
        BaseDto<PayDataDto> baseDto = couponService.createCoupon("couponTest", couponDto);
        assertEquals(false,baseDto.getData().getStatus());
        assertEquals("活动起期不能早于当前日期!",baseDto.getData().getMessage());
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
