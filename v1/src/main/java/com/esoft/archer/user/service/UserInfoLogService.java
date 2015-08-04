package com.esoft.archer.user.service;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zzg on 15/8/4.
 */
public interface UserInfoLogService {
    public String generateUserInfoString(User user);

    public void logUserOperation(String objId, String description, boolean isSuccess);
}
