package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AccountMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldCreateAccount() throws Exception {
        UserModel fakeUser = createFakeUser();

        AccountModel model = new AccountModel(fakeUser.getLoginName(), "userName", "identityNumber", "payUserId", "payAccountId", new Date());

        accountMapper.create(model);

        AccountModel savedAccount = accountMapper.findByLoginName(fakeUser.getLoginName());

        assertNotNull(savedAccount);
    }

    private UserModel createFakeUser() {
        UserModel model = new UserModel();
        model.setLoginName("loginName");
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile("13900000000");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }
}
