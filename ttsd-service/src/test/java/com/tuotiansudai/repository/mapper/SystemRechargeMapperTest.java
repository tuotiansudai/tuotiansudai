package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.*;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class SystemRechargeMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SystemRechargeMapper systemRechargeMapper;


    @Test
    public void shouldCreateSystemRecharge(){
        UserModel userModel = getFakeUserModel();
        userMapper.create(userModel);

        SystemRechargeModel systemRechargeModel = getFakeSystemRechargeModel();

        systemRechargeMapper.create(systemRechargeModel);

        SystemRechargeModel systemRechargeModel1 = systemRechargeMapper.findById(systemRechargeModel.getId());


        assertNotNull(systemRechargeModel1);


    }
    @Test
    public void shouldUpdateSystemRecharge(){
        UserModel userModel = getFakeUserModel();
        userMapper.create(userModel);

        SystemRechargeModel systemRechargeModel = getFakeSystemRechargeModel();

        systemRechargeMapper.create(systemRechargeModel);

        systemRechargeModel.setStatus(SystemRechargeStatus.SUCCESS);

        systemRechargeMapper.updateSystemRecharge(systemRechargeModel);

        SystemRechargeModel systemRechargeModel1 = systemRechargeMapper.findById(systemRechargeModel.getId());
        assertEquals(SystemRechargeStatus.SUCCESS, systemRechargeModel1.getStatus());
    }

    @Test
    public void shouldFindByLoginNameIsSuccess(){
        UserModel userModel = getFakeUserModel();
        userMapper.create(userModel);

        SystemRechargeModel systemRechargeModel = getFakeSystemRechargeModel();

        systemRechargeMapper.create(systemRechargeModel);

        List<SystemRechargeModel> systemRechargeModels = systemRechargeMapper.findByLoginName(systemRechargeModel.getLoginName());
        assertThat(systemRechargeModels.size(),is(1));

    }

    public UserModel getFakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("tuotian");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    public SystemRechargeModel getFakeSystemRechargeModel(){
        SystemRechargeModel systemRechargeModel = new SystemRechargeModel();
        systemRechargeModel.setLoginName("tuotian");
        systemRechargeModel.setTime(new Date());
        systemRechargeModel.setAmount(10000);
        systemRechargeModel.setSuccessTime(new Date());
        systemRechargeModel.setStatus(SystemRechargeStatus.SUCCESS);
        systemRechargeModel.setRemark("remark");
        return systemRechargeModel;
    }
}
