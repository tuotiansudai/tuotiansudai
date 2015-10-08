package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserInfoLogService {

    void logUserOperation(UserModel userModel,List<UserRoleModel> userRoles,String userIp);
}
