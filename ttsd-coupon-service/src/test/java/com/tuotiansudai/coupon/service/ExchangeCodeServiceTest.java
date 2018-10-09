package com.tuotiansudai.coupon.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.exception.CreateCouponException;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.ExchangeCouponDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class ExchangeCodeServiceTest {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Test
    public void shouldExchangeCodeFailed1() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        String exchangeCode = "ab12sdrfujthyf";
        BaseDataDto baseDataDto = exchangeCodeService.exchange("couponTest", exchangeCode);
        assertThat(baseDataDto.getStatus(), is(false));
        assertThat(baseDataDto.getMessage(), is("请输入正确的兑换码"));
    }

    @Test
    public void shouldExchangeCodeFailed2() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        String exchangeCode = "3012sdrfujth";
        BaseDataDto baseDataDto = exchangeCodeService.exchange("couponTest", exchangeCode);
        assertThat(baseDataDto.getStatus(), is(false));
        assertThat(baseDataDto.getMessage(), is("请输入正确的兑换码"));
    }

    @Test
    public void shouldExchangeCodeFailed3() {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        String exchangeCode = "3012sdrfujtheg";
        BaseDataDto baseDataDto = exchangeCodeService.exchange("couponTest", exchangeCode);
        assertThat(baseDataDto.getStatus(), is(false));
        assertThat(baseDataDto.getMessage(), is("请输入正确的兑换码"));
    }

    @Test
    public void shouldExchangeCodeFailed4() throws Exception {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto();
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        CouponModel couponModel = this.createCoupon("couponTest", exchangeCouponDto);

        String exchangeCode = exchangeCodeService.toBase31Prefix(couponModel.getId()) + "sdrfujtheg";
        BaseDataDto baseDataDto = exchangeCodeService.exchange("couponTest", exchangeCode);
        assertThat(baseDataDto.getStatus(), is(false));
        assertThat(baseDataDto.getMessage(), is("请输入正确的兑换码"));
    }

    @Test
    public void shouldExchangeCodeFailed5() throws Exception {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto();
        exchangeCouponDto.setStartTime(new DateTime().minusDays(7).toDate());
        exchangeCouponDto.setEndTime(new DateTime().minusDays(5).toDate());
        CouponModel couponModel = this.createCoupon("couponTest", exchangeCouponDto);

        String exchangeCode = exchangeCodeService.toBase31Prefix(couponModel.getId()) + "sdrfujtheg";
        redisWrapperClient.hset(ExchangeCodeService.EXCHANGE_CODE_KEY + couponModel.getId(), exchangeCode, "0", 1000000);
        BaseDataDto baseDataDto = exchangeCodeService.exchange("couponTest", exchangeCode);
        assertThat(baseDataDto.getStatus(), is(false));
        assertThat(baseDataDto.getMessage(), is("该兑换码已过期"));
        redisWrapperClient.del(ExchangeCodeService.EXCHANGE_CODE_KEY + couponModel.getId());
    }

    @Test
    public void shouldExchangeCodeFailed6() throws Exception {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto();
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        CouponModel couponModel = this.createCoupon("couponTest", exchangeCouponDto);

        String exchangeCode = exchangeCodeService.toBase31Prefix(couponModel.getId()) + "SDRFUJTHEG";
        redisWrapperClient.hset(ExchangeCodeService.EXCHANGE_CODE_KEY + couponModel.getId(), exchangeCode, "1", 1000000);
        BaseDataDto baseDataDto = exchangeCodeService.exchange("couponTest", exchangeCode);
        assertThat(baseDataDto.getStatus(), is(false));
        assertThat(baseDataDto.getMessage(), is("该兑换码已被使用"));
        redisWrapperClient.del(ExchangeCodeService.EXCHANGE_CODE_KEY + couponModel.getId());
    }

    @Test
    public void shouldExchangeCodeSuccess() throws Exception {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto();
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        exchangeCouponDto.setActive(true);
        exchangeCouponDto.setMultiple(true);
        exchangeCouponDto.setUserGroup(UserGroup.EXCHANGER_CODE);
        long couponId = this.createCoupon("couponTest", exchangeCouponDto).getId();
        String exchangeCode = exchangeCodeService.toBase31Prefix(couponId) + "sdrfujtheg";
        redisWrapperClient.hset(ExchangeCodeService.EXCHANGE_CODE_KEY + couponId, exchangeCode, "0", 1000000);
        BaseDataDto baseDataDto = exchangeCodeService.exchange("couponTest", exchangeCode);
        assertThat(baseDataDto.getStatus(), is(true));
        assertThat(baseDataDto.getMessage(), is("恭喜您兑换成功"));
        CouponModel couponModel = couponMapper.findById(couponId);
        assertThat(couponModel.getIssuedCount(), is(1L));
        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(couponId);
        assertThat(userCouponModels.get(0).getLoginName(), is("couponTest"));
        redisWrapperClient.del(ExchangeCodeService.EXCHANGE_CODE_KEY + couponId);
    }

    @Test
    public void shouldGenerateExchangeCode() throws CreateCouponException {
        UserModel userModel = fakeUserModel();
        userMapper.create(userModel);
        ExchangeCouponDto exchangeCouponDto = fakeCouponDto();
        DateTime dateTime = new DateTime().plusDays(1);
        exchangeCouponDto.setStartTime(dateTime.toDate());
        exchangeCouponDto.setEndTime(dateTime.toDate());
        CouponModel couponModel = this.createCoupon("couponTest", exchangeCouponDto);

        exchangeCodeService.generateExchangeCode(couponModel.getId(), 100);

        long codeCount = redisWrapperClient.hlen(ExchangeCodeService.EXCHANGE_CODE_KEY + couponModel.getId());
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
        exchangeCouponDto.setStartTime(new Date());
        exchangeCouponDto.setEndTime(new Date());
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setCouponType(CouponType.INVEST_COUPON);
        List<ProductType> productTypes = Lists.newArrayList();
        productTypes.add(ProductType._180);
        exchangeCouponDto.setProductTypes(productTypes);
        exchangeCouponDto.setCouponSource("couponSource");
        return exchangeCouponDto;
    }

    @Test
    public void shouldGet31BasePrefix() {

        assert (exchangeCodeService.toBase31Prefix(124123).equals("EFE9")); // EFE9
        assert (exchangeCodeService.toBase31Prefix(0).equals("AAAA")); // AAAA
        assert (exchangeCodeService.toBase31Prefix(31).equals("AABA")); // AABA
        assert (exchangeCodeService.toBase31Prefix(29791).equals("BAAA")); // BAAA
        assert (exchangeCodeService.toBase31Prefix(923520).equals("9999")); // 9999
        assert (exchangeCodeService.toBase31Prefix(100013).equals("DMCH")); // DMCH
        assert (exchangeCodeService.toBase31Prefix(100014).equals("DMCJ")); // DMCJ
        assert (exchangeCodeService.toBase31Prefix(100015).equals("DMCK")); // DMCK
        assert (exchangeCodeService.toBase31Prefix(100016).equals("DMCL")); // DMCL
        assert (exchangeCodeService.toBase31Prefix(100017).equals("DMCM")); // DMCM
        assert (exchangeCodeService.toBase31Prefix(100018).equals("DMCN")); // DMCN
    }

    @Test
    public void shouldGetBase31LongValue() {

        assert (exchangeCodeService.getValueBase31("EFE9rghtyuiojn") == 124123); // EFE9
        assert (exchangeCodeService.getValueBase31("AAAArghtyuiojn") == 0); // AAAA
        assert (exchangeCodeService.getValueBase31("AABArghtyuiojn") == 31); // AABA
        assert (exchangeCodeService.getValueBase31("BAAArghtyuiojn") == 29791); // BAAA
        assert (exchangeCodeService.getValueBase31("9999rghtyuiojn") == 923520); // 9999
        assert (exchangeCodeService.getValueBase31("DMCHrghtyuiojn") == 100013); // DMCH
        assert (exchangeCodeService.getValueBase31("DMCJrghtyuiojn") == 100014); // DMCJ
        assert (exchangeCodeService.getValueBase31("DMCKrghtyuiojn") == 100015); // DMCK
        assert (exchangeCodeService.getValueBase31("DMCLrghtyuiojn") == 100016); // DMCL
        assert (exchangeCodeService.getValueBase31("DMCMrghtyuiojn") == 100017); // DMCM
        assert (exchangeCodeService.getValueBase31("DMCNrghtyuiojn") == 100018); // DMCN

        assert (exchangeCodeService.getValueBase31("") == 0); // ""
        assert (exchangeCodeService.getValueBase31(null) == 0); // null
        assert (exchangeCodeService.getValueBase31("Oa3") == 0); // Oa3 Throw Exception, return 0
    }

    private CouponModel createCoupon(String loginName, ExchangeCouponDto exchangeCouponDto) {
        CouponModel couponModel = new CouponModel(exchangeCouponDto);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponModel.setActive(true);
        couponModel.setActivatedBy(loginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.create(couponModel);
        return couponModel;
    }
}
