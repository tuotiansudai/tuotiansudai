package com.tuotiansudai.api.dto;

import com.tuotiansudai.point.repository.model.PointTask;

import java.io.Serializable;

public class PointTaskRecordResponseDataDto implements Serializable{
    private String pointTaskId;
    private PointTask pointTaskType;
    private String pointTaskDesc;
    private String pointTaskTitle;
    private String point;
    private boolean completed;

    public String getPointTaskId() {
        return pointTaskId;
    }

    public void setPointTaskId(String pointTaskId) {
        this.pointTaskId = pointTaskId;
    }

    public PointTask getPointTaskType() {
        return pointTaskType;
    }

    public void setPointTaskType(PointTask pointTaskType) {
        this.pointTaskType = pointTaskType;
    }

    public String getPointTaskDesc() {
        return pointTaskDesc;
    }

    public void setPointTaskDesc(String pointTaskDesc) {
        this.pointTaskDesc = pointTaskDesc;
    }

    public String getPointTaskTitle() {
        return pointTaskTitle;
    }

    public void setPointTaskTitle(String pointTaskTitle) {
        this.pointTaskTitle = pointTaskTitle;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
