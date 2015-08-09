package com.ttsd.api.dto;

/**
 * Created by tuotian on 15/8/7.
 */
public class QueryBindAndSignStatusRequestDto extends BaseParamDto{
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 查询绑卡状态：query_bind_status
     * 查询签约状态：query_sign_status
     */
    private String operationType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
