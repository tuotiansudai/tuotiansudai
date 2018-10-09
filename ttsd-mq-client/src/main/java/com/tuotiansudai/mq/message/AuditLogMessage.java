package com.tuotiansudai.mq.message;

import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.util.IdGenerator;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qduljs2011 on 2018/10/9.
 */
public class AuditLogMessage implements Serializable {
    private long id;

    private String auditorLoginName;

    private String auditorMobile;

    private String operatorLoginName;

    private String operatorMobile;

    private String targetId;

    private OperationType operationType;

    private String ip;

    private Date operationTime = new Date();

    private String description;

    public AuditLogMessage() {
    }

    public AuditLogMessage(long id, String auditorLoginName, String auditorMobile, String operatorLoginName, String operatorMobile, String targetId, OperationType operationType, String ip, Date operationTime, String description) {
        this.id = id;
        this.auditorLoginName = auditorLoginName;
        this.auditorMobile = auditorMobile;
        this.operatorLoginName = operatorLoginName;
        this.operatorMobile = operatorMobile;
        this.targetId = targetId;
        this.operationType = operationType;
        this.ip = ip;
        this.operationTime = operationTime;
        this.description = description;
    }


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

    public String getAuditorMobile() {
        return auditorMobile;
    }

    public void setAuditorMobile(String auditorMobile) {
        this.auditorMobile = auditorMobile;
    }

    public String getOperatorLoginName() {
        return operatorLoginName;
    }

    public void setOperatorLoginName(String operatorLoginName) {
        this.operatorLoginName = operatorLoginName;
    }

    public String getOperatorMobile() {
        return operatorMobile;
    }

    public void setOperatorMobile(String operatorMobile) {
        this.operatorMobile = operatorMobile;
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

    public static AuditLogMessage createUserActiveLogMessage(String loginName, String operatorLoginName,        n  userStatus, String userIp){
        String operation = userStatus == UserStatus.ACTIVE ? " 解禁" : " 禁止";
        String description = operatorLoginName + operation + "了用户［" + getRealName(loginName) + "］。";

        AuditLogMessage log = new AuditLogMessage();
        log.setId(IdGenerator.generate());
        log.setTargetId(loginName);
        log.setOperatorLoginName(operatorLoginName);
        log.setOperatorMobile(this.getMobile(operatorLoginName));
        log.setOperationType(OperationType.USER);
        log.setIp(userIp);
        log.setDescription(description);
        return  log;
    }
}
