package com.tuotiansudai.mq.consumer.message;

import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CouponAssignedUserMessageSendingMessageConsumerTest {

    @Autowired
    @Qualifier("couponAssignedUserMessageSendingMessageConsumer")
    private MessageConsumer consumer;

    @Test
    @Transactional
    public void shouldConsume() {
        System.out.printf(consumer.toString());
    }
}
