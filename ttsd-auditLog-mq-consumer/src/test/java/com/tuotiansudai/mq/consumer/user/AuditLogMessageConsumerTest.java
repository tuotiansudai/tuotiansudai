package com.tuotiansudai.mq.consumer.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.log.repository.mapper.AuditLogMapper;
import com.tuotiansudai.log.repository.model.AuditLogModel;
import com.tuotiansudai.util.JsonConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AuditLogMessageConsumerTest {

    @MockBean
    private AuditLogMapper auditLogMapper;

    @Autowired
    @Qualifier("auditLogMessageConsumer")
    private MessageConsumer consumer;

    @Test
    @Transactional
    public void shouldConsume() {

        AuditLogModel auditLogModel = buildMockedAuditLogModel();

        final ArgumentCaptor<AuditLogModel> auditLogModelCaptor = ArgumentCaptor.forClass(AuditLogModel.class);

        doNothing().when(auditLogMapper).create(auditLogModelCaptor.capture());

        try {
            consumer.consume(JsonConverter.writeValueAsString(auditLogModel));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals("22222", auditLogModelCaptor.getValue().getTargetId());
    }

    private AuditLogModel buildMockedAuditLogModel() {
        AuditLogModel log = new AuditLogModel();
        log.setId(111111);
        log.setOperatorLoginName("aaa");
        log.setAuditorLoginName("bbb");
        log.setTargetId("22222");
        log.setOperationType(OperationType.ACTIVITY);
        log.setIp("111.111.111.111");
        log.setDescription("description");
        return log;
    }

}
