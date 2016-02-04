package com.tuotiansudai.task;

import com.google.common.primitives.Longs;

import java.io.Serializable;
import java.util.Date;

public class OperationTask<T> implements Serializable, Comparable<OperationTask> {

    private long id;

    private TaskType taskType;

    private OperationType operationType;

    private String operator;

    private Date operateTime;

    private String objId;

    private String description;

    private T obj;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return Longs.compare(this.operateTime.getTime(), o.getOperateTime().getTime());
    }
}
