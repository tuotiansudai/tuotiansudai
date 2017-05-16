package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals("SWEET", model.getPkGroup());
    }

    @Test
    public void shouldInsertOrAddTotalInvestAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "19877778888");
        dragonBoatFestivalModel.setTotalInvestAmount(10000L);
        dragonBoatFestivalMapper.addTotalInvestAmount(dragonBoatFestivalModel);

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals(10000, model.getTotalInvestAmount());

        dragonBoatFestivalMapper.addTotalInvestAmount(dragonBoatFestivalModel);
        List<DragonBoatFestivalModel> list2 = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertEquals(20000, list2.get(0).getTotalInvestAmount());
    }

    @Test
    public void shouldAddPKInvestAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "19877778888");
        dragonBoatFestivalModel.setPkGroup("SWEET");
        dragonBoatFestivalMapper.setPKGroup(dragonBoatFestivalModel);

        dragonBoatFestivalMapper.addPKInvestAmount("aa", 10000L);

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals(10000, model.getPkInvestAmount());
    }

    @Test
    public void shouldInsertOrAddInviteExperienceAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "19877778888");
        dragonBoatFestivalModel.setInviteExperienceAmount(10000L);
        dragonBoatFestivalMapper.addInviteExperienceAmount(dragonBoatFestivalModel);

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals(10000, model.getInviteExperienceAmount());

        dragonBoatFestivalMapper.addInviteExperienceAmount(dragonBoatFestivalModel);
        List<DragonBoatFestivalModel> list2 = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertEquals(20000, list2.get(0).getInviteExperienceAmount());
    }

    @Test
    public void shouldAddPKExperienceAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "19877778888");
        dragonBoatFestivalModel.setPkGroup("SWEET");
        dragonBoatFestivalMapper.setPKGroup(dragonBoatFestivalModel);

        dragonBoatFestivalMapper.addPKExperienceAmount("aa", 10000L);

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals(10000, model.getPkExperienceAmount());
    }

    @Test
    public void shouldInsertOrAddInviteNewUserCount() {
        dragonBoatFestivalMapper.addInviteNewUserCount("aa", "aa", "18611110000");

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals(1, model.getInviteNewUserCount());

        dragonBoatFestivalMapper.addInviteNewUserCount("aa", "aa", "18611110000");
        List<DragonBoatFestivalModel> list2 = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertEquals(2, list2.get(0).getInviteNewUserCount());
    }

    @Test
    public void shouldInsertOrAddInviteOldUserCount() {
        dragonBoatFestivalMapper.addInviteOldUserCount("aa", "aa", "18611110000");

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals(1, model.getInviteOldUserCount());

        dragonBoatFestivalMapper.addInviteOldUserCount("aa", "aa", "18611110000");
        List<DragonBoatFestivalModel> list2 = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertEquals(2, list2.get(0).getInviteOldUserCount());
    }

}
