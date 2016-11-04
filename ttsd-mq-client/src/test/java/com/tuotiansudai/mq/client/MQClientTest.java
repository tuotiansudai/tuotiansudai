package com.tuotiansudai.mq.client;

import com.tuotiansudai.mq.client.config.TestConfig;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Clock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class MQClientTest {
    @Autowired
    private MQClient mqClient;

    @Test
    public void shouldSendTopicMessage() throws Exception {
        mqClient.publishMessage(MessageTopic.UserRegistered, "hello world " + Clock.systemUTC().millis());
        new Thread(() ->
                mqClient.subscribe(MessageTopicQueue.UserRegistered_CouponGiving, message -> {
                    System.out.println("message body : " + message.getMessage());
                    System.out.println("message id   : " + message.getMessageId());
                    System.out.println("message time : " + message.getPublishTime());
                    System.out.println("message topic: " + message.getTopicName());
                })).start();

        Thread.sleep(1000);
        mqClient.publishMessage(MessageTopic.UserRegistered, "hello world " + Clock.systemUTC().millis());
        Thread.sleep(1000);
        mqClient.publishMessage(MessageTopic.UserRegistered, "hello world " + Clock.systemUTC().millis());
        Thread.sleep(1000);
        mqClient.publishMessage(MessageTopic.UserRegistered, "hello world " + Clock.systemUTC().millis());
        Thread.sleep(1000);
        mqClient.publishMessage(MessageTopic.UserRegistered, "hello world " + Clock.systemUTC().millis());
        Thread.sleep(40000);
    }
}
