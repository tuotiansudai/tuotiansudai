package com.tuotiansudai.mq.consumer.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CouponAssignedUserMessageSendingMessageConsumerTest {

    @Test
    @Transactional
    public void shouldConsume() {
    }
}
