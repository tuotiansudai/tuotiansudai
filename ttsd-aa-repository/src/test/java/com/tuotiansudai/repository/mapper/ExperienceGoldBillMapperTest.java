package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.ExperienceType;
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
public class ExperienceGoldBillMapperTest {

    @Autowired
    private ExperienceGoldBillMapper experienceGoldBillMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateExperienceGoldBill() throws Exception {
        UserModel fakeUser = createFakeUser();
        ExperienceGoldBillModel experienceGoldBillModel = new ExperienceGoldBillModel();

        experienceGoldBillModel.setLoginName(fakeUser.getLoginName());
        experienceGoldBillModel.setAmount(100);
        experienceGoldBillModel.setExperienceType(ExperienceType.REGISTER);
        experienceGoldBillModel.setNote("test新手注册");
        experienceGoldBillModel.setCreatedTime(new Date());
        experienceGoldBillMapper.create(experienceGoldBillModel);

        assertNotNull(experienceGoldBillModel.getId());
    }

    @Test
    public void shouldUpdateExperienceGoldBill() throws Exception {
        UserModel fakeUser = createFakeUser();
        ExperienceGoldBillModel experienceGoldBillModel = new ExperienceGoldBillModel();

        experienceGoldBillModel.setLoginName(fakeUser.getLoginName());
        experienceGoldBillModel.setAmount(100);
        experienceGoldBillModel.setExperienceType(ExperienceType.REGISTER);
        experienceGoldBillModel.setNote("test新手注册");
        experienceGoldBillModel.setCreatedTime(new Date());
        experienceGoldBillMapper.create(experienceGoldBillModel);

        experienceGoldBillModel.setAmount(500);
        experienceGoldBillModel.setExperienceType(ExperienceType.MONEY_TREE);
        experienceGoldBillModel.setNote("摇钱树获得体验金");
        experienceGoldBillMapper.update(experienceGoldBillModel);

        assertEquals("摇钱树获得体验金", experienceGoldBillModel.getNote());
        assertEquals(500, experienceGoldBillModel.getAmount());
        assertEquals(ExperienceType.MONEY_TREE.toString(), experienceGoldBillModel.getExperienceType().toString());
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
