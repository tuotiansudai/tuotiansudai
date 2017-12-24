package com.tuotiansudai.mq.consumer.point;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.message.ObtainPointMessage;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.util.JsonConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ObtainPointMessageConsumerTest {

    @MockBean
    private UserPointMapper userPointMapper;

    @Autowired
    @Qualifier("obtainPointMessageConsumer")
    private MessageConsumer consumer;

    @Test
    @Transactional
    public void shouldConsume() {

        ObtainPointMessage obtainPointMessage = buildMockedObtainPointMessage();

        try {
            consumer.consume(JsonConverter.writeValueAsString(obtainPointMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertEquals(100L, userPointMapper.getPointByLoginName(obtainPointMessage.getLoginName()));
    }

    private ObtainPointMessage buildMockedObtainPointMessage() {
        return new ObtainPointMessage("testAA", 100);
    }

}
