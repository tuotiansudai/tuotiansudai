package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.repository.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExperienceBillMapperTest {

    @Autowired
    private ExperienceBillMapper experienceBillMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateExperienceBill() throws Exception {
        UserModel fakeUser = createFakeUser();
        ExperienceBillModel experienceBillModel = new ExperienceBillModel();

        experienceBillModel.setLoginName(fakeUser.getLoginName());
        experienceBillModel.setOperationType(ExperienceBillOperationType.OUT);
        experienceBillModel.setAmount(100);
        experienceBillModel.setBusinessType(ExperienceBillBusinessType.REGISTER);
        experienceBillModel.setNote("test新手注册");
        experienceBillModel.setCreatedTime(new Date());
        experienceBillMapper.create(experienceBillModel);

        assertNotNull(experienceBillModel.getId());
    }

    @Test
    public void shouldUpdateExperienceBill() throws Exception {
        UserModel fakeUser = createFakeUser();
        ExperienceBillModel experienceBillModel = new ExperienceBillModel();

        experienceBillModel.setLoginName(fakeUser.getLoginName());
        experienceBillModel.setOperationType(ExperienceBillOperationType.OUT);
        experienceBillModel.setAmount(100);
        experienceBillModel.setBusinessType(ExperienceBillBusinessType.REGISTER);
        experienceBillModel.setNote("test新手注册");
        experienceBillModel.setCreatedTime(new Date());
        experienceBillMapper.create(experienceBillModel);

        experienceBillModel.setAmount(500);
        experienceBillModel.setBusinessType(ExperienceBillBusinessType.MONEY_TREE);
        experienceBillModel.setNote("摇钱树获得体验金");
        experienceBillMapper.update(experienceBillModel);

        assertEquals("摇钱树获得体验金", experienceBillModel.getNote());
        assertEquals(500, experienceBillModel.getAmount());
        assertEquals(ExperienceBillBusinessType.MONEY_TREE.toString(), experienceBillModel.getBusinessType().toString());
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
