package com.tuotiansudai.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserCouponServiceTest {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Test
    public void shouldAssignUserCoupon(){
        UserModel fakeUser = this.createFakeUser("expiredMembership", RandomStringUtils.randomNumeric(11), new Date());
        List<UserCouponModel>  userCouponModels = userCouponMapper.findByLoginName(fakeUser.getLoginName(), null);
        fakeCouponModel();
        userCouponService.assignUserCoupon(fakeUser.getLoginName());
        List<UserCouponModel>  newUserCouponModels = userCouponMapper.findByLoginName(fakeUser.getLoginName(), null);
        assertTrue(newUserCouponModels.size() > userCouponModels.size());

    }

    private CouponModel fakeCouponModel(){
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy("expiredMembership");
        couponModel.setActive(false);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy("expiredMembership");
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.INTEREST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponModel.setUserGroup(UserGroup.NEW_REGISTERED_USER);
        couponModel.setCouponSource("couponSource");
        return couponModel;
    }

    private UserModel createFakeUser(String loginName, String mobile, Date registerTime) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("password");
        userModel.setEmail(loginName + "@email.com");
        userModel.setMobile(mobile);
        userModel.setRegisterTime(registerTime);
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModel);
        return userModel;
    }
}
