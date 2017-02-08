package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
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
public class ExperienceRepaySuccessCollectorTest {

    @Autowired
    private UserCollector experienceRepaySuccessCollector;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Test
    public void shouldVerifyFalseWhenUserIsNull() {
        assertFalse(experienceRepaySuccessCollector.contains(new CouponModel(), null));
    }

    @Test
    public void shouldVerifyFalseWhenUserNotInvestExperienceLoan() throws Exception {
        UserModel userModel = this.fakeUserModel();
        assertFalse(experienceRepaySuccessCollector.contains(null, userModel));
    }

    @Test
    public void shouldVerifyTrueWhenUserAllRepayAreCompleted() throws Exception {
        UserModel userModel = this.fakeUserModel();
        InvestModel investModel = new InvestModel(Long.parseLong(RandomStringUtils.randomNumeric(10)), 1, null, 1, userModel.getLoginName(), new Date(), Source.WEB, null, 0.1);
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);
        investRepayMapper.create(Lists.newArrayList(new InvestRepayModel(Long.parseLong(RandomStringUtils.randomNumeric(10)), investModel.getId(), 1, 1, 1, 1, new Date(), RepayStatus.COMPLETE)));

        assertTrue(experienceRepaySuccessCollector.contains(null, userModel));
    }

    @Test
    public void shouldVerifyFalseWhenUserNotAllRepayAreCompleted() throws Exception {
        UserModel userModel = this.fakeUserModel();
        InvestModel investModel = new InvestModel(Long.parseLong(RandomStringUtils.randomNumeric(10)), 1, null, 1, userModel.getLoginName(), new Date(), Source.WEB, null, 0.1);
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);
        investRepayMapper.create(Lists.newArrayList(new InvestRepayModel(Long.parseLong(RandomStringUtils.randomNumeric(10)), investModel.getId(), 1, 1, 1, 1, new Date(), RepayStatus.COMPLETE)));
        investRepayMapper.create(Lists.newArrayList(new InvestRepayModel(Long.parseLong(RandomStringUtils.randomNumeric(10)), investModel.getId(), 2, 1, 1, 1, new Date(), RepayStatus.REPAYING)));

        assertFalse(experienceRepaySuccessCollector.contains(null, userModel));
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
