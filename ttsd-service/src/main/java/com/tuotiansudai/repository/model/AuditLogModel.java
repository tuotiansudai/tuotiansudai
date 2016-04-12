package com.tuotiansudai.repository.model;


import com.tuotiansudai.task.OperationType;

import java.io.Serializable;
import java.util.Date;

public class AuditLogModel implements Serializable {

    private long id;

    private String auditorLoginName;

    private String operatorLoginName;

    private String targetId;

    private OperationType operationType;

    private String ip;

    private Date operationTime = new Date();

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
