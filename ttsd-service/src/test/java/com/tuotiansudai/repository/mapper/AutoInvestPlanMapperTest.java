package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
        List<AutoInvestPlanModel> allModel = autoInvestPlanMapper.findEnabledPlanByPeriod(0, DateUtils.addDays(new Date(),1));
        for(AutoInvestPlanModel existsModel : allModel){
            autoInvestPlanMapper.disable(existsModel.getLoginName());
        }
    }

    @Test
    public void shouldCreateAndFind() throws Exception {
        AutoInvestPlanModel model = createUserAutoInvestPlan(User_ID,AutoInvestMonthPeriod.Month_1.getPeriodValue());

        AutoInvestPlanModel dbModel = autoInvestPlanMapper.findByLoginName(User_ID);
        assertNotNull(dbModel);
        assertEquals(dbModel.getId(), model.getId());

        assertEquals(dbModel.getAutoInvestPeriods(), model.getAutoInvestPeriods());
    }

    @Test
    public void shouldUpdate(){
        AutoInvestPlanModel model = createUserAutoInvestPlan(User_ID,AutoInvestMonthPeriod.Month_1.getPeriodValue());

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

    @Test
    public void shouldFindByPeriod() {
        createUserByUserId("test00001");
        createUserAutoInvestPlan("test00001",
                AutoInvestMonthPeriod.Month_1.getPeriodValue());
        createUserByUserId("test00002");
        createUserAutoInvestPlan("test00002", AutoInvestMonthPeriod.merge(
                AutoInvestMonthPeriod.Month_1,
                AutoInvestMonthPeriod.Month_2).getPeriodValue());
        createUserByUserId("test00003");
        createUserAutoInvestPlan("test00003",AutoInvestMonthPeriod.merge(
                AutoInvestMonthPeriod.Month_1,
                AutoInvestMonthPeriod.Month_2,
                AutoInvestMonthPeriod.Month_3,
                AutoInvestMonthPeriod.Month_4).getPeriodValue());
        createUserByUserId("test00004");
        createUserAutoInvestPlan("test00004",AutoInvestMonthPeriod.merge(
                AutoInvestMonthPeriod.Month_1,
                AutoInvestMonthPeriod.Month_2,
                AutoInvestMonthPeriod.Month_3,
                AutoInvestMonthPeriod.Month_4).getPeriodValue());
        createUserByUserId("test00005");
        createUserAutoInvestPlan("test00005",AutoInvestMonthPeriod.merge(
                AutoInvestMonthPeriod.Month_6,
                AutoInvestMonthPeriod.Month_12).getPeriodValue());
        createUserByUserId("test00006");
        createUserAutoInvestPlan("test00006",
                AutoInvestMonthPeriod.Month_6.getPeriodValue());

        List<AutoInvestPlanModel> models = autoInvestPlanMapper.findEnabledPlanByPeriod(
                AutoInvestMonthPeriod.Month_1.getPeriodValue(),
                DateUtils.addDays(new Date(), 1)
        );
        assert models.size() == 4;
        models = autoInvestPlanMapper.findEnabledPlanByPeriod(
                AutoInvestMonthPeriod.Month_1.getPeriodValue(),
                DateUtils.addDays(new Date(), -1)
        );
        assert models.size() == 0;
        models = autoInvestPlanMapper.findEnabledPlanByPeriod(
                AutoInvestMonthPeriod.Month_2.getPeriodValue(),
                DateUtils.addDays(new Date(), 1)
        );
        assert models.size() == 3;
        models = autoInvestPlanMapper.findEnabledPlanByPeriod(
                AutoInvestMonthPeriod.Month_12.getPeriodValue(),
                DateUtils.addDays(new Date(), 1)
        );
        assert models.size() == 1;
    }

    @Test
    public void shouldEnableDisable(){
        AutoInvestPlanModel model = createUserAutoInvestPlan(User_ID,AutoInvestMonthPeriod.Month_1.getPeriodValue());

        AutoInvestPlanModel dbModel = autoInvestPlanMapper.findByLoginName(User_ID);
        assertNotNull(dbModel);

        autoInvestPlanMapper.disable(User_ID);
        dbModel = autoInvestPlanMapper.findByLoginName(User_ID);
        assert !dbModel.isEnabled();

        autoInvestPlanMapper.enable(User_ID);
        dbModel = autoInvestPlanMapper.findByLoginName(User_ID);
        assert dbModel.isEnabled();
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

    private AutoInvestPlanModel createUserAutoInvestPlan(String userId, int periods){
        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setEnabled(true);
        model.setLoginName(userId);
        model.setRetentionAmount(10000);
        model.setAutoInvestPeriods(periods);
        model.setCreatedTime(new Date());
        model.setId(idGenerator.generate());
        model.setMaxInvestAmount(1000000);
        model.setMinInvestAmount(50000);
        autoInvestPlanMapper.create(model);
        return model;
    }
}
