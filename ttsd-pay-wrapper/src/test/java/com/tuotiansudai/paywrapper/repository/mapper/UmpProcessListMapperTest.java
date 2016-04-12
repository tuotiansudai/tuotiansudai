package com.tuotiansudai.paywrapper.repository.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional(value = "payTransactionManager")
public class UmpProcessListMapperTest {

    @Autowired
    private UmpProcessListMapper umpProcessListMapper;

    @Test
    public void shouldCount() throws Exception {
        assertTrue(umpProcessListMapper.count() > 0);
    }
}
