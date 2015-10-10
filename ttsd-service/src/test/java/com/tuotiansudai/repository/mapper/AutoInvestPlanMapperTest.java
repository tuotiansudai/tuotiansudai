package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.AutoInvestMonthPeriod;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AutoInvestPlanMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    private final String User_ID = "helloworld";

    @Before
    public void setup(){
        createUserByUserId(User_ID);
    }

    @Test
    public void shouldCreate() throws Exception {
        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setEnabled(true);
        model.setLoginName(User_ID);
        model.setRetentionAmount(10000);
        model.setAutoInvestPeriods(AutoInvestMonthPeriod.Month_1.getPeriodValue());
        model.setCreatedTime(new Date());
        model.setId(idGenerator.generate());
        model.setMaxInvestAmount(1000000);
        model.setMinInvestAmount(50000);
        autoInvestPlanMapper.create(model);

        AutoInvestPlanModel dbModel = autoInvestPlanMapper.findByLoginName(User_ID);
        assertNotNull(dbModel);
        assertEquals(dbModel.getId(), model.getId());

        assertEquals(dbModel.getAutoInvestPeriods(), model.getAutoInvestPeriods());
    }

    @Test
    public void shouldUpdate(){
        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setEnabled(true);
        model.setLoginName(User_ID);
        model.setRetentionAmount(10000);
        model.setAutoInvestPeriods(AutoInvestMonthPeriod.Month_1.getPeriodValue());
        model.setCreatedTime(new Date());
        model.setId(idGenerator.generate());
        model.setMaxInvestAmount(1000000);
        model.setMinInvestAmount(50000);
        autoInvestPlanMapper.create(model);

        AutoInvestPlanModel dbModel = autoInvestPlanMapper.findByLoginName(User_ID);
        assertNotNull(dbModel);
        assertEquals(dbModel.getAutoInvestPeriods(), model.getAutoInvestPeriods());
        assertEquals(dbModel.getRetentionAmount(), model.getRetentionAmount());

        model.setRetentionAmount(20000);
        model.setAutoInvestPeriods(AutoInvestMonthPeriod.Month_2.getPeriodValue());
        autoInvestPlanMapper.update(model);
        AutoInvestPlanModel dbModel2 = autoInvestPlanMapper.findByLoginName(User_ID);
        assertNotNull(dbModel2);
        assertEquals(dbModel2.getAutoInvestPeriods(), AutoInvestMonthPeriod.Month_2.getPeriodValue());
        assertEquals(dbModel2.getRetentionAmount(), 20000);
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }
}
