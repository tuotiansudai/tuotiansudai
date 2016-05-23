package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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

    @Autowired
    private CouponActivationService couponActivationService;

    @Test
    public void shouldAssignUserCoupon() throws Exception{
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", exchangeCouponDto);

        couponActivationService.assignUserCoupon("couponTest", Lists.newArrayList(UserGroup.ALL_USER), exchangeCouponDto.getId(), null);

        CouponModel couponModel = couponMapper.findById(exchangeCouponDto.getId());
        assertThat(couponModel.getIssuedCount(), is(1L));

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(exchangeCouponDto.getId());

        assertThat(userCouponModels.get(0).getLoginName(), is("couponTest"));
    }

    @Test
    public void shouldAssignUserCouponFailedUserGroup() throws Exception{
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", exchangeCouponDto);

        couponActivationService.assignUserCoupon("couponTest", Lists.newArrayList(UserGroup.WINNER), exchangeCouponDto.getId(), null);

        CouponModel couponModel = couponMapper.findById(exchangeCouponDto.getId());
        assertThat(couponModel.getIssuedCount(), is(0L));

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(exchangeCouponDto.getId());

        assertThat(userCouponModels.size(), is(0));
    }

    @Test
    public void shouldAssignUserCouponNewbieCouponUserGroup() throws Exception{
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.NEWBIE_COUPON, UserGroup.NEW_REGISTERED_USER);
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", exchangeCouponDto);

        couponActivationService.assignUserCoupon("couponTest", Lists.newArrayList(UserGroup.NEW_REGISTERED_USER), exchangeCouponDto.getId(), null);

        CouponModel couponModel = couponMapper.findById(exchangeCouponDto.getId());
        assertThat(couponModel.getIssuedCount(), is(1L));

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(exchangeCouponDto.getId());

        assertThat(userCouponModels.size(), is(1));
        assertThat(userCouponModels.get(0).getStartTime(), is(new DateTime().withTimeAtStartOfDay().toDate()));
        assertThat(userCouponModels.get(0).getEndTime(), is(new DateTime().plusDays(2+1).withTimeAtStartOfDay().minusSeconds(1).toDate()));

    }

    @Test
    public void shouldAssignUserCouponInvestCouponUserGroup() throws Exception{
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", exchangeCouponDto);

        couponActivationService.assignUserCoupon("couponTest", Lists.newArrayList(UserGroup.ALL_USER), exchangeCouponDto.getId(), null);

        CouponModel couponModel = couponMapper.findById(exchangeCouponDto.getId());
        assertThat(couponModel.getIssuedCount(), is(1L));

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(exchangeCouponDto.getId());

        assertThat(userCouponModels.size(), is(1));
        assertThat(userCouponModels.get(0).getStartTime(), is(new DateTime().withTimeAtStartOfDay().toDate()));
        assertThat(userCouponModels.get(0).getEndTime(), is(new DateTime().plusDays(2+1).withTimeAtStartOfDay().minusSeconds(1).toDate()));

    }

    @Test
    public void shouldAssignUserCouponRedEnvelopeUserGroup() throws Exception{
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.RED_ENVELOPE, UserGroup.ALL_USER);
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", exchangeCouponDto);

        couponActivationService.assignUserCoupon("couponTest", Lists.newArrayList(UserGroup.ALL_USER), exchangeCouponDto.getId(), null);

        CouponModel couponModel = couponMapper.findById(exchangeCouponDto.getId());
        assertThat(couponModel.getIssuedCount(), is(1L));

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(exchangeCouponDto.getId());

        assertThat(userCouponModels.size(), is(1));
        assertThat(userCouponModels.get(0).getStartTime(), is(new DateTime().withTimeAtStartOfDay().toDate()));
        assertThat(userCouponModels.get(0).getEndTime(), is(new DateTime().plusDays(2+1).withTimeAtStartOfDay().minusSeconds(1).toDate()));

    }

    @Test
    public void shouldAssignUserCouponInterestCouponUserGroup() throws Exception{
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INTEREST_COUPON, UserGroup.ALL_USER);
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", exchangeCouponDto);

        couponActivationService.assignUserCoupon("couponTest", Lists.newArrayList(UserGroup.ALL_USER), exchangeCouponDto.getId(), null);

        CouponModel couponModel = couponMapper.findById(exchangeCouponDto.getId());
        assertThat(couponModel.getIssuedCount(), is(1L));

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(exchangeCouponDto.getId());

        assertThat(userCouponModels.size(), is(1));
        assertThat(userCouponModels.get(0).getStartTime(), is(new DateTime().withTimeAtStartOfDay().toDate()));
        assertThat(userCouponModels.get(0).getEndTime(), is(new DateTime().plusDays(2+1).withTimeAtStartOfDay().minusSeconds(1).toDate()));

    }

    @Test
    public void shouldCreateCouponIsSuccess() throws CreateCouponException {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", exchangeCouponDto);

    }

    @Test
    public void shouldCreateInterestCouponSuccess() throws CreateCouponException{
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        exchangeCouponDto.setCouponType(CouponType.INTEREST_COUPON);
        couponService.createCoupon("couponTest", exchangeCouponDto);
        List<CouponDto> couponDtos = couponService.findInterestCoupons(1, 1);
        assertThat(couponDtos.get(0).getCouponType(), is(CouponType.INTEREST_COUPON));
    }

    @Test
    public void shouldCreateCouponAmountIsInvalid() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        exchangeCouponDto.setAmount("0.00");
        try {
            couponService.createCoupon("couponTest", exchangeCouponDto);
        } catch (CreateCouponException e) {
            assertEquals("投资体验券金额应大于0!", e.getMessage());
        }

    }

    @Test
    public void shouldCreateCouponStartTimeIsInvalid() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        DateTime dateTime = new DateTime().plusDays(-1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        try {
            couponService.createCoupon("couponTest", exchangeCouponDto);
        } catch (CreateCouponException e) {
            assertEquals("活动起期不能早于当前日期!", e.getMessage());
        }
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


        CouponDto couponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        DateTime startDateTime = new DateTime().plusDays(-1);
        DateTime endDateTime = new DateTime().plusDays(1);

        couponDto.setStartTime(startDateTime.toDate());
        couponDto.setEndTime(endDateTime.toDate());
        CouponModel couponModel = new CouponModel(couponDto);
        couponModel.setCreatedBy(userModel.getLoginName());
        couponModel.setActive(true);
        couponModel.setCouponType(CouponType.NEWBIE_COUPON);
        couponModel.setUserGroup(UserGroup.NEW_REGISTERED_USER);
        couponModel.setCreatedTime(new Date());
        couponMapper.create(couponModel);

        userService.registerUser(registerUserDto);


        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(registerUserDto.getLoginName(), null);
        CouponModel couponModel1 = couponMapper.findById(couponModel.getId());
        assertEquals(true, CollectionUtils.isNotEmpty(userCouponModels));
        assertEquals(1, couponModel1.getIssuedCount());

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

    private ExchangeCouponDto fakeCouponDto(CouponType couponType, UserGroup userGroup) {
        ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto();
        exchangeCouponDto.setAmount("1000.00");
        exchangeCouponDto.setTotalCount(100L);
        exchangeCouponDto.setEndTime(new Date());
        exchangeCouponDto.setStartTime(new Date());
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setCouponType(couponType);
        List<ProductType> productTypes = Lists.newArrayList();
        productTypes.add(ProductType._180);
        exchangeCouponDto.setProductTypes(productTypes);
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setUserGroup(userGroup);
        exchangeCouponDto.setDeadline(2);
        return exchangeCouponDto;
    }

    private RegisterUserDto fakeRegisterUserDto() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setCaptcha("111111");
        registerUserDto.setLoginName("couponTest1");
        registerUserDto.setMobile("18600000101");
        registerUserDto.setPassword("123abc");
        return registerUserDto;
    }

}
