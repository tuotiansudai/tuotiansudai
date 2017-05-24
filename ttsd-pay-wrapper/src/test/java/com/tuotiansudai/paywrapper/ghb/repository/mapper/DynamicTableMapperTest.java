package com.tuotiansudai.paywrapper.ghb.repository.mapper;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional(value = "ghbTransactionManager")
public class DynamicTableMapperTest {

    @Autowired
    private DynamicTableMapper dynamicTableMapper;

    @Test
    public void shouldFind() throws Exception {
        List<Map<String, Object>> requests = dynamicTableMapper.findProcessingRequest("RequestOGW00042");
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requests.get(0).get("created_time").toString());
        assertThat(requests.size(), is(1));
    }
}
