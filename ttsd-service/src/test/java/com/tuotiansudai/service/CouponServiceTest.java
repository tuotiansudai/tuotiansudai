package com.tuotiansudai.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCouponMapper userCouponMapper;

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
        couponMapper.create(couponModel);

        couponService.afterReturningUserRegistered(userModel.getLoginName());
        CouponModel couponModel2 = couponMapper.findCouponById(couponModel.getId());

        assertEquals(1, couponModel2.getIssuedCount());

    }

    @Test
    public void shouldRegisterUserIsSuccess() throws ReferrerRelationException {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);

        RegisterUserDto registerUserDto = fakeRegisterUserDto();

        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setMobile("18600000101");
        smsCaptchaModel.setCaptcha("111111");
        smsCaptchaModel.setCaptchaType(CaptchaType.REGISTER_CAPTCHA);
        smsCaptchaModel.setCreatedTime(new Date());
        DateTime expiredTime = new DateTime().plusDays(1);
        smsCaptchaModel.setExpiredTime(expiredTime.toDate());
        smsCaptchaMapper.create(smsCaptchaModel);

        CouponDto couponDto = fakeCouponDto();
        DateTime startDateTime = new DateTime().plusDays(-1);
        DateTime endDateTime = new DateTime().plusDays(1);
        couponDto.setStartTime(startDateTime.toDate());
        couponDto.setEndTime(endDateTime.toDate());
        CouponModel couponModel = new CouponModel(couponDto);
        couponModel.setCreateUser(userModel.getLoginName());
        couponModel.setActive(true);
        couponMapper.create(couponModel);

        userService.registerUser(registerUserDto);


        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(registerUserDto.getLoginName());
        CouponModel couponModel1 = couponMapper.findCouponById(couponModel.getId());
        assertEquals(true, CollectionUtils.isNotEmpty(userCouponModels));
        assertEquals(1,couponModel1.getIssuedCount());

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

    private RegisterUserDto fakeRegisterUserDto(){
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setCaptcha("111111");
        registerUserDto.setLoginName("couponTest1");
        registerUserDto.setMobile("18600000101");
        registerUserDto.setPassword("123abc");
        return registerUserDto;
    }


}
