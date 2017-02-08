package com.tuotiansudai.coupon.util;

import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExperienceInvestSuccessCollectorTest {

    @Autowired
    private UserCollector experienceInvestSuccessCollector;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Test
    public void shouldVerifyFalseWhenUserIsNull() {
        assertFalse(experienceInvestSuccessCollector.contains(new CouponModel(), null));
    }

    @Test
    public void shouldVerifyFalseWhenUserNotInvestExperienceLoan() throws Exception {
        UserModel userModel = this.fakeUserModel();
        InvestModel investModel = new InvestModel(Long.parseLong(RandomStringUtils.randomNumeric(10)), 1, null, 1, userModel.getLoginName(), new Date(), Source.WEB, null, 0.1);
        investModel.setStatus(InvestStatus.FAIL);
        investMapper.create(investModel);

        assertFalse(experienceInvestSuccessCollector.contains(null, userModel));
    }

    @Test
    public void shouldVerifyTrueWhenUserInvestExperienceLoan() throws Exception {
        UserModel userModel = this.fakeUserModel();
        InvestModel investModel = new InvestModel(Long.parseLong(RandomStringUtils.randomNumeric(10)), 1, null, 1, userModel.getLoginName(), new Date(), Source.WEB, null, 0.1);
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);

        assertTrue(experienceInvestSuccessCollector.contains(null, userModel));
    }

    private UserModel fakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(RandomStringUtils.randomAlphabetic(5));
        fakeUserModel.setPassword("123abc");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile(RandomStringUtils.randomNumeric(11));
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        return fakeUserModel;
    }
}
