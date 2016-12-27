package com.tuotiansudai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.log.repository.model.AuditLogModel;
import com.tuotiansudai.log.repository.model.OperationType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static Logger logger = Logger.getLogger(AuditLogServiceImpl.class);

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserService userService;

    @Transactional
    public void createUserActiveLog(String loginName, String operatorLoginName, UserStatus userStatus, String userIp) {

        String operation = userStatus == UserStatus.ACTIVE ? " 解禁" : " 禁止";
        String description = operatorLoginName + operation + "了用户［" + userService.getRealName(loginName) + "］。";

        AuditLogModel log = new AuditLogModel();
        log.setId(idGenerator.generate());
        log.setTargetId(loginName);
        log.setOperatorLoginName(operatorLoginName);
        log.setOperatorMobile(userService.getMobile(operatorLoginName));
        log.setOperationType(OperationType.USER);
        log.setIp(userIp);
        log.setDescription(description);
        sendAuditLogMessage(log);
    }

    public void createAuditLog(String auditorLoginName, String operatorLoginName, OperationType operationType, String targetId, String description, String auditorIp) {
        AuditLogModel log = new AuditLogModel();
        log.setId(idGenerator.generate());
        log.setOperatorLoginName(operatorLoginName);
        log.setOperatorMobile(userService.getMobile(operatorLoginName));
        log.setAuditorLoginName(auditorLoginName);
        log.setAuditorMobile(userService.getMobile(auditorLoginName));
        log.setTargetId(targetId);
        log.setOperationType(operationType);
        log.setIp(auditorIp);
        log.setDescription(description);
        sendAuditLogMessage(log);
    }

    private void sendAuditLogMessage(AuditLogModel log) {
        try {
            String message = JsonConverter.writeValueAsString(log);
            mqWrapperClient.sendMessage(MessageQueue.AuditLog, message);
        } catch (JsonProcessingException e) {
            logger.error("[MQ] send audit log message fail.", e);
        }
    }

}
