package com.esoft.archer.user.service;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;

import javax.servlet.http.HttpServletRequest;

public interface UserInfoLogService {
    public String generateUserInfoString(User user, User oldUserInfo);

    public void logUserOperation(String objId, String description, boolean isSuccess);
}
