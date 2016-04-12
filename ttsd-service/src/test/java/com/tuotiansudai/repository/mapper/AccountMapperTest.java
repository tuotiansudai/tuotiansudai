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
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-security.xml"})
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

    @Test
    public void shouldUpdateAccount() throws Exception {
        UserModel fakeUser = createFakeUser();

        AccountModel model = new AccountModel(fakeUser.getLoginName(), "userName", "identityNumber", "payUserId", "payAccountId", new Date());

        accountMapper.create(model);

        model.setBalance(1);
        model.setFreeze(1);

        accountMapper.update(model);

        AccountModel updatedAccount = accountMapper.findByLoginName(fakeUser.getLoginName());

        assertThat(updatedAccount.getBalance(), is(1L));
        assertThat(updatedAccount.getFreeze(), is(1L));

    }

    @Test
    public void shouldFindByIdentityNumber() throws Exception {
        UserModel fakeUser = createFakeUser();

        AccountModel model = new AccountModel(fakeUser.getLoginName(), "userName", "identityNumber", "payUserId", "payAccountId", new Date());

        accountMapper.create(model);

        List<AccountModel> accountModels = accountMapper.findByIdentityNumber(model.getIdentityNumber());

        assertNotNull(accountModels);
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
