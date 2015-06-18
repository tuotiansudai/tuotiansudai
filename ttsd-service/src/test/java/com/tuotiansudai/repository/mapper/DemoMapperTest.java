package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.DemoModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@TransactionConfiguration(defaultRollback=true)
public class DemoMapperTest {

    @Autowired
    private DemoMapper demoMapper;

    @Test
    public void shouldGetDemoById() throws Exception {

        DemoModel demoModel = demoMapper.getDemoById("admin");
        assertNotNull(demoModel);
    }
}