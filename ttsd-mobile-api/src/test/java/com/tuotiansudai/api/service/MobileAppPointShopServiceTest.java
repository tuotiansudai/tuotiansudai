package com.tuotiansudai.api.service;


import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class MobileAppPointShopServiceTest {

    @Autowired
    private MobileAppPointShopService mobileAppPointShopService;

    @Autowired
    private UserMapper userMapper;


    public void shouleUpdateUserAddressIsOk(){
        UserModel userModel = getUserModelTest("testUserAddress");
        mobileAppPointShopService.updateUserAddress(userModel.getLoginName(),"test",userModel.getMobile(),"test");
    }


    private UserModel getUserModelTest(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest)
        return userModelTest;
    }
}
