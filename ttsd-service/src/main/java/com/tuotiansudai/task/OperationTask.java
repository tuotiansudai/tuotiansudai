package com.tuotiansudai.task;

import com.google.common.primitives.Longs;

import java.io.Serializable;
import java.util.Date;

public class OperationTask<T> implements Serializable, Comparable<OperationTask> {

    private static final long serialVersionUID = 5818931956921106930L;

    private String id;

    private TaskType taskType;

    private OperationType operationType;

    private String sender;

    private String receiver;

    private Date createdTime;

    private String objId;

    private String objName;

    private String description;

    private String operateURL;

    private T obj;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperateURL() {
        return operateURL;
    }

    public void setOperateURL(String operateURL) {
        this.operateURL = operateURL;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public int compareTo(OperationTask o) {
        return Longs.compare(o.getCreatedTime().getTime(), this.createdTime.getTime());
    }
}
