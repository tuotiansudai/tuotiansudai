package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.log.repository.model.AuditLogModel;
import com.tuotiansudai.log.repository.model.OperationType;
import com.tuotiansudai.repository.model.UserStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface AuditLogService {

    void createUserActiveLog(String loginName, String operatorLoginName, UserStatus userStatus, String userIp);

    void createAuditLog(String auditorLoginName, String operatorLoginName, OperationType operationType, String targetId, String description, String auditorIp);

    BasePaginationDataDto<AuditLogModel> getAuditLogPaginationData(OperationType operationType, String targetId,
                                                                   String operatorMobile, String auditorMobile, Date startTime, Date endTime, int index, int pageSize);

    String clearMybatisCache();

}
