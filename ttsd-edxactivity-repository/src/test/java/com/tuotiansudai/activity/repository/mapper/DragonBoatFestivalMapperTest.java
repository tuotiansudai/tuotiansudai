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
    public void shouldInsertOrAddInvestAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "18611110000", 10000, 10000, 10, 10);
        dragonBoatFestivalMapper.addInvestAmount(dragonBoatFestivalModel);

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals(10000, model.getInvestAmount());

        dragonBoatFestivalMapper.addInvestAmount(dragonBoatFestivalModel);
        List<DragonBoatFestivalModel> list2 = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertEquals(20000, list2.get(0).getInvestAmount());
    }

    @Test
    public void shouldInsertOrAddExperienceAmount() {
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel("aa", "aa", "18611110000", 10000, 10000, 10, 10);
        dragonBoatFestivalMapper.addExperienceAmount(dragonBoatFestivalModel);

        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertNotNull(list);
        DragonBoatFestivalModel model = list.get(0);
        assertEquals(10000, model.getExperienceAmount());

        dragonBoatFestivalMapper.addExperienceAmount(dragonBoatFestivalModel);
        List<DragonBoatFestivalModel> list2 = dragonBoatFestivalMapper.getDragonBoatFestivalList(0, 10);
        assertEquals(20000, list2.get(0).getExperienceAmount());
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
