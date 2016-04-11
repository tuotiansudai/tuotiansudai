package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.coupon.service.impl.ExchangeCodeServiceImpl;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.repository.mapper.UserMapper;
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
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExchangeCodeServiceTest {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Test
    public void shouldGenerateExchangeCode() throws CreateCouponException {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto();
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        couponService.createCoupon("couponTest", exchangeCouponDto);

        long couponId = exchangeCouponDto.getId();
        exchangeCodeService.generateExchangeCode(couponId, 100);

        long codeCount = redisWrapperClient.hlen(ExchangeCodeServiceImpl.EXCHANGE_CODE_KEY + couponId);
        assert (codeCount == 100);
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


    private ExchangeCouponDto fakeCouponDto() {
        ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto();
        exchangeCouponDto.setAmount("1000.00");
        exchangeCouponDto.setTotalCount(100L);
        exchangeCouponDto.setEndTime(new Date());
        exchangeCouponDto.setStartTime(new Date());
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setCouponType(CouponType.INVEST_COUPON);
        List<ProductType> productTypes = Lists.newArrayList();
        productTypes.add(ProductType.JYF);
        exchangeCouponDto.setProductTypes(productTypes);
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        return exchangeCouponDto;
    }


}
