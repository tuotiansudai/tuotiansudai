package com.tuotiansudai.service;

import com.tuotiansudai.dto.AuditLogPaginationItemDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.task.OperationType;

import java.util.Date;
import java.util.List;

public interface AuditLogService {

    void createUserActiveLog(String loginName, String operatorLoginName, UserStatus userStatus, String userIp);

    void createAuditLog(String auditorLoginName, String operatorLoginName, OperationType operationType, String objectId, String description, String auditorIp);

    String clearMybatisCache();

    BasePaginationDataDto<AuditLogPaginationItemDataDto> getAuditLogPaginationData(OperationType operationType, String targetId, String operatorLoginName, String auditorLoginName, Date startTime, Date endTime, int index, int pageSize);
}
