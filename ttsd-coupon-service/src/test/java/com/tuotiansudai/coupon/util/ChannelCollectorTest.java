package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.CouponUserGroupMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class ChannelCollectorTest {

    @Autowired
    private UserCollector channelCollector;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponUserGroupMapper couponUserGroupMapper;

    @Before
    public void setupMock(){

    }

    @Test
    public void shouldVerifyFalseWhenCouponOrUserOrCouponUserGroupIsNull() {
        assertFalse(channelCollector.contains(null, new UserModel()));
        assertFalse(channelCollector.contains(new CouponModel(), null));
        assertFalse(channelCollector.contains(new CouponModel(), new UserModel()));
    }

    @Test
    public void shouldVerifyTrueWhenUserIsFromChannel() {
        UserModel activatedUser = this.fakeUserModel(null);
        CouponModel couponModel = this.fakeCouponModel(activatedUser.getLoginName(), CouponType.RED_ENVELOPE);

        this.fakeCouponUserGroup(couponModel, couponModel.getChannels().get(0));

        assertTrue(channelCollector.contains(couponModel, this.fakeUserModel(couponModel.getChannels().get(0))));
    }

    @Test
    public void shouldVerifyFalseWhenUserIsNotFromChannel() {
        UserModel activatedUser = this.fakeUserModel(null);
        CouponModel couponModel = this.fakeCouponModel(activatedUser.getLoginName(), CouponType.RED_ENVELOPE);

        this.fakeCouponUserGroup(couponModel, couponModel.getChannels().get(0));

        assertFalse(channelCollector.contains(couponModel, this.fakeUserModel("otherChannel")));
        assertFalse(channelCollector.contains(couponModel, this.fakeUserModel(null)));
    }

    @Test
    public void shouldVerifyFalseWhenUserIsFromChannelButRegisterIsNotBetweenCouponStartAndEndTime() {
        UserModel activatedUser = this.fakeUserModel(null);
        CouponModel couponModel = this.fakeCouponModel(activatedUser.getLoginName(), CouponType.RED_ENVELOPE);
        this.fakeCouponUserGroup(couponModel, couponModel.getChannels().get(0));
        UserModel userModel = this.fakeUserModel(couponModel.getChannels().get(0));

        userModel.setRegisterTime(new DateTime(couponModel.getStartTime()).minusDays(10).toDate());
        userMapper.updateUser(userModel);
        assertFalse(channelCollector.contains(couponModel, userModel));

        userModel.setRegisterTime(new DateTime(couponModel.getEndTime()).plusDays(10).toDate());
        userMapper.updateUser(userModel);
        assertFalse(channelCollector.contains(couponModel, userModel));
    }

    private UserModel fakeUserModel(String channel) {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(RandomStringUtils.randomAlphabetic(5));
        fakeUserModel.setPassword("123abc");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile(RandomStringUtils.randomNumeric(11));
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setChannel(channel);
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        return fakeUserModel;
    }

    private CouponModel fakeCouponModel(String activatedBy, CouponType couponType) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1L);
        couponModel.setActivatedBy(activatedBy);
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setStartTime(new Date());
        couponModel.setEndTime(new DateTime(couponModel.getStartTime()).plusDays(10).toDate());
        couponModel.setDeadline(10);
        couponModel.setCreatedBy(activatedBy);
        couponModel.setTotalCount(1L);
        couponModel.setUsedCount(0L);
        couponModel.setInvestLowerLimit(0L);
        couponModel.setChannels(Lists.newArrayList("channel"));
        couponModel.setCouponType(couponType);
        couponModel.setCouponSource("source");
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponMapper.create(couponModel);
        return couponModel;
    }

    private CouponUserGroupModel fakeCouponUserGroup(CouponModel couponModel, String channel) {
        CouponUserGroupModel fakeCouponUserGroupModel = new CouponUserGroupModel();
        fakeCouponUserGroupModel.setCouponId(couponModel.getId());
        fakeCouponUserGroupModel.setUserGroup(UserGroup.CHANNEL);
        fakeCouponUserGroupModel.setUserGroupItems(Lists.newArrayList(channel));
        couponUserGroupMapper.create(fakeCouponUserGroupModel);
        return fakeCouponUserGroupModel;
    }
}
