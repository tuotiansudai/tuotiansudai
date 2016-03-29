package com.tuotiansudai.util;

import com.tuotiansudai.repository.mapper.AuditLogMapper;
import com.tuotiansudai.repository.model.AuditLogModel;
import com.tuotiansudai.task.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogUtil {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private IdGenerator idGenerator;

    public void createAuditLog(String auditorLoginName, String operatorLoginName, OperationType operationType, String targetId, String description, String auditorIp) {
        AuditLogModel log = new AuditLogModel();
        log.setId(idGenerator.generate());
        log.setOperatorLoginName(operatorLoginName);
        log.setAuditorLoginName(auditorLoginName);
        log.setTargetId(targetId);
        log.setOperationType(operationType);
        log.setIp(auditorIp);
        log.setDescription(description);
        auditLogMapper.create(log);
    }
}
