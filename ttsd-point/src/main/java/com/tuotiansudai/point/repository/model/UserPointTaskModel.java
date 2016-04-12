package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserPointTaskModel implements Serializable {

    private String loginName;

    private long pointTaskId;

    private Date createdTime;

    private PointTaskModel pointTask;

    public UserPointTaskModel() {
    }

    public UserPointTaskModel(String loginName, long pointTaskId) {
        this.loginName = loginName;
        this.pointTaskId = pointTaskId;
        this.createdTime = new Date();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getPointTaskId() {
        return pointTaskId;
    }

    public void setPointTaskId(long pointTaskId) {
        this.pointTaskId = pointTaskId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public PointTaskModel getPointTask() {
        return pointTask;
    }

    public void setPointTask(PointTaskModel pointTask) {
        this.pointTask = pointTask;
    }
}
