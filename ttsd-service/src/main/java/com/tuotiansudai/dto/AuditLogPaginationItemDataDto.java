package com.tuotiansudai.dto;

import com.tuotiansudai.task.OperationType;

import java.util.Date;

public class AuditLogPaginationItemDataDto {

    private String auditorLoginName;

    private String operatorLoginName;

    private String targetId;

    private OperationType operationType;

    private String ip;

    private String description;

    private Date operationTime;

    public AuditLogPaginationItemDataDto(String auditorLoginName, String operatorLoginName, String targetId, OperationType operationType, String ip, String description, Date operationTime) {
        this.auditorLoginName = auditorLoginName;
        this.operatorLoginName = operatorLoginName;
        this.targetId = targetId;
        this.operationType = operationType;
        this.ip = ip;
        this.description = description;
        this.operationTime = operationTime;
    }

    public String getAuditorLoginName() {
        return auditorLoginName;
    }

    public void setAuditorLoginName(String auditorLoginName) {
        this.auditorLoginName = auditorLoginName;
    }

    public String getOperatorLoginName() {
        return operatorLoginName;
    }

    public void setOperatorLoginName(String operatorLoginName) {
        this.operatorLoginName = operatorLoginName;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
}
