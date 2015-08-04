package com.esoft.archer.user.service.impl;

import com.esoft.archer.user.model.AdminOperationLog;
import com.esoft.archer.user.service.AdminOperationLogService;

/**
 * Created by zzg on 15/8/4.
 */
public class AdminOperationLogServiceImpl implements AdminOperationLogService {
    private void LogOperation(String ip, String objId, String operationType, String description, String userId, String userName, boolean isSuccess) {
        System.out.println(description);
    }

    @Override
    public void LogUserOperation(String ip, String objId, String description, String userId, String userName, boolean isSuccess) {
        LogOperation(ip, objId, AdminOperationLog.OPERATE_TYPE_USER, description, userId, userName, isSuccess);
    }

    @Override
    public void LogFinanceOperation(String ip, String objId, String description, String userId, String userName, boolean isSuccess) {
        LogOperation(ip, objId, AdminOperationLog.OPERATE_TYPE_FINANCE, description, userId, userName, isSuccess);
    }
}
