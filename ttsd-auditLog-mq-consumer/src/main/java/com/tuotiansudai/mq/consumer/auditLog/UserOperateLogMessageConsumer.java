package com.tuotiansudai.mq.consumer.auditLog;

import com.tuotiansudai.log.repository.mapper.UserOpLogMapper;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.message.UserOpLogMessage;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class UserOperateLogMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(UserOperateLogMessageConsumer.class);

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.UserOperateLog;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            UserOpLogModel userOpLogModel;
            try {
                UserOpLogMessage userOpLogMessage = JsonConverter.readValue(message, UserOpLogMessage.class);
                userOpLogModel=new UserOpLogModel(userOpLogMessage.getId(),userOpLogMessage.getLoginName(),userOpLogMessage.getOpType(),userOpLogMessage.getIp(),userOpLogMessage.getDeviceId(),userOpLogMessage.getSource(),userOpLogMessage.getDescription());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            logger.info("[MQ] ready to consume message: UserOperateLog. loginName:{}, opType:{}", userOpLogModel.getLoginName(), userOpLogModel.getOpType());

            // 幂等判断
            if (userOpLogMapper.findById(userOpLogModel.getId()) == null) {
                userOpLogMapper.create(userOpLogModel);
            }
        }
        logger.info("[MQ] consume message success.");
    }

}