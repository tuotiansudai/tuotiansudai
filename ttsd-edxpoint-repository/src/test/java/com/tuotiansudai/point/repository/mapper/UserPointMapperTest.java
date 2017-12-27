package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.UserPointModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserPointMapperTest {

    @Autowired
    private UserPointMapper userPointMapper;

    @Test
    public void shouldFindNullByLoginName() {
        String fakeLoginName = "fakeUser";
        UserPointModel userPointModel = userPointMapper.findByLoginName(fakeLoginName);
        assertNull(userPointModel);
    }

    @Test
    public void shouldTestFalseExists() {
        String fakeLoginName = "fakeUser";
        boolean exist = userPointMapper.exists(fakeLoginName);
        assertFalse(exist);
    }

    @Test
    public void shouldCreate() {
        UserPointModel fakeUserPointModel = new UserPointModel("fakeUser", 100, 50, "北京一分");
        int affectedCount = userPointMapper.createIfNotExist(fakeUserPointModel);
        assertEquals(1, affectedCount);
        boolean exist = userPointMapper.exists(fakeUserPointModel.getLoginName());
        assertTrue(exist);
        UserPointModel userPointModel = userPointMapper.findByLoginName(fakeUserPointModel.getLoginName());
        assertNotNull(userPointModel);
        assertEquals(fakeUserPointModel.getPoint(), userPointModel.getPoint());
        assertEquals(fakeUserPointModel.getSudaiPoint(), userPointModel.getSudaiPoint());
        assertEquals(fakeUserPointModel.getChannelPoint(), userPointModel.getChannelPoint());
        assertEquals(fakeUserPointModel.getChannel(), userPointModel.getChannel());
        assertEquals(fakeUserPointModel.getLoginName(), userPointModel.getLoginName());
    }

    @Test
    public void shoudUpdateChannel() {
        UserPointModel fakeUserPointModel = new UserPointModel("fakeUser", 100, 0, null);
        userPointMapper.createIfNotExist(fakeUserPointModel);
        UserPointModel model1 = userPointMapper.findByLoginName(fakeUserPointModel.getLoginName());
        assertNull(model1.getChannel());
        userPointMapper.updateChannel(fakeUserPointModel.getLoginName(), "北京1分", new Date());
        UserPointModel model2 = userPointMapper.findByLoginName(fakeUserPointModel.getLoginName());
        assertEquals("北京1分", model2.getChannel());
    }

    @Test
    public void shouldIncreaseChannelPoint() {
        UserPointModel fakeUserPointModel = new UserPointModel("fakeUser", 100, 50, "北京一分");
        userPointMapper.createIfNotExist(fakeUserPointModel);
        userPointMapper.increaseChannelPoint(fakeUserPointModel.getLoginName(), 30, new Date());
        UserPointModel dbModel = userPointMapper.findByLoginName(fakeUserPointModel.getLoginName());
        assertEquals(100, dbModel.getSudaiPoint());
        assertEquals(80, dbModel.getChannelPoint());
        assertEquals(180, dbModel.getPoint());
    }

    @Test
    public void shouldIncreaseSudaiPoint() {
        UserPointModel fakeUserPointModel = new UserPointModel("fakeUser", 100, 50, "北京一分");
        userPointMapper.createIfNotExist(fakeUserPointModel);
        userPointMapper.increaseSudaiPoint(fakeUserPointModel.getLoginName(), 20, new Date());
        UserPointModel dbModel = userPointMapper.findByLoginName(fakeUserPointModel.getLoginName());
        assertEquals(120, dbModel.getSudaiPoint());
        assertEquals(50, dbModel.getChannelPoint());
        assertEquals(170, dbModel.getPoint());
    }

    @Test
    public void shouldIncreasePoint() {
        UserPointModel fakeUserPointModel = new UserPointModel("fakeUser", 100, 50, "北京一分");
        userPointMapper.createIfNotExist(fakeUserPointModel);
        userPointMapper.increasePoint(fakeUserPointModel.getLoginName(), 20, 30, new Date());
        UserPointModel dbModel = userPointMapper.findByLoginName(fakeUserPointModel.getLoginName());
        assertEquals(120, dbModel.getSudaiPoint());
        assertEquals(80, dbModel.getChannelPoint());
        assertEquals(200, dbModel.getPoint());
    }

    @Test
    public void shouldListAndCount() {
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser01", 1100, 1650, "北京一分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser02", 2100, 1550, "北京一分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser03", 3100, 1450, "北京一分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser04", 4100, 1350, "北京二分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser05", 5100, 1250, "北京二分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser06", 6100, 1150, "北京二分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser07", 7100, 1050, "北京三分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser08", 8100, 950, "北京三分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser09", 9100, 850, "北京三分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser10", 10100, 750, "北京四分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser11", 11100, 650, "北京四分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser12", 12100, 550, "北京四分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser13", 13100, 450, "北京四分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser14", 14100, 350, "北京四分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser15", 15100, 250, "北京四分"));
        userPointMapper.createIfNotExist(new UserPointModel("fakeUser16", 16100, 150, "北京四分"));

        List<UserPointModel> result1 = userPointMapper.list(null, 8000, 10000, null, null, null, null, 0, 10);
        long count1 = userPointMapper.count(null, 8000, 10000, null, null, null, null);
        assertEquals(3, result1.size());
        assertEquals("fakeUser09", result1.get(0).getLoginName());
        assertEquals("fakeUser07", result1.get(2).getLoginName());
        assertEquals(3, count1);

        List<UserPointModel> result2 = userPointMapper.list("北京四分", null, null, null, null, 300, 500, 0, 10);
        long count2 = userPointMapper.count("北京四分", null, null, null, null, 300, 500);
        assertEquals(2, result2.size());
        assertEquals("fakeUser14", result2.get(0).getLoginName());
        assertEquals("fakeUser13", result2.get(1).getLoginName());
        assertEquals(2, count2);

        List<UserPointModel> result3 = userPointMapper.list(null, null, null, 2000, 15000, null, null, 0, 10);
        long count3 = userPointMapper.count(null, null, null, 2000, 15000, null, null);
        assertEquals(10, result3.size());
        assertEquals("fakeUser14", result3.get(0).getLoginName());
        assertEquals("fakeUser05", result3.get(9).getLoginName());
        assertEquals(13, count3);
    }
}
