package com.tuotiansudai.mq.consumer.auditLog;

import com.tuotiansudai.log.repository.mapper.LoginLogMapper;
import com.tuotiansudai.log.repository.model.LoginLogModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class LoginLogMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoginLogMessageConsumer.class);

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoginLog;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            LoginLogModel loginLogModel;
            try {
                loginLogModel = JsonConverter.readValue(message, LoginLogModel.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Date loginDate = loginLogModel.getLoginTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
            String table = "login_log_" + formatter.format(loginDate);

            logger.info("[MQ] ready to consume message: LoginLogModel. loginName:{}", loginLogModel.getLoginName());

            loginLogMapper.create(table, loginLogModel);
        }
        logger.info("[MQ] consume message success.");
    }
}