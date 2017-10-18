package com.tuotiansudai.mq.consumer.message;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class ApplicationTest {
    @Autowired
    private EventMessageConsumer eventMessageConsumer;

    @Test
    public void shouldLoadContext() {
        assertNotNull(eventMessageConsumer);
    }
}
