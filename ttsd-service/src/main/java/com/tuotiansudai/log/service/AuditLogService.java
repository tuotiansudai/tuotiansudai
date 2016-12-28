package com.tuotiansudai.log.service;

import com.tuotiansudai.log.repository.model.OperationType;
import com.tuotiansudai.repository.model.UserStatus;
import org.springframework.stereotype.Service;

@Service
public interface AuditLogService {

    void createUserActiveLog(String loginName, String operatorLoginName, UserStatus userStatus, String userIp);

    void createAuditLog(String auditorLoginName, String operatorLoginName, OperationType operationType, String targetId, String description, String auditorIp);

}
