package com.tuotiansudai.api.service;

import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
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
        fakeUser.setMobile("18900000000");
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }
}
