package com.tuotiansudai.mq.consumer.message;

import com.tuotiansudai.message.util.UserMessageEventGenerator;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CouponAssignedUserMessageSendingMessageConsumerTest {

    @Autowired
    @Qualifier("couponAssignedUserMessageSendingMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private UserMessageEventGenerator userMessageEventGenerator;

    @Test
    @Transactional
    public void shouldConsume() {
        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(userMessageEventGenerator).generateAssignCouponSuccessEvent(captor.capture());
        consumer.consume("UserCoupon:100001");
        assertEquals(100001, captor.getValue().longValue());
    }
}
