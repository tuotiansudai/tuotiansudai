package com.esoft.archer.user.service;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zzg on 15/8/4.
 */
public interface AdminOperationLogService {
    public String generateUserInfoString(User user);

    public String generateFinanceInfoString(UserBill userBill);

    public void logUserOperation(String objId, String description, boolean isSuccess);

    public void logFinanceOperation(String objId, String description, boolean isSuccess);

}
