package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class BindBankCardServiceTest {

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldAccountBalanceIsZeroIsManualIsOk() {
        UserModel userModel = getUserModelTest();
        userMapper.create(userModel);
        AccountModel accountModel = getAccountModel(userModel.getLoginName());
        accountMapper.create(accountModel);
        boolean isManual = bindBankCardService.isManual(userModel.getLoginName());
        assertTrue(!isManual);
    }

    @Test
    public void shouldAccountBalanceIsNotZeroIsManualIsOk() {
        UserModel userModel = getUserModelTest();
        userMapper.create(userModel);
        AccountModel accountModel = getAccountModel(userModel.getLoginName());
        accountModel.setBalance(100l);
        accountMapper.create(accountModel);
        boolean isManual = bindBankCardService.isManual(userModel.getLoginName());
        assertTrue(isManual);
    }

    private AccountModel getAccountModel(String loginName) {
        AccountModel model = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        model.setBalance(0l);
        return model;
    }

    private UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
