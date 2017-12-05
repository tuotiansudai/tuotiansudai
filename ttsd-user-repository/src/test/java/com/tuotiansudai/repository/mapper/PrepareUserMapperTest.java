package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.PrepareUserModel;
import com.tuotiansudai.repository.model.Source;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PrepareUserMapperTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Test
    public void shouldCreatePrepare() {
        UserModel userModel = fakeUserModel("prepareUser", "18999999999");
        userMapper.create(userModel);
        PrepareUserModel prepareUserModel = fakePrepareModel(userModel.getMobile(), "18998888888");
        prepareUserMapper.create(prepareUserModel);
    }


    private UserModel fakeUserModel(String loginName, String mobile) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setMobile(mobile);
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }


    private PrepareUserModel fakePrepareModel(String referrerMobile, String mobile) {
        PrepareUserModel prepareUserModel = new PrepareUserModel();
        prepareUserModel.setReferrerMobile(referrerMobile);
        prepareUserModel.setMobile(mobile);
        prepareUserModel.setChannel(Source.ANDROID);
        prepareUserModel.setCreatedTime(new Date());
        return prepareUserModel;
    }
}
