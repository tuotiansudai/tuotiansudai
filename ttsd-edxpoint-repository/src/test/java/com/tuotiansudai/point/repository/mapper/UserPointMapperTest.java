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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserPointMapperTest {

    @Autowired
    private UserPointMapper userPointMapper;

    @Test
    public void shouldFindByLoginName() throws Exception {
        String fakeLoginName = "fakeUser";
        UserPointModel userPointModel = userPointMapper.findByLoginName(fakeLoginName);
        assertNull(userPointModel);
    }

    @Test
    public void shouldTestExists() {
        String fakeLoginName = "fakeUser";
        boolean exist = userPointMapper.exists(fakeLoginName);
        assertFalse(exist);
    }


    @Test
    public void shouldCreate() {
        String fakeLoginName = "fakeUser";
        int affectedCount = userPointMapper.create(fakeLoginName, 100, new Date());
        assertEquals(1, affectedCount);
        boolean exist = userPointMapper.exists(fakeLoginName);
        assertTrue(exist);
        UserPointModel userPointModel = userPointMapper.findByLoginName(fakeLoginName);
        assertNotNull(userPointModel);
        assertEquals(100, userPointModel.getPoint());
        assertEquals(fakeLoginName, userPointModel.getLoginName());
    }

    @Test
    public void shouldIncrease() {
        String fakeLoginName = "fakeUser2";
        userPointMapper.create(fakeLoginName, 100, new Date());
        userPointMapper.increaseOrCreate(fakeLoginName, 100);
        long newPoint = userPointMapper.getPointByLoginName(fakeLoginName);
        assertEquals(200, newPoint);
    }

    @Test
    public void shouldIncreaseOrCreate() {
        String fakeLoginName = "fakeUser3";
        userPointMapper.increaseOrCreate(fakeLoginName, 100);
        long newPoint = userPointMapper.getPointByLoginName(fakeLoginName);
        assertEquals(100, newPoint);
        userPointMapper.increaseOrCreate(fakeLoginName, 100);
        newPoint = userPointMapper.getPointByLoginName(fakeLoginName);
        assertEquals(200, newPoint);
    }

}
