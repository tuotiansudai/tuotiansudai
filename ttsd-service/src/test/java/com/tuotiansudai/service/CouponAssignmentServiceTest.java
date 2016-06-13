package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class CouponAssignmentServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Test
    public void shouldExchangeCode() throws Exception {
        UserModel fakeUser = getFakeUser("fakeUser");
        CouponModel fakeCoupon = getFakeCoupon(UserGroup.EXCHANGER_CODE);
        exchangeCodeService.generateExchangeCode(fakeCoupon.getId(), 1);
        List<String> exchangeCodes = exchangeCodeService.getExchangeCodes(fakeCoupon.getId());

        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), exchangeCodes.get(0));

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(fakeCoupon.getId());

        assertThat(userCouponModels.size(), is(1));
        assertThat(userCouponModels.get(0).getExchangeCode(), is(exchangeCodes.get(0)));
    }

    @Test
    public void shouldAssignCouponId() throws Exception {
        UserModel fakeUser = getFakeUser("fakeUser");
        CouponModel fakeCoupon = getFakeCoupon(UserGroup.ALL_USER);

        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), fakeCoupon.getId());

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(fakeCoupon.getId());

        assertThat(userCouponModels.size(), is(1));
    }

    private CouponModel getFakeCoupon(UserGroup userGroup) {
        UserModel couponCreator = getFakeUser("couponCreator");
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1);
        couponModel.setTotalCount(0L);
        couponModel.setStartTime(new Date());
        couponModel.setEndTime(new DateTime().plusDays(1).toDate());
        couponModel.setDeadline(1);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30));
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setInvestLowerLimit(0);
        couponModel.setUserGroup(userGroup);
        couponModel.setCreatedBy(couponCreator.getLoginName());
        couponModel.setCreatedTime(new Date());
        couponModel.setActive(true);
        couponMapper.create(couponModel);
        return couponModel;
    }

    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("fakeUser@tuotiansudai.com");
        fakeUser.setMobile(loginName);
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        return fakeUser;
    }
}
