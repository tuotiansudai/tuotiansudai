package com.tuotiansudai.log.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.log.repository.model.AuditLogModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.AuditLogMessage;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    private static Logger logger = Logger.getLogger(AuditLogService.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void createUserActiveLog(String loginName, String operatorLoginName, UserStatus userStatus, String userIp) {

        String operation = userStatus == UserStatus.ACTIVE ? " 解禁" : " 禁止";
        String description = operatorLoginName + operation + "了用户［" + getRealName(loginName) + "］。";

        AuditLogMessage log = new AuditLogMessage();
        log.setId(IdGenerator.generate());
        log.setTargetId(loginName);
        log.setOperatorLoginName(operatorLoginName);
        log.setOperatorMobile(this.getMobile(operatorLoginName));
        log.setOperationType(OperationType.USER);
        log.setIp(userIp);
        log.setDescription(description);
        sendAuditLogMessage(log);
    }

    public void createAuditLog(String auditorLoginName, String operatorLoginName, OperationType operationType, String targetId, String description, String auditorIp) {
        AuditLogMessage log = new AuditLogMessage();
        log.setId(IdGenerator.generate());
        log.setOperatorLoginName(operatorLoginName);
        log.setOperatorMobile(this.getMobile(operatorLoginName));
        log.setAuditorLoginName(auditorLoginName);
        log.setAuditorMobile(this.getMobile(auditorLoginName));
        log.setTargetId(targetId);
        log.setOperationType(operationType);
        log.setIp(auditorIp);
        log.setDescription(description);
        sendAuditLogMessage(log);
    }

    private void sendAuditLogMessage(AuditLogMessage log) {
        try {
            String message = JsonConverter.writeValueAsString(log);
            mqWrapperClient.sendMessage(MessageQueue.AuditLog, message);
        } catch (JsonProcessingException e) {
            logger.error("[MQ] send audit log message fail.", e);
        }
    }


    private String getRealName(String loginNameOrMobile) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        return userModel == null ? loginNameOrMobile : userModel.getUserName();
    }

    private String getMobile(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel != null ? userModel.getMobile() : "";
    }
}
