package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.PrepareModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class PrepareUserMapperTest extends BaseMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Test
    public void shouldCreatePrepare() {
        UserModel userModel = fakeUserModel("prepareUser", "18999999999");
        userMapper.create(userModel);
        PrepareModel prepareModel = fakePrepareModel(userModel.getMobile(), "18998888888");
        prepareUserMapper.create(prepareModel);
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


    private PrepareModel fakePrepareModel(String referrerMobile, String mobile) {
        PrepareModel prepareModel = new PrepareModel();
        prepareModel.setReferrerMobile(referrerMobile);
        prepareModel.setMobile(mobile);
        prepareModel.setChannel(Source.ANDROID);
        return prepareModel;
    }
}
