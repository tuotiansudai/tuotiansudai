package com.esoft.archer.user.service;

/**
 * Created by zzg on 15/8/4.
 */
public interface AdminOperationLogService {
    public void LogUserOperation(String ip, String objId, String description, String userId, String userName, boolean isSuccess);

    public void LogFinanceOperation(String ip, String objId, String description, String userId, String userName, boolean isSuccess);

}
