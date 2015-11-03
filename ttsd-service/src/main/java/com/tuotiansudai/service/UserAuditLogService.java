package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;

import java.util.List;

public interface UserAuditLogService {

    void generateAuditLog(UserModel beforeUpdateUserModel, List<UserRoleModel> beforeUpdateUserRoleModels,
                          UserModel afterUpdateUserModel, List<UserRoleModel> afterUpdateUserRoleModels,
                          String userIp);
}
