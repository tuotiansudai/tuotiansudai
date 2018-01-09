package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-session.xml"})
@Transactional
public abstract class ServiceTestBase {

    @Before
    public void baseSetup() {
        MockitoAnnotations.initMocks(this);
    }

    public UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("email@tuotiansudai.com");
        fakeUser.setMobile(RandomStringUtils.randomNumeric(11));
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }

    public CouponModel fakeCouponModel(UserModel userModel, CouponType couponType) {
        CouponModel couponModel = new CouponModel();
        couponModel.setId(IdGenerator.generate());
        couponModel.setAmount(1000L);
        couponModel.setRate(0.1);
        couponModel.setBirthdayBenefit(0.5);
        couponModel.setMultiple(false);
        couponModel.setStartTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setUsedCount(500L);
        couponModel.setTotalCount(1000L);
        couponModel.setActive(false);
        couponModel.setShared(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setCreatedBy(userModel.getLoginName());
        couponModel.setActivatedBy(userModel.getLoginName());
        couponModel.setActivatedTime(new Date());
        couponModel.setUpdatedBy(userModel.getLoginName());
        couponModel.setUpdatedTime(new Date());
        couponModel.setIssuedCount(200);
        couponModel.setActualAmount(120);
        couponModel.setInvestLowerLimit(100);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90));
        couponModel.setCouponType(couponType);
        couponModel.setUserGroup(UserGroup.ALL_USER);
        couponModel.setTotalInvestAmount(150);
        couponModel.setDeleted(false);
        couponModel.setImportIsRight(true);
        couponModel.setCouponSource("couponSource");

        return couponModel;
    }
}
