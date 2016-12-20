package com.tuotiansudai.web.ask;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml", "classpath:spring-session.xml"})
@WebAppConfiguration
@Transactional
public class ApplicationTest {

    @Test
    public void shouldLoadContext() {

    }
}
