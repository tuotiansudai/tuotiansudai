package com.tuotiansudai.service;

import com.tuotiansudai.dto.AuditLogPaginationItemDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;

import java.util.Date;
import java.util.List;

public interface AuditLogService {

    void generateAuditLog(UserModel beforeUpdateUserModel, List<UserRoleModel> beforeUpdateUserRoleModels,
                          UserModel afterUpdateUserModel, List<UserRoleModel> afterUpdateUserRoleModels,
                          String userIp);

    BasePaginationDataDto<AuditLogPaginationItemDataDto> getAuditLogPaginationData(String loginName, String operatorLoginName, Date startTime, Date endTime, int index, int pageSize);
}
