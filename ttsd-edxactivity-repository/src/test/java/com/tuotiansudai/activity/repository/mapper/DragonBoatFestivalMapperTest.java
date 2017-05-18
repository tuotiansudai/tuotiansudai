package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class DragonBoatFestivalMapperTest {

    @Autowired
    private DragonBoatFestivalMapper dragonBoatFestivalMapper;

    @Test
    public void shouldUpdateOrAddPKGroup() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "19877778888");
        dragonBoatFestivalModel.setPkGroup("SWEET");
        dragonBoatFestivalMapper.setPKGroup(dragonBoatFestivalModel);

        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName("aa");
        assertNotNull(model);
        assertEquals("SWEET", model.getPkGroup());
    }

    @Test
    public void shouldInsertOrAddTotalInvestAmount() {
        dragonBoatFestivalMapper.addTotalInvestAmount("aa", "aa", "19877778888", 10000L);

        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName("aa");
        assertNotNull(model);
        assertEquals(10000, model.getTotalInvestAmount());

        dragonBoatFestivalMapper.addTotalInvestAmount("aa", "aa", "19877778888", 10000L);
        DragonBoatFestivalModel model2 = dragonBoatFestivalMapper.findByLoginName("aa");
        assertEquals(20000, model2.getTotalInvestAmount());
    }

    @Test
    public void shouldAddPKInvestAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "19877778888");
        dragonBoatFestivalModel.setPkGroup("SWEET");
        dragonBoatFestivalMapper.setPKGroup(dragonBoatFestivalModel);

        dragonBoatFestivalMapper.addPKInvestAmount("aa", 10000L);

        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName("aa");
        assertNotNull(model);
        assertEquals(10000, model.getPkInvestAmount());
    }

    @Test
    public void shouldInsertOrAddInviteExperienceAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "19877778888");
        dragonBoatFestivalModel.setInviteExperienceAmount(10000L);
        dragonBoatFestivalMapper.addInviteExperienceAmount(dragonBoatFestivalModel);

        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName("aa");
        assertNotNull(model);
        assertEquals(10000, model.getInviteExperienceAmount());

        dragonBoatFestivalMapper.addInviteExperienceAmount(dragonBoatFestivalModel);
        DragonBoatFestivalModel model2 = dragonBoatFestivalMapper.findByLoginName("aa");
        assertEquals(20000, model2.getInviteExperienceAmount());
    }

    @Test
    public void shouldAddPKExperienceAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "19877778888");
        dragonBoatFestivalModel.setPkGroup("SWEET");
        dragonBoatFestivalMapper.setPKGroup(dragonBoatFestivalModel);

        dragonBoatFestivalMapper.setPKExperienceAmount("aa", 10000L);

        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName("aa");
        assertNotNull(model);
        assertEquals(10000, model.getPkExperienceAmount());
    }

    @Test
    public void shouldInsertOrAddInviteNewUserCount() {
        dragonBoatFestivalMapper.addInviteNewUserCount("aa", "aa", "18611110000");

        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName("aa");
        assertNotNull(model);
        assertEquals(1, model.getInviteNewUserCount());

        dragonBoatFestivalMapper.addInviteNewUserCount("aa", "aa", "18611110000");
        DragonBoatFestivalModel model2 = dragonBoatFestivalMapper.findByLoginName("aa");
        assertEquals(2, model2.getInviteNewUserCount());
    }

    @Test
    public void shouldInsertOrAddInviteOldUserCount() {
        dragonBoatFestivalMapper.addInviteOldUserCount("aa", "aa", "18611110000");

        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName("aa");
        assertNotNull(model);
        assertEquals(1, model.getInviteOldUserCount());

        dragonBoatFestivalMapper.addInviteOldUserCount("aa", "aa", "18611110000");
        DragonBoatFestivalModel model2 = dragonBoatFestivalMapper.findByLoginName("aa");
        assertEquals(2, model2.getInviteOldUserCount());
    }

}
