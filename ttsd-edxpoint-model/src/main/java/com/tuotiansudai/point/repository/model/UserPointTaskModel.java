package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserPointTaskModel implements Serializable {

    private String loginName;

    private long pointTaskId;

    private PointTaskModel pointTask;

    private long point;

    private long taskLevel;

    private Date createdTime;

    public UserPointTaskModel() {
    }

    public UserPointTaskModel(String loginName, long pointTaskId, long point, long taskLevel) {
        this.loginName = loginName;
        this.pointTaskId = pointTaskId;
        this.point = point;
        this.taskLevel = taskLevel;
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

    public PointTaskModel getPointTask() {
        return pointTask;
    }

    public void setPointTask(PointTaskModel pointTask) {
        this.pointTask = pointTask;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(long taskLevel) {
        this.taskLevel = taskLevel;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
