package com.tuotiansudai.task;

import com.google.common.primitives.Longs;

import java.io.Serializable;
import java.util.Date;

public class OperationTask<T> implements Serializable, Comparable<OperationTask> {

    private long id;

    private String operator;

    private Date operateTime;

    private String objId;

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

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    @Override
    public int compareTo(OperationTask o) {
        return Longs.compare(this.operateTime.getTime(), o.getOperateTime().getTime());
    }
}
