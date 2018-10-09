package com.tuotiansudai.console.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.coupon.exception.CreateCouponException;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.CouponDto;
import com.tuotiansudai.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.response.UserInfo;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ConsoleCouponServiceTest {

    @Autowired
    private ConsoleCouponService consoleCouponService;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponService couponService;

    @Value("${user.rest.server}")
    private String userRestServer;

    private MockWebServer mockUserService(UserModel userModel) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserRestUserInfo userRestUserInfo = new UserRestUserInfo();
        userRestUserInfo.setSuccess(true);
        userRestUserInfo.setMessage("");
        userRestUserInfo.setUserInfo(UserInfo.fromUserModel(userModel));

        String userRestInfoJson = objectMapper.writeValueAsString(userRestUserInfo);

        MockWebServer mockWebServer = new MockWebServer();
        URL url = new URL(userRestServer);
        mockWebServer.start(InetAddress.getByName(url.getHost()), url.getPort());

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(userRestInfoJson);
        mockResponse.setResponseCode(201);
        mockWebServer.enqueue(mockResponse);

        return mockWebServer;
    }

    @Test
    public void shouldCreateCouponIsSuccess() throws CreateCouponException {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        consoleCouponService.createCoupon("couponTest", exchangeCouponDto);
    }

    @Test
    public void shouldCreateCouponAmountIsInvalid() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER);
        exchangeCouponDto.setAmount("0.00");
        try {
            consoleCouponService.createCoupon("couponTest", exchangeCouponDto);
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
            consoleCouponService.createCoupon("couponTest", exchangeCouponDto);
        } catch (CreateCouponException e) {
            assertEquals("活动起期不能早于当前日期!", e.getMessage());
        }
    }

    @Test
    public void shouldRegisterUserIsSuccess() throws IOException {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);

        MockWebServer mockServer = mockUserService(userModel);

        RegisterUserDto registerUserDto = fakeRegisterUserDto();

        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setMobile("18600000101");
        smsCaptchaModel.setCaptcha("111111");
        smsCaptchaModel.setSmsCaptchaType(SmsCaptchaType.REGISTER_CAPTCHA);
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
        couponModel.setCouponSource("couponSource");
        couponMapper.create(couponModel);

        userService.registerUser(registerUserDto);

        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(registerUserDto.getLoginName(), null);
        CouponModel couponModel1 = couponMapper.findById(couponModel.getId());
        //       assertEquals(true, CollectionUtils.isNotEmpty(userCouponModels));
        //       assertEquals(1, couponModel1.getIssuedCount());

        mockServer.shutdown();
    }

    @Test
    public void shouldFindCouponByUserGroupIsOk() {
        String loginName = "testCoupon";
        UserModel userModel = fakeUserModel();
        userModel.setLoginName(loginName);
        userMapper.create(userModel);

        List<CouponModel> firstCoupons = couponService.findCouponByUserGroup(Lists.newArrayList(UserGroup.FIRST_INVEST_ACHIEVEMENT));
        List<CouponModel> lastCoupons = couponService.findCouponByUserGroup(Lists.newArrayList(UserGroup.LAST_INVEST_ACHIEVEMENT));
        List<CouponModel> maxCoupons = couponService.findCouponByUserGroup(Lists.newArrayList(UserGroup.MAX_AMOUNT_ACHIEVEMENT));
        List<CouponModel> allCoupons = couponService.findCouponByUserGroup(Lists.newArrayList(UserGroup.FIRST_INVEST_ACHIEVEMENT,
                UserGroup.LAST_INVEST_ACHIEVEMENT, UserGroup.MAX_AMOUNT_ACHIEVEMENT, UserGroup.MEMBERSHIP_V4));

        couponMapper.create(fakeCouponModel(loginName, UserGroup.FIRST_INVEST_ACHIEVEMENT));
        couponMapper.create(fakeCouponModel(loginName, UserGroup.LAST_INVEST_ACHIEVEMENT));
        couponMapper.create(fakeCouponModel(loginName, UserGroup.MAX_AMOUNT_ACHIEVEMENT));
        couponMapper.create(fakeCouponModel(loginName, UserGroup.MEMBERSHIP_V4));

        List<CouponModel> newCoupons = couponService.findCouponByUserGroup(Lists.newArrayList(UserGroup.FIRST_INVEST_ACHIEVEMENT));
        assertTrue((firstCoupons.size() + 1) == newCoupons.size());
        newCoupons = couponService.findCouponByUserGroup(Lists.newArrayList(UserGroup.LAST_INVEST_ACHIEVEMENT));
        assertTrue((lastCoupons.size() + 1) == newCoupons.size());
        newCoupons = couponService.findCouponByUserGroup(Lists.newArrayList(UserGroup.MAX_AMOUNT_ACHIEVEMENT));
        assertTrue((maxCoupons.size() + 1) == newCoupons.size());
        newCoupons = couponService.findCouponByUserGroup(Lists.newArrayList(UserGroup.FIRST_INVEST_ACHIEVEMENT,
                UserGroup.LAST_INVEST_ACHIEVEMENT, UserGroup.MAX_AMOUNT_ACHIEVEMENT, UserGroup.MEMBERSHIP_V4));
        assertTrue((allCoupons.size() + 4) == newCoupons.size());
    }

    private CouponModel fakeCouponModel(String loginName, UserGroup userGroup) {
        CouponModel couponModel = new CouponModel();
        couponModel.setId(IdGenerator.generate());
        couponModel.setAmount(1000L);
        couponModel.setRate(0.1);
        couponModel.setBirthdayBenefit(0.5);
        couponModel.setMultiple(false);
        couponModel.setStartTime(DateTime.now().plusDays(-1).toDate());
        couponModel.setEndTime(DateTime.now().plusDays(1).toDate());
        couponModel.setDeadline(10);
        couponModel.setUsedCount(500L);
        couponModel.setTotalCount(1000L);
        couponModel.setActive(true);
        couponModel.setShared(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setCreatedBy(loginName);
        couponModel.setActivatedBy(loginName);
        couponModel.setActivatedTime(new Date());
        couponModel.setUpdatedBy(loginName);
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
        couponModel.setUserGroup(userGroup);
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
        exchangeCouponDto.setCouponSource("couponSource");
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
