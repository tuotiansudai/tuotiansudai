package com.tuotiansudai.dto;

import java.util.Date;

public class AuditLogPaginationItemDataDto {

    private String loginName;

    private String operatorLoginName;

    private String ip;

    private String description;

    private Date operationTime;

    public AuditLogPaginationItemDataDto(String loginName, String operatorLoginName, String ip, String description, Date operationTime) {
        this.loginName = loginName;
        this.operatorLoginName = operatorLoginName;
        this.ip = ip;
        this.description = description;
        this.operationTime = operationTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getOperatorLoginName() {
        return operatorLoginName;
    }

    public String getIp() {
        return ip;
    }

    public String getDescription() {
        return description;
    }

    public Date getOperationTime() {
        return operationTime;
    }
}
