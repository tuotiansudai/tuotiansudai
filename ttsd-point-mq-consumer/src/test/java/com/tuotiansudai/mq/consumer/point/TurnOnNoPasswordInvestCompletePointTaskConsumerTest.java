package com.tuotiansudai.mq.consumer.point;

import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TurnOnNoPasswordInvestCompletePointTaskConsumerTest extends PointTaskConsumerTestBase {
    @Autowired
    @Qualifier("turnOnNoPasswordInvestCompletePointTaskConsumer")
    private MessageConsumer consumer;

    @MockBean
    private PointTaskService pointTaskService;

    @Test
    @Transactional
    public void shouldConsume() {
        shouldCompleteAdvancedTask(consumer, pointTaskService, PointTask.FIRST_TURN_ON_NO_PASSWORD_INVEST);
    }
}
