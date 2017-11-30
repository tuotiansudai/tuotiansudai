package com.tuotiansudai.repository.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExperienceAccountMapperTest {

    @Autowired
    private ExperienceAccountMapper mapper;

    @Test
    public void shouldCRU() {
        String fakeLoginName = "fakeUser";
        assertEquals(0, mapper.getExperienceBalance(fakeLoginName));
        mapper.create(fakeLoginName, 1000);
        assertEquals(1000, mapper.getExperienceBalance(fakeLoginName));
        mapper.addBalance(fakeLoginName, 1000);
        assertEquals(2000, mapper.getExperienceBalance(fakeLoginName));
        mapper.addBalance(fakeLoginName, -500);
        assertEquals(1500, mapper.getExperienceBalance(fakeLoginName));
    }
}
