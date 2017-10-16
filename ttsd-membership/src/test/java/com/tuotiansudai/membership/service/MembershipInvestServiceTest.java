package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MembershipInvestServiceTest {
    @Autowired
    FakeUserHelper userMapper;

    @Autowired
    MembershipMapper membershipMapper;

    @Autowired
    UserMembershipMapper userMembershipMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    MembershipInvestService membershipInvestService;

    Random random = new Random();

    @Test
    public void testUpgradeShouldNotMakePurchasedMembershipExpired() {
        UserModel user = createFakeUser("testUser1", "18612340001", DateTime.parse("2010-06-30T12:30").withTimeAtStartOfDay().toDate());
        MembershipModel membershipModelLevel2 = membershipMapper.findByLevel(2);
        createFakeAccount(user, membershipModelLevel2.getExperience() - 1);
        MembershipModel membershipModelLevel1 = membershipMapper.findByLevel(1);
        createUserMembershipModel(user.getLoginName(), UserMembershipType.UPGRADE, membershipModelLevel1.getLevel());
        MembershipModel membershipModelLevel5 = membershipMapper.findByLevel(5);
        createUserMembershipModel(user.getLoginName(), UserMembershipType.UPGRADE, membershipModelLevel5.getLevel());

        long investId = random.nextLong();

        membershipInvestService.afterInvestSuccess(user.getLoginName(), 100L, investId, "测试标的");
        MembershipModel currentMembership = membershipMapper.findById(userMembershipMapper.findCurrentMaxByLoginName(user.getLoginName()).getMembershipId());
        assertEquals(membershipModelLevel5.getLevel(), currentMembership.getLevel());
    }

    @Test
    public void testUpgradeShouldMakeOldUpgradeMembershipExpired() {
        UserModel user = createFakeUser("testUser1", "18612340001", DateTime.parse("2010-06-30T12:30").withTimeAtStartOfDay().toDate());
        MembershipModel membershipModelLevel2 = membershipMapper.findByLevel(2);
        createFakeAccount(user, membershipModelLevel2.getExperience() - 1);
        MembershipModel membershipModelLevel1 = membershipMapper.findByLevel(1);
        createUserMembershipModel(user.getLoginName(), UserMembershipType.UPGRADE, membershipModelLevel1.getLevel());

        long investId = random.nextLong();

        membershipInvestService.afterInvestSuccess(user.getLoginName(), 100L, investId, "测试标的");
        MembershipModel currentMembership = membershipMapper.findById(userMembershipMapper.findCurrentMaxByLoginName(user.getLoginName()).getMembershipId());
        assertEquals(membershipModelLevel2.getLevel(), currentMembership.getLevel());
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

    private AccountModel createFakeAccount(UserModel userModel, long membershipPoint) {
        AccountModel accountModel = new AccountModel(userModel.getLoginName(), RandomStringUtils.randomNumeric(32), RandomStringUtils.randomNumeric(14), userModel.getRegisterTime());
        accountModel.setMembershipPoint(membershipPoint);
        accountMapper.create(accountModel);
        return accountModel;
    }

    private UserMembershipModel createUserMembershipModel(String loginName, UserMembershipType userMembershipType, int level) {
        UserMembershipModel userMembershipModel = new UserMembershipModel();
        userMembershipModel.setLoginName(loginName);
        userMembershipModel.setCreatedTime(new Date());
        userMembershipModel.setExpiredTime(DateTime.parse("2040-06-30T12:30").toDate());
        userMembershipModel.setType(userMembershipType);
        userMembershipModel.setMembershipId(membershipMapper.findByLevel(level).getId());
        userMembershipMapper.create(userMembershipModel);
        return userMembershipModel;
    }
}
