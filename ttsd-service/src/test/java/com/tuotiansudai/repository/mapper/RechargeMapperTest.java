package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.utils.UUIDGenerator;
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
public class RechargeMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RechargeMapper rechargeMapper;


    @Test
    public void shouldCreateRecharge() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        RechargeModel model = new RechargeModel();
        model.setId(UUIDGenerator.generate());
        model.setLoginName(fakeUserModel.getLoginName());
        model.setBank("bank");
        model.setStatus(RechargeStatus.WAIT_PAY);

        rechargeMapper.create(model);

        assertNotNull(rechargeMapper.findById(model.getId()));

    }

    public UserModel getFakeUserModel() {
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
