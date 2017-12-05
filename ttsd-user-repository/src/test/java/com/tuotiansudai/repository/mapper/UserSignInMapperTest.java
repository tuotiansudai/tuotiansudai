package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserSignInModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserSignInMapperTest {
    @Autowired
    private UserSignInMapper mapper;

    @Test
    public void should_create() {
        String fakeLoginName = "fakeUser";
        mapper.create(fakeLoginName, 5);
        UserSignInModel userSignInModel = mapper.findByLoginName(fakeLoginName);
        assertEquals(5, userSignInModel.getSignInCount());
    }

    @Test
    public void should_update() {
        String fakeLoginName = "fakeUser";
        assertFalse(mapper.exists(fakeLoginName));
        assertEquals(0, mapper.getUserSignInCount(fakeLoginName));
        assertFalse(mapper.exists(fakeLoginName));
        mapper.create(fakeLoginName, 0);
        assertTrue(mapper.exists(fakeLoginName));
        mapper.updateSignInCount(fakeLoginName, 5);
        assertEquals(5, mapper.getUserSignInCount(fakeLoginName));
        mapper.updateSignInCount(fakeLoginName, 10);
        assertEquals(10, mapper.getUserSignInCount(fakeLoginName));
    }
}
