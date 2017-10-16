package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class RechargeMapperTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Test
    public void shouldCreateRecharge() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        RechargeModel model = new RechargeModel();
        model.setId(IdGenerator.generate());
        model.setLoginName(fakeUserModel.getLoginName());
        model.setBankCode("bank");
        model.setCreatedTime(new Date());
        model.setSource(Source.WEB);
        model.setStatus(RechargeStatus.WAIT_PAY);

        rechargeMapper.create(model);

        assertNotNull(rechargeMapper.findById(model.getId()));

    }

    @Test
    public void shouldUpdateRecharge() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        RechargeModel model = new RechargeModel();
        model.setId(IdGenerator.generate());
        model.setLoginName(fakeUserModel.getLoginName());
        model.setBankCode("bank");
        model.setCreatedTime(new Date());
        model.setSource(Source.WEB);
        model.setStatus(RechargeStatus.WAIT_PAY);

        rechargeMapper.create(model);

        model.setStatus(RechargeStatus.SUCCESS);

        rechargeMapper.updateStatus(model.getId(), RechargeStatus.SUCCESS);

        assertThat(rechargeMapper.findById(model.getId()).getStatus(), is(RechargeStatus.SUCCESS));
    }

    public UserModel getFakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(RandomStringUtils.randomNumeric(11));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
