package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanServiceTest {

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Before
    public void createLoanTitle(){
        LoanTitleModel loanTitleModel = new LoanTitleModel(IdGenerator.generate(), LoanTitleType.BASE_TITLE_TYPE, "身份证");
        loanTitleMapper.create(loanTitleModel);
    }

    public UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13" + RandomStringUtils.randomNumeric(9));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    @Test
    public void shouldShowEncryptLoginNameWhenAnonymousAndExcludeShowRandomLoginNameList() {
        UserModel fakeUser = getFakeUser("loginName1");
        userMapper.create(fakeUser);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(100000L);
        assertEquals(fakeUser.getMobile().substring(0,3)+"****"+fakeUser.getMobile().substring(7), randomUtils.encryptMobile("", investModel1.getLoginName(), investModel1.getId()));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenAnonymousAndIncludeShowRandomLoginNameList() {
        UserModel fakeUser = getFakeUser("loginName1");
        userMapper.create(fakeUser);
        UserModel fakeUser2 = getFakeUser("loginName2");
        userMapper.create(fakeUser2);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(1000002L);

        assertEquals(fakeUser.getMobile().substring(0,3) + "****", randomUtils.encryptMobile("", investModel1.getLoginName(), investModel1.getId()).substring(0,7));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameSameAsInvestorLoginName() {
        UserModel fakeUser = getFakeUser("loginName1");
        userMapper.create(fakeUser);
        UserModel fakeUser2 = getFakeUser("loginName2");
        userMapper.create(fakeUser2);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(1000002L);
        UserModel userModel = this.createUserByUserId("ttdblvjing", "13333333333");

        assertEquals(fakeUser.getMobile().substring(0,3)+"****"+fakeUser.getMobile().substring(7), randomUtils.encryptMobile(userModel.getLoginName(), investModel1.getLoginName(), investModel1.getId()));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameNotSameAsInvestorLoginNameAndIncludeShowRandomLoginNameList() {
        UserModel fakeUser = getFakeUser("loginName1");
        userMapper.create(fakeUser);
        UserModel fakeUser2 = getFakeUser("loginName2");
        userMapper.create(fakeUser2);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(1000002L);
        assertEquals(fakeUser.getMobile().substring(0, 3)+"****", randomUtils.encryptMobile("loginName2", investModel1.getLoginName(), investModel1.getId()).substring(0,7));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameNotSameAsInvestorLoginNameAndExcludeShowRandomLoginNameList() {
        UserModel fakeUser = getFakeUser("loginName3");
        userMapper.create(fakeUser);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(1000003L);

        UserModel userModel2 = createUserByUserId("loginName2", "13444444444");
        assertEquals(fakeUser.getMobile().substring(0,3)+"****" + fakeUser.getMobile().substring(7), randomUtils.encryptMobile(userModel2.getLoginName(), investModel1.getLoginName(), investModel1.getId()));
    }

    private UserModel createUserByUserId(String userId, String mobile) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(mobile);
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }
}
