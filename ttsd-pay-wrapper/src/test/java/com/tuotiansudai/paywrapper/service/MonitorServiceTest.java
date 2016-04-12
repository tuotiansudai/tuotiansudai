package com.tuotiansudai.paywrapper.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
public class MonitorServiceTest {

    @Autowired
    private MonitorService monitorService;

    @Test
    public void shouldGetAADatabaseStatus() throws Exception {
        boolean status = monitorService.getAADatabaseStatus();
        assertTrue(status);
    }

    @Test
    public void shouldGetUMPDatabaseStatus() throws Exception {
        boolean status = monitorService.getUMPDatabaseStatus();
        assertTrue(status);
    }
}
