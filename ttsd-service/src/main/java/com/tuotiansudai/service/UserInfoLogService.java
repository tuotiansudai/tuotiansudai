package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserInfoLogService {

    String generateUserInfoString(UserModel userModel,List<UserRoleModel> userRoles, UserModel oldUserModel,List<UserRoleModel> oldUserRoles);

    void logUserOperation(String objId, String description, boolean isSuccess,HttpServletRequest request);
}
