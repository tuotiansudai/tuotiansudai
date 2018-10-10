package com.tuotiansudai.mq.consumer.auditLog;

import com.tuotiansudai.log.repository.mapper.AuditLogMapper;
import com.tuotiansudai.log.repository.model.AuditLogModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.message.AuditLogMessage;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class AuditLogMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(AuditLogMessageConsumer.class);

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.AuditLog;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            AuditLogModel auditLogModel;
            try {
                auditLogModel=convertToLog(JsonConverter.readValue(message, AuditLogMessage.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            logger.info("[MQ] ready to consume message: AuditLog. operatorLoginName:{}, operationType:{}, targetId:{}",
                    auditLogModel.getOperatorLoginName(), auditLogModel.getOperationType(), auditLogModel.getTargetId());

            // 幂等处理
            if (auditLogMapper.findById(auditLogModel.getId()) == null) {
                auditLogMapper.create(auditLogModel);
            }
        }
        logger.info("[MQ] consume message success.");
    }

    private AuditLogModel convertToLog(AuditLogMessage auditLogMessage){
        AuditLogModel auditLogModel=new AuditLogModel();
        BeanUtils.copyProperties(auditLogMessage,auditLogModel);
        return auditLogModel;
    }

}