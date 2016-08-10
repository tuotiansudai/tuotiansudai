package com.tuotiansudai.service;

import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.mapper.BaseMapperTest;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class PrepareServiceTest extends BaseMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PrepareUserService prepareService;

    @Test
    public void buildPrepareRegister() {
        UserModel referrerUserModel = fakeReferrerUserMobile("prepareRegisterUser");
        userMapper.create(referrerUserModel);
        PrepareRegisterRequestDto requestDto = new PrepareRegisterRequestDto();
        requestDto.setReferrerMobile(referrerUserModel.getMobile());
        requestDto.setMobile("18999999999");
        prepareService.prepareRegister(requestDto);
    }

    @Test
    public void buildRegister() {
        UserModel referrerUserModel = fakeReferrerUserMobile("registerUser");
        userMapper.create(referrerUserModel);
        RegisterUserDto requestDto = new RegisterUserDto();
        requestDto.setCaptcha("00000");
        requestDto.setChannel("tuotiansudai");
        requestDto.setMobile("18999999999");
        requestDto.setPassword("123abc");
        requestDto.setReferrer(referrerUserModel.getMobile());
        requestDto.setSource(Source.MOBILE);
        prepareService.register(requestDto);
    }

    private UserModel fakeReferrerUserMobile(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
